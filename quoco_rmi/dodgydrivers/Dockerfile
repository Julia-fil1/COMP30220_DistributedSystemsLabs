FROM openjdk:8-jre-alpine
ENV SERVER_HOST=broker
COPY target/dodgydrivers-*-jar-with-dependencies.jar /dodgydrivers.jar
CMD /usr/bin/java -jar /dodgydrivers.jar $SERVER_HOST
