package pages;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserApi extends BaseApi {

    private String token = AuthUtils.getToken();

    public Response getUsers() {
        return given()
                .header("Authorization", token)
                .get(BASE_URL + "/usuarios");
    }

    public Response getUserById(String id) {
        return given()
                .header("Authorization", token)
                .get(BASE_URL + "/usuarios/" + id);
    }

    public Response createUser(String nome, String email, String password, String admin) {
        return given()
                .contentType("application/json") // adiciona o contentType, que é o correto
                .body("{\"nome\":\"" + nome + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\",\"administrador\":\"" + admin + "\"}")
                .post(BASE_URL + "/usuarios");
    }


    public Response updateUser(String id, String nome, String email, String password, String admin) {
        return given()
                .header("Authorization", token)
                .contentType("application/json")
                .body("{\"nome\":\"" + nome + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\",\"administrador\":\"" + admin + "\"}")
                .put(BASE_URL + "/usuarios/" + id);
    }

        public Response deleteUser(String userId) {
        return given()
                .baseUri(BASE_URL)               // Garante que a URL base seja correta
                .header("accept", "application/json")
                .when()
                .delete("/usuarios/{id}", userId) // Passa o id via template
                .then()
                .log().all()                      // Mostra no console a requisição e resposta
                .extract()
                .response();
    }


}
