FROM openjdk:17-jdk-alpine3.14
MAINTAINER isosalud.cl
COPY target/service-0.0.1-SNAPSHOT.jar be-isosalud-prod.jar
RUN apk update
RUN apk add tzdata
ENV LANG es_ES.UTF-8
ENV LANGUAGE es_ES.UTF-8
ENV LC_ALL es_ES.UTF-8
ENTRYPOINT ["java","-jar","/be-isosalud-prod.jar"]