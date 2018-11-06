package com.hellofresh.config;

import org.testng.annotations.DataProvider;

public class DataSupporter {

    @DataProvider(name = "dataProviderCode")
    public Object[][] dataProviderCode() {
        return new Object[][]{{"US"}, {"DE"}, {"GB"}};
    }

}
