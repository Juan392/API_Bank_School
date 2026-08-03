# 🏦 Banco REST API — Spring Boot & Security

API REST para la gestión de clientes bancarios, cuentas y transferencias monetarias transaccionales con autenticación sin estado mediante **Spring Security** y **JWT (JSON Web Tokens)**.

---

## 🛠️ Tecnologías y Arquitectura

- **Java 21**
- **Spring Boot 4.1.0** (Web, Data JPA, Security, Validation)
- **PostgreSQL** & **Flyway** (Control de versiones de base de datos)
- **Auth0 Java-JWT (4.6.0)**
- **Lombok**
- **Gradle**

---

## 🔒 Arquitectura de Seguridad

1. **Autenticación sin estado (Stateless):** Mediante `SecurityFilter` (`OncePerRequestFilter`), validando firmas HMAC256 y extrayendo `Subject` y `Claims`.
2. **Encriptación de Contraseñas:** Algoritmo **BCrypt** (`PasswordEncoder`).
3. **Validación de Propiedad (Ownership Guard):** Ningún usuario autenticado puede consultar saldos, movimientos ni transferir fondos desde cuentas que no le pertenezcan.
4. **Manejo Centralizado de Excepciones:** `GlobalExceptionHandler` (`@RestControllerAdvice`) devolviendo respuestas estándar en formato JSON (400, 401, 403, 404, 500).

---

## 🚀 Endpoints de la API

### 1. Autenticación (`/api/auth`)

| Método | Endpoint | Descripción | Acceso |
|---|---|---|---|
| `POST` | `/api/auth/register` | Registro de nuevo cliente con hash BCrypt | Público |
| `POST` | `/api/auth/login` | Login con email y contraseña (retorna JWT) | Público |
| `GET` | `/api/auth/me` | Obtiene los datos del cliente autenticado | Requiere JWT |

#### Payload Registro (`/api/auth/register`)
```json
{
  "name": "Juan Pablo",
  "email": "juan@bank.com",
  "password": "miPasswordSeguro123"
}
```

#### Payload Login (`/api/auth/login`)
```json
{
  "email": "juan@bank.com",
  "password": "miPasswordSeguro123"
}
```

---

### 2. Cuentas Bancarias (`/api/cuentas`)

| Método | Endpoint | Descripción | Acceso |
|---|---|---|---|
| `GET` | `/api/cuentas` | Lista todas las cuentas del cliente autenticado | Requiere JWT |
| `POST` | `/api/cuentas` | Apertura de una nueva cuenta bancaria | Requiere JWT |
| `GET` | `/api/cuentas/{id}/saldo` | Consulta el saldo disponible de una cuenta | Requiere JWT (Dueño) |
| `GET` | `/api/cuentas/{id}/movimientos` | Historial de transferencias (origen y destino) | Requiere JWT (Dueño) |

#### Payload Apertura de Cuenta (`/api/cuentas`)
```json
{
  "initialDeposit": 1000.00
}
```

---

### 3. Transferencias (`/api/transacciones`)

| Método | Endpoint | Descripción | Acceso |
|---|---|---|---|
| `POST` | `/api/transacciones/transferir` | Realiza una transferencia entre dos cuentas | Requiere JWT (Dueño de origen) |

#### Payload Transferencia (`/api/transacciones/transferir`)
```json
{
  "idAccountOrigin": 1,
  "numberAccountDestiny": "ACC-1002",
  "amount": 250.00
}
```

---

## 💻 Ejecución Local

### 1. Variables de Entorno (Opcional)
```bash
export DB_URL=jdbc:postgresql://localhost:5432/banco
export DB_USER=postgres
export DB_PASSWORD=tu_password
export JWT_SECRET=clave_secreta_super_segura
```

### 2. Compilación y Ejecución
```bash
./gradlew bootRun
```
