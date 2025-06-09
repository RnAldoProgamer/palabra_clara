# Etapa de construcción: usar OpenJDK 21 y descargar Maven 3.9.2 manualmente
FROM openjdk:21-slim AS build
WORKDIR /app

ENV LANG C.UTF-8
ENV LC_ALL C.UTF-8

# Variables para Maven
ARG MAVEN_VERSION=3.9.2
ARG MAVEN_BASE_URL=${MAVEN_VERSION}/binaries

# Instalar dependencias, descargar y descomprimir Maven
RUN apt-get update && apt-get install -y curl tar && \
    curl -fsSL ${MAVEN_BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz -o maven.tar.gz && \
    tar -xzf maven.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm maven.tar.gz && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

ENV MAVEN_HOME=/opt/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

# Copiar archivos del proyecto y compilar
COPY pom.xml . 
COPY src/ ./src/
RUN mvn clean package -DskipTests

# Etapa final: imagen de ejecución con OpenJDK 21 y FFmpeg
FROM openjdk:21-jdk-slim
WORKDIR /app

# Instalar FFmpeg con dependencias ACTUALIZADAS para Debian 12
RUN apt-get update && apt-get install -y ffmpeg && apt-get clean && rm -rf /var/lib/apt/lists/*

# Copiar el archivo JAR construido en la etapa de construcción
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]