#FROM openjdk:21
#COPY "target/sistema-censo.jar" sistema-censo.jar
#ENV PORT 1705
#EXPOSE $PORT
#ENTRYPOINT ["java", "-jar", "sistema-censo.jar", "--server.port=${PORT}"]
# ---- Etapa de construcción (Build) ----
# ---- Etapa de construcción (Build) ----
# Etapa 1: Construcción de la aplicación con Maven y Java 21
FROM maven:3.9.2-eclipse-temurin-21 AS build
WORKDIR /app
# Copia los archivos de configuración y el código fuente
COPY pom.xml .
COPY src/ ./src/
# Empaqueta la aplicación (se generará el archivo .jar en target/)
RUN mvn clean package -DskipTests

# Etapa 2: Creación de la imagen final para ejecutar la aplicación
FROM openjdk:21-jdk-slim
WORKDIR /app
# Copia el JAR generado en la etapa de construcción
COPY --from=build /app/target/*.jar app.jar
# Expone el puerto 8080 (ajusta si tu aplicación utiliza otro puerto)
EXPOSE 8080
# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
