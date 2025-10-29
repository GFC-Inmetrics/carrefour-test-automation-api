
  Feature: Exclusão de usuários

    Scenario: Deletar usuário existente
      Given que um usuário novo é cadastrado
      When o delete é executado
      Then o usuário é deletado com sucesso

    Scenario: Deletar usuário inexistente
      When um usuário com ID inválido é deletado
      Then a API retorna que nenhum regitro foi excluído

    Scenario: Deletar usuário com carrinho vinculado
      Given um usuário com carrinho vinculado
      When um usuário com carrinho vinculado é enviado para deleção
      Then a api retorna que a ação não é permitida