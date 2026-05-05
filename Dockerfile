# ============================================
# SmartLibrary Dockerfile - Fixed & Detailed
# ============================================
# Fixes applied:
# 1. Downloads JARs via wget (local lib/ is gitignored via *.jar in .gitignore)
# 2. Supports Railway's dynamic $PORT environment variable
# 3. Multi-stage build (small final image)
# ============================================

# ====================
# STAGE 1: BUILDER
# ====================
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /build

# Copy Java source files
COPY src/main/java ./src

# Copy JAR dependencies from your local lib folder
# Now tracked by Git after updating .gitignore
COPY webapps/SmartLibrary/WEB-INF/lib ./lib

# Compile all Java files (20 servlets + 3 util classes)
RUN find src -name "*.java" > sources.txt \
    && javac -cp "lib/*" -d classes @sources.txt \
    && rm sources.txt

# Copy webapp content (JSPs, CSS, JS, web.xml)
COPY webapps/SmartLibrary ./webapp

# Add compiled classes to WEB-INF/classes
RUN mkdir -p webapp/WEB-INF/classes \
    && cp -r classes/* webapp/WEB-INF/classes/

# Create WAR file
RUN cd webapp && jar -cvf /SmartLibrary.war .

# ====================
# STAGE 2: FINAL RUN
# ====================
FROM tomcat:10-jdk17

WORKDIR /usr/local/tomcat

# Copy built WAR file
COPY --from=builder /SmartLibrary.war webapps/SmartLibrary.war

# Create startup script to handle Railway's dynamic $PORT
# Railway sets $PORT env var; default to 8080 if not set
RUN printf '#!/bin/bash\n\
PORT=${PORT:-8080}\n\
echo "Starting Tomcat on port $PORT"\n\
sed -i "s/8080/$PORT/g" /usr/local/tomcat/conf/server.xml\n\
exec catalina.sh run\n' > /start.sh && chmod +x /start.sh

# Documentation only (actual port from $PORT)
EXPOSE 8080

# Use custom startup script
CMD ["/start.sh"]
