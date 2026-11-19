LindaKey API Gateway
LindaKey is a lightweight, zero-trust API gateway and access management system. It acts as a secure proxy layer sitting between client-side applications and backend APIs, providing centralized authentication, real-time traffic logging, and web-based API key management.


LindaKey is designed to sit in front of existing platforms as a reverse proxy, meaning you can integrate into you systems it without rewriting the existing platform's core code. Because it operates over standard HTTP/HTTPS, it is completely agnostic to the language or framework of the backend it protects.

Core Features
Zero-Trust Proxy Routing: Intercepts, validates, and forwards incoming HTTP requests to protected backend microservices.

Dynamic API Key Management: Generate, scope, and instantly revoke API keys via a graphical web dashboard.

Traffic Analytics: Logs endpoint hits, HTTP methods, response statuses, client IPs, and upstream latency in real-time.

Rate Limiting: Protects backend resources from DDoS attacks and abusive client behavior.

Container-Native Deployment: Fully Dockerized architecture combining the Spring Boot gateway and PostgreSQL database into a single, highly portable stack.

Technology Stack
Backend: Java 21, Spring Boot 3, Spring Security, Spring Data JPA, Spring JDBC

Database: PostgreSQL (with Flyway for schema migrations)

Frontend Dashboard: Vanilla JavaScript, HTML5, CSS3

Infrastructure: Docker, Docker Compose
