# ADS Backend

ADS (Advanced Dental System) Backend is a Spring Boot application that powers the core features of a dental clinic management platform. It includes user management, appointment scheduling, surgery tracking, billing, and security via JWT.

## 🚀 Features

- ✅ Patient and Dentist Management
- 📅 Appointment Scheduling with Weekly Limits
- 🏥 Surgery CRUD operations
- 💳 Billing Management with Payment Status
- 🔐 Secure endpoints with JWT-based authentication
- 🐳 Dockerized for easy local and CI/CD deployment

## 🔧 Technologies Used

- Java 21
- Spring Boot 3.4
- PostgreSQL
- Maven
- Docker & Docker Compose
- GitHub Actions

## 🛠️ Local Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/elioccansey/ads-backend.git
   cd ads-backend
   ```
2. Run the release script to build, version, and launch the app with Docker:
   ```bash
   ./release.sh <version> <port>
   # Example:
   ./release.sh 1.0.0 8080
   ```

