package models;

import api.IngredientsApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OrderCreationModel {
    private List<String> ingredients;


    public OrderCreationModel(List<String> ingredients) {
        this.ingredients = ingredients;
    }


    public OrderCreationModel(){}


    public List<String> getIngredients() {
        return ingredients;
    }


    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }


    @Step("Set order with ingredients")
    public static OrderCreationModel createOrder(int countOfIngredients){
        List<String> listOfIngredients = new ArrayList<>();
        for (int i =0; i < countOfIngredients; i++){
            listOfIngredients.add(getRandomIngredientHash());
        }

        OrderCreationModel order = new OrderCreationModel(listOfIngredients);

        return order;
    }


    @Step("Extract valid random ingrediend hash")
    private static String getRandomIngredientHash(){
        IngredientsApi ingredientsApi = new IngredientsApi();
        Response response = ingredientsApi.getHashOfIngredients();
        String responseAsString = response.body().asString();
        List<String> hashes = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"_id\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(responseAsString);
        while (matcher.find()){
            hashes.add(matcher.group(1));
        }
        int countOfIngredients = hashes.size();
        Random random = new Random();
        int ingredientPosition = random.nextInt(countOfIngredients);

        return hashes.get(ingredientPosition);
    }
}