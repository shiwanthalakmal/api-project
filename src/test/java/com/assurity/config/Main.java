package com.assurity.config;

import java.util.List;



import static com.jayway.restassured.RestAssured.given;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;

public class Main
{
  public static Response doGetRequest(String endpoint) {
    RestAssured.defaultParser = Parser.JSON;

    return
        given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).
            when().get(endpoint).
            then().contentType(ContentType.JSON).extract().response();
  }

  public static void main(String[] args) {
    Response response = doGetRequest("https://jsonplaceholder.typicode.com/users");

    List<String> jsonResponse = response.jsonPath().getList("$");

    System.out.println(jsonResponse.size());

  }
}
