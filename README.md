# ApplyRec Spring Boot Backend

This module is a Spring Boot 3 / Java 17 rewrite of the existing Node.js + Express backend for ApplyRec. It preserves the same API routes and behavior so you can swap backends without changing the frontend.

## Tech stack

- Java 17
- Spring Boot 3
- Spring Web (REST APIs)
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- PostgreSQL
- Flyway for DB migrations

## Running locally

1. Make sure you have:
   - Java 17 installed
   - A PostgreSQL database (you can reuse the same one the Node backend uses)

2. Set environment variables (for example in a `.env` or your shell):

```bash
export DATABASE_URL='postgres://user:password@host:5432/dbname'
export JWT_SECRET='your-secret-key-change-in-production'
export SPRING_PROFILES_ACTIVE=dev
```

3. From the `spring-backend` folder, build and run:

```bash
mvn spring-boot:run
```

The backend will start on port `8080` by default.

## Endpoints

These mirror the existing Node backend:

- `GET /api/health`
- `POST /api/auth/signup`
- `POST /api/auth/login`
- `POST /api/applications/`
- `GET /api/applications/`
- `GET /api/applications/roles`
- `GET /api/applications/{id}`
- `PUT /api/applications/{id}`
- `PATCH /api/applications/{id}/status`
- `DELETE /api/applications/{id}`

All `/api/applications/**` routes require an `Authorization: Bearer <token>` header.

## Database & migrations

- Flyway migrations live in `src/main/resources/db/migration/`.
- `V1__init_schema.sql` creates the same `users` and `applications` tables and indexes as the Node backend.
- In the `dev` profile, if the DB is empty, a test user (`test@applyrec.com` / `test123`) and a few sample applications are seeded automatically.

## Deploying to Render (high level)

1. Create a new Git repo that contains at least the `spring-backend` folder (you can keep the rest of the project if you like).
2. On Render, create a new **Web Service** from that repo.
3. Set:
   - Build command: `mvn clean package -DskipTests`
   - Start command: `java -jar target/applyrec-backend-0.0.1-SNAPSHOT.jar`
4. Add environment variables:
   - `DATABASE_URL`
   - `JWT_SECRET`
   - `SPRING_PROFILES_ACTIVE=prod`

After deployment, point your frontend at the Render service URL instead of the Node backend.

