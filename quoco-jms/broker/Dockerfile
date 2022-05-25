FROM openjdk:8-jre-alpine
COPY target/*-jar-with-dependencies.jar /broker.jar
CMD sleep 6; /usr/bin/java -jar /broker.jar ${CONTAINER_NAME}
