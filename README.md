# ApplyRec Spring Boot Backend

Spring Boot 3 / Java 17 backend for ApplyRec. Mirrors the original Express API so the frontend works unchanged.

## Run locally

```bash
export SPRING_DATASOURCE_URL='jdbc:postgresql://host:5432/dbname?sslmode=require'
export SPRING_DATASOURCE_USERNAME='db_user'
export SPRING_DATASOURCE_PASSWORD='db_password'
export JWT_SECRET='change-me'
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

Starts on `http://localhost:8080` by default.

## Endpoints

- `GET /api/health`
- `POST /api/auth/signup`
- `POST /api/auth/login`
- `POST /api/applications`
- `GET /api/applications`
- `GET /api/applications/roles`
- `GET /api/applications/{id}`
- `PUT /api/applications/{id}`
- `PATCH /api/applications/{id}/status`
- `DELETE /api/applications/{id}`

All `/api/applications/**` routes require `Authorization: Bearer <token>`.

## Deploy (Render / Docker)

- Repo contains `Dockerfile` at the module root.
- Render env vars:
  - `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
  - `JWT_SECRET`
  - `SPRING_PROFILES_ACTIVE=prod`
  - Render sets `PORT` automatically (this app binds to `${PORT:8080}`).

