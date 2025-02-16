FROM eclipse-temurin:21
COPY ./build/libs/*SNAPSHOT.jar cstar-play.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=local", "cstar-play.jar"]
