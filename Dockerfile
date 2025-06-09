# Versión de emergencia - máxima compatibilidad y mínimo espacio
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Instalar Maven de forma mínima
RUN apk add --no-cache curl tar && \
    curl -fsSL https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz -o maven.tar.gz && \
    tar -xzf maven.tar.gz -C /opt && \
    ln -s /opt/apache-maven-3.9.6 /opt/maven && \
    rm maven.tar.gz && \
    rm -rf /var/cache/apk/*

ENV MAVEN_HOME=/opt/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

# Configurar Maven para usar mínima memoria
ENV MAVEN_OPTS="-Xmx256m -XX:MaxMetaspaceSize=64m"

# Copiar archivos y compilar en un solo paso para minimizar capas
COPY pom.xml ./
COPY src/ ./src/

# Compilar con configuración ultra-mínima
RUN mvn clean package -DskipTests -B \
    -Dcheckstyle.skip=true \
    -Dpmd.skip=true \
    -Dspotbugs.skip=true \
    -Dmaven.javadoc.skip=true \
    -Dmaven.source.skip=true && \
    rm -rf ~/.m2 && \
    rm -rf /opt/maven && \
    rm -rf src && \
    rm -rf /tmp/* && \
    rm -rf /var/tmp/*

# Etapa final - imagen mínima
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Instalar FFmpeg
RUN apk add --no-cache ffmpeg && rm -rf /var/cache/apk/*

# Crear usuario
RUN addgroup -g 1001 -S appuser && \
    adduser -S -D -H -u 1001 -h /app -s /sbin/nologin -G appuser appuser

# Copiar JAR
COPY --from=build /app/target/*.jar app.jar
RUN chown appuser:appuser app.jar

USER appuser
EXPOSE 8080

# JVM optimizada para contenedores pequeños
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseSerialGC", \
    "-Xss256k", \
    "-jar", "app.jar"]