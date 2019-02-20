FROM openjdk:11-jre-slim

EXPOSE 8080
WORKDIR /opwatch-server
COPY /build/libs/opwatch-server-*.jar app.jar
COPY /build/resources/main/* resources/*

ENTRYPOINT ["exec", "java", "$JAVA_OPTS", "-jar", "/opwatch-server/app.jar"]
