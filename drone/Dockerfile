FROM openjdk:19-jdk

COPY build/libs/drone.jar app.jar

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar

