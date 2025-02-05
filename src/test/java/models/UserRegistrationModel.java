package models;

import helpers.Generation;
import io.qameta.allure.Step;

public class UserRegistrationModel {
    private String email;
    private String password;
    private String name;


    public UserRegistrationModel(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }


    public UserRegistrationModel(){}


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


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Step("Generate random user")
    public static UserRegistrationModel generateUser(){
        return new UserRegistrationModel(
                Generation.getEmail(),
                Generation.getPassword(),
                Generation.getName());
    }
}