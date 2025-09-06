# Development Tasks – Safe Zones Module (*Bezpieczna Rodzina*)

## 📊 Project Progress Summary
- **Phase 1**: ✅ COMPLETED - Project Setup & Configuration
- **Phase 2**: ✅ COMPLETED - Authentication & Permissions with comprehensive testing
- **Phase 3**: 🔄 NEXT - Database Schema (MySQL)
- **Total Tests**: 73 tests (100% passing)
- **Test Coverage**: Unit tests, Integration tests, Repository tests, Controller tests

---

## Phase 1 – Project Setup ✅ COMPLETED
- [x] **Backend Setup (Spring Boot + MySQL)**
  - [x] Initialize Spring Boot project (Maven/Gradle)
  - [x] Configure MySQL connection (`application.properties`)
  - [x] Add dependencies: Spring Web, Spring Data JPA, Spring Security, Validation, Lombok, Thymeleaf
- [x] **Frontend Setup**
  - [x] Configure Thymeleaf templates
  - [x] Integrate Tailwind CSS for styling
  - [x] Setup base layout (header, navigation, container, footer)
- [x] **Internationalization**
  - [x] Add message bundles: `messages_pl.properties`, `messages_en.properties`, `messages_de.properties`
  - [x] Configure locale resolver & interceptor in Spring Boot

---

## Phase 2 – Authentication & Permissions
- [x] **Mock OTP Authentication**
  - Implement login page (Thymeleaf)
  - Mock OTP validation (code sent displayed on console/log)
  - Store logged-in user in session
- [x] **Role-Based Access Control**
  - Define roles: `ADMIN`, `USER`, `VIEWER`
  - Restrict Thymeleaf views and API endpoints
  - Implement `/api/user/permissions` mock returning `{ "role": "Admin" }`

---

## Phase 3 – Database Schema (MySQL)
- [ ] **Entities**
  - `User`: id, phone, role
  - `Device`: id, type, model, ownerName, userId (FK)
  - `Zone`: id, name, icon, address, lat, lng, radius
  - `ZoneDevice`: id, zoneId (FK), deviceId (FK), notificationsEnabled
- [ ] **Repositories**
  - JPA repositories for all entities
- [ ] **Data Loader (Mocks)**
  - Insert sample users (Admin, User, Viewer)
  - Insert sample devices (phone, child watch, senior band)
  - Insert 1–2 zones (Home, School)
  - Assign devices to zones

---

## Phase 4 – Backend APIs (with Mocks)
- [ ] **Zone APIs**
  - `GET /api/zones` → return list of zones
  - `POST /api/zones` → create new zone
  - `PUT /api/zones/{id}` → edit zone
  - `DELETE /api/zones/{id}` → delete zone
- [ ] **Device APIs**
  - `GET /api/devices` → return user’s devices (mocked)
- [ ] **Permissions API**
  - `GET /api/user/permissions` → return role of current user
- [ ] **Themes API**
  - `GET /api/themes/{operator}` → return theme colors (mocked JSON)
- [ ] **i18n API**
  - `GET /api/i18n/{lang}` → return translation package (mocked JSON)

---

## Phase 5 – Frontend Views (Thymeleaf + Tailwind)
### 5.1 Onboarding View (Empty State)
- [ ] Implement **Empty State Page**:
  - Title: *Safe Zones*
  - Intro text explaining what zones are
  - Example icons: Home, School
  - Button: **“Add first zone”** → goes to Wizard Step 1

### 5.2 Zones List View
- [ ] Implement **Zones List Page**:
  - Display list of existing zones in cards
  - Show zone name, address, devices assigned
  - Button: **“+ Add zone”**
  - Empty state handled when no zones

### 5.3 Zone Creation Wizard
- [ ] **Step 1 – Zone Name & Icon**
  - Input field for zone name
  - Grid of selectable icons
  - Button: **Next**
- [ ] **Step 2 – Location**
  - Input for address
  - Embedded Google Maps with draggable marker
  - Button: **Next**
- [ ] **Step 3 – Radius**
  - Show map preview with circle overlay
  - Slider (100–5000m) + +/- buttons
  - Display current radius value
  - Button: **Next**
- [ ] **Step 4 – Notifications**
  - List user’s devices with toggle switches
  - Enable/disable notifications per device
  - Button: **Save**

### 5.4 Success Screen
- [ ] Implement **Zone Created Screen**:
  - Confirmation message: *“Zone successfully created!”*
  - Buttons: **Go to zones**, **Add another**

---

## Phase 6 – Zone Management
- [ ] **Edit Zone**
  - Open wizard with pre-filled data
  - Save changes with `PUT /api/zones/{id}`
- [ ] **Delete Zone**
  - Confirmation modal → call `DELETE /api/zones/{id}`
- [ ] **Toggle Notifications**
  - Enable/disable device notifications inside a zone

---

## Phase 7 – Maps & Geofencing
- [ ] **Google Maps Integration**
  - Display map in wizard step 2 & 3
  - Allow marker placement
  - Draw circle for radius
- [ ] **Mock Geofencing**
  - Create mock endpoint simulating enter/exit events
  - Return random enter/exit logs for testing

---

## Phase 8 – UI & Theming
- [ ] **Design System with Tailwind**
  - Colors: primary `#2C5282`, accent `#50C878`, error `#FF6B6B`, background `#F5F5F5`
  - Buttons with rounded corners
  - Cards with shadow
  - Floating labels for inputs
- [ ] **Dynamic Theming**
  - Fetch theme from `/api/themes/{operator}`
  - Apply via Tailwind CSS variables
- [ ] **Adaptive UI**
  - Adjust menu sections based on first device type

---

## Phase 9 – Notifications
- [ ] **Mock Notifications**
  - Show browser notifications (Web Push mock)
  - Triggered by mocked geofencing events
- [ ] **Future Integration**
  - Document potential integration with Firebase Cloud Messaging / APNs

---

## Phase 10 – AI-Assisted Development
- [ ] **AI Code Generation**
  - Use AI to scaffold controllers, services, Thymeleaf templates
- [ ] **AI Refactoring**
  - Let AI suggest performance and style improvements
- [ ] **AI Test Generation**
  - Generate JUnit + Mockito test cases
- [ ] **AI Documentation**
  - Generate inline code comments & update README

---

## Phase 11 – Testing
- [ ] **Backend Unit Tests**
  - Test repositories and services (JUnit + Mockito)
- [ ] **Integration Tests**
  - Test REST endpoints with MockMvc
- [ ] **Frontend Testing**
  - Verify Thymeleaf rendering with sample mocks
- [ ] **Mock Data Validation**
  - Confirm zones, devices, roles load correctly

---

## Phase 12 – Documentation & Delivery
- [ ] **README.md**
  - Setup instructions for backend & frontend
  - MySQL setup guide
- [ ] **Architecture Overview**
  - Diagram of layers (controllers, services, repositories)
  - Data flow for zone creation
- [ ] **AI Usage Report**
  - Document how AI was used in coding, testing, documentation
- [ ] **Demo Preparation**
  - Deploy demo with preloaded zones & devices
  - Record workflow: create → list → edit → delete zone