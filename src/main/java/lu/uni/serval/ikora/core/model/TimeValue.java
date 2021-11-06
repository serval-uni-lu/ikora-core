package lu.uni.serval.ikora.core.model;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.regex.Pattern;

public class TimeValue {
    private static final Pattern stringPattern;
    private static final Pattern timerPattern;

    static {
        stringPattern = Pattern.compile("-?\\s*(\\d+(\\.\\d+)?\\s*(days|day|d))?\\s*(\\d+(\\.\\d+)?\\s*(hours|hour|h))?\\s*(\\d+(\\.\\d+)?\\s*(minutes|minute|mins|min|m))?\\s*(\\d+(\\.\\d+)?\\s*(seconds|second|secs|sec|s))?\\s*(\\d+(\\.\\d+)?\\s*(milliseconds|millisecond|millis|ms))?", Pattern.CASE_INSENSITIVE);
        timerPattern = Pattern.compile("-?\\s*(\\d+:)?\\d\\d:\\d\\d(\\.\\d+)?", Pattern.CASE_INSENSITIVE);
    }

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

    public TimeValue(Token text){

    }

    public TimeValue(Sign sign, int days, int hours, int minutes, int seconds, int milliseconds){
        this.sign = sign;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.milliseconds = milliseconds;
    }

    public int getSign(){
        return sign == Sign.NEGATIVE ? -1 : 1;
    }

    public double getTimeInSeconds(){
        return (SECONDS_IN_DAY * days
                + SECONDS_IN_HOUR * hours
                + SECONDS_IN_MINUTE * minutes
                + seconds
                + SECONDS_IN_MILLISECOND * milliseconds)
                * getSign();
    }

    public double getTimeInMilliseconds(){
        return getTimeInSeconds() / SECONDS_IN_MILLISECOND;
    }

    public static boolean isValid(Token token) {
        if(token.isEmpty()){
            return false;
        }

        if(NumberUtils.isCreatable(token.getText())){
            return true;
        }

        return stringPattern.matcher(token.getText()).matches()
                || timerPattern.matcher(token.getText()).matches();
    }
}
