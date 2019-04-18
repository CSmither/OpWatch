FROM openjdk:8-jre-slim

EXPOSE 25565
WORKDIR /spigot
RUN echo "eula=true" > /spigot/eula.txt
RUN mkdir /spigot/plugins
RUN mkdir /spigot/plugins/opwatch
ADD https://cdn.getbukkit.org/spigot/spigot-1.13.1.jar /spigot/spigot.jar
COPY ./docker/config/server.properties /spigot/server.properties
COPY ./build/libs/opwatch-spigot-*.jar /spigot/plugins/

ENTRYPOINT ["java", "-jar", "spigot.jar"]