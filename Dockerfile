# Используем официальный образ Java 21 в качестве базового образа
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файл сборки (например, pom.xml) и исходный код в контейнер
COPY pom.xml .
COPY src ./src

# Устанавливаем Maven и собираем проект
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Копируем собранный JAR-файл в контейнер
COPY target/recommendation-signature-server-0.0.1-SNAPSHOT.jar app.jar

# Указываем порт, который будет использоваться приложением
EXPOSE 8080

# Запускаем приложение при старте контейнера
ENTRYPOINT ["java", "-jar", "app.jar"]