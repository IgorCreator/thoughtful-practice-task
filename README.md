# Package Sorting System

A RESTful API for sorting packages into stacks based on dimensions and weight.

## Sorting Rules

Packages are classified as:

- **Bulky**: volume ≥ 1,000,000 cm³ OR any dimension ≥ 150 cm
- **Heavy**: mass ≥ 20 kg

Stack assignment:

- `STANDARD` → neither bulky nor heavy
- `SPECIAL` → bulky OR heavy (but not both)
- `REJECTED` → both bulky AND heavy

## Prerequisites

- Java 17+
- Maven 3.6+

Install on macOS:

```bash
brew install openjdk@17 maven
```

## Quick Start

### 1. Build the application

```bash
mvn clean package
```

### 2. Run the API server

```bash
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

### 3. Access the Web Interface

Open your browser and navigate to:

```
http://localhost:8080
```

You'll see a beautiful web interface where you can:

- Enter package dimensions (width, height, length) in cm
- Enter package mass in kg
- Get instant sorting results (STANDARD, SPECIAL, or REJECTED)
- See detailed package information and classification

### 4. Run tests

```bash
mvn test
```

## API Documentation

### Endpoints

#### Health Check

```
GET /api/health
```

**Response:**

```
Package Sorting API is running
```

#### Sort Package

```
POST /api/sort
```

**Request Body:**

```json
{
  "width": 100.0,
  "height": 50.0,
  "length": 75.0,
  "mass": 15.0
}
```

**Success Response (200 OK):**

```json
{
  "category": "STANDARD"
}
```

**Validation Error Response (400 Bad Request):**

```json
{
  "timestamp": "2025-12-18T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "messages": ["Width must be positive"],
  "path": "/api/sort"
}
```

### Request Validation

All fields are required and must be positive numbers:

- `width` (Double): Package width in cm (> 0)
- `height` (Double): Package height in cm (> 0)
- `length` (Double): Package length in cm (> 0)
- `mass` (Double): Package mass in kg (> 0)

### Example cURL Commands

**Standard Package:**

```bash
curl -X POST http://localhost:8080/api/sort \
  -H "Content-Type: application/json" \
  -d '{"width":50,"height":50,"length":50,"mass":10}'
```

**Bulky Package (by dimension):**

```bash
curl -X POST http://localhost:8080/api/sort \
  -H "Content-Type: application/json" \
  -d '{"width":150,"height":50,"length":50,"mass":10}'
```

**Heavy Package:**

```bash
curl -X POST http://localhost:8080/api/sort \
  -H "Content-Type: application/json" \
  -d '{"width":50,"height":50,"length":50,"mass":20}'
```

**Rejected Package (bulky and heavy):**

```bash
curl -X POST http://localhost:8080/api/sort \
  -H "Content-Type: application/json" \
  -d '{"width":150,"height":100,"length":100,"mass":25}'
```

**Invalid Input (negative value):**

```bash
curl -X POST http://localhost:8080/api/sort \
  -H "Content-Type: application/json" \
  -d '{"width":-50,"height":50,"length":50,"mass":10}'
```

## Project Structure

```
src/
├── main/
│   ├── java/com/thoughtful/sorting/
│   │   ├── PackageSortingApplication.java  # Spring Boot main class
│   │   ├── PackageSortingController.java   # REST controller
│   │   ├── PackageSorter.java              # Core sorting logic
│   │   ├── PackageRequest.java             # Request DTO
│   │   ├── PackageResponse.java            # Response DTO
│   │   ├── ErrorResponse.java              # Error DTO
│   │   └── GlobalExceptionHandler.java     # Exception handling
│   └── resources/
│       ├── application.properties           # Configuration
│       └── static/
│           └── index.html                   # Web interface
└── test/
    └── java/com/thoughtful/sorting/
        ├── PackageSorterTest.java           # Unit tests
        └── PackageSortingControllerTest.java # Integration tests
```

## Features

### Backend (RESTful API)

- ✅ POST `/api/sort` - Sort packages based on dimensions and mass
- ✅ GET `/api/health` - Health check endpoint
- ✅ Comprehensive input validation with clear error messages
- ✅ Exception handling for invalid JSON and data types
- ✅ Bean Validation for request parameters

### Frontend (Web Interface)

- ✅ Modern, responsive UI with Tailwind CSS
- ✅ Real-time client-side validation
- ✅ Interactive form with helpful error messages
- ✅ Visual sorting results with detailed package information
- ✅ Mobile-friendly design
- ✅ No build process required

## Development

### Running in Development Mode

```bash
mvn spring-boot:run
```

### Building for Production

```bash
mvn clean package
java -jar target/package-sorting-1.0.0.jar
```

### Code Quality

Run Checkstyle:

```bash
mvn checkstyle:check
```
