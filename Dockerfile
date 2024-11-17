FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/MyProducts-0.0.3-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java","-jar","/application.jar"]

