# Roteiro da apresentação — até 10 minutos

## 1. Problema — 1 min
A instituição precisa permitir que professores reservem equipamentos para aulas sem conflitos de equipamento, sala e horário.

## 2. Arquitetura — 1,5 min
A solução foi organizada com inspiração em DDD:
- `domain`: entidades e portas de persistência;
- `application`: casos de uso e validações;
- `infrastructure`: implementação JPA dos repositórios;
- `web`: REST controllers e tratamento de erros.

## 3. Entidades — 1 min
- Professor
- Sala
- Equipamento
- Reserva

Reserva é o agregado central: possui professor, curso, sala, intervalo de horário e um ou vários equipamentos.

## 4. Fluxo de reserva — 1 min
1. Cliente envia POST `/reservas`.
2. Application Service carrega professor, sala e equipamentos.
3. Valida antecedência, horários, status do equipamento e conflitos.
4. Se tudo estiver correto, cria a Reserva e persiste.
5. Se houver erro, API devolve 400/404 com mensagem clara.

## 5. Regras de negócio — 2 min
- Mínimo de 7 dias de antecedência.
- Retirada < entrega.
- Equipamento precisa estar ativo.
- Equipamento não pode estar em outra reserva sobreposta.
- Sala não pode estar em outra reserva sobreposta.

## 6. Testes — 1,5 min
JUnit + Mockito cobrem:
- reserva válida;
- antecedência insuficiente;
- equipamento inativo;
- conflito de equipamento;
- conflito de sala;
- horário inválido no agregado.

## 7. Melhorias — 1 min
1. Transformação da API de Produtos em um domínio de reservas.
2. Centralização das regras no caso de uso/agregado, evitando regras espalhadas no controller.
3. Tratamento padronizado de erros e testes automatizados.

## 8. Desafio — 1 min
O principal desafio foi modelar e validar conflitos de intervalos de tempo sem colocar as regras diretamente nos controllers.
