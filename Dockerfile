FROM openjdk:20-jdk-alpine AS build
WORKDIR /app

ENV LANG C.UTF-8
ENV LC_ALL C.UTF-8

# Variables para Maven
ARG MAVEN_VERSION=3.9.2
ARG MAVEN_BASE_URL=${MAVEN_VERSION}/binaries

# Instalar dependencias, descargar y descomprimir Maven
RUN apk update && apk add --no-cache curl tar && \
    curl -fsSL ${MAVEN_BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz -o maven.tar.gz && \
    tar -xzf maven.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm maven.tar.gz && \
    rm -rf /var/cache/apk/*

ENV MAVEN_HOME=/opt/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

# Copiar archivos del proyecto y compilar
COPY pom.xml . 
COPY src/ ./src/
RUN mvn clean package -DskipTests

# Etapa final: imagen de ejecución con OpenJDK 20 y FFmpeg
FROM openjdk:20-jdk-alpine
WORKDIR /app

# Instalar FFmpeg
RUN apk update && apk add --no-cache ffmpeg

# Copiar el archivo JAR construido en la etapa de construcción
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]