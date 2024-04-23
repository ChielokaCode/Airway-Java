FROM maven:3.8.4-openjdk-17 AS maven_dependencies
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

FROM maven_dependencies AS maven_build
COPY . .
RUN mvn package -DskipTests -B

FROM amazoncorretto:17-alpine
ARG JAR_FILE=target/Airway-Backend-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY --from=maven_build /app/${JAR_FILE} .

ENV PORT=8080 \
    JAVA_OPTS="-Xmx512m" \
    DB_PASSWORD="" \
    DB_HOST="" \
    DB_USERNAME=""

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "Airway-Backend-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]