package tests;

import api.AuthApi;
import api.OrdersApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.OrderCreationModel;
import models.UserLoginModel;
import models.UserRegistrationModel;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;


public class GetUsersOrdersListTest {
    private AuthApi authApi;
    private OrdersApi ordersApi;
    private String token;
    public static final String UNAUTHORIZED_MESSAGE = "You should be authorised";


    @Before
    public void setup(){
        authApi = new AuthApi();
        ordersApi = new OrdersApi();

        UserRegistrationModel user = UserRegistrationModel.generateUser();
        authApi.registerUser(user);
        token = authApi.loginUser(new UserLoginModel(user))
                .jsonPath().getString("accessToken");
    }


    @After
    public void cleanup(){
        if (token != null) authApi.deleteUser(token);
    }


    @Test
    @DisplayName("Check if user don't have orders then response is success and has empty list of orders")
    public void getUsersOrdersListReturnsSuccessAnswerAndEmptyListIfNoOrdersExistTest(){
        Response response = ordersApi.getUsersOrders(token);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("orders", hasSize(0));
    }


    @Test
    @DisplayName("Check request of user's orders list returns all user's orders")
    public void getUsersOrdersListReturnsAllOrdersOfUserTest(){
        Random random = new Random();
        int countOfOrders = random.nextInt(4)+2;
        for (int i = 0; i < countOfOrders; i++) {
            OrderCreationModel order = OrderCreationModel.createOrder(2);
            ordersApi.createOrder(token, order);
        }

        Response response = ordersApi.getUsersOrders(token);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("orders", hasSize(countOfOrders));
    }


    @Test
    @DisplayName("Check if unauthorized impossible to get orders list")
    public void getUsersOrdersWithoutAuthorizationReturnUnauthorizedErrorTest(){
        Response response = ordersApi.getUsersOrders("");

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo(UNAUTHORIZED_MESSAGE));
    }
}