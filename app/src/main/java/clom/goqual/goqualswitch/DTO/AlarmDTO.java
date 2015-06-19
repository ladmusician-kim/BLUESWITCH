package clom.goqual.goqualswitch.DTO;

import com.orm.SugarRecord;

/**
 * Created by admin on 2015. 6. 19..
 */
public class AlarmDTO extends SugarRecord<AlarmDTO> {
    private int HOUR = -1;
    private int MIN = -1;
    private String RINGTONE = "";
    private boolean SUN = false;
    private boolean MON = false;
    private boolean TUE = false;
    private boolean WED = false;
    private boolean THUR = false;
    private boolean FRI = false;
    private boolean SAT = false;

    public AlarmDTO() {
    }

    public AlarmDTO(int hour, int min, String ringtone, boolean[] repeat) {
        this.HOUR = hour;
        this.MIN = min;
        this.RINGTONE = ringtone;
        this.SUN = repeat[0];
        this.MON = repeat[1];
        this.TUE = repeat[2];
        this.WED = repeat[3];
        this.THUR = repeat[4];
        this.FRI = repeat[5];
        this.SAT = repeat[6];
    }

    public int getHour() {
        return this.HOUR;
    }
    public int getMin() {
        return this.MIN;
    }
    public String getRingtone() {
        return this.RINGTONE;
    }
    public boolean[] getRepeat() {
        boolean[] isRepeat = new boolean[7];
        isRepeat[0] = this.SUN;
        isRepeat[1] = this.MON;
        isRepeat[2] = this.TUE;
        isRepeat[3] = this.WED;
        isRepeat[4] = this.THUR;
        isRepeat[5] = this.FRI;
        isRepeat[6] = this.SAT;

        return isRepeat;
    }
}