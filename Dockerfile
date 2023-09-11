FROM eclipse-temurin:17

RUN apt-get update && \
    apt-get install -y curl && \
    curl -sL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

COPY . /app
WORKDIR /app

RUN ./gradlew npmInstall npm_run_build build

CMD ["java", "-jar", "heapdive-server/build/libs/heapdive-server-0.0.1-SNAPSHOT.jar"]
