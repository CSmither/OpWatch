FROM openjdk:11-jre-slim

EXPOSE 25565
WORKDIR /spigot
RUN echo "eula=false" > /spigot/eula.txt
RUN mkdir /spigot/plugins
RUN mkdir /spigot/plugins/opwatch
ADD https://cdn.getbukkit.org/spigot/spigot-1.13.2.jar /spigot/spigot.jar
COPY ./config/server.properties /spigot/server.properties
COPY ../build/libs/opwatch-spigot-*.jar /spigot/plugins/opwatch-spigot.jar

ENTRYPOINT ["java", "-jar", "spigot.jar"]