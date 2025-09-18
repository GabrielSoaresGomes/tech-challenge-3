FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /build

COPY pom.xml ./pom.xml
COPY services/history_service/pom.xml ./services/history_service/pom.xml

RUN mvn -q -DskipTests -f services/history_service/pom.xml dependency:go-offline

COPY services/history_service/src ./services/history_service/src

RUN mvn -q -DskipTests -f services/history_service/pom.xml clean package

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /build/services/history_service/target/*.jar app.jar

EXPOSE 2002

ENTRYPOINT ["java","-jar","app.jar"]
