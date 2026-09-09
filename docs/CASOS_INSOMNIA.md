# Casos para testar no Insomnia

## 1. Cadastrar professor

POST `http://localhost:8080/professores`

```json
{"nome":"Maria Souza","email":"maria@fiap.com.br"}
```

## 2. Cadastrar sala

POST `http://localhost:8080/salas`

```json
{"nome":"205"}
```

## 3. Cadastrar equipamento

POST `http://localhost:8080/equipamentos`

```json
{"nome":"Caixa de Som 01","tipo":"Caixa de som","ativo":true}
```

## 4. Reserva válida

POST `http://localhost:8080/reservas`

```json
{
  "professorId": 1,
  "curso": "Engenharia de Software",
  "salaId": 1,
  "retirada": "2026-09-20T18:30:00",
  "entrega": "2026-09-20T22:30:00",
  "equipamentosIds": [1,2]
}
```

## 5. Menos de 7 dias

Use uma `retirada` com menos de sete dias de antecedência. Esperado: HTTP 400.

## 6. Equipamento inativo

Use:

`PATCH /equipamentos/3/status?ativo=false`

Depois tente reservá-lo. Esperado: HTTP 400.

## 7. Conflito de equipamento

Crie uma reserva com um equipamento e depois outra reserva sobrepondo o mesmo horário e equipamento. Esperado: HTTP 400.

## 8. Conflito de sala

Crie duas reservas na mesma sala com horários sobrepostos. Esperado: HTTP 400.

## 9. Horário inválido

Envie `retirada` posterior ou igual à `entrega`. Esperado: HTTP 400.

## 10. Recurso inexistente

Consulte, por exemplo:

`GET /professores/99999`

Esperado: HTTP 404.

## 11. Validação de entrada

Envie um professor sem nome ou com e-mail inválido. Esperado: HTTP 400 com mensagem de validação.
