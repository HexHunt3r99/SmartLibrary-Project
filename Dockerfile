# ============================================
# SmartLibrary Dockerfile - Fixed & Detailed
# ============================================
# Setup:
# 1. All JARs in lib/ folder (included in Git via .gitignore exception)
# 2. Supports Railway's dynamic $PORT environment variable
# 3. Multi-stage build (small final image)
# 4. Works offline (no wget needed)
# ============================================

# ====================
# STAGE 1: BUILDER
# ====================
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /build

# Copy Java source files
COPY src/main/java ./src

# Copy local JAR dependencies (PostgreSQL + checker-qual + Jakarta Servlet API)
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
