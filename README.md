# Car Tilt Game – Android (Kotlin)

This project is an Android car game developed in Kotlin.  
The goal of the project was to practice working with device sensors, game logic, and Android lifecycle management, while keeping the code modular and easy to maintain.

The player controls a car by tilting the device. Horizontal tilt moves the car left and right, and forward/backward tilt affects the driving speed.

---

## Project Description

The game uses the phone’s accelerometer to translate physical device movement into in-game actions.  
During development, special attention was given to separating game logic from UI code and handling system resources such as sensors, audio, and vibration in a safe way.

The project includes multiple screens, persistent score storage, and feedback mechanisms such as sound and vibration to improve the user experience.

---

## Main Features

- Car movement using device tilt (accelerometer)
- Speed control based on forward and backward tilt
- Background music and sound effects
- Vibration feedback for player actions
- Top 10 high-score saving and display
- Navigation between multiple activities and fragments

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
- `MapFragment` – additional screen for map-related features

---

### `utilities`
Helper classes used across the project:
- `BackgroundMusicPlayer`
- `SoundEffectPlayer`
- `VibrationHelper`
- `LocationHelper`
- `PermissionHelper`

---

## Design Considerations

- Clear separation between UI, logic, and data
- Use of callbacks to reduce tight coupling between components
- Proper lifecycle handling for sensors and media components
- Avoiding memory leaks by careful use of Context references

---

## Technologies

- Kotlin
- Android SDK
- Accelerometer Sensor API
- MediaPlayer
- RecyclerView

---

## Running the Project

1. Open the project in Android Studio
2. Run the app on an emulator or a physical Android device
3. Tilt the device to control the car

---

## Notes

This project was developed as part of an Android development course and focuses on practical experience with sensors, game logic, and application structure rather than advanced graphics.
