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

## Setup Guide

### Step 1 — Firebase Project

1. Go to [console.firebase.google.com](https://console.firebase.google.com)
2. Create project → name it `KaushalSetu`
3. Add Android app → package name: `com.kaushal.setu`
4. Download `google-services.json` → place it in `app/`
5. Enable these services:
   - **Authentication** → Sign-in method → Email/Password → Enable
   - **Firestore Database** → Create database → Start in test mode → Region: `asia-south1`
   - **Storage** → Get started → Test mode → Same region

**Firestore rules** (paste in Firestore → Rules → Publish):
```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

**Storage rules** (paste in Storage → Rules → Publish):
```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

---

### Step 2 — Gemini AI (Optional)

The app works with a built-in offline mock. To enable real AI:

1. Go to [aistudio.google.com/app/apikey](https://aistudio.google.com/app/apikey)
2. Create a free API key
3. Open `app/src/main/java/com/kaushal/setu/data/repository/GeminiRepository.kt`
4. Replace:
   ```kotlin
   private val apiKey = "YOUR_GEMINI_API_KEY"
   ```
   with your actual key.

---

### Step 3 — Run

1. Open **Android Studio** (Hedgehog 2023.1.1 or newer)
2. **File → Open** → select the `KaushalSetu` folder
3. Wait for Gradle sync (2–5 min first time)
4. Run on emulator or physical device ▶

---

## How to Add New Features (without breaking anything)

The architecture is designed so new features never touch existing files.

| What to add | What to create | Where |
|---|---|---|
| New screen | `ActivityXxx.kt` + `activity_xxx.xml` + register in `AndroidManifest.xml` | `ui/worker/` or `ui/customer/` |
| New list | `XxxAdapter.kt` + `item_xxx.xml` | `ui/common/` |
| New data type | `Xxx.kt` | `data/model/` |
| New Firebase operation | Function in existing repo, or new `XxxRepository.kt` | `data/repository/` |
| New feature group (e.g. Chat) | New `ChatViewModel.kt` + `ChatRepository.kt` | No changes to existing files |
| New string | Add to both `values/strings.xml` AND `values-kn/strings.xml` | Never hardcode strings |
| New color | Add to `values/colors.xml` | Never hardcode color hex in layouts |

---

## Free Tier Usage

| Service | Free Limit | This App |
|---|---|---|
| Firebase Auth | Unlimited users | ✅ |
| Firestore | 50K reads/day, 20K writes/day, 1GB | ✅ Well within |
| Firebase Storage | 5GB storage, 1GB/day download | ✅ Well within |
| Gemini API | 15 req/min free tier | ✅ Well within |

**Total monthly cost: ₹0**

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

*Built with ❤️ for Karnataka's skilled workers*
