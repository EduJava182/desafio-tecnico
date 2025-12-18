# Cooperative Voting API

API REST para gerenciamento de sessões de votação em cooperativas. O sistema permite criar pautas, abrir sessões de votação, receber votos dos associados e contabilizar os resultados.

## Tecnologias Utilizadas

O projeto foi desenvolvido utilizando as seguintes tecnologias e ferramentas:

*   **Java 21**
*   **Spring Boot 3.5.8**
    *   **Spring Data JPA**
    *   **Spring Web**
    *   **Spring Validation**
*   **MySQL 8.1**: Banco de dados relacional.
*   **H2 Database**: Banco de dados em memória para testes de integração.
*   **Lombok**: Redução de código boilerplate.
*   **ModelMapper**: Mapeamento entre objetos (DTOs e Entidades).
*   **SpringDoc OpenAPI (Swagger)**: Documentação interativa da API.
*   **Docker & Docker Compose**: Containerização e orquestração de serviços.
*   **Maven**: Gerenciamento de dependências e build.

## Como Executar

O projeto está configurado para rodar facilmente utilizando o Docker Compose.

### Pré-requisitos

*   Docker

### Configuração Inicial

O projeto possui configurações distintas para execução via Docker e execução Local.

1.  **Para rodar com Docker (Padrão)**:
    *   O arquivo `src/main/resources/application.example.yml` contém as configurações para o ambiente Docker (conectando ao host `mysql`).
    *   Renomeie este arquivo para `src/main/resources/application.yml` para que seja utilizado como configuração padrão.

2.  **Para rodar Localmente**:
    *   Utilize o arquivo `src/main/resources/application-local.yml` (já configurado para `localhost`)


### Passos para Execução (Docker)

1.  Navegue até a pasta `docker` na raiz do projeto (ou onde se encontra o arquivo `docker-compose.yml`):
    ```bash
    cd docker
    ```

2.  Execute o comando para subir os containers (API e Banco de Dados):
    ```bash
    docker-compose up -d
    ```

3.  A API estará disponível em: `http://localhost:8080`

### Banco de Dados e Inicialização

Foi criado um script de inicialização (`schema.sql`) que é executado automaticamente quando o container do banco de dados sobe.

*   **Criação Automática**: O script verifica e cria o banco de dados `cooperative_db` e as tabelas `agenda` e `vote` caso não existam.
*   Não é necessário rodar scripts SQL manualmente.

## Documentação da API

A documentação interativa (Swagger UI) pode ser acessada em:
`http://localhost:8080/swagger-ui.html`

### Endpoints e Objetos (DTOs)

#### 1. Pautas (Agendas)

**Criar Nova Pauta**
*   **URL**: `POST /agendas`
*   **Descrição**: Cria uma nova pauta para votação.
*   **Corpo da Requisição (JSON)**: `AgendaRequestDto`
    ```json
    {
      "title": "Título da Pauta",
      "description": "Descrição detalhada da pauta"
    }
    ```
*   **Resposta (JSON)**: `AgendaResponseDto`

**Abrir Sessão de Votação**
*   **URL**: `PATCH /agendas/{agendaId}/open`
*   **Descrição**: Abre uma sessão de votação para uma pauta existente.
*   **Corpo da Requisição (JSON)**: `OpenAgendaRequestDto`
    ```json
    {
      "durationMinutes": 10
    }
    ```
    *(Se `durationMinutes` não for enviado ou for nulo, o padrão de 1 minuto será considerado - lógica a ser confirmada na implementação)*
*   **Resposta (JSON)**: `AgendaSessionDto`

#### 2. Votos (Votes)

**Votar em uma Pauta**
*   **URL**: `POST /votes/{agendaId}/votes`
*   **Descrição**: Registra o voto de um associado.
*   **Corpo da Requisição (JSON)**: `VoteRequestDto`
    ```json
    {
      "userId": 123,
      "vote": "YES" 
    }
    ```
    *(Valores aceitos para `vote`: "YES" ou "NO")*

    > **Observação**: Não é necessário realizar cadastro prévio de usuários. Para votar, basta informar qualquer ID numérico (`userId`) válido. O sistema apenas valida se aquele ID já realizou um voto na pauta em questão.

*   **Resposta**: `201 Created` (Sem corpo)

**Obter Resultado da Votação**
*   **URL**: `GET /votes/{agendaId}/result`
*   **Descrição**: Contabiliza os votos e retorna o resultado (apenas se a sessão estiver encerrada).
*   **Resposta (JSON)**: `VoteResponseDto`
    ```json
    {
      "agendaId": 1,
      "yesVotes": 10,
      "noVotes": 5,
      "totalVotes": 15
    }
    ```

## Testes

O projeto inclui testes unitários e de integração.
*   **Unitários**: Utilizam JUnit 5 e Mockito.
*   **Integração**: Utilizam `@SpringBootTest` com banco H2 em memória e `TestRestTemplate` para validar os endpoints de ponta a ponta.