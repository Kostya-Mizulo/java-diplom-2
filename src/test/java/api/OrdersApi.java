package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.OrderCreationModel;

import static io.restassured.RestAssured.given;


public class OrdersApi extends RestApi{
    public static final String ORDER_URL = "/orders";


    @Step("Create order with authorized user")
    public Response createOrder(String token, OrderCreationModel order){
        Response response = given()
                .spec(requestSpecification())
                .and()
                .header("Authorization", token)
                .and()
                .body(order)
                .when()
                .post(ORDER_URL);

        return response;
    }


    @Step("Create order without authorization")
    public Response createOrder(OrderCreationModel order){
        Response response = given()
                .spec(requestSpecification())
                .and()
                .body(order)
                .when()
                .post(ORDER_URL);

        return response;
    }


    @Step("Get orders of current user")
    public Response getUsersOrders(String token){
        Response response = given()
                .spec(requestSpecification())
                .and()
                .header("Authorization", token)
                .when()
                .get(ORDER_URL);

        return response;
    }
}