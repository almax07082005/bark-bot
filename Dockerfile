FROM eclipse-temurin:25-jdk-jammy

WORKDIR /app

ARG JAR_FILE=target/bark-bot-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
