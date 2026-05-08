# Boards-SpringBoot-API

Spring Boot backend for the Boards app. REST API, real-time WebSocket events, and AI endpoints. Uses Spring Data JPA with PostgreSQL, Flyway migrations, and JWT-based authentication.

## Stack

- Spring Boot 4
- Java 25
- Spring Web MVC
- Spring Data JPA + PostgreSQL
- Flyway migrations
- Spring Security + JWT (`jjwt`)
- STOMP WebSocket messaging
- Springdoc OpenAPI
- LangChain4j + Gemini

## Modules

- `Auth`: login and JWT token.
- `Users`: user registration, profile endpoints, and user lookup.
- `Boards`: board CRUD, ownership transfer, members, roles, and permissions.
- `BoardLists`: list CRUD and ordering inside a board.
- `Cards`: card CRUD, assignments, and ordering.
- `Authorization`: board role and permission checks.
- `WebSockets`: real-time board, list, and card events over STOMP.
- `AI`: description generation and grammar checks with Gemini AI.

## API Docs

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Database

- Migrations: `src/main/resources/db/migration`
- Development seed data: `src/main/resources/db/seed`
- Local PostgreSQL: `docker compose up -d`

## Environment

Required variables (check .env.example):

- `SPRING_ENV`
- `PORT`
- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRES_IN`
- `CORS_ALLOWED_ORIGIN_PATTERNS`
- `GEMINI_API_KEY`
- `GEMINI_MODEL`

Typical local values:

- `SPRING_ENV=dev`
- `PORT=8080`
- `DB_URL=jdbc:postgresql://localhost:5432/boards_spring`
- `DB_USER=postgres`
- `DB_PASSWORD=postgres`

## Real-Time

- WebSocket endpoint: `ws://localhost:8080/ws`
- User-specific events are published to `/user/queue/boards`
- WebSocket auth requires `Authorization: Bearer <token>` in the STOMP `CONNECT` headers

## Notes

- `POST /users` and `POST /auth/login` are public; the rest of the API requires JWT Bearer auth unless documented otherwise.
- In the `dev` profile, Flyway also loads `db/seed`; in `prod`, only migrations are enabled.
- Swagger is enabled in `dev` and disabled in `prod`.
