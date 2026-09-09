# Equipment Reservation API

Spring Boot 4.1 API for professors reserving rooms and equipment. The project uses
Clean Architecture, Java 21, Spring Data JPA, Flyway, H2 for tests, and Oracle for
the default and production profiles.

## Architecture

```text
presentation -> application -> domain
infrastructure -> application and domain
```

The domain contains plain Java models, repository ports, exceptions, and reusable
policies. It has no Spring or JPA imports. Application use cases expose input ports,
use DTO records and invoke domain ports. Infrastructure contains JPA entities,
Spring Data repositories, adapters, configuration, and ProblemDetail handling.
Controllers depend only on application input ports and DTOs.

## Packages

```text
br.com.fiap.reservas
├── domain
│   ├── model
│   ├── exception
│   ├── repository
│   └── service
├── application
│   ├── usecase/port/in
│   ├── usecase/port/out
│   ├── dto/request
│   ├── dto/response
│   └── mapper
├── infrastructure
│   ├── persistence/entity
│   ├── persistence/repository
│   ├── persistence/adapter
│   ├── config
│   └── exception
└── presentation/controller
```

All request and response DTOs are Java records. Domain and JPA models are separate.

## Business Rules

- A reservation requires a professor, course, room, date, valid time range, and at least one equipment item.
- The reservation must be at least seven days in advance. Exactly seven days is accepted.
- Pickup must be strictly before return; adjacent reservations do not overlap.
- Equipment must be active.
- Equipment and rooms cannot be double-booked in an overlapping `[pickup, return)` window.
- Business failures identify the specific rule and resource in an RFC 7807 response.

## API

Professor endpoints:

- `POST /professores`
- `GET /professores`
- `GET /professores/{id}`

Room endpoints:

- `POST /salas`
- `GET /salas`
- `GET /salas/{id}`

Equipment endpoints:

- `POST /equipamentos`
- `GET /equipamentos`
- `GET /equipamentos/{id}`
- `PATCH /equipamentos/{id}/status?ativo=true|false`

Reservation endpoints:

- `POST /reservas`
- `GET /reservas`
- `GET /reservas/{id}`
- `DELETE /reservas/{id}`

Example reservation request:

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

Legacy equipment ID requests map each item to quantity one. The domain and
persistence model support quantities through `reserva_equipamento.quantidade`.
New clients may send the quantity-aware form instead:

```json
"equipamentos": [
  {"equipamentoId": 1, "quantidade": 2}
]
```

## Error Responses

The global handler returns `application/problem+json` with `type`, `title`, `status`,
`detail`, `instance` when available, and rule-specific properties such as
`missingDays` or `equipment`.

- `400`: request shape, malformed JSON, or invalid time range.
- `404`: referenced resource does not exist.
- `409`: availability conflict, inactive equipment, minimum advance notice, duplicate data, or invalid reservation state.

## Database Profiles

The default and `prod` profiles use Oracle. For the local Docker Compose database,
set these environment variables:

```text
ORACLE_DB_URL=jdbc:oracle:thin:@localhost:1521/XEPDB1
ORACLE_DB_USERNAME=RESERVAS
ORACLE_DB_PASSWORD=the_value_from_.env
```

For an external Oracle installation, use its actual service name, username, and
password instead.

`.env.example` contains the variable names without secrets. Never commit a real
`.env` file or credentials.

The `test` profile uses an in-memory H2 database. Flyway runs
`db/migration/h2` for tests and `db/migration/oracle` for production. The scripts
are separate where Oracle and H2 require different identity, boolean, and varchar
syntax. Hibernate runs in `validate` mode in both profiles.

## Docker Compose

The local Compose stack runs the API and Oracle XE. It uses
`gvenzl/oracle-xe:21-slim-faststart`, the slim/fast-start Oracle XE image, with a
named volume for database data.

```powershell
Copy-Item .env.example .env
docker compose up --build
```

The API is available at `http://localhost:8080`. Oracle is available from the host
at `localhost:1521/XEPDB1`. The API waits for the Oracle health check before starting.

Stop the stack while preserving data:

```powershell
docker compose down
```

Remove the database volume and start from an empty schema:

```powershell
docker compose down -v
```

The `.env` file is ignored by Git. Change the sample development passwords before
using the stack outside a disposable local environment.

## Run

Windows:

```powershell
$env:SPRING_PROFILES_ACTIVE="test"
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
SPRING_PROFILES_ACTIVE=test ./mvnw spring-boot:run
```

Run tests:

```powershell
mvnw.cmd test
```

Run the Oracle HTTP integration tests from IntelliJ by loading the Oracle
environment variables and adding `RUN_ORACLE_INTEGRATION_TESTS=true` to the test
configuration. From PowerShell, set the same variables in the process and run:

```powershell
$env:RUN_ORACLE_INTEGRATION_TESTS="true"
mvnw.cmd -Dtest=OracleApiIntegrationTest test
```

The Oracle suite is disabled during the normal test run and creates uniquely
named local test data in the configured schema.

## API Documentation

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Verification

The normal test suite includes pure domain tests, Mockito use-case tests, H2
controller integration tests, and `CleanArchitectureTest` ArchUnit rules.
