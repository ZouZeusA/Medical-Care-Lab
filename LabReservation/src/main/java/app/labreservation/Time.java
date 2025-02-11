package app.labreservation;

public class Time {
    private int hour;
    private int minute;
    private int second;

    public Time(int hour, int minute, int second) {
        setHour(hour);
        setMinute(minute);
        setSecond(second);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        if (hour<0 || hour >23){
            throw new RuntimeException("Wrong hours");
        }
        else{
            this.hour = hour;
        }
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        if (minute<0 || minute >59){
            throw new RuntimeException("Wrong minutes");
        }
        else {
            this.minute = minute;
        }
    }

    @Override
    public String toString() {
        return hour +
                ":" + minute +
                ":" + second;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        if (second<0 || second >59){
            throw new RuntimeException("Wrong second");
        }
        else {
            this.second = second;
        }
    }
}
