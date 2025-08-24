FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /build/services/api_gateway/target/*.jar app.jar
EXPOSE 2004
ENTRYPOINT ["java", "-jar", "app.jar"]
