FROM openjdk:19

COPY target/kursach6sem-0.0.1-SNAPSHOT.jar kursach6sem.jar

ENTRYPOINT ["java", "-jar", "/kursach6sem.jar"]