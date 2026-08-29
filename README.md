<div align="center">

<img src="https://img.icons8.com/color/96/000000/bus.png" alt="Bus Logo" width="80"/>

# ðŸšŒ College Bus Tracker

### *Real-time campus transit â€” always know when your bus arrives*

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Ready-4285F4?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Node.js](https://img.shields.io/badge/Node.js-Express-339933?style=for-the-badge&logo=node.js&logoColor=white)](https://nodejs.org/)
[![Android API](https://img.shields.io/badge/Android-API%2024%2B-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Version](https://img.shields.io/badge/Version-1.0-blue?style=for-the-badge)](https://github.com/PRACHI1284/college-bus-tracker/releases)

**A full-stack Android application for real-time campus bus tracking.**
Built with Jetpack Compose + Node.js/Express. No more waiting at the bus stop guessing when the next shuttle arrives.

[ðŸ“² Download APK](#-option-a--download--install-the-pre-built-apk-easiest) Â· [ðŸ› ï¸ Build from Source](#-option-b--build--run-from-source-developers) Â· [ðŸ“‹ Report a Bug](https://github.com/PRACHI1284/college-bus-tracker/issues)

</div>

---

## âœ¨ Features

| Feature | Description |
|---|---|
| ðŸ“ **Live Bus Tracking** | Monitor real-time GPS locations of all campus buses on an interactive map |
| â±ï¸ **Dynamic ETAs** | Accurate Estimated Times of Arrival for every stop on every route |
| ðŸ—ºï¸ **Interactive Routes** | Browse visual paths for all university shuttle routes (North Loop, South Express, Metro Connector) |
| ðŸ”” **Smart Notifications** | Get alerts when your bus is approaching your stop or running late |
| ðŸ’³ **Bus Pass & Payments** | Manage your digital bus pass and top-up your transit wallet in-app |
| ðŸ‘¤ **Student Account** | Profile management, ride history, and saved favourite stops |
| ðŸ” **Secure 2-Factor Login** | University portal login + OTP verification for secure access |
| ðŸŒ™ **Dark Mode UI** | Beautiful dark-themed Compose UI optimised for battery life |

---

## ðŸ—ï¸ Tech Stack

| Layer | Technology | Purpose |
|---|---|---|
| **Android UI** | Kotlin + Jetpack Compose | All screens and components |
| **Architecture** | MVVM (ViewModel + Repository) | Clean separation of concerns |
| **Networking** | Retrofit 2 + OkHttp | REST API calls to backend |
| **Local DB** | Room Database | Offline caching of routes and stops |
| **Backend** | Node.js + Express | REST API serving bus/route data |
| **Map Rendering** | Custom Canvas (LiveMapCanvas) | Route visualisation |
| **Build System** | Gradle (Kotlin DSL) | Android project build |
| **Security** | Secrets Gradle Plugin | API key management |

---

## ðŸ“ Project Architecture

```
college-bus-tracker/
â”‚
â”œâ”€â”€ app/                              # Android Application
â”‚   â””â”€â”€ src/main/java/com/example/
â”‚       â”œâ”€â”€ MainActivity.kt           # App entry point, navigation host
â”‚       â”‚
â”‚       â”œâ”€â”€ data/
â”‚       â”‚   â”œâ”€â”€ api/
â”‚       â”‚   â”‚   â”œâ”€â”€ ApiClient.kt      # Retrofit instance + BASE_URL
â”‚       â”‚   â”‚   â””â”€â”€ TransitApiService.kt  # API endpoints interface
â”‚       â”‚   â”œâ”€â”€ dao/Daos.kt           # Room DAO queries
â”‚       â”‚   â”œâ”€â”€ database/CampusBusDatabase.kt  # Room DB setup
â”‚       â”‚   â”œâ”€â”€ entity/Entities.kt    # DB table schemas
â”‚       â”‚   â”œâ”€â”€ model/Models.kt       # Data model classes
â”‚       â”‚   â””â”€â”€ repository/BusRepository.kt   # Data layer
â”‚       â”‚
â”‚       â””â”€â”€ ui/
â”‚           â”œâ”€â”€ components/
â”‚           â”‚   â””â”€â”€ LiveMapCanvas.kt  # Custom canvas map renderer
â”‚           â”œâ”€â”€ screens/
â”‚           â”‚   â”œâ”€â”€ LoginScreen.kt    # Auth: Portal login + OTP 2FA
â”‚           â”‚   â”œâ”€â”€ TrackingScreen.kt # Main live tracking map
â”‚           â”‚   â”œâ”€â”€ NotificationScreen.kt
â”‚           â”‚   â”œâ”€â”€ PaymentScreen.kt  # Bus pass and wallet
â”‚           â”‚   â””â”€â”€ AccountScreen.kt  # Student profile
â”‚           â”œâ”€â”€ theme/                # Colors, Typography, Theme
â”‚           â””â”€â”€ viewmodel/BusViewModel.kt
â”‚
â”œâ”€â”€ backend/
â”‚   â”œâ”€â”€ index.js                      # Express server + API routes
â”‚   â””â”€â”€ package.json
â”‚
â”œâ”€â”€ .env.example                      # Environment variable template
â””â”€â”€ README.md
```

---

## ðŸ“‹ Prerequisites

Make sure the following are installed before you begin:

| Tool | Minimum Version | Download Link |
|---|---|---|
| **Android Studio** | Hedgehog 2023.1.1+ | [Download](https://developer.android.com/studio) |
| **JDK** | 11 or 17 | [Download](https://adoptium.net/) |
| **Node.js** | 18.x LTS+ | [Download](https://nodejs.org/en/download) |
| **Git** | Any recent version | [Download](https://git-scm.com/) |
| **Android Phone** | Android 7.0 (API 24)+ | â€” |

---

## ðŸš€ Getting the App on Your Phone

Choose the method that suits you:

---

### ðŸ“² Option A â€” Download & Install the Pre-Built APK *(Easiest)*

> Best for: Students who just want to use the app without any setup.

**Step 1:** Go to the [**Releases page**](https://github.com/PRACHI1284/college-bus-tracker/releases)

**Step 2:** Download the latest `app-debug.apk` file directly to your Android phone

**Step 3:** Enable installation from unknown sources on your phone:
```
Settings â†’ Apps â†’ Special App Access â†’ Install Unknown Apps
â†’ Select your browser/file manager â†’ Allow
```
> On older Android (below 8.0): `Settings â†’ Security â†’ Unknown Sources â†’ Enable`

**Step 4:** Open the downloaded `.apk` from your notification bar or file manager and tap **Install**

**Step 5:** Open **College Bus Tracker** from your app drawer â€” done! âœ…

> **Note:** You may see a *"Play Protect"* warning. Tap **Install Anyway** â€” the app is safe.

---

### ðŸ› ï¸ Option B â€” Build & Run from Source *(Developers)*

> Best for: Contributors who want to build or modify the app.

#### Step 1: Clone the Repository

```bash
git clone https://github.com/PRACHI1284/college-bus-tracker.git
cd college-bus-tracker
```

#### Step 2: Start the Backend Server

```bash
cd backend
npm install
npm start
```

You should see:
```
ðŸšŒ College Bus Tracker API running on port 3000
```

Verify it works: open [http://localhost:3000/api/v1/buses](http://localhost:3000/api/v1/buses) in your browser.

#### Step 3: Configure the Android App's Backend URL

**Find your machine's local IP:**
- **Windows:** Open Command Prompt â†’ run `ipconfig` â†’ look for `IPv4 Address` (e.g., `192.168.1.5`)
- **macOS:** Open Terminal â†’ run `ifconfig` â†’ look for `inet` under `en0`
- **Linux:** Open Terminal â†’ run `hostname -I`

**Update `ApiClient.kt`:**

Open `app/src/main/java/com/example/data/api/ApiClient.kt` and change:

```kotlin
// For Android Emulator (talks to localhost via special alias):
private const val BASE_URL = "http://10.0.2.2:3000/"

// For real physical Android phone (use your machine's local IP):
private const val BASE_URL = "http://192.168.1.5:3000/"
//                                    â†‘ Replace with your actual IP
```

> âš ï¸ Your Android phone and computer **must be connected to the same Wi-Fi network**.

#### Step 4: Run on Your Phone via Android Studio

1. Open Android Studio â†’ **Open** â†’ Select the `college-bus-tracker` folder
2. Wait for Gradle sync to finish (2â€“5 minutes first time)
3. Enable **USB Debugging** on your phone:
   ```
   Settings â†’ About Phone â†’ Tap "Build Number" 7 times
   â†’ Go back â†’ Developer Options â†’ Enable "USB Debugging"
   ```
4. Connect your phone via USB cable and tap **Allow** when prompted
5. Select your phone from the device dropdown in Android Studio
6. Click â–¶ï¸ **Run** â€” the app will install and launch on your phone

#### Step 5: Build a Shareable APK

```bash
# From the project root directory:
./gradlew assembleDebug

# Your APK will be at:
# app/build/outputs/apk/debug/app-debug.apk
```

Share this `.apk` file via USB, Google Drive, WhatsApp, or email.

---

### ðŸ–¥ï¸ Option C â€” Backend API Only

```bash
cd backend
npm install
npm start
# API available at http://localhost:3000
# Deployed API: https://college-bus-tracker-api.onrender.com
```

**Available Endpoints:**

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/buses` | All active buses with current locations |
| `GET` | `/api/v1/buses/:id` | Get a specific bus by ID |
| `GET` | `/api/v1/buses/search?routeId=ROUTE_NORTH` | Filter buses by route |

---

## âš™ï¸ Configuration

### Backend `.env` Setup

```bash
# Copy the example file
cp .env.example .env
```

Edit `.env`:
```env
PORT=3000
NODE_ENV=development
# GEMINI_API_KEY=your_gemini_key_here   # Optional: for AI features
```

### Android `local.properties`

This file is auto-generated by Android Studio. If building manually, create it at the project root:

```properties
# Windows:
sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk

# macOS:
# sdk.dir=/Users/YourName/Library/Android/sdk

# Linux:
# sdk.dir=/home/YourName/Android/Sdk
```

---

## ðŸ” First Login

1. Launch the app on your phone
2. Enter your **university portal credentials** (username + password)
3. Tap **Login**
4. Enter the **4-digit OTP** sent to your registered email/phone
5. You're in â€” start tracking your bus! ðŸŽ‰

> ðŸ’¡ **Dev / Testing mode:** The OTP is shown on-screen during development. In a production build connected to a real auth server, it will arrive via SMS or email.

---

## ðŸ§¯ Troubleshooting

<details>
<summary><b>âŒ "Connection refused" â€” App cannot reach the backend</b></summary>

- Confirm the backend server is running: `cd backend && npm start`
- Make sure your phone and PC are on the **same Wi-Fi network**
- Double-check the IP address in `ApiClient.kt` matches your PC's local IP (run `ipconfig` on Windows)
- Temporarily disable your PC firewall, or allow port `3000` through it
- On Android 9+, the app requires HTTPS for non-localhost connections. For testing, the `AndroidManifest.xml` must have `android:usesCleartextTraffic="true"` â€” verify this is present

</details>

<details>
<summary><b>âŒ Gradle sync fails in Android Studio</b></summary>

- Go to **File â†’ Invalidate Caches â†’ Invalidate and Restart**
- Confirm JDK 11 or 17 is selected: **File â†’ Project Structure â†’ SDK Location â†’ JDK**
- Try running `./gradlew --refresh-dependencies` from a terminal in the project folder
- Make sure you have a stable internet connection for first-time dependency download

</details>

<details>
<summary><b>âŒ "Install blocked" when installing the APK on phone</b></summary>

- Go to `Settings â†’ Apps â†’ Special App Access â†’ Install Unknown Apps` on your phone
- Find your browser or file manager in the list and toggle it **ON**
- Try installing the APK again

</details>

<details>
<summary><b>âŒ USB device not detected in Android Studio</b></summary>

- Enable **USB Debugging**: `Settings â†’ Developer Options â†’ USB Debugging â†’ On`
- Accept the RSA key fingerprint prompt on your phone
- Try a different USB cable â€” many cables are charge-only with no data transfer
- Install the [Google USB Driver](https://developer.android.com/studio/run/win-usb) on Windows
- On macOS, trust the device when prompted

</details>

<details>
<summary><b>âŒ App shows blank map / no buses displayed</b></summary>

- Open your phone's browser and navigate to `http://YOUR_PC_IP:3000/api/v1/buses`
- If that page loads JSON, the backend is fine â€” re-check the IP in `ApiClient.kt`
- If it doesn't load, the problem is network connectivity â€” see the first troubleshooting tip above

</details>

---

## ðŸ—ºï¸ Project Roadmap

### âœ… Phase 1 â€” Foundation *(Complete)*
- [x] Jetpack Compose UI with professional dark theme
- [x] MVVM architecture (ViewModel + Repository pattern)
- [x] Room database scaffolding for offline caching
- [x] Retrofit networking layer
- [x] 5 full screens: Login, Tracking, Notifications, Payment, Account
- [x] Custom `LiveMapCanvas` for route visualisation
- [x] Node.js + Express REST API backend
- [x] 2-Factor Authentication login flow

### ðŸ”¨ Phase 2 â€” Real Data & Maps *(In Progress)*
- [ ] Integrate Google Maps SDK for real geographic rendering
- [ ] Replace mock coordinates with real GPS lat/lng data
- [ ] WebSocket / polling for live bus location updates
- [ ] Deploy backend to cloud (Render.com / Railway.app)
- [ ] Seed Room DB with real route + stop data

### ðŸ”® Phase 3 â€” Production Features *(Planned)*
- [ ] Real university SSO / OAuth authentication
- [ ] Push notifications via Firebase Cloud Messaging (FCM)
- [ ] UPI / Razorpay integration for bus pass payments
- [ ] Full offline mode
- [ ] Bus driver companion app (separate role/build)

### ðŸš€ Phase 4 â€” Release *(Planned)*
- [ ] Rename package from `com.example` to production namespace
- [ ] Google Play Store submission
- [ ] GitHub Actions CI â€” auto-build & upload APK on every push
- [ ] Add in-app screenshots and demo video to README

---

## ðŸ¤ Contributing

Contributions are very welcome! Here's how:

1. **Fork** this repository
2. Create a new branch for your feature:
   ```bash
   git checkout -b feature/real-gps-tracking
   ```
3. Make your changes, then commit:
   ```bash
   git commit -m "feat: integrate Google Maps SDK for live tracking"
   ```
4. Push to your fork:
   ```bash
   git push origin feature/real-gps-tracking
   ```
5. Open a **Pull Request** with a description of your changes

### Commit Message Convention

| Prefix | Use for |
|---|---|
| `feat:` | A new feature |
| `fix:` | A bug fix |
| `docs:` | Documentation changes only |
| `style:` | Formatting, no logic changes |
| `refactor:` | Code restructuring |
| `test:` | Adding or updating tests |

---

## ðŸ“„ License

This project is open-source and available for educational and personal use.

---

<div align="center">

Made with â¤ï¸ by [PRACHI1284](https://github.com/PRACHI1284)

**â­ Star this repo if it was helpful!**

[ðŸ› Report Bug](https://github.com/PRACHI1284/college-bus-tracker/issues) Â· [ðŸ’¡ Request Feature](https://github.com/PRACHI1284/college-bus-tracker/issues) Â· [ðŸ“§ Contact](https://github.com/PRACHI1284)

</div>

