FROM openjdk:11-jre-slim

WORKDIR /spigot
VOLUME /spigot/plugins/
ADD https://cdn.getbukkit.org/spigot/spigot-1.13.2.jar /spigot/spigot.jar
RUN mkdir /spigot/plugins
COPY build/libs/opwatch-spigot-*/.jar

ENTRYPOINT ["java","-jar","$JAVA_OPTS","spigot.jar"]