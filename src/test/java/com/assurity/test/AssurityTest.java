package com.assurity.test;

import static org.junit.Assert.assertTrue;

import com.assurity.config.API;
import com.assurity.config.TestNGListener;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.ValidatableResponse;
import org.apache.log4j.Logger;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.jayway.restassured.path.json.JsonPath.from;

/**
 * @author shiwantha
 * @since 2018
 * Test suite files with includes all implemented acceptance criteria
 */
public class AssurityTest
{
    final static Logger log = Logger.getLogger(AssurityTest.class);

    /**
     * The output of the code passes the assertion functional nun-functional verification.
     * Verify api response comes within given time period and contains customer name records.
     * Q1 : Name = "Carbon credits"
     */
    @Test(groups = {"BAT"})
    public void test_VerifyCustomerNameAvailability()
    {
        API.response.then().log().ifValidationFails().
                time(Matchers.lessThan(5L), TimeUnit.SECONDS).
                statusCode(200).
                assertThat().body("Name", Matchers.equalTo("Carbon credits"));
        log.info("log : finished first api test cases - verify name availability...");
    }

    /**
     * The output of the code passes the assertion functional nun-functional verification.
     * Verify api response comes within given time period and contains Can Re-List records
     * Q2 : CanRelist = true
     */
    @Test(groups = {"BAT"})
    public void test_VerifyCanReListAvailability()
    {
        API.response.then().log().ifValidationFails().
                time(Matchers.lessThan(5L), TimeUnit.SECONDS).
                statusCode(200).
                assertThat().body("CanRelist", Matchers.equalTo(true));
        log.info("log : finished second api test cases - verify can-re-list status...");
    }

    /**
     * The output of the code passes the assertion conditions based api contains verification.
     * Promotions element with Name = "Gallery" has a Description that contains the text "2x larger image".
     * Q3 : Promotions -> { it.Name == 'Gallery' }.Description
     */
    @Test(groups = {"BAT"})
    public void test_VerifyPromotionsDetailsAvailability()
    {
        List<String> PromotionsDscr = from(API.response.asString()).getList("Promotions.findAll { it.Name == 'Gallery' }.Description");
        Assert.assertTrue(PromotionsDscr.toString().contains("2x larger image"), "Verify Description that contains the text 2x larger image");
        log.info("log : finished third api test cases - verify condition based promotion-description status...");
    }


    /**
     * The output of the code passes the assertion and it also prints the acceptance criteria details retrieved from the Response
     * and convert into json object. all verification will happen by referring json path and this is a alternative way to verify response contains
     */
    @Test(groups = {"BAT", "REG"})
    public void test_VerifyAllAcceptanceCriteriaUsing_JsonPath()
    {
        JsonPath jsonPathEvaluator = API.response.jsonPath();

        Assert.assertEquals(jsonPathEvaluator.get("Name"), "Carbon credits", "Verify Customer Name ...");
        Assert.assertEquals(jsonPathEvaluator.get("CanRelist").toString(), "true", "Verify Can Re-list ...");

        List<String> PromotionsDscr = from(API.response.asString()).getList("Promotions.findAll { it.Name == 'Gallery' }.Description");
        Assert.assertTrue(PromotionsDscr.toString().contains("2x larger image"), "Verify Promotions element with Name = Gallery, has a Description that contains the text 2x larger image");
        log.info("log : finished all above test cases using json-path approach - alternative way to verify api response");
    }
}
