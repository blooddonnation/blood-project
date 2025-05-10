# Use official Maven image as the base image
FROM maven:3.9.6-eclipse-temurin-17

# Set working directory
WORKDIR /build

# Copy all project directories
COPY . .

# Build all Maven projects
RUN echo '#!/bin/bash\n\
echo "Building all Maven projects..."\n\
\n\
# Build eureka server\n\
if [ -d "euroka/eureka-server" ]; then\n\
    echo "Building eureka-server..."\n\
    cd euroka/eureka-server\n\
    mvn clean package -DskipTests\n\
    cd ../..\n\
fi\n\
\n\
# Build auth service\n\
if [ -d "blod/auth-service" ]; then\n\
    echo "Building auth-service..."\n\
    cd blod/auth-service\n\
    mvn clean package -DskipTests\n\
    cd ../..\n\
fi\n\
\n\
# Build donation history service\n\
if [ -d "blood1/donation-history-service" ]; then\n\
    echo "Building donation-history-service..."\n\
    cd blood1/donation-history-service\n\
    mvn clean package -DskipTests\n\
    cd ../..\n\
fi\n\
\n\
# Build request post service\n\
if [ -d "majda/Request_post" ]; then\n\
    echo "Building request-post service..."\n\
    cd majda/Request_post\n\
    mvn clean package -DskipTests\n\
    cd ../..\n\
fi\n\
\n\
# Build microservice banque\n\
if [ -d "raja/Microservice_Banque" ]; then\n\
    echo "Building microservice-banque..."\n\
    cd raja/Microservice_Banque\n\
    mvn clean package -DskipTests\n\
    cd ../..\n\
fi\n\
\n\
# Build position tracking service\n\
if [ -d "l9ssir/positionTracking" ]; then\n\
    echo "Building position-tracking service..."\n\
    cd l9ssir/positionTracking\n\
    mvn clean package -DskipTests\n\
    cd ../..\n\
fi\n\
\n\
echo "All Maven projects have been built."' > /build/build-all.sh

# Make the script executable
RUN chmod +x /build/build-all.sh

# Run the build script
CMD ["/build/build-all.sh"] 