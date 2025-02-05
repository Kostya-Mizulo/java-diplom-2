package helpers;

import com.github.javafaker.Faker;


public class Generation {
    public static String getEmail(){
        Faker faker = new Faker();

        return faker.internet().emailAddress();
    }


    public static String getPassword(){
        Faker faker = new Faker();

        return faker.internet().password(8, 16);
    }


    public static String getName(){
        Faker faker = new Faker();

        return faker.name().firstName();
    }


    public static String getRandomHash(){
        Faker faker = new Faker();
        int hashInt = faker.gameOfThrones().character().hashCode();

        return Integer.toString(hashInt);
    }
}