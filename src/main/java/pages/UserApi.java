package pages;

import io.restassured.response.Response;
import utils.AuthUtils;
import hooks.UserHooks;

import java.util.List;
import java.util.Map;
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
        return given().get("/usuarios");
    }

    public Response getUserById(String id) {
        return given().get("/usuarios/" + id);
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
        return given()
                .header("Authorization", token)
                .delete("/usuarios/" + id);
    }

    public String generateRandomEmail() {
        return "user" + UUID.randomUUID() + "@teste.com";
    }

    public Response getUserCarts(String userId) {
        return given()
                .header("Authorization", token)
                .get("/carrinhos/usuario/" + userId);
    }

    public Response deleteCart(String cartId, String userToken) {
        return given()
                .header("Authorization", userToken)
                .delete("/carrinhos/" + cartId);
    }

    public Response deleteUserWithCart(String userId, String userToken) {
        return given()
                .header("Authorization", userToken)
                .delete("/usuarios/" + userId);
    }

    public Response createCart(String userToken) {
        String productId = getFirstProductId(); // busca um produto válido

        String body = String.format("""
        {
            "produtos": [
                {"idProduto": "%s", "quantidade": 1}
            ]
        }
        """, productId);

        return given()
                .header("Authorization", userToken)
                .contentType("application/json")
                .body(body)
                .post("/carrinhos");
    }


    public String getFirstProductId() {
        Response response = given()
                .get("/produtos");

        return response.jsonPath().getString("produtos[0]._id");
    }


}
