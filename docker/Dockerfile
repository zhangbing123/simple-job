#FROM jdk8
FROM java
VOLUME /tmp
ADD simple-job-1.0-SNAPSHOT.jar simple-job.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/simple-job.jar"]