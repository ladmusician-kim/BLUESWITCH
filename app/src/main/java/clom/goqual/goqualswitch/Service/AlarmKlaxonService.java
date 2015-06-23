package clom.goqual.goqualswitch.Service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import clom.goqual.goqualswitch.Helper.AudioFocusHelper;
import clom.goqual.goqualswitch.Interface.MusicFocusable;
import clom.goqual.goqualswitch.R;

/**
 * Created by admin on 2015. 6. 19..
 */
public class AlarmKlaxonService extends Service implements MusicFocusable, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    private static final String TAG = "SERVICE_ALARM";

    // do we have audio focus?
    public enum AudioFocus {
        NoFocusNoDuck,    // we don't have audio focus, and can't duck
        NoFocusCanDuck,   // we don't have focus, but can play at a low volume ("ducking")
        Focused           // we have full audio focus
    }

    private AudioFocusHelper mAudioFocusHelper = null;
    private AudioFocus mAudioFocus = AudioFocus.NoFocusNoDuck;
    private MediaPlayer mPlayer = null;
    private String mRingtoneURI = "";

    private static final float DUCK_VOLUME = 0.1f;

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.e(TAG, "MEDIA PLAYER PREPARED");
        //configAndStartMediaPlayer();
        mPlayer.start();
    }
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "Error: what=" + String.valueOf(what) + ", extra=" + String.valueOf(extra));

        relaxResources(true);
        giveUpAudioFocus();
        return true; // true indicates we handled the error
    }
    @Override
    public void onGainedAudioFocus() {
        mAudioFocus = AudioFocus.Focused;
        configAndStartMediaPlayer();

        Log.e(TAG, "GOT FOCUS");
    }
    @Override
    public void onLostAudioFocus(boolean canDuck) {
        mAudioFocus = canDuck ? AudioFocus.NoFocusCanDuck : AudioFocus.NoFocusNoDuck;

        // start/restart/pause media player with new focus settings
        if (mPlayer != null && mPlayer.isPlaying())
            configAndStartMediaPlayer();

        Log.e(TAG, "LOST FOCUS");
    }

    void giveUpAudioFocus() {
        if (mAudioFocus == AudioFocus.Focused && mAudioFocusHelper != null && mAudioFocusHelper.abandonFocus())
            mAudioFocus = AudioFocus.NoFocusNoDuck;
    }
    void configAndStartMediaPlayer() {
        Log.e(TAG, "AUDIO FOCUE: " + mAudioFocus);

        if (mAudioFocus == AudioFocus.NoFocusNoDuck) {
            // If we don't have audio focus and can't duck, we have to pause, even if mState
            // is State.Playing. But we stay in the Playing state so that we know we have to resume
            // playback once we get the focus back.
            if (mPlayer.isPlaying()) mPlayer.pause();
            return;
        }
        else if (mAudioFocus == AudioFocus.NoFocusCanDuck)
            mPlayer.setVolume(DUCK_VOLUME, DUCK_VOLUME);  // we'll be relatively quiet
        else
            mPlayer.setVolume(1.0f, 1.0f); // we can be loud

        if (!mPlayer.isPlaying()) mPlayer.start();
    }
    void relaxResources(boolean releaseMediaPlayer) {
        // stop being a foreground service
        stopForeground(true);

        // stop and release the Media Player, if it's available
        if (releaseMediaPlayer && mPlayer != null) {
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
    }
    void createMediaPlayerIfNeeded() {
        if (mPlayer == null) {
            Log.e(TAG, "RINGTONE : " + mRingtoneURI);
            if (mRingtoneURI.equals(getString(R.string.BASIC_RINGTONE))) {
                Log.e(TAG, "BASIC RINGTONE RING");
                mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.hangouts_incoming_call);
            } else {
                try {
                    mPlayer = new MediaPlayer();
                    mPlayer.setDataSource(mRingtoneURI);
                } catch (Exception e) {
                    mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.hangouts_incoming_call);
                }
            }

            // Make sure the media player will acquire a wake-lock while playing. If we don't do
            // that, the CPU might go to sleep while the song is playing, causing playback to stop.
            //
            // Remember that to use this, we have to declare the android.permission.WAKE_LOCK
            // permission in AndroidManifest.xml.
            mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

            // we want the media player to notify us when it's ready preparing, and when it's done
            // playing:
            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnErrorListener(this);
        }
        else
            mPlayer.reset();
    }
    void tryToGetAudioFocus() {
        if (mAudioFocus != AudioFocus.Focused && mAudioFocusHelper != null && mAudioFocusHelper.requestFocus()) {
            mAudioFocus = AudioFocus.Focused;
        }
    }
    void playRingtone() {
        relaxResources(false); // release everything except MediaPlayer

        try {
            createMediaPlayerIfNeeded();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setLooping(true);
            mPlayer.prepareAsync();


        }
        catch (Exception ex) {
            Log.e("MusicService", "IOException playing next song: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    void processPlayRequest() {
        Log.e(TAG, "play the music");
        tryToGetAudioFocus();
        playRingtone();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action.equals(getString(R.string.ACTION_ALARM_KLAXON))) {
            mRingtoneURI = intent.getStringExtra(getString(R.string.key_alarm_ringtone));
            processPlayRequest();
        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // create the Audio Focus Helper, if the Audio Focus feature is available (SDK 8 or above)
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            Log.e(TAG, "run focus helper");
            mAudioFocusHelper = new AudioFocusHelper(getApplicationContext(), this);
        } else{
            mAudioFocus = AudioFocus.Focused; // no focus feature, so we always "have" audio focus
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "KLAXON SERVICE DESTROY");
        if(mPlayer.isPlaying()) mPlayer.stop();
        if(mPlayer != null) mPlayer = null;

        relaxResources(true);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
