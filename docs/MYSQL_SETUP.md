# MySQL Setup Guide

This guide provides detailed instructions for setting up MySQL database for the Safe Zones Module application.

## 📋 Prerequisites

- MySQL 8.0 or higher
- MySQL Workbench (optional, for GUI management)
- Command line access to MySQL

## 🗄️ Database Setup

### 1. Install MySQL

#### macOS (using Homebrew)
```bash
brew install mysql
brew services start mysql
```

#### Ubuntu/Debian
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql
```

#### Windows
Download and install from [MySQL Official Website](https://dev.mysql.com/downloads/mysql/)

### 2. Secure MySQL Installation

```bash
# Run the security script
sudo mysql_secure_installation

# Follow the prompts to:
# - Set root password
# - Remove anonymous users
# - Disable remote root login
# - Remove test database
# - Reload privilege tables
```

### 3. Create Database and User

Connect to MySQL as root:
```bash
mysql -u root -p
```

Execute the following SQL commands:

```sql
-- Create the database
CREATE DATABASE safe_zones CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create a dedicated user for the application
CREATE USER 'safe_zones_user'@'localhost' IDENTIFIED BY 'SecurePassword123!';

-- Grant all privileges on the safe_zones database
GRANT ALL PRIVILEGES ON safe_zones.* TO 'safe_zones_user'@'localhost';

-- Grant privileges for creating tables and indexes
GRANT CREATE, DROP, ALTER, INDEX ON safe_zones.* TO 'safe_zones_user'@'localhost';

-- Flush privileges to ensure changes take effect
FLUSH PRIVILEGES;

-- Verify the user was created
SELECT User, Host FROM mysql.user WHERE User = 'safe_zones_user';

-- Exit MySQL
EXIT;
```

### 4. Test Database Connection

Test the connection with the new user:
```bash
mysql -u safe_zones_user -p safe_zones
```

You should be able to connect successfully. Type `EXIT;` to quit.

## ⚙️ Application Configuration

### 1. Update Application Properties

Create or update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/safe_zones?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=safe_zones_user
spring.datasource.password=SecurePassword123!
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true

# Connection Pool Configuration
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1200000
spring.datasource.hikari.connection-timeout=20000

# Server Configuration
server.port=8080
server.servlet.context-path=/

# Logging Configuration
logging.level.com.maciejwasiak.locon=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### 2. Environment-Specific Configuration

#### Development Profile (`application-dev.properties`)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/safe_zones_dev
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
logging.level.com.maciejwasiak.locon=DEBUG
```

#### Production Profile (`application-prod.properties`)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/safe_zones
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.com.maciejwasiak.locon=INFO
```

## 🗃️ Database Schema

The application will automatically create the following tables:

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    otp_code VARCHAR(6),
    otp_expires_at TIMESTAMP
);
```

### Devices Table
```sql
CREATE TABLE devices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    model VARCHAR(100) NOT NULL,
    owner_name VARCHAR(100) NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Zones Table
```sql
CREATE TABLE zones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    icon VARCHAR(50) NOT NULL,
    address VARCHAR(255) NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    radius INTEGER NOT NULL,
    notifications_enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### ZoneDevices Table
```sql
CREATE TABLE zone_devices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    zone_id BIGINT NOT NULL,
    device_id BIGINT NOT NULL,
    notifications_enabled BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (zone_id) REFERENCES zones(id) ON DELETE CASCADE,
    FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE,
    UNIQUE KEY unique_zone_device (zone_id, device_id)
);
```

## 🔧 Troubleshooting

### Common Issues

#### 1. Connection Refused
```bash
# Check if MySQL is running
sudo systemctl status mysql

# Start MySQL if not running
sudo systemctl start mysql
```

#### 2. Access Denied
```bash
# Reset user password
mysql -u root -p
ALTER USER 'safe_zones_user'@'localhost' IDENTIFIED BY 'NewPassword123!';
FLUSH PRIVILEGES;
```

#### 3. Character Set Issues
```sql
-- Check database character set
SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME 
FROM information_schema.SCHEMATA 
WHERE SCHEMA_NAME = 'safe_zones';

-- Fix character set if needed
ALTER DATABASE safe_zones CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 4. Time Zone Issues
```sql
-- Set timezone
SET time_zone = '+00:00';

-- Check timezone
SELECT @@global.time_zone, @@session.time_zone;
```

### Performance Optimization

#### 1. Create Indexes
```sql
-- Indexes for better performance
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_devices_user_id ON devices(user_id);
CREATE INDEX idx_zones_user_id ON zones(user_id);
CREATE INDEX idx_zones_location ON zones(latitude, longitude);
CREATE INDEX idx_zone_devices_zone_id ON zone_devices(zone_id);
CREATE INDEX idx_zone_devices_device_id ON zone_devices(device_id);
```

#### 2. MySQL Configuration
Add to `/etc/mysql/mysql.conf.d/mysqld.cnf`:
```ini
[mysqld]
innodb_buffer_pool_size = 1G
innodb_log_file_size = 256M
innodb_flush_log_at_trx_commit = 2
innodb_flush_method = O_DIRECT
max_connections = 200
query_cache_size = 64M
query_cache_type = 1
```

## 🧪 Testing Database Setup

### 1. Run Application Tests
```bash
# Test with MySQL
mvn test -Dspring.profiles.active=test

# Test database connection
mvn spring-boot:run -Dspring.profiles.active=dev
```

### 2. Verify Data
```sql
-- Check if tables were created
SHOW TABLES;

-- Check table structures
DESCRIBE users;
DESCRIBE devices;
DESCRIBE zones;
DESCRIBE zone_devices;

-- Check sample data (after running the app)
SELECT * FROM users;
SELECT * FROM devices;
SELECT * FROM zones;
```

## 🔒 Security Best Practices

1. **Use Strong Passwords**: Generate secure passwords for database users
2. **Limit Privileges**: Only grant necessary permissions
3. **Network Security**: Use SSL connections in production
4. **Regular Backups**: Set up automated database backups
5. **Monitor Access**: Enable MySQL audit logging
6. **Update Regularly**: Keep MySQL updated to latest version

## 📊 Monitoring

### 1. Check Database Status
```sql
-- Check database size
SELECT 
    table_schema AS 'Database',
    ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)'
FROM information_schema.tables 
WHERE table_schema = 'safe_zones'
GROUP BY table_schema;

-- Check table sizes
SELECT 
    table_name AS 'Table',
    ROUND(((data_length + index_length) / 1024 / 1024), 2) AS 'Size (MB)'
FROM information_schema.tables 
WHERE table_schema = 'safe_zones'
ORDER BY (data_length + index_length) DESC;
```

### 2. Performance Monitoring
```sql
-- Check slow queries
SHOW VARIABLES LIKE 'slow_query_log';
SHOW VARIABLES LIKE 'long_query_time';

-- Check connection status
SHOW STATUS LIKE 'Connections';
SHOW STATUS LIKE 'Threads_connected';
```

---

This setup guide ensures a robust and secure MySQL database configuration for the Safe Zones Module application. 🗄️✨
