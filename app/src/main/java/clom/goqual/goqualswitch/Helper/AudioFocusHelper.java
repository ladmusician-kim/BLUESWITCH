package clom.goqual.goqualswitch.Helper;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import clom.goqual.goqualswitch.Interface.MusicFocusable;

/**
 * Created by admin on 2015. 6. 19..
 */
public class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {
    private static final String TAG = "AUDIOFOCUSHELPER";
    AudioManager mAM;
    MusicFocusable mFocusable;

    public AudioFocusHelper(Context ctx, MusicFocusable focusable) {
        mAM = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        mFocusable = focusable;
    }

    public boolean requestFocus() {
        Log.e(TAG, "REQUEST FOCUS");
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                mAM.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    public boolean abandonFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                mAM.abandonAudioFocus(this);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        Log.e(TAG, "FOUCS CHANGE");
        if (mFocusable == null) return;
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                mFocusable.onGainedAudioFocus();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                mFocusable.onLostAudioFocus(false);
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                mFocusable.onLostAudioFocus(true);
                break;
            default:
        }
    }
}
