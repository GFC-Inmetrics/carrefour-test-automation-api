Feature: Atualização de dados de usuários

Scenario: Atualizar usuário existente
Given que um usuário novo é cadastrado
When a atualização é aplicada
Then o usuário é atualizado com sucesso

Scenario: Atualizar usuário com dados inválidos
Given que um usuário novo é cadastrado
When o usuário é atualizado com email inválido
Then a API retorna erro de validação