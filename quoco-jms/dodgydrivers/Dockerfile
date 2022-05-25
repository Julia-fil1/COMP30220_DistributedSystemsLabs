FROM openjdk:8-jre-alpine
COPY target/*-jar-with-dependencies.jar /dodgydrivers.jar
CMD sleep 5; /usr/bin/java -jar /dodgydrivers.jar ${CONTAINER_NAME}
