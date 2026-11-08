# Properties

Propiedades/unidades, medidores, pertenencia, asignación de inquilinos por el propietario y cuota por cuenta.

DDD: application/internal, domain/model y domain/services, infrastructure, interfaces/acl e interfaces/rest.
La base es exclusiva de este contexto. Consultas de configuración y colaboración entre servicios usan RabbitMQ RPC;
los cambios pasan por outbox y los consumidores confirman después de procesar.

## Operación en MAIN

En la red de Compose, el servicio escucha en `properties:8080` y utiliza la base `hs_properties`.

| Comprobación | Ruta interna |
|---|---|
| Proceso | `GET /actuator/health/liveness` |
| Disponibilidad | `GET /actuator/health/readiness` |

Readiness verifica PostgreSQL y RabbitMQ. Revisa el resultado en el [panel de MAIN](http://127.0.0.1:8099/). Cuando una unidad o un medidor no aparece, confirma primero la pertenencia de la cuenta y el estado del medidor. Las asignaciones activas se registran en tenancies.

Construye y ejecuta las pruebas del Dockerfile desde este clon con `docker build -t hydrosmart-properties .`. Para la prueba conjunta, ejecuta `./scripts/Test-Local.ps1 -DatabaseMode Hosted` desde la carpeta de orquestación.
