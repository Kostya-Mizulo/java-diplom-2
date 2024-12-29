package tests;

import api.AuthApi;
import helpers.Generation;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.UserDto;
import models.UserLoginModel;
import models.UserRegistrationModel;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;


public class ChangeUserDataTest {
    public static final String UNAUTHORIZED_MESSAGE = "You should be authorised";
    public static final String FORBIDDEN_MESSAGE = "User with such email already exists";
    private String userNameInitial;
    private String userEmailInitial;
    private String accessToken;
    AuthApi authApi;


    @Before
    public void setup(){
        authApi = new AuthApi();
        UserRegistrationModel userRegistrationModel = UserRegistrationModel.generateUser();
        userNameInitial = userRegistrationModel.getName();
        userEmailInitial = userRegistrationModel.getEmail();
        authApi.registerUser(userRegistrationModel);
        Response loginResponse = authApi.loginUser(new UserLoginModel(userRegistrationModel));
        accessToken = loginResponse.jsonPath().getString("accessToken");
    }


    @After
    public void cleanup(){
        authApi.deleteUser(accessToken);
    }


    @Test
    @DisplayName("Check user email could be changed")
    public void changeUserDataWithEmailChangesEmailCorrectlyTest(){
        UserDto userDto = new UserDto(Generation.getEmail(), null);
        Response response = authApi.changeUserData(accessToken, userDto);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("user.email", equalTo(userDto.getEmail()));
    }


    @Test
    @DisplayName("Check user name could be changed")
    public void changeUserDataWithNameChangesEmailCorrectlyTest(){
        UserDto userDto = new UserDto(null, Generation.getName());
        Response response = authApi.changeUserData(accessToken, userDto);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("user.name", equalTo(userDto.getName()));
    }


    @Test
    @DisplayName("Check user email and name could be changed together in one request")
    public void changeUserDataWithNameAndEmailChangesNameAndEmailCorrectlyTest(){
        UserDto userDto = new UserDto(Generation.getEmail(), Generation.getName());
        Response response = authApi.changeUserData(accessToken, userDto);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("user.name", equalTo(userDto.getName()))
                .and()
                .body("user.email", equalTo(userDto.getEmail()));
    }


    @Test
    @DisplayName("Check impossible to change user data without authorization")
    public void changeUserDataWithoutAuthorizationReturnsUnauthorizedTest(){

        UserDto userDto = new UserDto(Generation.getEmail(), Generation.getName());
        Response response = authApi.changeUserData("", userDto);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo(UNAUTHORIZED_MESSAGE));
    }


    @Test
    @DisplayName("Check impossible to change email if new value already exists for another user")
    public void changeUserDataWithEmailInUseReturnsForbiddenTest(){
        UserRegistrationModel newUser = UserRegistrationModel.generateUser();
        String accessTokenOfNewUser = authApi.registerUser(newUser).jsonPath().getString("accessToken");
        String emailInUse = newUser.getEmail();

        UserDto userDto = new UserDto(emailInUse, Generation.getName());
        Response response = authApi.changeUserData(accessToken, userDto);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo(FORBIDDEN_MESSAGE));

        authApi.deleteUser(accessTokenOfNewUser);
    }
}