package org.ukwikora.model;

public class TimeValue {
    public enum Sign{
        POSITIVE, NEGATIVE
    }

    private static final double SECONDS_IN_DAY = 86400;
    private static final double SECONDS_IN_HOUR = 3600;
    private static final double SECONDS_IN_MINUTE = 60;
    private static final double SECONDS_IN_MILLISECOND = 0.001;

    private Sign sign;
    private int days;
    private int hours;
    private int minutes;
    private int seconds;
    private int milliseconds;

    public TimeValue(Sign sign, int days, int hours, int minutes, int seconds, int milliseconds){
        this.sign = sign;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.milliseconds = milliseconds;
    }

    public int getSign(){
        switch (sign) {
            case POSITIVE: return 1;
            case NEGATIVE: return -1;
        }

        return 1;
    }

    public double getTimeInSeconds(){
        return (SECONDS_IN_DAY * days
                + SECONDS_IN_HOUR * hours
                + SECONDS_IN_MINUTE * minutes
                + seconds
                + SECONDS_IN_MILLISECOND * milliseconds)
                * (float)getSign();
    }

    public double getTimeInMilliseconds(){
        return getTimeInSeconds() / SECONDS_IN_MILLISECOND;
    }
}
