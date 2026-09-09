# API Scenarios

## 1. Create a professor

`POST /professores`

```json
{"nome":"Maria Souza","email":"maria@fiap.com.br"}
```

Expected status: `201 Created`.

## 2. Create a room

`POST /salas`

```json
{"nome":"205"}
```

Expected status: `201 Created`.

## 3. Create an equipment item

`POST /equipamentos`

```json
{"nome":"Caixa de Som 01","tipo":"Caixa de som","ativo":true}
```

Expected status: `201 Created`.

## 4. Create a valid reservation

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

Expected status: `201 Created`.

## 5. Less than seven days

Send a `retirada` before the seven-day boundary. Expected status: `409 Conflict`
with type `minimum-advance-not-met` and a `missingDays` property.

## 6. Inactive equipment

Deactivate an item with `PATCH /equipamentos/{id}/status?ativo=false`, then use
it in a reservation. Expected status: `409 Conflict` with type
`inactive-equipment` and the equipment name in the response.

## 7. Equipment conflict

Create a reservation, then create another reservation with the same equipment and
an overlapping window. Expected status: `409 Conflict` with type
`equipment-unavailable`.

## 8. Room conflict

Create two reservations in the same room with overlapping windows. Expected status:
`409 Conflict` with type `room-unavailable`.

## 9. Invalid time range

Send equal or reversed `retirada` and `entrega` values. Expected status: `400 Bad
Request` with type `invalid-time-range`.

## 10. Missing resource

Request `GET /professores/99999`. Expected status: `404 Not Found` with type
`resource-not-found`.

## 11. Request validation

Send a professor without a name or with an invalid email. Expected status:
`400 Bad Request` with field-level errors in the ProblemDetail `errors` property.
