
  Feature: Exclusão de usuários

    Scenario: Deletar usuário existente
      Given que um usuário novo é cadastrado
      When o delete é executado
      Then o usuário é deletado com sucesso

    Scenario: Deletar usuário inexistente
      When um usuário com ID inválido é deletado
      Then a API retorna que nenhum regitro foi excluído