# Etapa de construcción: usar OpenJDK 21 y descargar Maven 3.9.2 manualmente
FROM openjdk:21-slim AS build
WORKDIR /app

# Usar formato recomendado para ENV
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

# Variables para Maven
ARG MAVEN_VERSION=3.9.2
ARG MAVEN_BASE_URL=https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries

# Instalar dependencias, descargar y descomprimir Maven en una sola capa
RUN apt-get update && apt-get install -y curl tar && \
    curl -fsSL ${MAVEN_BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz -o maven.tar.gz && \
    tar -xzf maven.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm maven.tar.gz && \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

ENV MAVEN_HOME=/opt/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

# Copiar archivos del proyecto y compilar
COPY pom.xml . 
COPY src/ ./src/
RUN mvn clean package -DskipTests && \
    rm -rf ~/.m2/repository

# Etapa final: imagen de ejecución con OpenJDK 21 y FFmpeg
FROM openjdk:21-jdk-slim
WORKDIR /app

# Instalar FFmpeg con limpieza agresiva para ahorrar espacio
RUN apt-get update && \
    apt-get install -y --no-install-recommends ffmpeg && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* /var/cache/apt/archives/*

# Copiar el archivo JAR construido en la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]