FROM openjdk:17
ADD target/SMS4-0.0.1-SNAPSHOT.jar SMS4-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/SMS4-0.0.1-SNAPSHOT.jar"]