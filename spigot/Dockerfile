FROM openjdk:11-jre-slim

EXPOSE 25565
WORKDIR /spigot
RUN mkdir /spigot/plugins
RUN mkdir /spigot/plugins/opwatch
RUN curl https://cdn.getbukkit.org/spigot/spigot-1.13.2.jar -o /spigot/spigot.jar
COPY ./build/libs/opwatch-spigot-*.jar /spigot/plugins/opwatch-spigot.jar


ENTRYPOINT ["java","-jar","$JAVA_OPTS","spigot.jar"]