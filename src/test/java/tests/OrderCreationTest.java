package tests;

import api.AuthApi;
import api.OrdersApi;
import helpers.Generation;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.OrderCreationModel;
import models.UserLoginModel;
import models.UserRegistrationModel;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Random;

import static models.OrderCreationModel.createOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;


public class OrderCreationTest {
    AuthApi authApi;
    OrdersApi ordersApi;
    String authToken;


    @Before
    public void setUp(){
        authApi = new AuthApi();
        ordersApi = new OrdersApi();
    }


    @After
    public void cleanup(){
        if (authToken != null) authApi.deleteUser(authToken);
    }


    @Step("Register and Authorize user")
    private void createAndAuthorizeUser(){
        UserRegistrationModel userRegistrationModel = UserRegistrationModel.generateUser();
        authApi.registerUser(userRegistrationModel);
        Response response = authApi.loginUser(new UserLoginModel(userRegistrationModel));
        authToken = response.jsonPath().getString("accessToken");
    }


    @Test
    @DisplayName("Check order creation with 1 ingredient")
    public void createOrderWithOneIngredientAndAuthorizedOrderCreatedTest(){
        createAndAuthorizeUser();

        OrderCreationModel order = createOrder(1);

        Response response = ordersApi.createOrder(authToken, order);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true));
    }


    @Test
    @DisplayName("Check order creation with multiple ingredients")
    public void createOrderWithSeveralIngredientAndAuthorizedOrderCreatedTest(){
        createAndAuthorizeUser();
        Random random = new Random();
        int countOfIngredients = random.nextInt(6)+2;

        OrderCreationModel order = createOrder(countOfIngredients);

        Response response = ordersApi.createOrder(authToken, order);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("order.ingredients", hasSize(countOfIngredients));
    }


    @Test
    @DisplayName("Check if no ingredients then error on order creation")
    public void createOrderWithoutIngredientsAndAuthorizedOrderReturnsBadRequestTest(){
        createAndAuthorizeUser();
        OrderCreationModel order = new OrderCreationModel();

        Response response = ordersApi.createOrder(authToken, order);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("success", is(false));
    }


    @Test
    @DisplayName("Check error if invalid ingredient on order creation")
    public void createOrderWithIncorrectIngredientAndAuthorizedOrderReturnsInternalServerErrorTest(){
        createAndAuthorizeUser();
        OrderCreationModel order = new OrderCreationModel();
        order.setIngredients(List.of(Generation.getRandomHash()));

        Response response = ordersApi.createOrder(authToken, order);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }


    @Test
    @DisplayName("Check unauthorized error if try to create order without authorization")
    public void createOrderWithSeveralIngredientAndUnauthorizedOrderCreatedTest(){
        Random random = new Random();
        int countOfIngredients = random.nextInt(6)+2;
        OrderCreationModel order = createOrder(countOfIngredients);

        Response response = ordersApi.createOrder(order);

        response.then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true));
    }
}