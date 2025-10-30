package pages;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class AuthUtils {

    private static String token;
    private static String userId;
    private static final String BASE_URL = "https://serverest.dev";

    // Cria usuário e obtém token
    public static void init() {
        if (token == null || userId == null) {
            String email = "user_" + System.currentTimeMillis() + "@qa.com";

            // Criação do usuário
            Response response = given()
                    .contentType("application/json")
                    .body("{\"nome\": \"Usuario Teste\", \"email\": \"" + email + "\", \"password\": \"123456\", \"administrador\": \"true\"}")
                    .post(BASE_URL + "/usuarios");

            if (response.statusCode() != 201) {
                throw new RuntimeException(" Falha ao criar usuário. Status: " + response.statusCode() + " | " + response.asString());
            }

            userId = response.jsonPath().getString("_id");

            // Login para obter token
            Response login = given()
                    .contentType("application/json")
                    .body("{\"email\": \"" + email + "\", \"password\": \"123456\"}")
                    .post(BASE_URL + "/login");

            token = login.jsonPath().getString("authorization");
            System.out.println(" Usuário criado e autenticado: " + userId);
        }
    }

    public static String getToken() {
        if (token == null) init();
        return token;
    }

    public static String getUserId() {
        if (userId == null) init();
        return userId;
    }

    // Exclusão automática após cada cenário
    public static void deleteUser() {
        if (userId != null && token != null) {
            given()
                    .header("Authorization", token)
                    .delete(BASE_URL + "/usuarios/" + userId);
            System.out.println(" Usuário deletado: " + userId);
            token = null;
            userId = null;
        }
    }
}
