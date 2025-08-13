# Taufer Tales API

A Spring Boot 3 (Java 21) REST API for managing books (“tales”), reviews, and comments — with JWT-based authentication.

## ✨ Features

- JWT login/register with BCrypt password hashing
- CRUD for Tales
- Create/Update/Delete Reviews (1 review per user per tale) + average rating per tale
- Threaded Comments on reviews
- Validation via Jakarta Bean Validation
- Global error handler returning consistent JSON errors
- OpenAPI (Swagger UI) docs with Bearer authentication
- CORS configured for your frontend origin

## 🧱 Tech stack

- Spring Boot 3.x, Spring Web, Spring Security, Spring Data JPA
- MySQL (or any JDBC-compatible DB)
- JJWT for tokens
- springdoc-openapi for docs
- Lombok

## ⚙️ Configuration

All configuration lives in `src/main/resources/application.yml`. Override via environment variables where appropriate.

| Property | Env Var | Description |
|---|---|---|
| `spring.datasource.url` | `SPRING_DATASOURCE_URL` | JDBC URL |
| `spring.datasource.username` | `SPRING_DATASOURCE_USERNAME` | DB user |
| `spring.datasource.password` | `SPRING_DATASOURCE_PASSWORD` | DB password |
| `app.jwt.secret` | `APP_JWT_SECRET` | **Base64-encoded** secret for HS256 |
| `app.jwt.expiration-ms` | `APP_JWT_EXPIRATION_MS` | Token lifetime (ms) |
| `app.cors.allowed-origins` | `APP_CORS_ALLOWED_ORIGINS` | Comma-separated origins |

### Example (dev)

```bash
export SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/taufer_tales?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC'
export SPRING_DATASOURCE_USERNAME='root'
export SPRING_DATASOURCE_PASSWORD='root'
export APP_JWT_SECRET='REPLACE_WITH_BASE64_SECRET'   # e.g. `openssl rand -base64 32`
export APP_CORS_ALLOWED_ORIGINS='http://localhost:4200'

./mvnw spring-boot:run
```

## 🚀 Run with Docker

Build and run locally using the provided multi-stage Dockerfile:

```bash
docker build -t taufer-tales-api .
docker run --rm -p 8080:8080   -e SPRING_DATASOURCE_URL='jdbc:mysql://host.docker.internal:3306/taufer_tales?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC'   -e SPRING_DATASOURCE_USERNAME=root   -e SPRING_DATASOURCE_PASSWORD=root   -e APP_JWT_SECRET='REPLACE_WITH_BASE64_SECRET'   -e APP_CORS_ALLOWED_ORIGINS='http://localhost:4200'   taufer-tales-api
```

## 🔐 Auth

- `POST /api/auth/register` – create user; returns `{ token, username }`
- `POST /api/auth/login` – authenticate; returns `{ token, username }`

Use the token with `Authorization: Bearer <token>` for all protected endpoints.

## 📚 API (selected)

- `GET /api/tales` – list with pagination & optional search
- `POST /api/tales` – create tale
- `PATCH /api/tales/{id}` – update tale
- `DELETE /api/tales/{id}` – delete tale

- `GET /api/reviews/tale/{taleId}` – list reviews for a tale
- `POST /api/reviews` – create review (one per user per tale)
- `PATCH /api/reviews/{id}` – update review
- `DELETE /api/reviews/{id}` – delete review
- `GET /api/reviews/my?taleId={id}` – get **your** review for a tale

- `GET /api/comments/review/{reviewId}` – list comments
- `POST /api/comments` – add comment (optionally with `parentId`)

> Exact contracts are visible in Swagger UI: start the app, then open `http://localhost:8080/swagger-ui/index.html`.

## 🧰 Development Notes

- Entities are decoupled from API payloads using small DTOs and manual mappers.
- Controllers validate input with `@Validated`/`@Valid` and delegate to services.
- Security is stateless (JWT). Passwords use BCrypt and are never returned by the API (the entity field is `@JsonIgnore`, and controllers use DTOs).
- Consistent error responses via `GlobalExceptionHandler` (`/src/main/java/com/taufer/tales/error`).

## ✅ Consistency & Cleanups (what was improved)

- Removed duplicate imports and minor style nits across controllers/services.
- Added `@JsonIgnore` on `User.password` for extra safety.
- Swagger/OpenAPI now declares a `bearerAuth` security scheme and requires it globally.
- Kept all endpoint paths and request/response shapes untouched.

## 🧪 Testing

Use any HTTP client (curl, Postman). Quick smoke test:

```bash
# Register
curl -s http://localhost:8080/api/auth/register   -H 'Content-Type: application/json'   -d '{"username":"alice","email":"alice@example.com","password":"changeit123"}' | jq .

# Login
TOKEN=$(curl -s http://localhost:8080/api/auth/login   -H 'Content-Type: application/json'   -d '{"username":"alice","password":"changeit123"}' | jq -r .token)

# Create a tale
curl -s http://localhost:8080/api/tales   -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json'   -d '{"title":"Dune","author":"Frank Herbert"}' | jq .
```

## 📄 License

MIT (or your preferred license).

