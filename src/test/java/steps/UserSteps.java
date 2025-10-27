package steps;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import pages.UserApi;
import utils.AuthUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserSteps {

    UserApi userApi = new UserApi();
    Response response;
    String userId;
    String email;


    @Given("que um usuário novo é cadastrado")
    public void criarUsuario() {
        email = userApi.generateRandomEmail();
        response = userApi.createUser("Test User", email, "123456", "true");
        userId = response.jsonPath().getString("_id");
    }

    @Then("o usuário é criado com sucesso")
    public void validarCriacao() {
        response.then().statusCode(201);
        assertThat(response.jsonPath().getString("message"), equalTo("Cadastro realizado com sucesso"));
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
    public void deletarUsuario() {
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

    @When("um usuário com carrinho vinculado é enviado para deleção")
    public void deletarUsuárioComCarrinho() {
        response = userApi.deleteUserWithCart("TaC95V2jUM93nrna");
    }
    @Then("a api retorna que a ação não é permitida")
    public void ValidarMensagemCarrinhoCadastrado(){
        response.then().statusCode(400);
        System.out.println(response.asString());
        assertThat(response.jsonPath().getString("message"), containsString("Não é permitido excluir usuário com carrinho cadastrado"));
    }
}
