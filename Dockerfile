# Dockerfile ultra-optimizado para servidor ARM de AWS con espacio muy limitado
FROM openjdk:21-slim AS build
WORKDIR /app

# Variables de entorno básicas
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

# Variables para Maven
ARG MAVEN_VERSION=3.9.6
ARG MAVEN_BASE_URL=https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries

# PASO 1: Limpiar completamente antes de empezar
RUN apt-get clean && \
    rm -rf /var/lib/apt/lists/* /var/cache/apt/* /tmp/* /var/tmp/*

# PASO 2: Instalar solo curl y tar (sin FFmpeg por ahora)
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl tar && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /var/cache/apt/*

# PASO 3: Descargar e instalar Maven, luego limpiar inmediatamente
RUN curl -fsSL ${MAVEN_BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz -o maven.tar.gz && \
    tar -xzf maven.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm maven.tar.gz && \
    apt-get remove -y curl tar && \
    apt-get autoremove -y && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* /var/cache/apt/*

ENV MAVEN_HOME=/opt/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

# Configurar Maven para usar menos memoria y espacio
ENV MAVEN_OPTS="-Xmx512m -XX:MaxMetaspaceSize=128m -Dmaven.repo.local=/tmp/.m2"

# Copiar pom.xml primero para cache de dependencias
COPY pom.xml .

# Descargar dependencias por separado para limpiar cache Maven
RUN mvn dependency:go-offline -B --batch-mode --no-transfer-progress && \
    rm -rf /tmp/.m2/repository/org/apache/maven

# Copiar código fuente
COPY src/ ./src/

# Compilar con configuración específica para ARM y limpieza agresiva
RUN mvn clean package -DskipTests -B \
    -Dcheckstyle.skip=true \
    -Dpmd.skip=true \
    -Dspotbugs.skip=true \
    -Dmaven.javadoc.skip=true \
    -Dmaven.source.skip=true \
    --batch-mode --no-transfer-progress && \
    ls -la target/ && \
    rm -rf /tmp/.m2 && \
    rm -rf ~/.m2 && \
    rm -rf /opt/maven && \
    rm -rf src && \
    rm -rf /tmp/* /var/tmp/* && \
    find /app -name "*.jar" -not -path "*/target/*" -delete

# Etapa intermedia: Solo para instalar FFmpeg
FROM openjdk:21-slim AS ffmpeg-stage
WORKDIR /tmp

# Limpiar completamente
RUN apt-get clean && \
    rm -rf /var/lib/apt/lists/* /var/cache/apt/* /tmp/* /var/tmp/*

# Instalar FFmpeg con técnica de cache temporal
RUN apt-get update && \
    mkdir -p /tmp/apt-cache && \
    apt-get -o Dir::Cache="/tmp/apt-cache" install -y --no-install-recommends ffmpeg && \
    rm -rf /tmp/apt-cache && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /var/cache/apt/* /tmp/* /var/tmp/*

# Etapa final: imagen ligera para ejecución
FROM openjdk:21-slim
WORKDIR /app

# Copiar FFmpeg desde la etapa intermedia
COPY --from=ffmpeg-stage /usr/bin/ffmpeg /usr/bin/ffmpeg
COPY --from=ffmpeg-stage /usr/bin/ffprobe /usr/bin/ffprobe

# Copiar librerías esenciales de FFmpeg (ajustar según arquitectura)
COPY --from=ffmpeg-stage /usr/lib/*/libav*.so* /usr/lib/
COPY --from=ffmpeg-stage /usr/lib/*/libsw*.so* /usr/lib/
COPY --from=ffmpeg-stage /usr/lib/*/libpostproc*.so* /usr/lib/

# Copiar el JAR compilado
COPY --from=build /app/target/*.jar app.jar

# Verificar que el archivo existe
RUN ls -la app.jar

EXPOSE 8080

# JVM optimizada para ARM y 8GB de RAM
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseG1GC", \
    "-XX:+UseStringDeduplication", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]