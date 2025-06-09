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

# Copiar solo pom.xml primero para aprovechar cache de Docker
COPY pom.xml .

# Descargar dependencias por separado (aprovecha cache de Docker)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src/ ./src/

# Compilar con configuraciones de memoria limitada y limpiar cache
RUN MAVEN_OPTS="-Xmx512m -XX:MaxMetaspaceSize=128m" mvn clean package -DskipTests -B && \
    rm -rf ~/.m2/repository && \
    rm -rf /tmp/* /var/tmp/*

# Etapa final: imagen de ejecución más liviana
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -g 1001 -S appuser && \
    adduser -S -D -H -u 1001 -h /app -s /sbin/nologin -G appuser appuser

# Instalar solo FFmpeg con limpieza agresiva
RUN apk update && \
    apk add --no-cache ffmpeg && \
    rm -rf /var/cache/apk/*

# Copiar el archivo JAR construido
COPY --from=build /app/target/*.jar app.jar

# Cambiar ownership del archivo
RUN chown appuser:appuser app.jar

# Cambiar a usuario no-root
USER appuser

EXPOSE 8080

# Usar configuraciones de JVM optimizadas para contenedores
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseG1GC", \
    "-XX:+UseStringDeduplication", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]