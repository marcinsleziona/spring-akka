FROM openjdk:8-jdk-alpine
ADD target/akka-test.jar /usr/local/application/akka-test.jar
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /usr/local/application/akka-test.jar
