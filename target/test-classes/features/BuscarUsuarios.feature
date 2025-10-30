
Feature: Busca de usuarios

Scenario: Buscar a lista de usuários
When todos os usuários são buscados
Then a lista de usuários é retornada

Scenario: Buscar usuário específico
Given que um usuário novo é cadastrado
When o usuário é consultado pelo ID
Then o usuário é retornado

Scenario: Buscar usuário inexistente
When o usuário é buscado com ID inválido
Then erro de usuário não encontrado é exibido


Scenario: Buscar usuário utilizando quantidade de caracteres incorreta pelo campo ID
When o usuário é buscado com ID incompleto
Then erro de quantidade de caracteres incorreta é exibido
