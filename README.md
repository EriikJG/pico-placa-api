# Pico y Placa API

API REST desarrollada en Spring Boot para validar si un vehículo puede circular en una fecha y hora determinada, según las reglas de **Pico y Placa en Quito (Ecuador)**.

## Objetivo de la prueba técnica

Implementar un backend que permita:

- Consultar por **placa** si un vehículo puede circular en una fecha/hora.
- Validar que la fecha/hora de consulta no esté en el pasado.
- Retornar un resultado claro: **puede circular** o **no puede circular**.
- Exponer la lógica vía servicios REST.

## Reglas de negocio implementadas

- Ciudad objetivo: **Quito**.
- Restricción por último dígito de placa (automóvil) o penúltimo carácter numérico (motocicleta).
- Días restringidos:
  - Lunes: `1, 2`
  - Martes: `3, 4`
  - Miércoles: `5, 6`
  - Jueves: `7, 8`
  - Viernes: `9, 0`
- Franja horaria restringida:
  - `06:00 - 09:30`
  - `16:00 - 20:00`
- Sábados y domingos: libre circulación.
- Si el vehículo está marcado como `exento`, puede circular.

## Arquitectura actual del repo

La arquitectura y estructura detallada están documentadas en:

- `Prueba Técnica Documentacion/04. Arquitectura.md`

Capas principales:

- `controller`: endpoints REST y DTOs
- `service`: lógica de negocio de pico y placa
- `model`: entidades y objetos de dominio
- `repository`: persistencia con Spring Data JPA
- `config`: configuración general y manejo global de excepciones

## Tecnologías

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Validation
- Spring Data JPA
- PostgreSQL
- Springdoc OpenAPI (Swagger UI)
- Maven Wrapper

## Requisitos para correr localmente

- JDK 17 instalado
- Variables de entorno para base de datos PostgreSQL:
  - `DB_URL`
  - `DB_USERNAME`
  - `DB_PASSWORD`

> Nota: la configuración de datasource está bajo perfil `prod` en `src/main/resources/application.yaml`.

## Despliegue local (Spring Boot)

### 1) Configurar variables de entorno

#### Windows (PowerShell)

```powershell
$env:DB_URL="jdbc:postgresql://<host>:<puerto>/<db>?sslmode=require"
$env:DB_USERNAME="<usuario>"
$env:DB_PASSWORD="<password>"
```

#### Linux / macOS

```bash
export DB_URL="jdbc:postgresql://<host>:<puerto>/<db>?sslmode=require"
export DB_USERNAME="<usuario>"
export DB_PASSWORD="<password>"
```

### 2) Ejecutar la API

#### Windows

```bash
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

#### Linux / macOS

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

La aplicación inicia por defecto en:

- `http://localhost:8080`

### 3) Verificar en Swagger

- Swagger UI: `http://localhost:8080/swagger-ui-custom.html`

## Despliegue local con Docker

### 1) Construir la imagen

```bash
docker build -t pico-placa-api .
```

### 2) Ejecutar el contenedor

Desde la raíz del proyecto, ejecutar:

```bash
docker run -p 8080:8080 -e DB_URL="jdbc:postgresql://<host>:<puerto>/<db>?sslmode=require" -e DB_USERNAME="<usuario>" -e DB_PASSWORD="<password>" pico-placa-api
```

La API quedará disponible en:

- `http://localhost:8080`

Swagger:

- `http://localhost:8080/swagger-ui-custom.html`

## Endpoint principal

- `POST /api/pico-placa/consultar`

### Ejemplo de request

```json
{
  "placa": "PCE-1237",
  "tipoVehiculo": "AUTOMOVIL",
  "exento": false,
  "fechaHora": "2026-02-27T08:00:00"
}
```

### Ejemplo de response

```json
{
  "placa": "PCE-1237",
  "puedeCircular": false,
  "mensaje": "El vehículo NO puede circular"
}
```

## Validaciones y errores

- `400 Bad Request` cuando:
  - faltan campos obligatorios
  - el formato de placa es inválido
  - la fecha/hora está en el pasado
  - el JSON o formato de fecha es inválido
- `500 Internal Server Error` para errores no controlados

## Pruebas

Ejecutar pruebas unitarias:

#### Windows

```bash
mvnw.cmd test
```

#### Linux / macOS

```bash
./mvnw test
```

## Documentación adicional

- `Prueba Técnica Documentacion/01. Prueba Técnica.md`
- `Prueba Técnica Documentacion/02. Negocio.md`
- `Prueba Técnica Documentacion/03. HU-Sistema de verificación de Pico y Placa.md`
- `Prueba Técnica Documentacion/05. Pruebas swagger.md`
