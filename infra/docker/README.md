# Plantix - Entorno Dockerizado

Este entorno permite levantar toda la infraestructura de Plantix con un solo comando usando Docker Compose.

## Componentes incluidos

- **Broker MQTT (Eclipse Mosquitto)**
  - Contenedor con usuario ya creado.
  - La contraseña no se sube al repositorio por seguridad; se compartirá internamente cuando sea necesario.

- **Base de datos MySQL**
  - Persistencia de datos mediante volúmenes.
  - Inicialización automática de la base `plantix_db` usando el script `init.sql` en `/docker-entrypoint-initdb.d/`.
  - Healthcheck para asegurar que MySQL esté operativo antes de iniciar la aplicación Java.

- **Aplicación Spring Boot (Plantix)**
  - Construida con Maven dentro del contenedor.
  - Imagen final optimizada para producción.
  - Conexión automática a la base de datos una vez que MySQL está lista.

## Manejo de credenciales

- El archivo `.env` con credenciales reales **no** se sube al repositorio.
- Se incluye `.env.example` como referencia para variables necesarias. Cada desarrollador debe completarlo con sus datos y contraseñas compartidas internamente.

## Detalles técnicos

- **Healthcheck en MySQL:** Garantiza que la aplicación Java no se inicie hasta que la base esté lista para aceptar conexiones.
- **Inicialización de base de datos:** El script `init.sql` se ejecuta automáticamente la primera vez que se inicializa el volumen de datos.
- **Separación de credenciales:** Las variables sensibles (como `MYSQL_DB_PASSWORD`) se leen desde el entorno y no se almacenan en el repositorio.

## Estado actual

- Entorno listo para producción básica.
- Próximamente: entorno de desarrollo con hot-reload y configuraciones menos restrictivas para iteración rápida.

## Cómo probar

1. Clonar el repositorio y ubicarse en la carpeta donde está el `docker-compose.yml`.
2. Copiar `.env.example` a `.env` y completar con las credenciales correspondientes.
3. Ejecutar:
   ```sh
   docker compose up --build
   ```
4. Verificar:
   - Conexión al broker MQTT.
   - Conexión a MySQL (`plantix_db` creada).
   - Aplicación Java corriendo en [http://localhost:8080](http://localhost:8080).

---

Para dudas o problemas, consultar la tarjeta de Trello asociada o contactar al equipo de desarrollo.
