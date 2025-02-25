FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY src/main/resources /app/resources
COPY target/sct-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]