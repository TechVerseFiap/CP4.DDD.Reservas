# Roteiro da apresentação — arquitetura MVSC

## 1. Problema

A instituição precisa permitir que professores reservem equipamentos para aulas sem conflitos de equipamento, sala e horário.

## 2. Arquitetura

O projeto foi organizado em MVSC:

- `model`: entidades e invariantes do domínio;
- `view`: requests e responses da API;
- `service`: casos de uso, regras de aplicação e transações;
- `controller`: entrada HTTP e códigos de resposta;
- `repository`: contratos de persistência;
- `infrastructure`: implementação JPA;
- `exception`: erros de negócio;
- `config`: configurações técnicas.

## 3. Separação por responsabilidade

Cada recurso possui seu próprio controller e service:

- `ProfessorController` → `ProfessorService`
- `SalaController` → `SalaService`
- `EquipamentoController` → `EquipamentoService`
- `ReservaController` → `ReservaService`

Assim, não existe mais um controller genérico responsável por vários recursos.

## 4. Fluxo de reserva

1. Cliente envia `POST /reservas`.
2. `ReservaController` valida o request.
3. `ReservaService` carrega professor, sala e equipamentos.
4. O service valida antecedência e conflitos.
5. `Reserva` valida suas próprias invariantes.
6. O repository persiste a entidade.
7. O service devolve `ReservaResponse`.
8. O controller retorna HTTP 201.

## 5. Clean Code e SOLID

- Responsabilidade única por classe.
- Inversão de dependência através das interfaces de service e repository.
- Controllers sem regra de negócio.
- Services sem preocupação com detalhes HTTP.
- Model sem dependência da camada web.
- DTOs impedem exposição direta das entidades.
- Dependências injetadas por construtor.
- Métodos pequenos e nomeados de acordo com sua intenção.
- `Clock` injetável para regras de data e testes determinísticos.

## 6. Tratamento de erros

`ApiExceptionHandler` centraliza:

- HTTP 400 para validação e regras de negócio;
- HTTP 404 para recursos inexistentes;
- HTTP 409 para violações de unicidade/persistência.

A API utiliza o mesmo formato de resposta de erro.

## 7. Testes

Os testes unitários cobrem:

- reserva válida;
- antecedência insuficiente;
- equipamento inativo;
- conflito de equipamento;
- conflito de sala;
- horário inválido no Model.

## 8. Resultado

A refatoração reduz acoplamento e torna cada parte do sistema evolutiva de forma independente, sem misturar responsabilidades de HTTP, negócio, persistência e representação de dados.
