# Use official MySQL image
FROM mysql:8.0

# Set environment variables for database initialization
ENV MYSQL_DATABASE=locon
ENV MYSQL_USER=locon_user
ENV MYSQL_PASSWORD=locon_pass
ENV MYSQL_ROOT_PASSWORD=rootpass

# Copy initialization scripts (optional)
# If you have .sql files in docker-entrypoint-initdb.d they will run on container start
# COPY ./init.sql /docker-entrypoint-initdb.d/

# Expose default MySQL port
EXPOSE 3306