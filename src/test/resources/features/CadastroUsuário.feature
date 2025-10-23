Feature: Cadastro de usuários

Scenario: Criar um novo usuário com sucesso
Given que um usuário novo é cadastrado
Then o usuário é criado com sucesso

Scenario: Criar usuário com email duplicado
Given que um usuário é criado com email existente
Then a API retorna um erro de email duplicado