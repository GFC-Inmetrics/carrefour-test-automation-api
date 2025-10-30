package hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import pages.AuthUtils;

public class UserHooks {

    @Before
    public void beforeScenario() {
        AuthUtils.init(); // cria e autentica o usuário
    }

    @After
    public void afterScenario() {
        AuthUtils.deleteUser(); // exclui o usuário criado
    }
}
