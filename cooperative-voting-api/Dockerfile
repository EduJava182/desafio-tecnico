FROM eclipse-temurin:21-jdk AS build

WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean verify

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /app/target/cooperative-voting-api-1.0.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
