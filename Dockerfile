# Используем официальный образ Java 21 в качестве базового образа
FROM maven:3.9.6-amazoncorretto-21 AS build

# Устанавливаем рабочую директорию внутри контейнера
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app

# Собираем проект и создаем JAR-файл
RUN mvn -f /usr/src/app/pom.xml clean package

# Используем базовый образ с Java для запуска приложения
FROM amazoncorretto:21

# Копируем JAR-файл из контейнера сборки в текущий контейнер
COPY --from=build /usr/src/app/target/recommendation-signature-server-0.0.1-SNAPSHOT.jar app.jar

# Указываем порт, который будет использоваться приложением
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]