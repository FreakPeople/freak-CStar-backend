FROM eclipse-temurin:21
COPY ./build/libs/cstar-api.jar cstar-api.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "cstar-api.jar"]
