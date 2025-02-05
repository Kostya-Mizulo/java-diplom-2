package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


public class RestApi {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site/api";


    protected RequestSpecification requestSpecification(){
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .build()
                .log().all();
    }
}