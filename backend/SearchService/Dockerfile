# Use Debian-based OpenJDK 22 as the base image
FROM openjdk:22-jdk

# Set the working directory inside the container
WORKDIR /app

# Argument for the JAR file location
ARG JAR_FILE=target/*.jar

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Modify /etc/hosts to add the new entry
RUN echo "141.145.219.87 3fd97bdf9fa8" >> /etc/hosts

# Expose the port the app runs on
EXPOSE 8080

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
