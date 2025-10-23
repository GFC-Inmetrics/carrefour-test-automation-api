package pages;

import io.restassured.RestAssured;

public class BaseApi {
    protected String BASE_URL = "https://serverest.dev";

    public BaseApi() {
        RestAssured.baseURI = BASE_URL;
    }
}
