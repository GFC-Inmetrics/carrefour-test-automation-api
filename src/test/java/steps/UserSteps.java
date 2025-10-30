package steps;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import pages.UserApi;
import pages.AuthUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserSteps {

    UserApi userApi = new UserApi();
    Response response;
    private String emailDuplicado;

    // CENÁRIOS DE CRIAÇÃO
    @Given("que um usuário novo é cadastrado")
    public void criarUsuario() {
        AuthUtils.init();
    }

    @Then("o usuário é criado com sucesso")
    public void validarCriacao() {
        Assert.assertNotNull(AuthUtils.getUserId());
    }

    @Given("que um usuário é criado com email existente")
    public void criarUsuarioEmailDuplicado() {
        emailDuplicado = "duplicado" + java.util.UUID.randomUUID() + "@testqa.com";
        // Cria o primeiro usuário
        Response primeira = userApi.createUser("TestGFC", emailDuplicado, "123456", "true");
        if (primeira.statusCode() != 201) {
            throw new RuntimeException("Falha ao criar usuário base: " + primeira.asString());
        }
        // Tenta duplicar
        response = userApi.createUser("TestGFC2", emailDuplicado, "123456", "true");
        System.out.printf("Status duplicado: %d | Resposta: %s%n",
        response.statusCode(), response.asString());
    }



    @Then("a API retorna um erro de email duplicado")
    public void validarEmailDuplicado() {
        response.then().statusCode(400);
        String message = response.jsonPath().getString("message");
        System.out.println(" Mensagem retornada: " + message);
        assertThat(message, containsString("Este email já está sendo usado"));
    }

    // CENÁRIOS DE BUSCA
    @When("todos os usuários são buscados")
    public void buscarTodosUsuarios() {
        response = userApi.getUsers();
    }

    @Then("a lista de usuários é retornada")
    public void validarListaUsuarios() {
        response.then().statusCode(200);
        assertThat(response.jsonPath().getList("usuarios"), not(empty()));
    }

    @When("o usuário é consultado pelo ID")
    public void buscarUsuario() {
        response = userApi.getUserById(AuthUtils.getUserId());
    }

    @Then("o usuário é retornado")
    public void validarBusca() {
        response.then().statusCode(200);
        assertThat(response.jsonPath().getString("_id"), equalTo(AuthUtils.getUserId()));
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
    public void validarIdIncorreto() {
        response.then().statusCode(400);
        assertThat(response.asString(), containsString("id deve ter exatamente"));
    }

    // CENÁRIOS DE ATUALIZAÇÃO
    @When("a atualização é aplicada")
    public void atualizarUsuario() {
        response = userApi.updateUser(AuthUtils.getUserId(), "Atualizado", "novo@qa.com", "654321", "false");
    }

    @Then("o usuário é atualizado com sucesso")
    public void validarAtualizacao() {
        response.then().statusCode(200);
        assertThat(response.jsonPath().getString("message"), equalTo("Registro alterado com sucesso"));
    }

    @When("o usuário é atualizado com email inválido")
    public void atualizarUsuarioComEmailInvalido() {
        response = userApi.updateUser(AuthUtils.getUserId(), "Novo Nome", "emailinvalido", "123456", "true");
    }

    @Then("a API retorna erro de validação")
    public void validarErroEmailInvalido() {
        response.then().statusCode(400);
    }

    @When("o delete é executado")
    public void deletarUsuarioExistente() {
        response = userApi.deleteUser(AuthUtils.getUserId());
    }

    @Then("o usuário é deletado com sucesso")
    public void validarExclusao() {
        response.then().statusCode(200);
        assertThat(response.jsonPath().getString("message"), containsString("Registro excluído"));
    }

    @When("um usuário com ID inválido é deletado")
    public void deletarUsuarioInexistente() {
        String idInvalido = "0uxuPY0cbmQh21021id21";
        response = userApi.deleteUser(idInvalido);
        System.out.println("Status: " + response.statusCode());
        System.out.println("Body: " + response.asString());
    }


    @Then("a API retorna que nenhum regitro foi excluído")
    public void validarMensagemAoDeletarUsuarioInexistente() {
        response.then()
                .statusCode(200)
                .body("message", equalTo("Nenhum registro excluído"));
    }

}
