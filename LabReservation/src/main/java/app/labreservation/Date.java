package app.labreservation;

import java.util.Calendar;

public class Date {
    private int year;
    private int month;
    private int day;

    public Date(int year, int month, int day) {
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if(year< Calendar.getInstance().get(Calendar.YEAR)){
            throw new RuntimeException("Wrong year");
        }
        else{
            this.year = year;
        }
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if(month< Calendar.getInstance().get(Calendar.MONTH)){
            throw new RuntimeException("Wrong month");
        }
        else{
            this.month = month;
        }
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if(day< Calendar.getInstance().get(getDay())){
            throw new RuntimeException("Wrong day");
        }
        else{
            this.day = day;
        }
    }

    @Override
    public String toString() {
        return  year +
                "-" + month +
                "-" + day ;
    }
}
