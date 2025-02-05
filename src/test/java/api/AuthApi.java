package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.UserDto;
import models.UserLoginModel;
import models.UserRegistrationModel;

import static io.restassured.RestAssured.given;


public class AuthApi extends RestApi {
    public static final String USER_REGISTRATION_URL = "/auth/register";
    public static final String USER_DELETE_URL = "/auth/user";
    public static final String USER_LOGIN_URL = "/auth/login";
    public static final String CHANGE_USER_DATA_URL = "/auth/user";


    @Step("Send POST request to register user")
    public Response registerUser(UserRegistrationModel user){
        Response response = given()
                .spec(requestSpecification())
                .and()
                .body(user)
                .when()
                .post(USER_REGISTRATION_URL);

        return response;
    }


    @Step("Send request to delete user")
    public Response deleteUser(String token){
        Response response = given()
                .spec(requestSpecification())
                .and()
                .header("Authorization", token)
                .when()
                .delete(USER_DELETE_URL);
        System.out.println(response.body().asString());

        return response;
    }


    @Step("Send request to login sign-in")
    public Response loginUser(UserLoginModel user){
        Response response = given()
                .spec(requestSpecification())
                .and()
                .body(user)
                .when()
                .post(USER_LOGIN_URL);
        System.out.println(response.body().asString());

        return response;
    }


    @Step("Send patch request to change user data")
    public Response changeUserData(String token, UserDto userDto){
        Response response = given()
                .spec(requestSpecification())
                .and()
                .header("Authorization", token)
                .and()
                .body(userDto)
                .when()
                .patch(CHANGE_USER_DATA_URL);
        System.out.println(response.body().asString());

        return response;
    }
}