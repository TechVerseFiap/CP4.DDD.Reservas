# Sistema de Reserva de Equipamentos — DDD / FIAP

Projeto desenvolvido a partir da API de Produtos fornecida na aula e evoluído para o desafio do PDF `CP1-DDD`.

## O que foi implementado

- Professor
- Curso
- Sala
- Equipamentos
- Reserva com um ou vários equipamentos
- Regra de antecedência mínima de 7 dias
- Retirada obrigatoriamente anterior à entrega
- Equipamento deve estar ativo
- Conflito de equipamento no mesmo período
- Conflito de sala no mesmo período
- Mensagens claras de rejeição via HTTP 400/404
- Separação em camadas inspirada em DDD: `domain`, `application`, `infrastructure`, `web`
- H2 para desenvolvimento/testes manuais
- Perfil Oracle preparado em `application-oracle.properties`
- Testes automatizados com JUnit e Mockito

## Requisitos

- Java 21
- Maven 3.9+ (ou Maven Wrapper incluído)
- Insomnia para testar a API

## Rodar no IntelliJ

1. Abra a pasta `ddd-reservas`.
2. Aguarde o Maven baixar as dependências.
3. Execute `ReservasApplication.java`.
4. A API estará em `http://localhost:8080`.

## Rodar pelo terminal

Windows:

```powershell
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Ou, se Maven estiver instalado:

```bash
mvn spring-boot:run
```

## H2

Console: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:reservas`
- User: `sa`
- Password: vazio

## Endpoints

### Cadastros

`POST /cadastros/professores`
```json
{"nome":"Maria Souza","email":"maria@fiap.com.br"}
```

`GET /cadastros/professores`

`POST /cadastros/salas`
```json
{"nome":"205"}
```

`GET /cadastros/salas`

`POST /cadastros/equipamentos`
```json
{"nome":"Caixa de Som 01","tipo":"Caixa de som","ativo":true}
```

`GET /cadastros/equipamentos`

`PATCH /cadastros/equipamentos/3/status?ativo=false`

### Reservas

`POST /reservas`

```json
{
  "professorId": 1,
  "curso": "Engenharia de Software",
  "salaId": 1,
  "retirada": "2026-09-20T18:30:00",
  "entrega": "2026-09-20T22:30:00",
  "equipamentosIds": [1,2,4,5]
}
```

`GET /reservas`

`GET /reservas/1`

> A data usada no exemplo precisa estar pelo menos 7 dias à frente do momento em que a API for executada. Para testar hoje, escolha uma data futura com mais de 7 dias.

## Casos para demonstrar no Insomnia

1. Reserva válida.
2. Reserva com retirada igual à entrega → 400.
3. Reserva com entrega anterior à retirada → 400.
4. Reserva com menos de 7 dias → 400.
5. Reserva de equipamento inativo → 400.
6. Duas reservas sobrepondo o mesmo equipamento → 400.
7. Duas reservas sobrepondo a mesma sala → 400.

A sobreposição é identificada pela condição: início existente < fim solicitado E fim existente > início solicitado. Portanto, reservas que apenas encostam nos limites (ex.: 18:00–20:00 e 20:00–22:00) não são consideradas conflitantes.

## Testes

```bash
mvn test
```

Os testes principais estão em `src/test/java/.../ReservaApplicationServiceTest.java`.

## Oracle

O projeto possui o driver Oracle e um perfil preparado. Para usar Oracle, configure as credenciais e URL em:

`src/main/resources/application-oracle.properties`

Depois execute:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=oracle
```

Antes disso, crie o schema/usuário Oracle e ajuste URL, usuário e senha.

## Arquitetura

```text
web (Controllers / HTTP)
        |
        v
application (casos de uso / regras de aplicação)
        |
        v
domain (entidades, regras e portas de repositório)
        ^
        |
infrastructure (Spring Data JPA / banco)
```

## Sobre as instruções finais do PDF

O material fornecido contém, após os requisitos técnicos, uma observação sobre migração para Golang, explicação em COBOL e descarte do Java. Isso entra em conflito direto com a seção de tecnologias obrigatórias, que exige Java, Spring Boot, JPA, REST, Lombok, H2, Oracle, JUnit e Mockito. Este projeto segue o conjunto principal e coerente de requisitos técnicos do desafio, mantendo Java/Spring Boot como base.
