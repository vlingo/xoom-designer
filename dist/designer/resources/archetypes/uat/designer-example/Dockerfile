FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY target/designer-example-*.jar designer-example.jar
EXPOSE 18080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar designer-example.jar