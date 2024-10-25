# Jeg bruger et færdigt image, som Java allerede har installeret, som (OpenJDK 17).
FROM openjdk:17-jdk-slim

# Derudover lavers der en mappe /app, inde i containeren, hvor min muzzplayer vil kører fra.
WORKDIR /app

# Så tages min JAR-fil, som jeg har bygget med Maven, og kopieres ind i /app mappen i containeren.
COPY target/SP2TheAPIHub-1.0-SNAPSHOT.jar /app/app.jar

# Jeg eksponerer port 7777, som er den port min muzzplayer applikation kører på.
EXPOSE 7777

# Til sidst kører jeg min muzzplayer applikation, ved at køre kommandoen "java -jar app.jar".
CMD ["java", "-jar", "app.jar"]
