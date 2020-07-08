FROM gradle:6.4.1-jdk8 AS build
COPY --chown=gradle:gradle . /home/gradle/src
VOLUME /tmp
WORKDIR /home/gradle/src/
RUN gradle build --no-daemon

FROM openjdk:8-jre-slim

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/covid-app.jar

ENTRYPOINT ["java","-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","/app/covid-app.jar"]
