package hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import pages.UserApi;
import utils.AuthUtils;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserHooks {

    private static final UserApi userApi = new UserApi();

    public static String userId;
    public static String email;
    public static String token;



        // Armazena o ID do usuário criado com carrinho
        public static String userIdWithCart;

        // Armazena o token do usuário criado com carrinho
        public static String userTokenWithCart;

        // Armazena a resposta do delete para validação
        public static Response deleteResponse;



    public static String getUserId() {
        return userId;
    }

    @Before
    public void criarUsuarioDeTeste(Scenario scenario) {
        // ⚠️ Ignora o hook automático para o cenário "Deletar usuário com carrinho vinculado"
        if (scenario.getName().equals("Deletar usuário com carrinho vinculado")) {
            return;
        }

        // Usuário automático para os demais cenários
        String adminToken = AuthUtils.getToken();
        email = userApi.generateRandomEmail();
        Response response = userApi.createUser("Usuário de Teste", email, "123456", "true");

        if (response.statusCode() != 201) {
            throw new RuntimeException("Falha ao criar usuário de teste: " + response.asString());
        }

        userId = response.jsonPath().getString("_id");
        System.out.println("Usuário de teste criado: " + email + " | ID: " + userId);

        // Login com o próprio usuário
        token = loginUser(email, "123456");
    }

    private String loginUser(String email, String password) {
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

        if (response.statusCode() != 200) {
            throw new RuntimeException("Falha ao logar usuário: " + response.asString());
        }

        String userToken = response.jsonPath().getString("authorization");
        System.out.println("Token do usuário obtido: " + userToken);
        return userToken;
    }

    @After
    public void deletarUsuarioDeTeste() {
        if (userId != null) {
            // Deleta todos os carrinhos antes de deletar o usuário
            Response existingCart = userApi.getUserCarts(userId);
            List<Map<String, Object>> carrinhos = existingCart.jsonPath().getList("carrinhos");
            if (carrinhos != null && !carrinhos.isEmpty()) {
                for (Map<String, Object> c : carrinhos) {
                    String cartId = (String) c.get("_id");
                    userApi.deleteCart(cartId, token);
                }
                System.out.println("🧹 Carrinhos deletados antes de remover o usuário: " + userId);
            }

            // Deleta o usuário
            Response response = userApi.deleteUser(userId);

            if (response.statusCode() == 200) {
                System.out.println("Usuário de teste deletado: " + email);
            } else {
                System.out.println("Falha ao deletar usuário: " + response.asString());
            }

            userId = null; // limpa o hook
            email = null;
            token = null;
        }
    }
}
