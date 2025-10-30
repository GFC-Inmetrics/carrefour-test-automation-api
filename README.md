# Carrefour Test Automation API

Projeto de testes automatizados de APIs — Banco Carrefour

## Visão Geral

Este projeto realiza testes automatizados de API utilizando Java 17, RestAssured, Cucumber e JUnit 4, com integração contínua configurada via GitHub Actions.

O objetivo é validar os endpoints da API pública **ServeRest**, cobrindo as operações de CRUD de usuários: criação, leitura, atualização e exclusão.

## Tecnologias Utilizadas

| Ferramenta | Finalidade |
|------------|------------|
| Java 17 | Linguagem base |
| Maven | Gerenciador de dependências e build |
| RestAssured | Requisições e validações HTTP |
| Cucumber (Gherkin) | Escrita de cenários em linguagem natural |
| JUnit 4 | Executor dos testes |
| Hamcrest | Matchers para validações |
| GitHub Actions | Integração contínua (CI) |

## Estrutura do Projeto

```
carrefour-test-automation-api/
├── src
│   ├── main
│   │   └── java
│   │       └── pages/
│   │           ├── BaseApi.java
│   │           ├── UserApi.java
│   │           └── AuthUtils.java
│   └── test
│       ├── java
│       │   ├── hooks/
│       │   │   └── UserHooks.java
│       │   ├── runners/
│       │   │   └── TestRunner.java
│       │   └── steps/
│       │       └── UserSteps.java
│       └── resources/
│           └── features/
│               ├── BuscarUsuarios.feature
│               ├── CadastroUsuario.feature
│               ├── EditarUsuario.feature
│               └── DeletarUsuario.feature
├── pom.xml
└── .github/workflows/ci.yml
```

## Estrutura de Código

### BaseApi.java
Define a URL base da API (`https://serverest.dev`) e configura o RestAssured.

### UserApi.java
Contém os métodos principais de requisição:

- `getUsers()` → Lista todos os usuários  
- `getUserById(String id)` → Busca usuário pelo ID  
- `createUser(String nome, String email, String password, String admin)` → Cria um novo usuário  
- `updateUser(String id, String nome, String email, String password, String admin)` → Atualiza dados do usuário  
- `deleteUser(String userId)` → Exclui usuário existente  

### AuthUtils.java
Realiza login automático e armazena o token JWT em cache para reutilização nos testes.

### UserSteps.java
Implementa os passos do Cucumber (Gherkin) correspondentes aos cenários definidos nas features.

### TestRunner.java
Executa os testes com Cucumber e JUnit, gerando relatórios em:

- `target/cucumber-report/html/cucumber-report.html`
- `target/cucumber-report.json`
- `target/cucumber.xml`

## Cenários Cucumber

### Busca de Usuários
- Buscar lista de usuários  
- Buscar usuário específico  
- Buscar usuário inexistente  
- Buscar usuário com ID incompleto  

### Cadastro de Usuários
- Criar novo usuário com sucesso  
- Criar usuário com e-mail duplicado  

### Exclusão de Usuários
- Deletar usuário existente  
- Deletar usuário inexistente  

### Atualização de Usuários
- Atualizar usuário existente  
- Atualizar usuário com dados inválidos  

## Execução Local

### Pré-requisitos
- Java 17 instalado  
- Maven configurado (`mvn -v`)  
- Conexão com a internet (a API é pública)  

### Executar testes
```bash
mvn clean test
```

### Relatórios gerados
- HTML: `target/cucumber-report/html/cucumber-report.html`  
- JSON: `target/cucumber-report.json`  
- XML (JUnit): `target/cucumber.xml`  
- Surefire: `target/surefire-reports/`

Abrir relatório HTML:
```bash
start target/cucumber-report/html/cucumber-report.html
```

## Integração Contínua (GitHub Actions)

A pipeline de CI é acionada automaticamente em push e pull request para a branch `main`.

### Etapas principais
- Checkout do código  
- Configuração do JDK 17  
- Build e execução dos testes (`mvn clean test`)  
- Upload automático dos relatórios (`actions/upload-artifact@v4`)  

### Artefatos gerados
Após a execução, os relatórios ficam disponíveis na aba **Actions → Job → Artifacts**:
- `cucumber-reports`  
- `surefire-reports`  

## Boas Práticas Implementadas
- Reutilização de código com `BaseApi` e `AuthUtils`  
- Token JWT armazenado em cache (login único por execução)  
- Geração dinâmica de massa de teste (e-mails aleatórios)  
- Validações robustas com Hamcrest  
- Pipeline CI automatizada e integrada ao GitHub  

## Referências
- [ServeRest API](https://serverest.dev)  
- [Cucumber Docs](https://cucumber.io/docs)  
- [RestAssured Docs](https://rest-assured.io)  
- [GitHub Actions Docs](https://docs.github.com/en/actions)  

## Autor
**Guilherme Ferreira Clemente**

[Download da documentação do projeto](https://github.com/GFC-Inmetrics/carrefour-test-automation-api/raw/main/Projeto%20API%20-%20Carrefour.docx)


