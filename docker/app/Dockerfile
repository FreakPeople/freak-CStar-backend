FROM eclipse-temurin:21
COPY ./build/libs/*SNAPSHOT.jar cstar.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "cstar.jar"]