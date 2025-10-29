package utils;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthUtils {

    private static String cachedToken;

    public static String getToken() {
        if (cachedToken == null) {
            cachedToken = loginAndGetToken();
        }
        return cachedToken;
    }

    private static String loginAndGetToken() {
        String email = "test-gfc@automation.com";
        String password = "abcdef";

        String payload = String.format("""
                {
                    "email": "%s",
                    "password": "%s"
                }
                """, email, password);

        Response response = given()
                .contentType("application/json")
                .body(payload)
                .post("https://serverest.dev/login");

        // Se o login falhar, cria o usuário e tenta novamente
        if (response.statusCode() != 200) {
            System.out.println(" Login falhou, criando usuário de autenticação...");

            // Cria o usuário admin padrão
            String createUserBody = String.format("""
                    {
                        "nome": "Usuário Automático Token",
                        "email": "%s",
                        "password": "%s",
                        "administrador": "true"
                    }
                    """, email, password);

            Response createResponse = given()
                    .contentType("application/json")
                    .body(createUserBody)
                    .post("https://serverest.dev/usuarios");

            if (createResponse.statusCode() != 201 && createResponse.statusCode() != 400) {
                throw new RuntimeException(" Falha ao criar usuário para autenticação: " + createResponse.asString());
            }

            // Tenta o login novamente
            response = given()
                    .contentType("application/json")
                    .body(payload)
                    .post("https://serverest.dev/login");

            if (response.statusCode() != 200) {
                throw new RuntimeException(" Falha no login mesmo após criar o usuário: " + response.asString());
            }
        }

        String token = response.jsonPath().getString("authorization");
        System.out.println(" Token obtido com sucesso!");
        return token;
    }


}
