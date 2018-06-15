FROM openjdk:8-jdk
COPY ./target/spring-petclinic-2.0.0.BUILD-SNAPSHOT.jar /tmp/spring-petclinic-2.0.0.BUILD-SNAPSHOT.jar
WORKDIR /tmp
EXPOSE 7550
ENTRYPOINT ["java","-jar","spring-petclinic-2.0.0.BUILD-SNAPSHOT.jar"]