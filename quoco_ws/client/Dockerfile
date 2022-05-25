#FROM openjdk:8-jre-alpine
##ENV CONTAINER_NAME localhost
#COPY target/client-*-jar-with-dependencies.jar /client.jar
##CMD /usr/bin/java -cp /service-1.0.jar service.core.Quoter ${CONTAINER_NAME}
#CMD /usr/bin/java -jar /client.jar Client

#FROM openjdk:8-jre-alpine
#ENV SERVICE_HOST localhost
#ENV SERVICE_PORT 9000
#COPY target/client-*-jar-with-dependencies.jar /client.jar
#CMD /usr/bin/java -cp /client.jar Client -h ${SERVICE_HOST} -p ${SERVICE_PORT}

#FROM openjdk:8-jre-alpine
##ENV HOSTNAME=localhost
##ENV SERVICE_PORT 9000
#COPY target/*-jar-with-dependencies.jar /client.jar
##CMD java -jar /client.jar ${container_name}
##CMD /usr/bin/java -cp /client.jar Client -h ${SERVICE_HOST} -p ${SERVICE_PORT}
#CMD /usr/bin/java -jar /client.jar

FROM openjdk:8-jre-alpine
COPY target/client-*-jar-with-dependencies.jar /client.jar
CMD /usr/bin/java -jar /client.jar -h ${HOSTNAME}