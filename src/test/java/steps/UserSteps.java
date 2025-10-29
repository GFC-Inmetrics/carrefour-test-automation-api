package steps;

import hooks.UserHooks;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import pages.UserApi;
import utils.AuthUtils;


import java.util.List;
import java.util.Map;
import java.util.UUID;

import static hooks.UserHooks.token;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserSteps {

    UserApi userApi = new UserApi();
    Response response;
    String userId;
    String email;


    @Given("que um usuário novo é cadastrado")
    public void criarUsuario() {
        // Usa o ID do usuário criado automaticamente no @Before Hook
        userId = UserHooks.getUserId();
        System.out.println("Usuário do Hook: " + userId);
    }


    @Then("o usuário é criado com sucesso")
    public void validarCriacao() {
        String userId = UserHooks.getUserId();
        Assert.assertNotNull(userId);
        System.out.println(" Usuário criado e validado pelo Hook: " + userId);
    }

    @When("o usuário é consultado pelo ID")
    public void buscarUsuario() {
        response = userApi.getUserById(userId);
    }

    @Then("o usuário é retornado")
    public void validarBusca() {
        response.then().statusCode(200);
        assertThat(response.jsonPath().getString("_id"), equalTo(userId));
    }

    @When("a atualização é aplicada")
    public void atualizarUsuario() {
        response = userApi.updateUser(userId, "Atualizado", email, "654321", "false");
    }

    @Then("o usuário é atualizado com sucesso")
    public void validarAtualizacao() {
        response.then().statusCode(200);
        assertThat(response.jsonPath().getString("message"), equalTo("Registro alterado com sucesso"));
    }

    @When("o delete é executado")
    public void deletarUsuarioExistente() {
        response = userApi.deleteUser(userId);
    }

    @Then("o usuário é deletado com sucesso")
    public void validarExclusao() {
        response.then().statusCode(200);
        assertThat(response.jsonPath().getString("message"), equalTo("Registro excluído com sucesso"));
    }

    @Given("que um usuário é criado com email existente")
    public void criarUsuarioEmailDuplicado() {
        response = userApi.createUser("Fulano", "fulano@qa.com", "123456", "true");
    }

    @Then("a API retorna um erro de email duplicado")
    public void validarEmailDuplicado() {
        response.then().statusCode(400);
        assertThat(response.jsonPath().getString("message"), containsString("Este email já está sendo usado"));
    }

    @When("todos os usuários são buscados")
    public void buscarTodosUsuarios() {
        response = userApi.getUsers();
    }

    @Then("a lista de usuários é retornada")
    public void validarListaUsuarios() {
        response.then().statusCode(200);
        assertThat(response.jsonPath().getList("usuarios"), not(empty()));
    }

    @When("o usuário é buscado com ID inválido")
    public void buscarUsuarioIdInvalido() {
        response = userApi.getUserById("uSeRn0TcReAted00");
    }

    @Then("erro de usuário não encontrado é exibido")
    public void validarUsuarioNaoEncontrado() {
        response.then().statusCode(400);
        assertThat(response.jsonPath().getString("message"), containsString("Usuário não encontrado"));
    }

    @When("o usuário é buscado com ID incompleto")
    public void buscarUsuarioIdIncompleto() {
        response = userApi.getUserById("1nc0mpl3T0");
    }

    @Then("erro de quantidade de caracteres incorreta é exibido")
    public void QuantidadeCaracteresIncorreta() {
        response.then().statusCode(400);
        System.out.println(response.asString());
        assertThat(response.jsonPath().getString("id"), containsString("id deve ter exatamente 16 caracteres alfanuméricos"));
    }


    @When("o usuário é atualizado com email inválido")
    public void atualizarUsuarioComEmailInvalido() {
        response = userApi.updateUser(userId, "Novo Nome", "emailinvalido", "123456", "true");
    }

    @Then("a API retorna erro de validação")
    public void validarErroEmailInvalido() {
        response.then().statusCode(400);
        assertThat(response.body().asString(), containsString("email"));
    }

    @When("um usuário com ID inválido é deletado")
    public void deletarUsuarioInvalido() {
        response = userApi.deleteUser("id-nao-existe");
    }

    @Then("a API retorna que nenhum regitro foi excluído")
    public void validarMensagemAoDeletarUsuarioInexistente() {
        response.then().statusCode(200); // API ainda retorna 200 mesmo com ID inválido
        assertThat(response.jsonPath().getString("message"), containsString("Nenhum registro excluído"));
    }


    //   @Given("um carrinho é cadastrado para o usuário")
    //   public void usuarioComCarrinho() {
    //      // Criar usuário novo
    //       response = userApi.createUser("Teste", "teste@teste.com", "123", "true");
    //       userId = response.jsonPath().getString("_id");

    // }


    @When ("um usuário com carrinho vinculado é enviado para deleção")
    public void euTentoDeletarOUsuárioComCarrinhoVinculado() {
        UserApi userApi = new UserApi();

        Response deleteResponse = userApi.deleteUserWithCart(
                UserHooks.userIdWithCart,
                UserHooks.userTokenWithCart
        );

        // Guarda a resposta para a asserção
        UserHooks.deleteResponse = deleteResponse;
    }


    @Then("a api retorna que a ação não é permitida")
    public void validarMensagem() {
        response.then()
                .statusCode(400)
                .body("message", equalTo("Não é permitido excluir usuário com carrinho cadastrado"));
    }


    @Given("um usuário com carrinho vinculado")
    public void umUsuárioComCarrinhoVinculado() {
        UserApi userApi = new UserApi();

        // 1️⃣ Cria um usuário comum (não admin)
        String email = userApi.generateRandomEmail();
        Response createResponse = userApi.createUser("Usuário Carrinho", email, "123456", "false");
        Assert.assertEquals(201, createResponse.statusCode());

        String userId = createResponse.jsonPath().getString("_id");
        System.out.println("Usuário com carrinho criado: " + email + " | ID: " + userId);

        // 2️⃣ Faz login para obter o token desse usuário
        Response loginResponse = given()
                .contentType("application/json")
                .body("{\"email\": \"" + email + "\", \"password\": \"123456\"}")
                .post("/login");

        String userToken = "Bearer " + loginResponse.jsonPath().getString("authorization");
        System.out.println("Token do usuário com carrinho: " + userToken);

        // 3️⃣ Cria o carrinho para esse usuário usando o token dele
        Response cartResponse = userApi.createCart(userToken);

        // 👇 imprime a resposta completa da API antes do assert
        System.out.println("Resposta da criação do carrinho:");
        System.out.println(cartResponse.getBody().asString());

        Assert.assertEquals(201, cartResponse.statusCode());
        System.out.println("Carrinho criado para o usuário.");

        // 4️⃣ Guarda informações para os próximos steps (por exemplo, em variáveis estáticas)
        UserHooks.userIdWithCart = userId;
        UserHooks.userTokenWithCart = userToken;
    }


}
