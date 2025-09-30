# Stage 1: Build
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# Stage 2: Runtime (distroless-ish)
FROM eclipse-temurin:17-jre-alpine
ENV TZ=UTC \
    JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseContainerSupport"
WORKDIR /opt/app
# Copy fat jar
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar
# Non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring
EXPOSE 8080
# Healthcheck ping vào actuator/health nếu có
HEALTHCHECK --interval=30s --timeout=3s --retries=5 \
  CMD wget -qO- http://127.0.0.1:8080/actuator/health | grep -q '"status":"UP"' || exit 1
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
