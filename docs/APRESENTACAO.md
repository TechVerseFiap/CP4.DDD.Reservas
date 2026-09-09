# Presentation Guide - Clean Architecture

## 1. Problem

The API allows professors to reserve rooms and equipment for academic classes
without overlapping reservations.

## 2. Architecture

The project uses four explicit layers:

- `domain`: framework-free entities, value objects, exceptions, repository ports, and business policies;
- `application`: records, mappers, use-case input ports, outbound ports, and orchestration;
- `infrastructure`: JPA entities, Spring Data repositories, adapters, Flyway, configuration, and error handling;
- `presentation`: REST controllers that depend only on application ports and DTOs.

The dependency direction points inward. ArchUnit verifies this relationship and
rejects framework dependencies from the domain.

## 3. Reservation Flow

1. The controller validates request shape with Bean Validation.
2. The create-reservation use case builds a domain `TimeWindow`.
3. The domain policy checks the seven-day advance notice.
4. The use case loads professor, room, and equipment through domain repository ports.
5. The reservation aggregate checks required values and active equipment.
6. One reusable availability checker evaluates room and equipment windows.
7. The persistence adapter maps the domain aggregate to separate JPA entities.
8. The controller returns a response record without exposing JPA entities.

## 4. Error Handling

`GlobalExceptionHandler` returns RFC 7807 `ProblemDetail` responses:

- HTTP 400 for request shape and invalid time ranges;
- HTTP 404 for missing resources;
- HTTP 409 for availability, advance-notice, inactive-equipment, and persistence conflicts.

Each domain rule has a specific machine-readable type and human-readable detail.

## 5. Persistence

Flyway owns the schema. `test` uses H2 migrations and the default/`prod` profiles
use Oracle migrations. Oracle connection values are supplied through environment
variables. JPA is configured only to validate the migrated schema.

## 6. Tests

The suite includes framework-free domain rule tests, Mockito use-case tests, H2
MockMvc integration tests, and the ArchUnit `CleanArchitectureTest`.
