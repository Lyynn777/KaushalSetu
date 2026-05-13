# KaushalSetu 🔧

> **Bridging Skills with Opportunities**

![Academic Project](https://img.shields.io/badge/Academic-Internship%20Main%20Project-red)
![Project ID](https://img.shields.io/badge/ProjectID-92-black)
![Platform](https://img.shields.io/badge/Platform-Android-green)
![Language](https://img.shields.io/badge/Language-Kotlin-purple)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-blue)
![Backend](https://img.shields.io/badge/Backend-Firebase-orange)
![AI](https://img.shields.io/badge/AI-Gemini-yellow)
![Status](https://img.shields.io/badge/Version-v1.2.0-success)

KaushalSetu is a hyperlocal skilled worker discovery platform designed to connect local service providers with customers in Karnataka, India.  
The platform helps electricians, plumbers, carpenters, painters, mechanics, and other skilled workers build professional digital identities while enabling customers to easily discover trusted local services.

Built using modern Android development practices with Kotlin, MVVM Architecture, Firebase, and Gemini AI integration.

---
## Academic Project Information

- **Original Project Title:** Kaushalya-Karnataka – Hyperlocal Skill Showcase Platform for Blue-Collar Workers
- **Project ID:** 92
- **Developed By:** Pragya M S
- **USN:** 1OX22CS122
- **Institution:** The Oxford College of Engineering

---

# Problem Statement

In many small towns and semi-urban regions, skilled workers still rely heavily on word-of-mouth referrals for employment opportunities. While many workers possess strong technical expertise, they often lack digital platforms to professionally showcase their work and build customer trust.

This creates several challenges:

- Inconsistent work opportunities
- Limited visibility for local workers
- No proper portfolio system for completed projects
- Difficulty for customers to find trusted service providers
- Lack of transparent service information and pricing

KaushalSetu aims to bridge this gap by creating a digital ecosystem for local skilled labor and service discovery.

---

# Product Vision

KaushalSetu focuses on:

- Digital empowerment of skilled workers
- Building trust-based local service ecosystems
- Increasing visibility for blue-collar professionals
- Encouraging entrepreneurship and self-employment
- Simplifying local service discovery for customers

The platform enables workers to create digital portfolios, manage services, upload completed work photos, and receive ratings and reviews from customers. 

---

# Features

## Authentication & User Management
- 🔐 Firebase Authentication (Email + Password)
- 👤 Worker and Customer role-based onboarding
- 🌐 Kannada / English language support
- 🔄 Persistent login session handling

## Worker Features
- 🧰 Professional worker profile creation
- 📍 Skill category, experience, location, bio, availability
- 🖼️ Portfolio image uploads using Firebase Storage
- 🛠️ Add / edit / manage service cards
- 🪪 Shareable profile card generation
- 🤖 AI-powered service description generation using Gemini API

## Customer Features
- 🔍 Browse workers by category and location
- 📋 Send hire requests to workers
- ⭐ Submit ratings and reviews
- 📱 View detailed worker profiles and portfolios

## Platform Features
- ☁️ Firebase Firestore backend
- 📦 Firebase Storage integration
- 🎨 Material Design 3 UI
- 🧱 MVVM architecture
- ⚡ Kotlin Coroutines + Repository pattern

---

# Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| Architecture | MVVM (ViewModel + LiveData + Repository) |
| UI | XML Layouts + Material Design 3 |
| Authentication | Firebase Authentication |
| Database | Firebase Firestore |
| Storage | Firebase Storage |
| AI Integration | Gemini 1.5 Flash API |
| Image Loading | Glide |
| Async Handling | Kotlin Coroutines |
| Localization | English + Kannada |

---

## Project Structure

```
app/src/main/
├── java/com/kaushal/setu/
│   ├── KaushalSetuApp.kt               ← Application class (locale init)
│   ├── data/
│   │   ├── model/                      ← User, WorkerProfile, ServiceCard, Review, HireRequest
│   │   └── repository/                 ← AuthRepository, WorkerRepository, GeminiRepository
│   ├── viewmodel/                      ← AuthViewModel, WorkerViewModel
│   ├── ui/
│   │   ├── auth/                       ← SplashActivity, LoginActivity, RegisterActivity
│   │   ├── worker/                     ← WorkerDashboardActivity, ProfileSetupActivity,
│   │   │                                  AddServiceActivity, ProfileCardActivity
│   │   ├── customer/                   ← CustomerDashboardActivity, SearchActivity,
│   │   │                                  WorkerDetailActivity
│   │   └── common/                     ← LanguageSettingsActivity, all Adapters
│   └── utils/                          ← LocaleHelper, Extensions
└── res/
    ├── layout/                         ← 11 activity + 4 item layouts
    ├── drawable/                       ← shapes, icons, backgrounds
    ├── color/                          ← input stroke color states
    ├── values/                         ← colors, strings (English), themes, dimens
    ├── values-kn/                      ← strings (Kannada)
    └── xml/                            ← file_paths, backup_rules
```
---

# Current Features (v1.2.0)

- Firebase Authentication
- Worker Profile Management
- Service Card System
- AI Description Generator
- Portfolio Upload System
- Search & Browse Functionality
- Ratings & Reviews
- Hire Request Workflow
- Kannada / English Localization
- Shareable Profile Card Generation
- Improved UX and profile handling refinements

---

# Sample Test Accounts

## Worker Account
- **Name:** Ravi Kumar  
- **Phone:** 9876543210  
- **Email:** ravi@test.com  
- **Password:** ravi1234  

## Customer Account
- **Name:** Priya Sharma  
- **Phone:** 9845012345  
- **Email:** priya@test.com  
- **Password:** priya1234  

---

# Future Enhancements

- 💬 Real-time worker-customer chat
- 💳 Online payments
- 📍 Live location tracking
- 🛡️ Verified worker badges
- 🎥 Video portfolio uploads
- 🔔 Push notifications
- 📊 Worker analytics dashboard

---

# Expected Impact

KaushalSetu aims to:

- Create better employment visibility for skilled workers
- Promote digital entrepreneurship
- Improve trust between workers and customers
- Strengthen local community economies
- Support dignity of labor through technology

---

# License

This project is intended for educational and portfolio purposes.

