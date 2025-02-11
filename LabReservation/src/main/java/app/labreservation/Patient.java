package app.labreservation;

import java.util.regex.*;

public class Patient {
    private int ID;
    private String firstName,lastName,password,address,tel;
    private Labotest t;

    public Patient(String firstName, String lastName, String password, String address, String tel) {
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        setAddress(address);
        setTel(tel);
    }

    public Patient(int ID, String firstName, String lastName, String password, String address, String tel) {
        setID(ID);
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        setAddress(address);
        setTel(tel);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        if (ID<0){
            throw new RuntimeException("Wrong Patient ID");
        }
        else this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName.isEmpty()){
            throw new RuntimeException("Empty Patient FirstName");
        }
        else this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName.isEmpty()){
            throw new RuntimeException("Empty Patient FirstName");
        }
        else this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,20}$";
        if(password.length()>=8 && isValidPassword(password,regex)){
            this.password = password;
        }
        else throw new RuntimeException("Wrong password");
    }
    public static boolean isValidPassword(String password,String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) { // Email Validation Permitted by RFC 5322
        String regex = "^[a-zA-Z\\d_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(address);
        if(!matcher.matches()){
            throw new RuntimeException("Wrong email address");
        }
        else this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) { //Lebanese phone number
        String regex = "^(?:\\+961|961)?(1|0?3\\d?|[4-6]|70|71|76|78|79|03|81?|9)\\d{6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tel.replaceAll("\\s+",""));
        if(!matcher.matches()){
            throw new RuntimeException("Wrong telephone number");
        }
        else this.tel = tel;
    }

    public Labotest getT() {
        return t;
    }

    public void setT(Labotest t) {
        this.t = t;
    }
}
