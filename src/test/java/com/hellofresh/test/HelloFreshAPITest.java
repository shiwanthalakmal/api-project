package com.hellofresh.test;

import static com.jayway.restassured.path.json.JsonPath.from;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.hellofresh.config.API;
import com.hellofresh.config.DataSupporter;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HelloFreshAPITest
{
  final static Logger log = Logger.getLogger(HelloFreshAPITest.class);

  /**
   * The output of the code passes the assertion functional nun-functional verification.
   * Verify api response comes within given time period and contains Can Re-List records
   * Q: Get all countries and validate that US, DE and GB were returned in the response
   */
  @Test(groups = {"BAT"})
  public void get_all_countries_and_verify_given_content()
  {
    API.response = RestAssured.get("http://services.groupkt.com/country/get/all");
    API.response.then().log().ifValidationFails().time(Matchers.lessThan(5L), TimeUnit.SECONDS).statusCode(200).assertThat();

    final List<String> countryNameList = from(API.response.asString()).get("RestResponse.result.findAll { it.alpha2_code == 'US' }.alpha2_code");

    Assert.assertNotNull(countryNameList, "Error ! Country name list should not be null");
    Assert.assertEquals(countryNameList.size(),1, "Error ! Single alpha code allowed for single country");
    Assert.assertTrue(countryNameList.get(0).equals("US"), "Error ! Country not available for code : US");
  }

  /**
   * The output of the code passes the assertion functional nun-functional verification.
   * Verify api response comes within given time period and contains Can Re-List records
   * Q: Get each country (US, DE and GB) individually and validate the response
   */
  @Test(groups = {"BAT"})
  public void get_single_country_and_verify_response_content()
  {
    API.response = RestAssured.get("http://services.groupkt.com/country/get/iso2code/US");
    API.response.then().log().ifValidationFails().time(Matchers.lessThan(5L), TimeUnit.SECONDS).statusCode(200).assertThat();

    final List<String> successMessageList = from(API.response.asString()).get("RestResponse.messages");

    Assert.assertNotNull(successMessageList);
    Assert.assertEquals(successMessageList.size(),1);
    Assert.assertTrue(successMessageList.get(0).contains("Country found matching code [US]"), "Error ! Success message not available");

    Assert.assertTrue(from(API.response.asString()).get("RestResponse.result.name").toString().contains("United States of America"), "Error ! Country name is not available");
    Assert.assertTrue(from(API.response.asString()).get("RestResponse.result.alpha2_code").toString().contains("US"), "Error ! Alpha 2 is not available");
    Assert.assertTrue(from(API.response.asString()).get("RestResponse.result.alpha3_code").toString().contains("USA"), "Error ! Alpha 3 is not available");
  }

  /**
   * The output of the code passes the assertion functional nun-functional verification.
   * Verify api response comes within given time period and contains Can Re-List records
   * Q: Try to get information for inexistent countries and validate the response
   */
  @Test(groups = {"BAT"})
  public void get_non_exsiting_country_and_verify_response_content()
  {
    API.response = RestAssured.get("http://services.groupkt.com/country/get/iso2code/KD");
    API.response.then().log().ifValidationFails().time(Matchers.lessThan(5L), TimeUnit.SECONDS).statusCode(200).assertThat();

    final List<String> successMessageList = from(API.response.asString()).get("RestResponse.messages");

    Assert.assertNotNull(successMessageList, "Error ! Country name list should not be null");
    Assert.assertEquals(successMessageList.size(),1, "Error ! Single alpha code allowed for single country");
    Assert.assertTrue(successMessageList.get(0).contains("No matching country found for requested code"), "Error ! Country not available for code : KD");
  }

  /**
   * The output of the code passes the assertion functional nun-functional verification.
   * Verify api response comes within given time period and contains Can Re-List records
   * Q: Validate new country addition using POST(it will not work now, but no worries)
   */
  @Test(groups = {"BAT"})
  public void new_country_adding_using_post_request()
  {
    String end_point = "/register";

    RestAssured.baseURI ="http://services.groupkt.com/country"+end_point;
    RequestSpecification request = RestAssured.given();

    JSONObject requestParams = new JSONObject();
    requestParams.put("name", "Test Country");
    requestParams.put("alpha2_code", "TC");
    requestParams.put("alpha3_code", "TCY");

    request.header("Content-Type", "application/json");
    request.body(requestParams.toJSONString());
    Response response = request.post(end_point);

    Assert.assertEquals(response.getStatusCode(), "201");
    Assert.assertEquals( "Correct Success code was returned", response.jsonPath().get("SuccessCode"), "Erro ! POST Operation Not Success..");
  }
}
