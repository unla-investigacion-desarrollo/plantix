# Nginx Reverse Proxy

Este directorio contiene la configuración del reverse proxy con Nginx para el proyecto Plantix.

## Descripción

El reverse proxy actúa como punto de entrada para las peticiones HTTP/HTTPS, redirigiendo el tráfico hacia la aplicación Plantix que corre en el contenedor `plantix-app`.

## Configuración del Docker Compose

```yaml
services:
  reverse-proxy:
    image: nginx:1.24.0
    ports:
      - 80:80
      - 443:443
    restart: unless-stopped
    volumes:
      - ./reverse_proxy/conf.d:/etc/nginx/conf.d
    networks:
      - main_network

networks:
  main_network:
    external: true
```

### Características:
- **Imagen**: nginx:1.24.0
- **Puertos expuestos**: 
  - Puerto 80 (HTTP)
  - Puerto 443 (HTTPS)
- **Red**: Utiliza la red externa `main_network` para comunicarse con los demás servicios
- **Volumes**: Monta la configuración personalizada desde `./reverse_proxy/conf.d`

## Configuración de Nginx

El archivo de configuración en `reverse_proxy/conf.d/plantix.local.conf` contiene:

```nginx
server {
     listen 80;
     server_name plantix.local;

     location / {
         proxy_pass http://plantix-app:8080;
         proxy_set_header Host $host;
         proxy_set_header X-Real-IP $remote_addr;
         proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
         proxy_set_header X-Forwarded-Proto $scheme;
     }
}
```

Esta configuración redirige todas las peticiones recibidas en `plantix.local` al servicio `plantix-app` en el puerto 8080.

## Configuración del archivo hosts

Para acceder a la aplicación mediante el dominio `plantix.local`, es necesario modificar el archivo hosts del sistema operativo.

### Linux / macOS

1. Editar el archivo hosts con privilegios de administrador:
   ```bash
   sudo nano /etc/hosts
   ```

2. Agregar la siguiente línea:
   ```
   127.0.0.1 plantix.local
   ```

3. Guardar y cerrar el archivo (Ctrl+O, Enter, Ctrl+X en nano)

### Windows

1. Abrir el Bloc de notas como Administrador:
   - Buscar "Bloc de notas" en el menú inicio
   - Click derecho → "Ejecutar como administrador"

2. Abrir el archivo hosts:
   - Ir a: `C:\Windows\System32\drivers\etc\hosts`
   - Cambiar el filtro de archivos a "Todos los archivos (*.*)"

3. Agregar la siguiente línea al final del archivo:
   ```
   127.0.0.1 plantix.local
   ```

4. Guardar el archivo (Archivo → Guardar)

## Requisitos previos

⚠️ **Importante**: Para que la aplicación funcione correctamente, debe estar levantado el stack principal de Docker Compose ubicado en `../docker/docker-compose.yml`.

Esto es necesario porque:
- La aplicación `plantix-app` debe estar corriendo y conectada a la red `main_network`
- El reverse proxy necesita comunicarse con este servicio

## Cómo usar

1. Asegurarse de que el stack principal esté corriendo:
   ```bash
   cd ../docker
   docker compose up -d
   ```

2. Levantar el reverse proxy:
   ```bash
   cd ../nginx
   docker compose up -d
   ```

3. Verificar que los contenedores estén corriendo:
   ```bash
   docker compose ps
   ```

4. Acceder a la aplicación desde el navegador:
   ```
   http://plantix.local
   ```

## Detener el servicio

```bash
docker-compose down
```

## Logs

Para ver los logs del reverse proxy:
```bash
docker logs -f <reverse-proxy-container-id>
```
