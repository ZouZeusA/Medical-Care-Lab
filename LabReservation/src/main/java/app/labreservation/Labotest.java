package app.labreservation;

import java.util.Arrays;

public class Labotest {
    public Labotest(int num, String type, double price, String option1, String option2) {
        setNum(num);
        setType(type);
        setPrice(price);
        String [] options = new String[]{option1,option2};
        setOptions(options);
    }

    private int num;
    private String type;
    private String result;
    private double price;
    private Date test_date;
    private Time test_time;
    private String [] options = new String[2];

    public Labotest() {
    }

    public Labotest(int num, String type, double price, Date test_date, Time test_time) {
        this.num = num;
        this.type = type;
        this.price = price;
        this.test_date = test_date;
        this.test_time = test_time;
    }


    @Override
    public String toString() {
        if(getTest_date()==null){
            if(getOption2()==null)
            return new String(getNum()+" "+getType()+" "+getPrice()+"$ "+getOption1());
            else return new String(getNum()+" "+getType()+" "+getPrice()+"$ "+getOption1()+" "+getOption2());
        }
        else if (getOption1()==null){
            return new String(getNum()+" "+getType()+" "+getPrice()+"$ "+
                    getTest_date().toString()+ " "+getTest_time().toString());
        }
        else return "Labotest{" +
                    "num=" + num +
                    ", type='" + type + '\'' +
                    ", result='" + result + '\'' +
                    ", price=" + price +
                    ", test_date=" + test_date +
                    ", test_time=" + test_time +
                    ", options=" + Arrays.toString(options) +
                    '}';
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        if (num<0){
            throw new RuntimeException("Wrong LabTest ID");
        }
        else this.num = num;
    }

    public String getType() {
        return type;
    }
    public String getOption1() {
        return options[0];
    }
    public String getOption2() {
        return options[1];
    }
    public void setType(String type) {
        if (type.isEmpty()){
            throw new RuntimeException("Empty LabTest type");
        }
        else this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) { // Result can be empty
        this.result = result;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price<0){
            throw new RuntimeException("Wrong LabTest price");
        }
        else this.price = price;
    }

    public Date getTest_date() {
        return test_date;
    }

    public void setTest_date(Date test_date) {
        this.test_date = test_date;
    }

    public Time getTest_time() {
        return test_time;
    }

    public void setTest_time(Time test_time) {
        this.test_time = test_time;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) { // we can have a Test with no options
        this.options = options;
    }
}
