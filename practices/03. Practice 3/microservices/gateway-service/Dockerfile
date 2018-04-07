FROM openjdk
ADD target/gateway-service.jar gateway-service.jar
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "/gateway-service.jar"]
EXPOSE 8765
