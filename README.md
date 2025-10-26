# Textile Factory Inventory Backend

A comprehensive backend API for managing textile fabric inventory built with **Kotlin**, **Spring Boot**, and **MySQL**. Features JWT authentication, role-based access control, and full CRUD operations for product management.

## 🚀 Features

- **JWT Authentication**: Secure token-based authentication with Bearer tokens
- **Role-Based Access Control**: Admin and User roles with different permissions
- **Product Management**: Complete CRUD operations for textile products
- **MySQL Database**: Persistent storage with JPA/Hibernate
- **RESTful API**: Clean and well-structured API endpoints
- **OpenAPI/Swagger Documentation**: Interactive API documentation at `/swagger-ui.html`
- **Comprehensive Logging**: Application and error logs with daily rotation
- **Input Validation**: Request validation with meaningful error messages
- **Exception Handling**: Global exception handler for consistent error responses

## 📋 Prerequisites

- **Java 17** or higher
- **MySQL 8.0** or higher
- **Gradle 7.0** or higher (or use the Gradle wrapper)

## 🛠️ Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd textile_factory_inventory_web_backend_2025
```

### 2. Configure MySQL Database

Create a MySQL database or let the application create it automatically:

```sql
CREATE DATABASE IF NOT EXISTS textile_inventory;
```

Update database credentials in `src/main/resources/application.yml` if needed:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/textile_inventory?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: root
```

### 3. Build the Project

```bash
./gradlew build
```

Or on Windows:

```bash
gradlew.bat build
```

### 4. Run the Application

```bash
./gradlew bootRun
```

Or on Windows:

```bash
gradlew.bat bootRun
```

The application will start on `http://localhost:8080`

### 5. Access the Application

- **API Base URL**: `http://localhost:8080/api`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:8080/v3/api-docs`

## 👥 Default Users

The application creates two default users on first startup:

| Username | Password  | Role  |
|----------|-----------|-------|
| admin    | admin123  | ADMIN |
| user     | user123   | USER  |

## 📚 API Documentation

### Interactive Documentation

The API is fully documented using **OpenAPI 3.0** specification. You can explore and test all endpoints interactively:

**Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

The Swagger UI provides:
- Complete API documentation
- Interactive testing interface
- Request/response examples
- Authentication support (JWT Bearer token)

### API Endpoints Overview

### Authentication

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "admin",
  "role": "ADMIN"
}
```

### Products

All product endpoints require authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer <your-token>
```

#### Get All Products (USER & ADMIN)
```http
GET /api/products
```

#### Get Product by ID (USER & ADMIN)
```http
GET /api/products/{id}
```

#### Create Product (ADMIN only)
```http
POST /api/products
Content-Type: application/json

{
  "name": "Cotton Fabric",
  "description": "High quality cotton fabric",
  "color": "Blue",
  "size": "Large",
  "image": "https://example.com/image.jpg"
}
```

#### Update Product (ADMIN only)
```http
PUT /api/products/{id}
Content-Type: application/json

{
  "name": "Cotton Fabric Updated",
  "description": "Premium cotton fabric",
  "color": "Navy Blue",
  "size": "Extra Large",
  "image": "https://example.com/new-image.jpg"
}
```

#### Delete Product (ADMIN only)
```http
DELETE /api/products/{id}
```

#### Search Products by Name (USER & ADMIN)
```http
GET /api/products/search?name=cotton
```

#### Get Products by Color (USER & ADMIN)
```http
GET /api/products/color/Blue
```

#### Get Products by Size (USER & ADMIN)
```http
GET /api/products/size/Large
```

#### Upload Product Image (ADMIN only)
```http
POST /api/products/upload-image
Content-Type: multipart/form-data

file: [image file]
```

**Response:**
```json
{
  "message": "Image uploaded successfully",
  "filename": "1730000000000_a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg"
}
```

**Notes:**
- Supported formats: jpg, jpeg, png, gif, webp
- Maximum file size: 10MB
- The returned filename should be used in the `image` field when creating/updating products
- Images are stored in the `uploads/images/` directory
- Uploaded images can be accessed at: `http://localhost:8080/uploads/images/{filename}`

## 🔒 Security

- **JWT Token Expiration**: 24 hours
- **Password Encryption**: BCrypt
- **CORS**: Enabled for all origins (configure for production)
- **Role-Based Authorization**:
  - **ADMIN**: Full CRUD access to products
  - **USER**: Read-only access to products

## 📝 Logging

The application includes comprehensive logging with the following features:

### Log Files

- **Application Logs**: `logs/textile-inventory.log`
  - All application logs (INFO level and above)
  - Daily rotation with 30-day retention
  - Maximum total size: 1GB

- **Error Logs**: `logs/textile-inventory-error.log`
  - Error-level logs only
  - Daily rotation with 30-day retention
  - Maximum total size: 500MB

### Log Format

```
2025-10-24 18:00:00.123 [http-nio-8080-exec-1] INFO  c.t.i.controller.AuthController - Login attempt for user: admin
2025-10-24 18:00:00.456 [http-nio-8080-exec-1] INFO  c.t.i.controller.AuthController - User admin logged in successfully with role ADMIN
```

### Logged Events

- User authentication attempts and results
- Product CRUD operations
- Database queries (SQL statements in DEBUG mode)
- Application errors and exceptions

## 📦 Database Schema

### Users Table
- `id` (BIGINT, Primary Key)
- `username` (VARCHAR, Unique)
- `password` (VARCHAR, Encrypted)
- `role` (ENUM: ADMIN, USER)
- `enabled` (BOOLEAN)

### Products Table
- `id` (BIGINT, Primary Key)
- `name` (VARCHAR)
- `description` (TEXT)
- `color` (VARCHAR)
- `size` (VARCHAR)
- `image` (TEXT)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

**Note**: Tables are created automatically by Hibernate on first run. No manual SQL scripts required.

## 🧪 Testing with cURL

### Login as Admin
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### Create a Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-token>" \
  -d '{
    "name": "Silk Fabric",
    "description": "Premium silk fabric",
    "color": "Red",
    "size": "Medium",
    "image": "https://example.com/silk.jpg"
  }'
```

### Get All Products
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer <your-token>"
```

## 🏗️ Project Structure

```
src/main/kotlin/com/textile/inventory/
├── config/              # Configuration classes
│   ├── DataInitializer.kt
│   └── OpenApiConfig.kt
├── controller/          # REST controllers
│   ├── AuthController.kt
│   └── ProductController.kt
├── dto/                 # Data Transfer Objects
│   ├── LoginRequest.kt
│   ├── LoginResponse.kt
│   ├── ProductRequest.kt
│   ├── ProductResponse.kt
│   └── ErrorResponse.kt
├── exception/           # Exception handlers
│   └── GlobalExceptionHandler.kt
├── model/              # JPA entities
│   ├── User.kt
│   ├── Product.kt
│   └── Role.kt
├── repository/         # JPA repositories
│   ├── UserRepository.kt
│   └── ProductRepository.kt
├── security/           # Security configuration
│   ├── SecurityConfig.kt
│   ├── JwtTokenProvider.kt
│   ├── JwtAuthenticationFilter.kt
│   └── JwtAuthenticationEntryPoint.kt
├── service/            # Business logic
│   ├── AuthService.kt
│   ├── ProductService.kt
│   └── CustomUserDetailsService.kt
└── TextileFactoryInventoryApplication.kt

src/main/resources/
├── application.yml      # Application configuration
└── logback-spring.xml   # Logging configuration
```

## 🔧 Configuration

Key configuration properties in `application.yml`:

- **Server Port**: 8080
- **Database**: MySQL (auto-create enabled)
- **JWT Secret**: Configurable secret key
- **JWT Expiration**: 24 hours
- **File Upload**: Max 10MB
- **Logging**: Configured via `logback-spring.xml`

## 📝 License

This project is licensed under the MIT License.

## 👨‍💻 Author

Textile Factory Inventory Management System
