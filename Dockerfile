# Stage 1: Build stage
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /build

# Copy maven files
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src src

# Build the application
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy built jar from builder stage
COPY --from=builder /build/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Non-root user for security
RUN useradd -m -u 1000 appuser
USER appuser

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8081/api/v1/experiments/health || exit 1

ENTRYPOINT ["java", "-jar", "-Xmx512m", "-Xms256m", "app.jar"]
