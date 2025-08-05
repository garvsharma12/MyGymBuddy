# 🏋️‍♂️ MyGymBuddy

**MyGymBuddy** is a full-stack intelligent fitness application designed to help users track workouts, monitor health, set goals, and receive AI-powered exercise suggestions. Built with **Spring Boot** and **React**, the app offers a secure, scalable, and modular architecture with real-time insights and progress tracking.

---

## 🚀 Features

### 🔐 Authentication & Authorization
- **Keycloak Integration** for enterprise-grade identity and access management
- **JWT-based security** with **role-based access control** (User, Trainer, Admin)
- OAuth2/OpenID Connect ready for third-party providers (Google, GitHub, etc.)

### 📊 Health & Workout Tracking
- Track **daily workouts**, **BMI**, **body metrics**, and **progress**
- Set custom **fitness goals** (fat loss, muscle gain, endurance)
- Generate **dynamic workout plans** based on goal, age, and body type
- **Real-time progress monitoring** with visual dashboards

### 🤖 Google Gemini AI Integration
- Integrated with **Google Gemini API** for smart recommendations
- Accepts user health data (e.g., age, BMI, goal) and returns:
  - **Customized exercise suggestions**
  - Predictions on **workout effects** (fat loss, muscle gain, etc.)
  - **Nutrition and recovery guidance** based on your routine

### ⚙️ Modular Architecture
- Clean and maintainable **REST APIs** using **Spring Boot**
- **MongoDB + MySQL** hybrid storage for flexibility in structured + unstructured data
- **Spring Data JPA** for ORM, pagination, and filtering
- **DTOs**, **Service layers**, and **Controller layers** follow clean architecture principles

---

## 🛠️ Tech Stack

### 💻 Frontend
- **React.js**
- **TypeScript**
- **JavaScript**
- **HTML5**
- **CSS3**

### 🔙 Backend
- **Java 17**
- **Spring Boot**
- **Spring Security + JWT**
- **Spring Data JPA**
- **Keycloak** (Auth Server)
- **RESTful APIs**

### 🗄️ Databases
- **MySQL** – for structured data (users, workouts, roles)
- **MongoDB** – for flexible storage (activity logs, feedback, AI insights)

### 🤖 AI Integration
- **Google Gemini API** – real-time intelligent suggestions based on user data

### 🐳 DevOps & Tools
- **Docker** – containerized app deployment
- **Git** – version control
- **Postman** – API testing
- **Nginx** (optional) – reverse proxy (future)

---

📌 Future Enhancements

    📱 Integration with Google Fit / Apple Health

    🧠 Gemini-powered AI chatbot coach

    📈 Trainer dashboard for managing clients

    🔔 Push notifications for reminders and milestones
