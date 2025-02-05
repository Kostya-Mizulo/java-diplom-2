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


public class UserRegistrationTest {
    private static final String USER_ALREADY_EXISTS_MESSAGE = "User already exists";
    private static final String THERE_ARE_NO_REQUIRED_FIELDS_MESSAGE = "Email, password and name are required fields";
    private AuthApi authApi;
    private UserRegistrationModel userRegistrationModel;
    private Response response;


    @Before
    public void setUp(){
        authApi = new AuthApi();
    }


    @After
    public void cleanUp(){
        Response loginResponse = authApi.loginUser(new UserLoginModel(userRegistrationModel));
        String authorization = loginResponse.jsonPath().getString("accessToken");
        if (authorization != null) authApi.deleteUser(authorization);

    }


    @Test
    @DisplayName("Check valid user registration")
    public void registerUserSuccessIfSendAllValidFieldsTest(){
        userRegistrationModel = UserRegistrationModel.generateUser();

        response = authApi.registerUser(userRegistrationModel);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true));
    }


    @Test
    @DisplayName("Check impossible to signUp user with already existed email")
    public void registerUserWithEmailAndPassAlreadyUsedFailsTest(){
        userRegistrationModel = UserRegistrationModel.generateUser();
        authApi.registerUser(userRegistrationModel);

        response = authApi.registerUser(userRegistrationModel);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo(USER_ALREADY_EXISTS_MESSAGE));
    }


    @Test
    @DisplayName("Check impossible to signUp without email")
    public void registerUserWithoutEmailFailsOfRequiredFieldTest(){
        userRegistrationModel = UserRegistrationModel.generateUser();
        userRegistrationModel.setEmail(null);

        response = authApi.registerUser(userRegistrationModel);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo(THERE_ARE_NO_REQUIRED_FIELDS_MESSAGE));
    }


    @Test
    @DisplayName("Check impossible to signUp without password")
    public void registerUserWithoutPasswordFailsOfRequiredFieldTest(){
        userRegistrationModel = UserRegistrationModel.generateUser();
        userRegistrationModel.setPassword(null);

        response = authApi.registerUser(userRegistrationModel);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo(THERE_ARE_NO_REQUIRED_FIELDS_MESSAGE));
    }


    @Test
    @DisplayName("Check impossible to signUp without name")
    public void registerUserWithoutNameFailsOfRequiredFieldTest(){
        userRegistrationModel = UserRegistrationModel.generateUser();
        userRegistrationModel.setName(null);

        response = authApi.registerUser(userRegistrationModel);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo(THERE_ARE_NO_REQUIRED_FIELDS_MESSAGE));
    }
}