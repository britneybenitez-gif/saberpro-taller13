# Evidencias reales de ejecución

Capturadas el **16 de noviembre de 2026**. Las marcas de los logs son UTC; la fecha de entrega se expresa en America/Bogota. Los artefactos de esta carpeta provienen de comandos o peticiones ejecutados. No son capturas gráficas ni resultados esperados inventados.

| Archivo | Qué demuestra |
|---|---|
| `taller12-baseline-tests.log` | 23 pruebas originales antes de modificar |
| `maven-host-verify.log` | Compilación y 32 pruebas con el host y release 17 |
| `maven-java17-integration.log` | Suite completa en Java 17, BUILD SUCCESS |
| `resumen-pruebas.json` y `junit/` | 55 pruebas: 0 fallos, 0 errores, 0 omitidas; resultados por clase |
| `dominio-puro.txt` | Dominio/aplicación compilados sin dependencias externas |
| `docker-compose-up.log`, `docker-compose-build.log` | Construcción y arranque desde fuentes |
| `docker-compose-config.yml` | Configuración resuelta de Compose |
| `docker-compose-ps.txt`, `docker-ps.txt` | Estado de contenedores y puertos |
| `java-contenedor.txt` | JVM Java 17 real del servicio |
| `red-saberpro-net.json`, `conectividad-interna.txt` | Red bridge y llamada Question → Validation por DNS interno |
| `smoke-jpa.json`, `smoke-jpa.log` | 28 respuestas verificadas con JPA, fallos y recuperación |
| `smoke-memory.json`, `smoke-memory.log` | El mismo protocolo con HashMap |
| `persistencia-reinicio.json` | Datos JPA tras recreación, comprobantes tras reinicio y evento duplicado ignorado |
| `logs-*-service.txt`, `logs-rabbitmq.txt` | Ejecución y consumo real con IDs |
| `rabbitmq-queue.json` | Cola requerida, durabilidad, consumidor y métricas |
| `openjdk-manifest.txt` | Ausencia de la imagen indicada por la guía |
| `guias-sha256.json` | Identidad de los PDF copiados sin cambios |

Las advertencias de conexión rechazada corresponden a las pruebas deliberadas de caída. El error de contrato de `LifecycleIntegrationTest` corresponde a la prueba que verifica la DLQ. Las métricas de Management se actualizan periódicamente; una cola vacía después de procesar es normal.

IDs concretos para relacionar respuestas y logs:

- JPA: `5d7b62c6-28a0-420b-b691-e65cf11ab595`.
- HashMap: `511ff613-e629-4870-a722-2b741ced487f` (sus datos eran temporales y el despliegue final volvió a JPA).

Los logs finales de Question corresponden al contenedor recreado con JPA. Las respuestas y logs de scripts conservan las comprobaciones anteriores, incluidas las de caída del broker y rollback.

## Capturas que debe añadir el equipo

1. Terminal con `docker compose ps` y los cuatro servicios saludables, puertos visibles.
2. Thunder Client: GET colección 200; POST creación 201 con ID/BORRADOR; revisión 202 con el mismo ID; caso inválido 400 y consulta ausente 404.
3. RabbitMQ Management: `pregunta.revisar.queue`, consumidor y actividad de entrega.
4. Terminal con el log de `QuestionReviewListener` y el ID de la misma pregunta.
5. Terminal con BUILD SUCCESS y los resultados de Maven; revisar resumen total en `resumen-pruebas.json`.
6. Cambio a `APP_REPOSITORY=memory` y repetición del flujo; volver después a JPA.

No reutilizar como evidencia de Taller 13 las capturas históricas de `docs/figurasYEvidencias/`: corresponden al Taller 12, puerto 8080 y notificación por log. No se ejecutó ni se capturó la interfaz de Thunder Client durante esta implementación.
