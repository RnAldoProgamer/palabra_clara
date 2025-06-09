# Usar imagen base con Maven preinstalado para ahorrar espacio
FROM maven:3.9.2-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Configurar Maven para usar menos memoria y espacio
ENV MAVEN_OPTS="-Xmx256m -XX:MaxMetaspaceSize=64m -Dmaven.repo.local=/tmp/.m2"

# Copiar solo pom.xml y descargar dependencias críticas
COPY pom.xml .

# Intentar descargar dependencias con configuración minimal
RUN mvn dependency:resolve -B -DexcludeTransitive=false || true

# Copiar código fuente
COPY src/ ./src/

# Compilar con configuración mínima y limpieza inmediata
RUN mvn clean package -DskipTests -B -Dcheckstyle.skip=true -Dpmd.skip=true -Dspotbugs.skip=true && \
    ls -la target/ && \
    rm -rf /tmp/.m2 && \
    rm -rf ~/.m2 && \
    rm -rf /app/src && \
    rm -rf /tmp/* && \
    find /app/target -name "*.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar"

# Etapa final: imagen mínima
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Instalar FFmpeg de forma mínima
RUN apk add --no-cache ffmpeg && \
    rm -rf /var/cache/apk/* && \
    rm -rf /tmp/*

# Crear usuario no-root
RUN addgroup -g 1001 -S appuser && \
    adduser -S -D -H -u 1001 -h /app -s /sbin/nologin -G appuser appuser

# Copiar solo el JAR principal
COPY --from=build /app/target/*.jar app.jar

# Verificar que el JAR existe
RUN ls -la app.jar && chown appuser:appuser app.jar

USER appuser

EXPOSE 8080

# JVM optimizada para contenedores con poca memoria
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:InitialRAMPercentage=50.0", \
    "-XX:MaxRAMPercentage=80.0", \
    "-XX:+UseSerialGC", \
    "-Xss256k", \
    "-XX:MaxMetaspaceSize=64m", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]