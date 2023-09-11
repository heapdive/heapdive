FROM eclipse-temurin:17

RUN --mount=type=cache,target=/var/cache/apt \
    apt-get update && \
        # Install dependencies to add additional repos
        apt-get install -y --no-install-recommends \
            gnupg curl python3-requests wget

# Install nodejs
RUN --mount=type=cache,target=/var/cache/apt \
    mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg && \
    echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_18.x nodistro main" > /etc/apt/sources.list.d/nodesource.list && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
        nodejs && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

COPY . /app
WORKDIR /app

RUN ./gradlew build --info

CMD ["java", "-jar", "heapdive-server/build/libs/heapdive-server-0.0.1-SNAPSHOT.jar"]
