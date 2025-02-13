#FROM openjdk:21
#COPY "target/sistema-censo.jar" sistema-censo.jar
#ENV PORT 1705
#EXPOSE $PORT
#ENTRYPOINT ["java", "-jar", "sistema-censo.jar", "--server.port=${PORT}"]
# ---- Etapa de construcción (Build) ----
# ---- Etapa de construcción (Build) ----
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Instalar Maven manualmente
RUN apt-get update && \
    apt-get install -y curl && \
    curl -sL https://dlcdn.apache.org/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz -o maven.tar.gz && \
    mkdir -p /opt/maven && \
    tar -xzf maven.tar.gz -C /opt/maven --strip-components=1 && \
    ln -s /opt/maven/bin/mvn /usr/bin/mvn && \
    rm maven.tar.gz

# Copiar el código fuente
COPY pom.xml .
COPY src ./src

# Compilar el proyecto
RUN mvn clean package -DskipTests

# ---- Etapa de ejecución (Runtime) ----
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiar el JAR compilado
COPY --from=build /app/target/*.jar ./app.jar

EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]