# Car Tilt Game – Android (Kotlin)

This project is an Android car game developed in Kotlin.  
The goal of the project was to practice working with device sensors, game logic, and Android lifecycle management, while keeping the code modular and easy to maintain.

The player controls a car by tilting the device. Horizontal tilt moves the car left and right, and forward/backward tilt affects the driving speed.

---

## Project Description

The game uses the phone’s accelerometer to translate physical device movement into in-game actions.  
During development, special attention was given to separating game logic from UI code and handling system resources such as sensors, audio, vibration, and location services in a safe and controlled way.

The project includes multiple screens, persistent score storage, and feedback mechanisms such as sound and vibration to improve the user experience.

---

## Main Features

- Car movement using device tilt (accelerometer)
- Speed control based on forward and backward tilt
- Background music and sound effects
- Vibration feedback for player actions
- Top 10 high-score saving and display
- Extended driving track logic
- Navigation between multiple activities and fragments
- Google Maps integration

---

## Project Structure

### `callback`
Contains callback interfaces used to pass events between components:
- `TiltCallback`
- `ScoreClickedCallBack`

---

### `data`
Responsible for data storage:
- `TopTenStorage.kt` – handles saving and loading the top 10 scores

---

### `logic`
Core game logic:
- `GameManager` – manages game state and scoring
- `TiltDetector` – listens to accelerometer events and converts them into game actions

---

### `ui`
User interface layer:
- `MainActivity` – main menu screen
- `GameActivity` – gameplay screen
- `TopTenActivity` – high-score screen
- `ScoreAdapter` – adapter for displaying scores in a list

---

### `fragment`
Fragment-based screens:
- `ListFragment` – displays the score list
- `MapFragment` – map-based screen using Google Maps

---

### `utilities`
Helper classes used across the project:
- `BackgroundMusicPlayer`
- `SoundEffectPlayer`
- `VibrationHelper`
- `LocationHelper`
- `PermissionHelper`

---

## Google Maps API Key Setup

This project uses Google Maps features that require an API key.

For security reasons, the API key is **not included in the repository**.

### How to configure the API key

1. Open (or create) the file `local.properties` in the **root directory** of the project.
2. Add the following line:

```properties
MAPS_API_KEY=your_google_maps_api_key_here
