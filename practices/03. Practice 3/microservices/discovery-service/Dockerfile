FROM openjdk
ADD target/discovery-service.jar discovery-service.jar
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "/discovery-service.jar"]
EXPOSE 8761
