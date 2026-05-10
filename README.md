# KaushalSetu 🔧

> **Bridging Skills with Opportunities**

A hyperlocal skilled worker discovery platform for Karnataka, India.
Workers showcase services and portfolios; customers find trusted local help.

---

## Screenshots

| Login | Worker Dashboard | Customer Browse | Worker Detail |
|---|---|---|---|
| *(run app to see)* | *(run app to see)* | *(run app to see)* | *(run app to see)* |

---

## Features

### v1.0 (current)
- 🔐 **Auth** — Register / Login / Logout via Firebase Auth (Email + Password)
- 👤 **Worker Profile** — Name, skill, location, experience, bio, photo, availability
- 🛠️ **Service Cards** — Add / edit / delete services with price and description
- 🤖 **AI Description Generator** — Gemini API generates professional service descriptions (offline mock included)
- 🖼️ **Portfolio Photos** — Upload completed work photos via Firebase Storage
- 🔍 **Search & Browse** — Filter by skill category + location
- 📋 **Hire Requests** — Customer sends request; worker accepts or rejects
- ⭐ **Ratings & Reviews** — Star rating + comment; average auto-recalculates
- 🌐 **Kannada / English** — Full bilingual support; toggle from any dashboard
- 🪪 **Shareable Profile Card** — PNG card shareable via WhatsApp, other apps, or saved to gallery

### v2.0 (planned — slots ready in architecture)
- 💬 In-app chat between worker and customer
- 🏅 Trust Score & badges (Rising Star / Trusted Pro / Elite)
- 📊 Earnings Dashboard for workers
- 📅 Availability scheduling (set working days/hours)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| Architecture | MVVM (ViewModel + LiveData + Repository) |
| Auth | Firebase Authentication |
| Database | Firebase Firestore |
| Storage | Firebase Storage |
| AI | Gemini 1.5 Flash API (with offline mock fallback) |
| Image Loading | Glide |
| UI | Material Design 3 — Forest Green + Amber theme |

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

## Sample Test Data

**Worker account:**
- Name: `Ravi Kumar` | Phone: `9876543210`
- Email: `ravi@test.com` | Password: `ravi1234`
- Role: Skilled Worker

**Customer account:**
- Name: `Priya Sharma` | Phone: `9845012345`
- Email: `priya@test.com` | Password: `priya1234`
- Role: Customer

---

## Contributing

1. Fork the repo
2. Create a feature branch: `git checkout -b feature/chat`
3. Follow the architecture rules in the table above
4. Test on both English and Kannada
5. Submit a pull request

---

## License

MIT License — free to use, modify, and distribute.

---

