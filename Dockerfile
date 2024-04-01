FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/hororok-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} hororok.jar
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "-Duser.timezone=Asia/Seoul", "hororok.jar"]
