FROM openjdk:8u212-jdk-alpine3.9
MAINTAINER Walid Moustafa
WORKDIR /app
COPY ${project.basedir}/target/${project.name}-${project.version}.jar/ .
EXPOSE 80
ENTRYPOINT ["java"]
CMD ["-jar", "/app/${project.name}-${project.version}.jar"]
RUN apk --update --no-cache add curl
HEALTHCHECK CMD curl -f http://localhost:80/health || exit 1