# Carrefour Test Automation API
Projeto de testes automatizados de APIs — Banco Carrefour

---

## Visão Geral

Este projeto realiza **testes automatizados de API** utilizando **Java 17**, **RestAssured**, **Cucumber** e **JUnit**, com integração contínua configurada via **GitHub Actions**.

O objetivo é validar os endpoints da API pública [ServeRest](https://serverest.dev), cobrindo operações de CRUD de usuários: **criação, leitura, atualização e exclusão**.

---

## Tecnologias Utilizadas

| Ferramenta | Finalidade |
|-------------|-------------|
| **Java 17** | Linguagem base |
| **Maven** | Gerenciador de dependências e build |
| **RestAssured** | Requisições e validações HTTP |
| **Cucumber (Gherkin)** | Escrita de cenários em linguagem natural |
| **JUnit 4** | Executor dos testes |
| **Hamcrest** | Matchers para validações |
| **GitHub Actions** | Integração contínua (CI) |

---

## Estrutura do Projeto

```bash
CarrefourApiTestAutomation/
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── pages/
│   │   │   │   ├── BaseApi.java
│   │   │   │   └── UserApi.java
│   │   │   └── utils/
│   │   │       └── AuthUtils.java
│   └── test
│       ├── java
│       │   ├── runners/
│       │   │   └── TestRunner.java
│       │   └── steps/
│       │       └── UserSteps.java
│       └── resources/
│           └── features/
│               ├── cadastro_usuarios.feature
│               ├── busca_usuarios.feature
│               ├── exclusao_usuarios.feature
│               └── atualizacao_usuarios.feature
├── pom.xml
└── .github/workflows/ci.yml

Estrutura de Código
BaseApi.java
Define a URL base da API (https://serverest.dev) e configura o RestAssured.

UserApi.java
Contém todos os métodos de requisição:

createUser() → Cria um novo usuário

getUsers() → Lista todos os usuários

getUserById() → Busca usuário pelo ID

updateUser() → Atualiza dados do usuário

deleteUser() → Exclui usuário

deleteUserWithCart() → Exclui usuário com carrinho vinculado (cenário negativo)

generateRandomEmail() → Gera e-mail randômico para evitar duplicidade

AuthUtils.java
Realiza login automático e armazena o token JWT em cache para reutilização nos testes.

UserSteps.java
Implementa os passos do Cucumber (Gherkin) para os cenários definidos nas features.

TestRunner.java
Responsável por rodar os testes Cucumber com JUnit, gerando relatórios:
target/cucumber-report/html/cucumber-report.html
target/cucumber-report.json
target/cucumber.xml

Cenários Cucumber:
- Busca de Usuários
- Buscar todos os usuários
- Buscar usuário específico
- Buscar usuário inexistente
- Buscar usuário com ID incompleto
- Cadastro de Usuários
- Criar novo usuário com sucesso
- Criar usuário com e-mail duplicado
- Exclusão de Usuários
- Deletar usuário existente
- Deletar usuário inexistente
- Deletar usuário com carrinho vinculado
- Atualização de Usuários
- Atualizar usuário existente
- Atualizar usuário com dados inválidos

Execução Local
Pré-requisitos
- Java 17 instalado
- Maven configurado (mvn -v deve funcionar)
- Internet ativa (a API é pública)

Executar testes
comando git bash:
mvn clean test

Os relatórios serão gerados em:

comando git bash:
target/cucumber-report/html/cucumber-report.html

Para abrir o relatório HTML:

comando git bash:
start target/cucumber-report/html/cucumber-report.html

Integração Contínua (GitHub Actions)
A pipeline CI é acionada automaticamente em push e pull request para a branch main.

Etapas principais
- Checkout do código
- Configuração do JDK 17
- Build e execução dos testes (mvn clean test)
- Upload automático dos relatórios (actions/upload-artifact@v4)

Artefatos gerados
Após a execução, os relatórios ficam disponíveis na aba
Actions → Job → Artifacts:
- cucumber-reports
- surefire-reports (em caso de falha)

Relatórios Gerados
- Tipo	| Caminho
- HTML	| target/cucumber-report/html/cucumber-report.html
- JSON	| target/cucumber-report.json
- JUnit XML | target/cucumber.xml
- Surefire (JUnit) | target/surefire-reports/

Esses arquivos são automaticamente disponibilizados como artefatos no GitHub Actions.

Boas Práticas Implementadas
- Reutilização de código com BaseApi e AuthUtils
- Token JWT armazenado em cache (login único por execução)
- Geração dinâmica de massa de teste (e-mails aleatórios)
- Validações robustas com Hamcrest
- Pipeline CI automatizada e integrada ao GitHub

Referências
- ServeRest API
- Cucumber Docs
- RestAssured Docs
- GitHub Actions

Autor
Guilherme Ferreira Clemente

Documentação:

[Projeto API - Carrefour.docx](https://github.com/user-attachments/files/23174089/Projeto.API.-.Carrefour.docx)
