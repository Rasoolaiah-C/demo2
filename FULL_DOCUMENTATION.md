# Experiment Data Management System

A full-stack backend application for managing experiment data with comprehensive CRUD operations, RESTful APIs, containerization, Kubernetes deployment, and CI/CD automation.

## Features

✅ **Backend Architecture**
- Spring Boot 3.2.0 with Java 21
- JPA/Hibernate ORM for database management
- H2 in-memory database (easily switchable to PostgreSQL/MySQL)
- RESTful API with standardized response format
- CORS enabled for frontend integration

✅ **API Endpoints**
- Complete CRUD operations (Create, Read, Update, Delete)
- Advanced filtering by status, researcher, experiment type
- Full-text search functionality
- Health check endpoint
- Standardized JSON responses with success/error handling

✅ **Containerization**
- Optimized multi-stage Docker build
- Reduced image size using JRE instead of JDK in runtime
- Non-root user for security
- Health checks integrated
- Memory optimization flags

✅ **Kubernetes Deployment**
- Rolling updates for zero-downtime deployments
- Horizontal Pod Autoscaler (HPA) for auto-scaling
- Liveness and readiness probes
- Resource limits and requests
- ConfigMap for configuration management
- NodePort service for accessibility

✅ **CI/CD Pipeline**
- Jenkins pipeline with complete automation
- Build, test, and package stages
- Docker image build and push to registry
- Kubernetes deployment automation
- Health checks and rollout verification
- Email notifications for success/failure

✅ **Version Control**
- Git repository management
- GitHub integration with webhooks
- Proper commit history and branching

✅ **API Testing**
- Postman collection with all endpoints
- Sample workflows for testing
- Filter and query examples
- CRUD operation examples

## Project Structure

```
demo2/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java
│   │   │   ├── controller/
│   │   │   │   └── ExperimentController.java
│   │   │   ├── model/
│   │   │   │   └── Experiment.java
│   │   │   └── repository/
│   │   │       └── ExperimentRepository.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── k8s/
│   ├── deployment.yaml
│   └── service.yaml
├── Dockerfile
├── Jenkinsfile
├── pom.xml
├── mvnw & mvnw.cmd
└── Experiment_Data_API.postman_collection.json
```

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- Docker
- Kubernetes cluster (minikube, Docker Desktop K8s, or cloud)
- Jenkins (for CI/CD)
- Postman (for API testing)
- Git

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/Rasoolaiah-C/demo2.git
   cd demo2
   ```

2. **Build the project**
   ```bash
   mvn clean package
   ```

3. **Run the application**
   ```bash
   java -jar target/demo-0.0.1-SNAPSHOT.jar
   ```

4. **Access the application**
   - API: `http://localhost:8081/api/v1/experiments`
   - H2 Console: `http://localhost:8081/h2-console`

### Docker

1. **Build Docker image**
   ```bash
   docker build -t RasoolaiahC/demo-app:latest .
   ```

2. **Run Docker container**
   ```bash
   docker run -d --name demo-app -p 8081:8081 RasoolaiahC/demo-app:latest
   ```

3. **Access the application**
   ```bash
   curl http://localhost:8081/api/v1/experiments/health
   ```

### Kubernetes Deployment

1. **Apply configurations**
   ```bash
   kubectl apply -f k8s/deployment.yaml
   kubectl apply -f k8s/service.yaml
   ```

2. **Check deployment status**
   ```bash
   kubectl get pods
   kubectl get svc
   ```

3. **Access the service**
   ```bash
   # Get NodePort
   kubectl get svc demo-app-service -o jsonpath='{.spec.ports[0].nodePort}'
   
   # Access via NodePort
   curl http://localhost:30081/api/v1/experiments
   ```

### Jenkins CI/CD Pipeline

1. **Prerequisites**
   - Jenkins installed and running
   - Git plugin configured
   - Docker and kubectl installed on Jenkins agent
   - Docker credentials configured

2. **Create Jenkins Job**
   - Create new Pipeline job
   - Point to this repository
   - Set script path to `Jenkinsfile`
   - Configure GitHub webhook

3. **Pipeline Stages**
   - Checkout Code
   - Build Maven Project
   - Unit Tests
   - Code Quality Analysis
   - Docker Build
   - Docker Push
   - Deploy to Kubernetes
   - Health Check
   - Local Docker Run Testing

## API Documentation

### Base URL
```
http://localhost:8081/api/v1/experiments
```

### Endpoints

#### 1. Health Check
```http
GET /health
```
Response:
```json
{
  "success": true,
  "message": "API is healthy",
  "data": null
}
```

#### 2. Get All Experiments
```http
GET /
```

#### 3. Get Experiment by ID
```http
GET /{id}
```

#### 4. Create Experiment
```http
POST /
Content-Type: application/json

{
  "name": "Quantum Computing Experiment",
  "description": "Testing quantum algorithms",
  "status": "ACTIVE",
  "researcherName": "Dr. John Doe",
  "experimentType": "Quantum Computing",
  "durationHours": 48
}
```

#### 5. Update Experiment
```http
PUT /{id}
Content-Type: application/json

{
  "name": "Updated Name",
  "status": "IN_PROGRESS"
}
```

#### 6. Delete Experiment
```http
DELETE /{id}
```

#### 7. Filter by Status
```http
GET /status/{status}
```

#### 8. Filter by Researcher
```http
GET /researcher/{researcherName}
```

#### 9. Search by Name
```http
GET /search/{name}
```

### Response Format

**Success Response:**
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "id": 1,
    "name": "Experiment Name",
    "description": "Description",
    "status": "ACTIVE",
    "createdAt": "2024-11-15T10:30:00",
    "updatedAt": "2024-11-15T10:30:00",
    "researcherName": "Dr. Name",
    "experimentType": "Type",
    "durationHours": 24
  }
}
```

**Error Response:**
```json
{
  "success": false,
  "message": "Error message",
  "data": null
}
```

## Testing with Postman

1. **Import Collection**
   - Open Postman
   - Click Import
   - Select `Experiment_Data_API.postman_collection.json`

2. **Set Environment Variables**
   - Set `baseUrl` to your server URL

3. **Run Tests**
   - Use pre-built requests in the collection
   - Try sample workflows

## Database Configuration

### Current: H2 (In-Memory)
```properties
spring.datasource.url=jdbc:h2:mem:experimentdb
spring.h2.console.enabled=true
```

### Switch to PostgreSQL

1. Add dependency in `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.postgresql</groupId>
       <artifactId>postgresql</artifactId>
       <version>42.7.1</version>
   </dependency>
   ```

2. Update `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/experiments
   spring.datasource.username=postgres
   spring.datasource.password=password
   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
   ```

## Performance Optimization

- Connection pooling configured
- Hibernate batch processing enabled
- Lazy loading for relationships
- Indexed database columns
- Docker memory limits: 512MB max, 256MB min
- Kubernetes HPA configured with CPU and memory thresholds

## Security Features

- Non-root Docker user (appuser)
- CORS properly configured
- Input validation in controllers
- Prepared statements for SQL injection prevention
- Resource limits in Kubernetes

## Monitoring and Logging

- Comprehensive logging at DEBUG level for application
- INFO level for frameworks
- Health checks integrated
- Pod readiness and liveness probes
- Kubernetes event logging

## Troubleshooting

### Port Already in Use
```bash
# Find and kill process using port 8081
netstat -ano | findstr :8081
taskkill /PID <PID> /F
```

### Docker Image Issues
```bash
# Clean up
docker system prune -a

# Rebuild
docker build -t RasoolaiahC/demo-app:latest --no-cache .
```

### Kubernetes Deployment Issues
```bash
# Check pod logs
kubectl logs -f deployment/demo-app

# Describe pod
kubectl describe pod <pod-name>

# Check service
kubectl get svc demo-app-service -o wide
```

## Contributing

1. Create feature branch: `git checkout -b feature/name`
2. Commit changes: `git commit -am 'Add feature'`
3. Push to branch: `git push origin feature/name`
4. Create Pull Request

## License

This project is open source and available under the MIT License.

## Support

For issues and questions, please open an issue on GitHub or contact the development team.

---

**Last Updated:** November 15, 2024  
**Version:** 0.0.1-SNAPSHOT  
**Java Version:** 21  
**Spring Boot:** 3.2.0
