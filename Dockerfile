# Etapa 1: Construcción con Maven y JDK 21
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copiamos los archivos de Maven primero (mejor cache de Docker layers)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Hacemos ejecutable el wrapper y descargamos dependencias
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copiamos el código fuente y compilamos
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# Etapa 2: Imagen ligera con JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app

# Creamos usuario no-root para seguridad
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Copiamos el jar construido
COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

# Configuramos la JVM para contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Exponemos el puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando de inicio con configuraciones optimizadas
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
