FROM gradle:jdk11 as builder

COPY --chown=gradle:gradle . /dev
WORKDIR /dev
RUN gradle build bootJar


FROM openjdk:11-jre-slim

EXPOSE 8080
WORKDIR /opwatch-server
COPY --from=builder /dev/build/libs/opwatch-server-*/.jar app.jar
COPY --from=builder /dev/build/resources/main/* resources/*

ENTRYPOINT ["java", "-jar", "/opwatch-server/app.jar"]
