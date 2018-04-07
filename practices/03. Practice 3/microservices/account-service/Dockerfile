FROM openjdk
ADD target/account-service.jar account-service.jar
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "/account-service.jar"]
EXPOSE 2222
