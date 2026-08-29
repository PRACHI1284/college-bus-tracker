<div align="center">
  <img src="https://img.icons8.com/color/96/000000/bus.png" alt="Bus Logo"/>
  <h1>🚌 College Bus Tracker</h1>
  <p>A full-stack mobile application for real-time campus transit tracking.</p>

  <p>
    <a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-1.9.0-blue.svg?logo=kotlin" alt="Kotlin"></a>
    <a href="https://developer.android.com/jetpack/compose"><img src="https://img.shields.io/badge/Jetpack%20Compose-Ready-4285F4.svg?logo=android" alt="Jetpack Compose"></a>
    <a href="https://nodejs.org/"><img src="https://img.shields.io/badge/Node.js-Express-339933.svg?logo=node.js" alt="Node.js"></a>
    <a href="https://square.github.io/retrofit/"><img src="https://img.shields.io/badge/Retrofit-Networking-red.svg" alt="Retrofit"></a>
  </p>
</div>

<br />

## 🌟 Overview

The **College Bus Tracker** is an intelligent, modern Android application designed to help university students and faculty navigate campus transit effortlessly. Say goodbye to waiting at the bus stop and guessing when the next shuttle will arrive! 

Built with a **Jetpack Compose** frontend and a **Node.js/Express** backend, this project demonstrates a robust, full-stack mobile architecture using real-time polling and responsive UI design.

## ✨ Features

- **📍 Real-Time Tracking**: Monitor live GPS locations of all campus buses on an interactive map.
- **⏱️ Dynamic ETAs**: View highly accurate Estimated Times of Arrival for upcoming stops.
- **🗺️ Interactive Route Maps**: Browse visual paths for all university shuttle routes (North Campus, South Gate, Metro Connector, etc.).
- **💳 Integrated Fee Payments**: Securely pay semester transit fees within the app with dummy UPI/Card transactions.
- **🔔 Smart Notifications**: Receive instant push alerts for delays, route changes, or approaching buses.
- **🛡️ Secure Login**: Features a simulated 2-Factor Authentication (2FA) flow for student profiles.

## 🏗️ Architecture Stack

### Frontend (Android)
- **Language**: Kotlin
- **UI Toolkit**: Jetpack Compose (Modern Declarative UI)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Networking**: Retrofit 2 + OkHttp + Moshi (JSON Parsing)
- **Asynchronous Flow**: Kotlin Coroutines & StateFlow

### Backend (Node.js API)
- **Framework**: Express.js
- **Data Source**: Configured with mock dynamic transit data
- **Cross-Origin**: CORS enabled for broad testing

## 🚀 Getting Started

### 1. Run the Backend Server
The Android app relies on the Node.js backend for its live data stream.

```bash
cd backend
npm install
npm start
```
*The server will start on `http://localhost:3000`.*

### 2. Run the Android App
1. Open the project in **Android Studio**.
2. Allow Gradle to sync and download dependencies.
3. Launch the app on the **Android Emulator**.

*Note: The app is pre-configured to use `http://10.0.2.2:3000/` as its Base URL, allowing the Android Emulator to seamlessly connect to your local Node.js server.*

## 📸 Screenshots

*(Add your beautiful screenshots of the UI here to showcase the app!)*

---
<div align="center">
  <i>Built with ❤️ by Prachi Gadge.</i>
</div>
