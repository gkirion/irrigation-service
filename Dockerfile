FROM openjdk:11-jdk-slim-buster
MAINTAINER george kyritsas
COPY target/irrigation-service-2.0-SNAPSHOT.jar irrigation-service.jar
ENTRYPOINT ["java", "-jar", "irrigation-service.jar"]