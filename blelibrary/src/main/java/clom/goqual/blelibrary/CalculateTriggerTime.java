package clom.goqual.blelibrary;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by admin on 2015. 6. 19..
 */
public class CalculateTriggerTime {
    private static final String TAG = "CALCULATETRIGGERTIME";
    private int mTriggerHour = -1;
    private int mTriggerMin = -1;

    public CalculateTriggerTime(int triggerHour, int triggerMin) {
        this.mTriggerHour = triggerHour;
        this.mTriggerMin = triggerMin;
    }

    public long calSetTimerSecond () {
        GregorianCalendar currentTime = new GregorianCalendar();
        GregorianCalendar triggerTime = new GregorianCalendar();

        int currentYear = currentTime.get(Calendar.YEAR);
        int currentMonth = currentTime.get(Calendar.MONTH);
        int currentDay = currentTime.get(Calendar.DAY_OF_MONTH);
        int setTriggerHour = mTriggerHour;
        int setTriggerMin = mTriggerMin;

        triggerTime.set(currentYear, currentMonth, currentDay, setTriggerHour, setTriggerMin, 00);

        return triggerTime.getTimeInMillis();
    }
}
