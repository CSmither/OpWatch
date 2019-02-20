FROM openjdk:11-jre-slim

EXPOSE 25565
WORKDIR /spigot
RUN mkdir /spigot/plugins
RUN mkdir /spigot/plugins/opwatch
ADD https://cdn.getbukkit.org/spigot/spigot-1.13.2.jar /spigot/spigot.jar
COPY ./build/libs/opwatch-spigot-*.jar /spigot/plugins/opwatch-spigot.jar


ENTRYPOINT ["exec", "java","$JAVA_OPTS","-jar","spigot.jar"]