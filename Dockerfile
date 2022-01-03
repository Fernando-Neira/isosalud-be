FROM openjdk:17-jdk-alpine3.14
MAINTAINER isosalud.cl
COPY target/service-0.0.1.jar service-0.0.1.jar
ENTRYPOINT ["java","-jar","/service-0.0.1.jar"]