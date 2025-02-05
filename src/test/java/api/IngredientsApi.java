package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class IngredientsApi extends RestApi{
    public static final String GET_INGREDIENTS_URL = "/ingredients";


    @Step("Send request to get hash of ingredients")
    public Response getHashOfIngredients(){
        Response response = given()
                .spec(requestSpecification())
                .and()
                .when()
                .get(GET_INGREDIENTS_URL);

        return response;
    }
}