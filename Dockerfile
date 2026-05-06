FROM gradle:9.3.1-jdk25 AS build

WORKDIR /app

COPY . .

RUN gradle clean bootJar --no-daemon


# RUNTIME
FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]