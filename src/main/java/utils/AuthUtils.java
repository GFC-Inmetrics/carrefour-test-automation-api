package utils;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthUtils {

    // Guarda o token após o primeiro login
    private static String cachedToken;

    // Retorna o token (faz login apenas uma vez)
    public static String getToken() {
        if (cachedToken == null) {
            cachedToken = loginAndGetToken();
        }
        return cachedToken;
    }

    // Faz o login e retorna o token
    private static String loginAndGetToken() {
        String payload = """
                {
                    "email": "test-gfc@automation.com",
                    "password": "abcdef"
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(payload)
                .post("https://serverest.dev/login");

        System.out.println("Login response: " + response.asString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Falha no login: " + response.asString());
        }

        return response.jsonPath().getString("authorization");
    }

}

