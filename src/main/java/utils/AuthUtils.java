package utils;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthUtils {

    public static String getToken() {
        String payload = """
                {
                    "email": "jody_gulgowski11@hotmail.com",
                    "password": "123456"
                }
                """;
        Response response = given()
                .contentType("application/json")
                .body(payload)
                .post("https://serverest.dev/login");

        return response.jsonPath().getString("authorization");
    }
}
