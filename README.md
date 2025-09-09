# Safe Zones Module - Bezpieczna Rodzina

A comprehensive Spring Boot application for managing safety zones with real-time geofencing capabilities, designed to help families track their loved ones' locations and receive notifications when they enter or leave designated safe areas.

## 🚀 Features

- **Zone Management**: Create, edit, and delete safety zones with custom names, icons, and radius settings
- **Device Tracking**: Support for multiple device types (phones, smartwatches, fitness bands)
- **Real-time Notifications**: Geofencing alerts when devices enter or exit zones
- **Multi-language Support**: Internationalization (i18n) for English, Polish, and German
- **Dynamic Theming**: Operator-specific color schemes and UI customization
- **Role-based Access**: Admin, User, and Viewer permission levels
- **Interactive Maps**: Maps integration for zone creation and management
- **Responsive Design**: Modern UI built with Tailwind CSS and Thymeleaf

## 🏗️ Architecture

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.5.5 with Java 17
- **Database**: MySQL with H2 for testing
- **Security**: Spring Security with session-based authentication
- **API**: RESTful endpoints with comprehensive error handling
- **Testing**: 81 tests with 100% passing rate

### Frontend
- **Templates**: Thymeleaf with responsive design
- **Styling**: Tailwind CSS with daisyUI components
- **Maps**: leafletjs + OSM Maps JavaScript API
- **Internationalization**: Multi-language support

### Database Schema
- **Users**: Authentication and role management
- **Devices**: Device tracking and ownership
- **Zones**: Safety zone definitions with geolocation
- **ZoneDevices**: Many-to-many relationship for device-zone assignments

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+** (for production)

## 🛠️ Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd modul-stref-bezpieczenstwa
```

### 2. Database Setup

#### MySQL (Production)
```sql
CREATE DATABASE safe_zones;
CREATE USER 'safe_zones_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON safe_zones.* TO 'safe_zones_user'@'localhost';
FLUSH PRIVILEGES;
```

#### H2 (Development/Testing)
The application automatically uses H2 in-memory database for development and testing.

### 3. Configuration

Create `src/main/resources/application.properties`:
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/safe_zones
spring.datasource.username=safe_zones_user
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Server Configuration
server.port=8080
```

### 4. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## 🧪 Testing

The project includes comprehensive test coverage with 81 tests:

```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Test Categories
- **Unit Tests**: Service and repository layer testing
- **Integration Tests**: REST API endpoint testing with MockMvc
- **Repository Tests**: Database interaction testing
- **Controller Tests**: Web layer testing

## 📚 API Documentation

### Authentication Endpoints
- `POST /api/auth/send-otp` - Send OTP code to phone number
- `POST /api/auth/verify-otp` - Verify OTP code and authenticate user
- `GET /api/user/permissions` - Get current user permissions

### Zone Management
- `GET /api/zones` - Get all zones for current user
- `POST /api/zones` - Create new zone
- `PUT /api/zones/{id}` - Update existing zone
- `DELETE /api/zones/{id}` - Delete zone
- `POST /api/zones/{id}/toggle-notifications` - Toggle notifications for zone

### Device Management
- `GET /api/devices` - Get all devices for current user

### Utility Endpoints
- `GET /api/themes/{operator}` - Get theme configuration for operator
- `GET /api/i18n/{language}` - Get translations for language
- `GET /api/geofencing/events/{zoneId}` - Get mock geofencing events

## 🌐 Internationalization

The application supports multiple languages:
- **English** (en) - Default
- **Polish** (pl)
- **German** (de)

Language switching is available in the UI, and all text content is dynamically loaded based on the selected language.

## 🎨 Theming

Dynamic theming is supported for different operators:
- **Default Theme**: Blue color scheme
- **Dark Theme**: Dark mode variant
- **Custom Themes**: Operator-specific color configurations

## 🔐 Security

- **Authentication**: OTP-based phone number verification
- **Authorization**: Role-based access control (Admin, User, Viewer)
- **Session Management**: Secure session handling
- **CSRF Protection**: Enabled by default
- **Input Validation**: Comprehensive validation using Bean Validation

## 📱 Device Support

The application supports various device types:
- **Phone**: Smartphone tracking
- **Watch**: Smartwatch devices
- **Band**: Fitness tracking bands

## 🗺️ Maps Integration

- **Zone Creation**: Interactive map for setting zone locations
- **Radius Visualization**: Visual representation of zone boundaries
- **Drag & Drop**: Easy marker placement for zone positioning

## 🧪 Geofencing Simulation

### QA Testing Endpoint
- **Endpoint**: `GET /api/geofence/events/{zoneId}?count=N`
- **What it does**: Returns N random ENTER/EXIT mock events around the zone center for its devices (or a virtual one if none assigned)
- **How to run**:
  - Open browser: `http://localhost:8080/api/geofence/events/1?count=5`
  - Or Chrome DevTools (Console):
    ```javascript
    fetch('/api/geofence/events/1?count=3')
      .then(r => r.json())
      .then(d => { console.log('Mock events', d); alert('Generated ' + d.length + ' geofence events'); });
    ```
- **Logging**: Backend logs each event at INFO with event type, zone, device, lat/lng, radius, timestamp

## 🚀 Deployment

### Production Deployment
1. Configure production database settings
2. Configure security settings
3. Deploy to your preferred platform (Docker, cloud services, etc.)

### Docker Support
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/locon-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 📊 Monitoring

The application includes Spring Boot Actuator for monitoring:
- Health checks: `/actuator/health`
- Application metrics: `/actuator/metrics`
- Application info: `/actuator/info`

## 📖 Documentation

- **[Architecture Overview](docs/ARCHITECTURE.md)** - System architecture and design patterns
- **[MySQL Setup Guide](docs/MYSQL_SETUP.md)** - Detailed database configuration
- **[AI Usage Report](docs/AI_USAGE_REPORT.md)** - AI development documentation
- **[Project Summary](PROJECT_SUMMARY.md)** - Complete project overview

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Review the test cases for usage examples

## 🏆 Project Status

- **Phase 1**: ✅ Project Setup & Configuration
- **Phase 2**: ✅ Authentication & Permissions
- **Phase 3**: ✅ Database Schema (MySQL)
- **Phase 4**: ✅ Backend APIs (with Mocks)
- **Phase 5**: ✅ Frontend Views (Thymeleaf + Tailwind)
- **Phase 6**: ✅ Zone Management and i18n
- **Phase 7**: ✅ Maps & Geofencing
- **Phase 8**: ✅ UI & Theming
- **Phase 9**: ✅ Testing (81 tests, 100% passing)
- **Phase 10**: ✅ Documentation & Delivery

## 📈 Test Coverage

- **Total Tests**: 81 tests
- **Success Rate**: 100%
- **Coverage Areas**: Services, Repositories, Controllers, Security, Integration
- **Test Types**: Unit, Integration, Repository, MockMvc

---

**Safe Zones Module** - Keeping your loved ones safe, one zone at a time. 🏠❤️
