# ============================================
# SmartLibrary Dockerfile - Detailed Explanation
# ============================================
# This uses a multi-stage build:
# 1. Builder stage: Compiles Java code and builds WAR
# 2. Final stage: Runs the WAR on Tomcat 10
# Multi-stage keeps the final image small (no build tools included)
# ============================================

# ====================
# STAGE 1: BUILDER
# ====================
# Use Eclipse Temurin JDK 17 (matches your project's Java version)
FROM eclipse-temurin:17-jdk AS builder

# Set working directory inside the builder container
WORKDIR /build

# Copy your Java source files (package com.smartlibrary.*)
# Your source is at: src/main/java/com/smartlibrary/
COPY src/main/java ./src

# Copy JAR dependencies from your project's WEB-INF/lib
# This includes postgresql-42.7.1.jar and checker-qual-3.41.0.jar
COPY webapps/SmartLibrary/WEB-INF/lib ./lib

# Download Jakarta Servlet API 6.0.0 JAR for compilation
# Your project uses Jakarta Servlet 6.0 (requires Tomcat 10+)
# This JAR is not in your local lib folder because it's "provided" by Tomcat
RUN wget -q https://repo1.maven.org/maven2/jakarta/servlet/jakarta.servlet-api/6.0.0/jakarta.servlet-api-6.0.0.jar -P lib/

# Compile all Java source files
# -cp "lib/*" : Include all JARs in lib as classpath (Servlet API + PostgreSQL driver)
# -d classes : Output compiled .class files to classes/ directory
# @sources.txt : Read list of Java files to compile (handles many files)
RUN find src -name "*.java" > sources.txt \
    && javac -cp "lib/*" -d classes @sources.txt \
    && rm sources.txt

# Copy your entire webapp content (JSPs, CSS, JS, web.xml, etc.)
# Your webapp is at: webapps/SmartLibrary/
COPY webapps/SmartLibrary ./webapp

# Copy compiled Java classes to WEB-INF/classes in the webapp
# Tomcat expects classes here for servlets/util classes
RUN mkdir -p webapp/WEB-INF/classes \
    && cp -r classes/* webapp/WEB-INF/classes/

# Package everything into a WAR file (Web Application Archive)
# WAR is the standard format for Java web apps
RUN cd webapp && jar -cvf /SmartLibrary.war .

# ====================
# STAGE 2: FINAL RUN
# ====================
# Use Tomcat 10 with JDK 17
# Tomcat 10 is required because your project uses Jakarta Servlet 6.0
# (Tomcat 9 and below use javax.servlet, not jakarta.servlet)
FROM tomcat:10-jdk17

# Set working directory to Tomcat installation folder
WORKDIR /usr/local/tomcat

# Copy the built WAR file from the builder stage
# Tomcat will automatically deploy the WAR on startup
COPY --from=builder /SmartLibrary.war webapps/SmartLibrary.war

# Expose Tomcat's default port 8080
# This makes the app accessible from outside the container
EXPOSE 8080

# Start Tomcat in foreground mode
# "catalina.sh run" keeps the container running (default is background)
CMD ["catalina.sh", "run"]
