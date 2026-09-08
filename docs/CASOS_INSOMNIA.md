# Casos para testar no Insomnia

## 1. Cadastrar professor
POST http://localhost:8080/cadastros/professores

```json
{"nome":"Maria Souza","email":"maria@fiap.com.br"}
```

## 2. Cadastrar sala
POST http://localhost:8080/cadastros/salas

```json
{"nome":"205"}
```

## 3. Cadastrar equipamento
POST http://localhost:8080/cadastros/equipamentos

```json
{"nome":"Caixa de Som 01","tipo":"Caixa de som","ativo":true}
```

## 4. Reserva válida
POST http://localhost:8080/reservas

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

## 5. Menos de 7 dias
Altere `retirada` para uma data que esteja a menos de uma semana do momento da execução. Esperado: HTTP 400.

## 6. Equipamento inativo
Use `PATCH /cadastros/equipamentos/3/status?ativo=false` e tente reservá-lo. Esperado: HTTP 400.

## 7. Conflito de equipamento
Crie uma reserva para `equipamentosIds: [1]` e depois outra reserva sobrepondo o horário e usando o mesmo equipamento. Esperado: HTTP 400.

## 8. Conflito de sala
Crie duas reservas na mesma sala com horários sobrepostos. Esperado: HTTP 400.

## 9. Horário inválido
Envie `retirada` posterior ou igual a `entrega`. Esperado: HTTP 400.
