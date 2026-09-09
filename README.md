# Sistema de Reserva de Equipamentos — Spring Boot / MVSC

API REST para cadastro de professores, salas e equipamentos e criação de reservas acadêmicas sem conflitos.

## Arquitetura

O projeto foi refatorado para uma estrutura **MVSC (Model, View, Service, Controller)**, mantendo a persistência isolada em repositórios e infraestrutura.

```text
controller
    ↓
service
    ↓
model
    ↑
repository ← infrastructure/persistence
```

### Responsabilidades

- **Model**: entidades JPA e regras invariantes do domínio. Não conhece HTTP, DTOs ou Spring MVC.
- **View**: objetos de entrada e saída da API (`Request` / `Response`). Não contém regra de negócio.
- **Service**: casos de uso, orquestração, validações e transações.
- **Controller**: somente HTTP: recebe requests, valida entrada, chama o service e define status HTTP.
- **Repository**: contratos de persistência.
- **Infrastructure**: implementações Spring Data JPA dos repositories.
- **Exception**: exceções de negócio e recurso não encontrado.
- **Config**: configurações técnicas, como `Clock` injetável.

## Estrutura

```text
src/main/java/br/com/fiap/reservas/
├── config/
│   └── TimeConfig.java
├── controller/
│   ├── ApiExceptionHandler.java
│   ├── EquipamentoController.java
│   ├── ProfessorController.java
│   ├── ReservaController.java
│   └── SalaController.java
├── exception/
│   ├── RecursoNaoEncontradoException.java
│   └── RegraNegocioException.java
├── infrastructure/
│   └── persistence/
│       ├── JpaEquipamentoRepository.java
│       ├── JpaProfessorRepository.java
│       ├── JpaReservaRepository.java
│       └── JpaSalaRepository.java
├── model/
│   ├── Equipamento.java
│   ├── Professor.java
│   ├── Reserva.java
│   ├── ReservaStatus.java
│   └── Sala.java
├── repository/
│   ├── EquipamentoRepository.java
│   ├── ProfessorRepository.java
│   ├── ReservaRepository.java
│   └── SalaRepository.java
├── service/
│   ├── EquipamentoService.java
│   ├── EquipamentoServiceImpl.java
│   ├── ProfessorService.java
│   ├── ProfessorServiceImpl.java
│   ├── ReservaService.java
│   ├── ReservaServiceImpl.java
│   ├── SalaService.java
│   └── SalaServiceImpl.java
└── view/
    ├── request/
    │   ├── EquipamentoRequest.java
    │   ├── ProfessorRequest.java
    │   ├── ReservaRequest.java
    │   └── SalaRequest.java
    └── response/
        ├── EquipamentoResponse.java
        ├── ErrorResponse.java
        ├── ProfessorResponse.java
        ├── ReservaResponse.java
        └── SalaResponse.java
```

## Boas práticas aplicadas

- Controllers separados por recurso e responsabilidade.
- Nenhum controller acessa repository diretamente.
- Services separados por caso de uso/domínio.
- Interfaces de service para favorecer DIP e testabilidade.
- Entidades não são expostas diretamente pela API.
- Requests e Responses separados das entidades JPA.
- Validação de entrada com Bean Validation.
- Regras invariantes mantidas no Model.
- Regras de aplicação centralizadas nos Services.
- Injeção de dependências por construtor.
- Métodos pequenos e com responsabilidade única.
- Sem setters públicos nas entidades.
- Alteração de status do equipamento através de comportamento (`ativar` / `desativar`).
- Transações declaradas no Service.
- `Clock` injetado para tornar regras de tempo determinísticas e testáveis.
- Tratamento global e padronizado de exceções.
- Conflitos de sala/equipamento resolvidos na camada de persistência através de queries específicas.
- Testes unitários para as principais regras da reserva.

## Endpoints

### Professores

`POST /professores`

```json
{
  "nome": "Maria Souza",
  "email": "maria@fiap.com.br"
}
```

`GET /professores`

`GET /professores/{id}`

### Salas

`POST /salas`

```json
{
  "nome": "205"
}
```

`GET /salas`

`GET /salas/{id}`

### Equipamentos

`POST /equipamentos`

```json
{
  "nome": "Caixa de Som 01",
  "tipo": "Caixa de som",
  "ativo": true
}
```

`GET /equipamentos`

`GET /equipamentos/{id}`

`PATCH /equipamentos/{id}/status?ativo=false`

### Reservas

`POST /reservas`

```json
{
  "professorId": 1,
  "curso": "Engenharia de Software",
  "salaId": 1,
  "retirada": "2026-09-20T18:30:00",
  "entrega": "2026-09-20T22:30:00",
  "equipamentosIds": [1, 2]
}
```

`GET /reservas`

`GET /reservas/{id}`

## Regras de negócio

- Reserva exige no mínimo 7 dias de antecedência.
- Retirada deve ser anterior à entrega.
- Reserva precisa de pelo menos um equipamento.
- Equipamento precisa estar ativo.
- Um equipamento não pode ser reservado em períodos sobrepostos.
- Uma sala não pode possuir reservas em períodos sobrepostos.
- Reservas que apenas encostam no limite do intervalo não conflitam.

## Execução

Requisitos:

- Java 21
- Maven 3.9+ ou Maven Wrapper

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```powershell
mvnw.cmd spring-boot:run
```

Testes:

```bash
./mvnw test
```

## H2

Console: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:reservas`
- User: `sa`
- Password: vazio

## Oracle

O projeto mantém o perfil Oracle em:

`src/main/resources/application-oracle.properties`

Execute com:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=oracle
```

Ajuste URL, usuário e senha conforme o ambiente.

## Resultado da refatoração

A antiga concentração de operações em `CadastroController` e `CadastroApplicationService` foi removida.

Agora cada recurso possui seu próprio fluxo:

```text
ProfessorController → ProfessorService → ProfessorRepository
SalaController      → SalaService      → SalaRepository
EquipamentoController → EquipamentoService → EquipamentoRepository
ReservaController   → ReservaService   → ReservaRepository
```

Isso reduz acoplamento, facilita testes, manutenção e evolução individual de cada recurso.

## Swagger / OpenAPI

A API possui documentação OpenAPI integrada por meio do Springdoc.

### Acesso

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Console H2: `http://localhost:8080/h2-console`

### Documentação disponível

A documentação está organizada por recurso e descreve:

- objetivo de cada endpoint;
- parâmetros de rota e query parameters;
- corpo de requisição e seus campos obrigatórios;
- exemplos de payloads;
- modelos de request e response;
- códigos HTTP esperados;
- formato padronizado de erros;
- regras de negócio relevantes para criação de reservas.

### Abertura automática

Ao iniciar a aplicação localmente, o sistema tenta abrir automaticamente o Swagger UI no navegador padrão em `/swagger-ui/index.html`.

Esse comportamento pode ser desativado com:

```properties
app.swagger.auto-open=false
```

A abertura automática é tolerante a ambientes sem interface gráfica: se o navegador não estiver disponível, a aplicação continua iniciando normalmente.
