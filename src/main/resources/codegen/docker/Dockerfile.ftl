FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY target/${artifactId}-*.jar ${artifactId}.jar
EXPOSE 18080
CMD java -Dcom.sun.management.jmxremote -noverify ${r"${JAVA_OPTS}"} -jar ${artifactId}.jar