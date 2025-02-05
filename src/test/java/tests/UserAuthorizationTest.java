package tests;

import api.AuthApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.UserLoginModel;
import models.UserRegistrationModel;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;


public class UserAuthorizationTest {
    public static final String EMAIL_PASSWORD_INCORRECT = "email or password are incorrect";
    private AuthApi authApi;
    private UserRegistrationModel userRegistrationModel;
    Response responseRegister;
    Response response;


    @Before
    public void setUp(){
        authApi = new AuthApi();
        userRegistrationModel = UserRegistrationModel.generateUser();
        responseRegister = authApi.registerUser(userRegistrationModel);
    }


    @After
    public void cleanUp(){
        if (response.jsonPath().getBoolean("success")) {
            String authorization = response.jsonPath().getString("accessToken");
            authApi.deleteUser(authorization);
        } else {
            String authorization = responseRegister.jsonPath().getString("accessToken");
            authApi.deleteUser(authorization);
        }
    }


    @Test
    @DisplayName("Check possible to logIn if user exists")
    public void userAuthorizationAuthorizedWhenUserExistsTest(){
        response = authApi.loginUser(new UserLoginModel(userRegistrationModel));

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true));

        if (response.jsonPath().getBoolean("success")) responseRegister = response;
    }


    @Test
    @DisplayName("Check error if try to logIn with non-existed email-password")
    public void userAuthorizationFailedWhenUserNotExistsTest(){
         response = authApi.loginUser(new UserLoginModel(UserRegistrationModel.generateUser()));

         response.then().log().all()
                 .assertThat()
                 .statusCode(HttpStatus.SC_UNAUTHORIZED)
                 .and()
                 .body("success", is(false))
                 .and()
                 .body("message", equalTo(EMAIL_PASSWORD_INCORRECT));
    }
}