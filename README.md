# Properties

Propiedades/unidades, medidores, pertenencia, asignación de inquilinos por el propietario y cuota por cuenta.

DDD: application/internal, domain/model y domain/services, infrastructure, interfaces/acl e interfaces/rest.
La base es exclusiva de este contexto. Consultas de configuración y colaboración entre servicios usan RabbitMQ RPC;
los cambios pasan por outbox y los consumidores confirman después de procesar.

El Dockerfile compila y ejecuta sus pruebas. Iniciar desde la raíz de orquestación con .env privado.

[Funcionalidad y arranque](../README.md) · [Guía RabbitMQ](../docs/rabbitmq-guide.md) · [Contratos](../docs/contracts.md).
