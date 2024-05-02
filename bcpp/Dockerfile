FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
COPY target/bcpp-0.0.1-SNAPSHOT.jar bcpp.jar
ENTRYPOINT ["java","-jar","/bcpp.jar"]