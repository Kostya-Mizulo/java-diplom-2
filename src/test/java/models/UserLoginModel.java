package models;

public class UserLoginModel {
    private String email;
    private String password;


    public UserLoginModel(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public UserLoginModel(UserRegistrationModel user){
        this.email = user.getEmail();
        this.password = user.getPassword();
    }


    public UserLoginModel(){}


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }
}
