#FROM openjdk:21
#COPY "target/sistema-censo.jar" sistema-censo.jar
#ENV PORT 1705
#EXPOSE $PORT
#ENTRYPOINT ["java", "-jar", "sistema-censo.jar", "--server.port=${PORT}"]
# ---- Etapa de construcción (Build) ----
FROM maven:3.8.6-openjdk-21-slim AS build
WORKDIR /app

# Copiar el código fuente y el pom.xml
COPY pom.xml .
COPY src ./src

# Compilar el proyecto y generar el JAR
RUN mvn clean package -DskipTests

# ---- Etapa de ejecución (Runtime) ----
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiar el JAR desde la etapa de construcción
COPY --from=build /app/target/*.jar ./app.jar

# Exponer el puerto (por defecto Spring Boot usa 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "palabra_clara.jar"]