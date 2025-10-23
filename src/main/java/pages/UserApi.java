package pages;

import io.restassured.response.Response;
import utils.AuthUtils;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class UserApi extends BaseApi {

    private String token = AuthUtils.getToken();


    public Response createUser(String nome, String email, String password, String admin) {
        String body = String.format("""
                {
                    "nome": "%s",
                    "email": "%s",
                    "password": "%s",
                    "administrador": "%s"
                }
                """, nome, email, password, admin);

        return given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/usuarios");
    }

    public Response getUsers() {
        return given()
                .get("/usuarios");
    }

    public Response getUserById(String id) {
        return given()
                .get("/usuarios/" + id);
    }

    public Response updateUser(String id, String nome, String email, String password, String admin) {
        String body = String.format("""
                {
                    "nome": "%s",
                    "email": "%s",
                    "password": "%s",
                    "administrador": "%s"
                }
                """, nome, email, password, admin);

        return given()
                .header("Authorization", token)
                .contentType("application/json")
                .body(body)
                .put("/usuarios/" + id);
    }

    public Response deleteUser(String id) {
        String token = AuthUtils.getToken();
        return given()
                .header("Authorization", token)
                .delete("/usuarios/" + id);
    }

    public String generateRandomEmail() {
        return "user" + UUID.randomUUID() + "@teste.com";
    }

    public Response deleteUserWithCart(String id) {
        String userCartToken = AuthUtils.getToken();
        return given()
                .header("Authorization", userCartToken)
                .delete("/usuarios/" + id);
    }
}
