# API de Reservas de Salas e Equipamentos

API REST desenvolvida em **Java 21** com **Spring Boot**, utilizando os princípios de **Domain-Driven Design (DDD)** e **Clean Architecture**.

O projeto tem como objetivo permitir que professores realizem reservas de **salas e equipamentos acadêmicos**, aplicando regras de negócio para evitar conflitos de horários, reservas indevidas e utilização de equipamentos inativos.

---

## 📌 Sobre o projeto

A aplicação disponibiliza uma API para gerenciamento de:

* 👨‍🏫 Professores
* 🏫 Salas
* 🖥️ Equipamentos
* 📅 Reservas

Uma reserva relaciona um professor, uma sala, um período de utilização e um ou mais equipamentos.

A aplicação possui regras de negócio para garantir que os recursos sejam utilizados corretamente. Entre elas estão:

* A reserva deve ser realizada com pelo menos **7 dias de antecedência**.
* O horário de retirada deve ser anterior ao horário de entrega.
* Uma sala não pode possuir duas reservas em horários sobrepostos.
* Um equipamento não pode ser reservado simultaneamente em duas reservas.
* Equipamentos inativos não podem ser utilizados em novas reservas.
* Uma reserva cancelada não pode ser cancelada novamente.
* Professor, sala e equipamentos informados na reserva precisam existir.

---

# 🏗️ Arquitetura

O projeto utiliza **Clean Architecture**, separando as responsabilidades em quatro camadas principais:

```text
presentation
      ↓
application
      ↓
domain

infrastructure
      ↓
application / domain
```

A regra principal é que as dependências apontem para dentro da aplicação, evitando que o domínio dependa de frameworks ou detalhes de infraestrutura.

### Estrutura de pacotes

```text
br.com.fiap.reservas
│
├── domain
│   ├── model
│   ├── exception
│   ├── repository
│   └── service
│
├── application
│   ├── dto
│   │   ├── request
│   │   └── response
│   ├── mapper
│   └── usecase
│       ├── port
│       │   ├── in
│       │   └── out
│       └── ...
│
├── infrastructure
│   ├── config
│   ├── exception
│   └── persistence
│       ├── adapter
│       ├── entity
│       └── repository
│
└── presentation
    └── controller
```

## Domain

Contém as regras e conceitos centrais do sistema.

Entre os principais componentes estão:

* Entidades de domínio
* Objetos de valor
* Exceções de negócio
* Interfaces de repositório
* Políticas de validação
* Serviços de domínio

O domínio não possui dependências de Spring ou JPA.

## Application

Responsável por executar os casos de uso da aplicação.

Exemplos:

* Criar professor
* Criar sala
* Criar equipamento
* Criar reserva
* Listar reservas
* Buscar reserva
* Cancelar reserva
* Alterar status de equipamento

Também contém os DTOs utilizados na comunicação entre a API e os casos de uso.

## Infrastructure

Contém os detalhes técnicos necessários para executar a aplicação, como:

* Spring Data JPA
* Entidades JPA
* Implementações dos repositórios
* Adaptadores
* Flyway
* Configurações
* Tratamento global de exceções

As entidades de persistência são separadas das entidades de domínio.

## Presentation

Responsável pela exposição da API REST.

Os controllers dependem apenas dos contratos da camada de aplicação e dos DTOs.

---

# 🛠️ Tecnologias utilizadas

| Tecnologia        | Utilização                       |
| ----------------- | -------------------------------- |
| Java 21           | Linguagem de programação         |
| Spring Boot 4.1   | Framework principal              |
| Spring Web        | Construção da API REST           |
| Spring Data JPA   | Persistência de dados            |
| Bean Validation   | Validação dos requests           |
| Oracle Database   | Banco de dados principal         |
| H2                | Banco utilizado nos testes       |
| Flyway            | Versionamento e criação do banco |
| Maven             | Gerenciamento e build do projeto |
| Lombok            | Redução de código boilerplate    |
| Springdoc OpenAPI | Documentação da API              |
| JUnit 5           | Testes automatizados             |
| Mockito           | Testes de casos de uso           |
| ArchUnit          | Validação da arquitetura         |
| Docker            | Containerização                  |
| Docker Compose    | Orquestração da API + Oracle     |

---

# 📋 Requisitos

Para executar o projeto localmente, você pode utilizar uma das duas opções:

1. Executar diretamente com Maven e Java.
2. Executar utilizando Docker Compose.

## Execução local

Necessário:

* **Java 21**
* Maven ou Maven Wrapper
* Oracle Database

O projeto já possui o **Maven Wrapper**, portanto não é obrigatório instalar o Maven separadamente.

## Execução com Docker

Necessário:

* Docker
* Docker Compose

Nesse modo, o Docker Compose sobe automaticamente:

* API
* Oracle XE

---

# 🚀 Como executar o projeto

## Opção 1 — Docker Compose

Essa é a forma mais simples de executar a aplicação com o Oracle configurado.

### 1. Criar o arquivo `.env`

Na raiz do projeto existe o arquivo:

```text
.env.example
```

Copie-o para `.env`.

### Windows PowerShell

```powershell
Copy-Item .env.example .env
```

### Linux/macOS

```bash
cp .env.example .env
```

O arquivo terá configurações semelhantes a:

```env
ORACLE_SYS_PASSWORD=OracleSysDev_12345
ORACLE_DB_USERNAME=RESERVAS
ORACLE_DB_PASSWORD=ReservasDev_12345

ORACLE_DB_URL=jdbc:oracle:thin:@localhost:1521/XEPDB1

ORACLE_PORT=1521
API_PORT=8080
```

> **Importante:** o arquivo `.env` não deve ser versionado no Git, pois contém credenciais.

---

### 2. Subir os containers

Na raiz do projeto:

```bash
docker compose up --build
```

O Docker irá:

1. Criar o container do Oracle XE.
2. Aguardar o banco ficar saudável.
3. Criar o container da API.
4. Executar a aplicação Spring Boot.
5. Executar as migrations do Flyway.
6. Disponibilizar a API na porta `8080`.

A API estará disponível em:

```text
http://localhost:8080
```

O Oracle estará disponível em:

```text
localhost:1521/XEPDB1
```

---

### 3. Parar a aplicação

Para parar os containers mantendo os dados do banco:

```bash
docker compose down
```

Para remover também o volume do Oracle e começar novamente com um banco vazio:

```bash
docker compose down -v
```

> ⚠️ O comando `docker compose down -v` remove os dados persistidos do banco.

---

# 💻 Opção 2 — Executar localmente

A execução local utiliza o perfil `test`, que configura automaticamente um banco **H2 em memória**.

### Windows

```powershell
$env:SPRING_PROFILES_ACTIVE="test"
mvnw.cmd spring-boot:run
```

### Linux/macOS

```bash
SPRING_PROFILES_ACTIVE=test ./mvnw spring-boot:run
```

A aplicação será iniciada em:

```text
http://localhost:8080
```

Nesse perfil, o projeto utiliza:

```text
H2
Flyway
Hibernate
```

O banco é criado automaticamente em memória.

---

# 🗄️ Banco de dados

A aplicação utiliza **Flyway** para controlar a estrutura do banco.

Existem migrations específicas para cada banco:

```text
src/main/resources/db/migration/
│
├── h2
│   ├── V1__create_schema.sql
│   └── V2__seed_test_data.sql
│
└── oracle
    └── V1__create_schema.sql
```

### Oracle

Os perfis padrão utilizam:

```text
Oracle Database
```

com as seguintes configurações:

```env
ORACLE_DB_URL
ORACLE_DB_USERNAME
ORACLE_DB_PASSWORD
```

### H2

O perfil de testes utiliza:

```text
H2 em memória
```

com a configuração:

```text
jdbc:h2:mem:reservas
```

O Hibernate está configurado com:

```text
ddl-auto=validate
```

Isso significa que o Hibernate **não cria nem altera o schema**. A criação e atualização da estrutura do banco são responsabilidade do Flyway.

---

# 📚 Documentação da API

Após iniciar a aplicação, a documentação interativa pode ser acessada pelo Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

A especificação OpenAPI também está disponível em:

```text
http://localhost:8080/v3/api-docs
```

O Swagger permite visualizar os endpoints e executar requisições diretamente pela interface.

---

# 🔌 Endpoints

## 👨‍🏫 Professores

### Criar professor

```http
POST /professores
```

Exemplo:

```json
{
  "nome": "Maria Souza",
  "email": "maria@fiap.com.br"
}
```

Resposta:

```text
201 Created
```

### Listar professores

```http
GET /professores
```

### Buscar professor

```http
GET /professores/{id}
```

---

# 🏫 Salas

### Criar sala

```http
POST /salas
```

Exemplo:

```json
{
  "nome": "205"
}
```

### Listar salas

```http
GET /salas
```

### Buscar sala

```http
GET /salas/{id}
```

---

# 🖥️ Equipamentos

### Criar equipamento

```http
POST /equipamentos
```

Exemplo:

```json
{
  "nome": "Caixa de Som 01",
  "tipo": "Caixa de som",
  "ativo": true
}
```

### Listar equipamentos

```http
GET /equipamentos
```

### Buscar equipamento

```http
GET /equipamentos/{id}
```

### Alterar status

```http
PATCH /equipamentos/{id}/status?ativo=true
```

Para desativar:

```http
PATCH /equipamentos/{id}/status?ativo=false
```

---

# 📅 Reservas

### Criar reserva

```http
POST /reservas
```

Exemplo:

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

A API também possui suporte ao envio de quantidade por equipamento:

```json
{
  "professorId": 1,
  "curso": "Engenharia de Software",
  "salaId": 1,
  "retirada": "2026-09-20T18:30:00",
  "entrega": "2026-09-20T22:30:00",
  "equipamentos": [
    {
      "equipamentoId": 1,
      "quantidade": 2
    }
  ]
}
```

### Listar reservas

```http
GET /reservas
```

### Buscar reserva

```http
GET /reservas/{id}
```

### Cancelar reserva

```http
DELETE /reservas/{id}
```

Resposta:

```text
204 No Content
```

---

# ⚙️ Regras de negócio

## Antecedência mínima

Uma reserva deve ser realizada com pelo menos **7 dias de antecedência**.

Uma reserva exatamente no limite de 7 dias é aceita.

Caso contrário:

```text
409 Conflict
```

---

## Horário da reserva

O horário de retirada deve ser anterior ao horário de entrega.

Exemplos inválidos:

```text
retirada = 18:00
entrega  = 18:00
```

ou:

```text
retirada = 20:00
entrega  = 18:00
```

Nesses casos:

```text
400 Bad Request
```

---

## Conflito de sala

Uma mesma sala não pode possuir reservas em períodos sobrepostos.

Exemplo:

```text
Reserva 1: 18:00 ───── 20:00
Reserva 2:       19:00 ───── 21:00
```

Nesse caso existe conflito:

```text
409 Conflict
```

Reservas adjacentes são permitidas:

```text
Reserva 1: 18:00 ───── 20:00
Reserva 2:                 20:00 ───── 22:00
```

---

## Conflito de equipamento

Um equipamento não pode estar reservado simultaneamente em duas reservas.

Caso o mesmo equipamento seja utilizado em períodos sobrepostos:

```text
409 Conflict
```

---

## Equipamento inativo

Equipamentos marcados como:

```json
{
  "ativo": false
}
```

não podem ser utilizados em novas reservas.

Nesse caso:

```text
409 Conflict
```

---

## Recursos inexistentes

Caso uma reserva informe um professor, sala ou equipamento que não existe:

```text
404 Not Found
```

---

# ❌ Tratamento de erros

A aplicação possui um tratamento global de exceções através do `GlobalExceptionHandler`.

As respostas seguem o padrão **RFC 7807 Problem Details**, utilizando:

```text
application/problem+json
```

Uma resposta de erro pode conter informações como:

```json
{
  "type": "room-unavailable",
  "title": "Room unavailable",
  "status": 409,
  "detail": "A sala já está reservada para o período informado.",
  "instance": "/reservas"
}
```

Principais códigos utilizados:

| Código | Significado                              |
| ------ | ---------------------------------------- |
| `400`  | Dados inválidos ou horário incorreto     |
| `404`  | Recurso não encontrado                   |
| `409`  | Conflito ou violação de regra de negócio |

---

# 🧪 Testes

O projeto possui diferentes níveis de testes.

### Testes de domínio

Validam as regras de negócio sem depender de frameworks:

```text
InvalidTimeRangeTest
ReservationEquipmentRuleTest
MinimumAdvanceNoticePolicyTest
TimeWindowOverlapPolicyTest
```

### Testes de casos de uso

Utilizam Mockito para testar a camada de aplicação:

```text
CreateReservationServiceTest
```

### Testes de integração

Utilizam Spring Boot, MockMvc e H2:

```text
ReservationApiIntegrationTest
ResourceApiIntegrationTest
```

### Testes de arquitetura

O projeto utiliza **ArchUnit** para verificar as regras da Clean Architecture:

```text
CleanArchitectureTest
```

---

# ▶️ Executando os testes

Para executar toda a suíte de testes:

### Windows

```powershell
mvnw.cmd test
```

### Linux/macOS

```bash
./mvnw test
```

Os testes normais utilizam H2 e não precisam de um Oracle externo.

---

# 🧪 Testes de integração com Oracle

Existe também uma suíte específica para testar a aplicação utilizando Oracle:

```text
OracleApiIntegrationTest
```

Essa suíte é desabilitada durante a execução normal dos testes.

Para executá-la, configure:

```env
RUN_ORACLE_INTEGRATION_TESTS=true
```

Além disso, as variáveis de conexão com Oracle devem estar configuradas:

```env
ORACLE_DB_URL
ORACLE_DB_USERNAME
ORACLE_DB_PASSWORD
```

No Windows PowerShell:

```powershell
$env:RUN_ORACLE_INTEGRATION_TESTS="true"
mvnw.cmd -Dtest=OracleApiIntegrationTest test
```

---

# 🐳 Docker

A aplicação possui um `Dockerfile` com build em múltiplos estágios.

O primeiro estágio utiliza:

```text
Maven + Eclipse Temurin 21
```

para realizar o build da aplicação.

O segundo estágio utiliza:

```text
Eclipse Temurin 21 JRE Alpine
```

para executar somente o `.jar` gerado.

A aplicação é executada por um usuário sem privilégios de administrador dentro do container.

O `docker-compose.yml` disponibiliza dois serviços:

```text
┌─────────────────────────┐
│       Docker Compose    │
│                         │
│  ┌───────┐   ┌───────┐ │
│  │  API  │──▶│Oracle │ │
│  │ :8080 │   │ :1521 │ │
│  └───────┘   └───────┘ │
│                         │
└─────────────────────────┘
```

A API aguarda o Oracle estar saudável antes de iniciar.

---

# 📂 Estrutura principal do projeto

```text
CP4.DDD.Reservas/
│
├── docs/
│   ├── APRESENTACAO.md
│   └── CASOS_INSOMNIA.md
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/fiap/reservas/
│   │   │       ├── application/
│   │   │       ├── domain/
│   │   │       ├── infrastructure/
│   │   │       └── presentation/
│   │   │
│   │   └── resources/
│   │       ├── db/
│   │       │   └── migration/
│   │       └── application.yml
│   │
│   └── test/
│       ├── java/
│       └── resources/
│
├── .env.example
├── docker-compose.yml
├── Dockerfile
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

# 🔄 Fluxo de criação de uma reserva

O fluxo principal da aplicação funciona da seguinte maneira:

```text
Cliente
   │
   ▼
ReservaController
   │
   ▼
ICreateReservationUseCase
   │
   ▼
CreateReservationService
   │
   ├── Validação do horário
   │
   ├── Validação da antecedência mínima
   │
   ├── Busca do professor
   │
   ├── Busca da sala
   │
   ├── Busca dos equipamentos
   │
   ├── Verificação de equipamentos ativos
   │
   ├── Verificação de disponibilidade
   │
   ▼
Reserva (Domain)
   │
   ▼
Repository Port
   │
   ▼
JpaReservaRepository
   │
   ▼
Oracle / H2
```

Essa separação permite que as regras de negócio permaneçam independentes da tecnologia utilizada para persistência ou exposição da API.

---

# 📖 Documentação adicional

O projeto possui documentos complementares na pasta `docs/`.

### Guia de apresentação

```text
docs/APRESENTACAO.md
```

Contém uma explicação da arquitetura, fluxo de reserva, persistência, tratamento de erros e testes.

### Cenários de teste

```text
docs/CASOS_INSOMNIA.md
```

Contém exemplos de requisições para testar os principais comportamentos da API utilizando ferramentas como Insomnia.

---

# 🔐 Segurança e credenciais

As credenciais utilizadas no ambiente local devem ficar no arquivo:

```text
.env
```

Esse arquivo não deve ser commitado.

O projeto disponibiliza apenas:

```text
.env.example
```

como modelo.

Antes de utilizar a aplicação em um ambiente real, substitua as credenciais de desenvolvimento por valores seguros.

---

# 👨‍💻 Projeto acadêmico

Projeto desenvolvido como parte das atividades acadêmicas da **FIAP**, com foco em:

* Domain-Driven Design (DDD)
* Clean Architecture
* Desenvolvimento de APIs REST
* Spring Boot
* Persistência com JPA
* Oracle Database
* Testes automatizados
* Testes de integração
* Validação arquitetural
* Containerização com Docker

---

## 📌 Resumo

A **API de Reservas de Salas e Equipamentos** centraliza o gerenciamento de recursos acadêmicos e garante que as principais regras de negócio sejam aplicadas antes da criação de uma reserva.

O projeto foi estruturado utilizando **DDD + Clean Architecture**, mantendo o domínio independente de frameworks e separando claramente:

```text
Domínio
   ↓
Casos de uso
   ↓
Infraestrutura
   ↓
API REST
```

Com isso, a aplicação possui uma arquitetura organizada, testável e preparada para evolução.
