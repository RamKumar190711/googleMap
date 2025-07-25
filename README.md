# 🚀 Live Location Tracking with Foreground Service and Firebase 📍

This Android project demonstrates real-time location tracking using a **foreground service** to continuously update device location, and Firebase Realtime Database to sync and display locations on Google Maps.

---

## ✨ Features

- 📡 Continuously track device location in the background using a **foreground location service**.
- 🔐 Request and handle all required location permissions including new Android 12+ `FOREGROUND_SERVICE_LOCATION` permission.
- 🔄 Update the user’s current location to Firebase Realtime Database.
- 🗺️ Listen for location updates from Firebase and display markers on Google Maps.
- 🎯 Supports smooth real-time location updates with Google Maps markers and camera animation.
- 🤖 Integrated with CI/CD pipeline for automated builds and deployments.

---

## 🏗️ Architecture Overview

- **MapFragment**
    - Handles UI display with Google Maps 🗺️
    - Requests permissions and starts the foreground location service ▶️
    - Listens for Firebase location updates to display on the map 🔄

- **LocationUpdatesService**
    - Runs as a foreground service with `foregroundServiceType="location"` ⚙️
    - Fetches location updates using `FusedLocationProviderClient` 📍
    - Uploads location updates to Firebase continuously ☁️

- **Firebase Realtime Database**
    - Stores user location data under `/users/user1/location` node 💾
    - Syncs location data in real-time between devices 🔄

---

## 🔐 Permissions

The app requires the following permissions:

- `ACCESS_FINE_LOCATION` 📍
- `ACCESS_COARSE_LOCATION` 🌐
- `ACCESS_BACKGROUND_LOCATION` 🔙
- `FOREGROUND_SERVICE` 🔔
- `FOREGROUND_SERVICE_LOCATION` (required for Android 12+ foreground location service) 🛡️

---

## ⚙️ Setup Instructions

1. 📥 Clone this repository
2. 🔑 Add your **Google Maps API key** in `AndroidManifest.xml` inside the `<meta-data>` tag.
3. ☁️ Configure Firebase project and add your `google-services.json` to the app folder.
4. ✅ Make sure to request all runtime permissions, especially for Android 12+ `FOREGROUND_SERVICE_LOCATION`.
5. 📱 Build and run on a physical device or emulator with Google Play Services.

---

## 📝 Notes

- ⏱️ Location updates are requested with high accuracy and interval of 5 seconds.
- 🔄 The foreground service ensures location tracking continues even if the app is killed or in the background.
- 📱 This app targets Android API level 35 and above, so the new foreground service permission model is enforced.
- 🤖 CI/CD pipeline automates build and deployment processes for faster development.

---

## 🛠️ Troubleshooting

- **⚠️ SecurityException on starting service**: Ensure `FOREGROUND_SERVICE_LOCATION` permission is declared in the manifest and requested at runtime.
- **❌ Firebase location not updating**: Verify your Firebase Realtime Database rules and internet connection.
- **🗺️ Map markers not showing**: Check Google Maps API key and permissions.

---

## 📄 License

This project is licensed under the MIT License.

---

## 📬 Contact

For questions or suggestions, please open an issue or contact me directly.

---

