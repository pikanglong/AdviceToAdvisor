FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/advicetoadvisor.jar advicetoadvisor.jar
ENTRYPOINT ["java","-jar","/advicetoadvisor.jar","&"]