FROM openjdk:17-jdk-alpine3.14
MAINTAINER isosalud.cl
COPY target/servservice-0.0.1-SNAPSHOT.jar be-isosalud-prod.jar
ENTRYPOINT ["java","-jar","/be-isosalud-prod.jar"]