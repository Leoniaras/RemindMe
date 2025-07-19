# RemindMe - Android Reminder App

A modern Android reminder application built with Kotlin, Java 21, and Material Design. The app features a gamified experience system with 10 levels and experience points for completing reminders.

## Features

### 🎯 Core Functionality
- **Create Reminders**: Set reminders with title, description, date, and time
- **Calendar View**: View all reminders organized by date
- **Progress Tracking**: Gamified experience system with 10 levels
- **Notifications**: Push notifications when reminders are due

### 📱 Three Main Screens

#### 1. Create Reminder Screen
- Form to create new reminders
- Date and time pickers
- Title and optional description fields
- Validation for past dates/times

#### 2. Calendar Screen
- Calendar view to select dates
- List of reminders for selected date
- Mark reminders as completed
- Delete reminders
- Visual indicators for dates with reminders

#### 3. Progress Screen
- Current level and experience display
- Progress bar showing advancement to next level
- Statistics: total reminders, completed reminders, success rate
- Level names: Beginner → Novice → Apprentice → Student → Learner → Practitioner → Adept → Expert → Master → Grandmaster

### 🎮 Gamification System
- **10 Experience Points** per completed reminder
- **10 Levels** with increasing experience requirements:
  - Level 1: 0 XP (Beginner)
  - Level 2: 10 XP (Novice)
  - Level 3: 25 XP (Apprentice)
  - Level 4: 50 XP (Student)
  - Level 5: 100 XP (Learner)
  - Level 6: 200 XP (Practitioner)
  - Level 7: 400 XP (Adept)
  - Level 8: 800 XP (Expert)
  - Level 9: 1600 XP (Master)
  - Level 10: 3200 XP (Grandmaster)

## Technical Stack

### 🛠️ Technologies
- **Kotlin** - Primary language
- **Java 21** - Runtime compatibility
- **Android SDK 34** - Target API
- **Material Design 3** - UI components
- **Room Database** - Local data persistence
- **Hilt** - Dependency injection
- **Navigation Component** - Screen navigation
- **ViewModel & LiveData** - Architecture components
- **Work Manager** - Background tasks
- **Alarm Manager** - Notification scheduling

### 🏗️ Architecture
- **MVVM Pattern** - Model-View-ViewModel
- **Repository Pattern** - Data access abstraction
- **Clean Architecture** - Separation of concerns
- **Dependency Injection** - Hilt for DI

### 📦 Key Dependencies
```gradle
// Core Android
implementation 'androidx.core:core-ktx:1.12.0'
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.11.0'

// Navigation
implementation 'androidx.navigation:navigation-fragment-ktx:2.7.5'
implementation 'androidx.navigation:navigation-ui-ktx:2.7.5'

// Room Database
implementation 'androidx.room:room-runtime:2.6.1'
implementation 'androidx.room:room-ktx:2.6.1'

// Hilt DI
implementation 'com.google.dagger:hilt-android:2.48'

// Work Manager
implementation 'androidx.work:work-runtime-ktx:2.9.0'
```

## Project Structure

```
app/src/main/java/com/remindme/app/
├── data/
│   ├── converter/          # Room type converters
│   ├── dao/               # Database access objects
│   ├── database/          # Room database setup
│   ├── model/             # Data models
│   └── repository/        # Repository layer
├── di/                    # Dependency injection
├── receiver/              # Broadcast receivers
├── service/               # Background services
└── ui/                    # User interface
    ├── create/            # Create reminder screen
    ├── calendar/          # Calendar screen
    └── progress/          # Progress screen
```

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Java 21 JDK
- Android SDK 34
- Gradle 8.0+

### Installation
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Build and run on device/emulator

### Permissions Required
- `SCHEDULE_EXACT_ALARM` - For precise reminder scheduling
- `POST_NOTIFICATIONS` - For reminder notifications
- `VIBRATE` - For notification vibration

## Database Schema

### Reminders Table
```sql
CREATE TABLE reminders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT,
    reminderTime INTEGER NOT NULL,
    isCompleted INTEGER DEFAULT 0,
    createdAt INTEGER NOT NULL,
    completedAt INTEGER
);
```

### User Progress Table
```sql
CREATE TABLE user_progress (
    id INTEGER PRIMARY KEY DEFAULT 1,
    experience INTEGER DEFAULT 0,
    level INTEGER DEFAULT 1,
    remindersCompleted INTEGER DEFAULT 0
);
```

## Features in Detail

### Notification System
- Uses `AlarmManager` for precise timing
- `BroadcastReceiver` handles notification triggers
- Material Design notification styling
- Tap to open app functionality

### Data Persistence
- Room database with Kotlin coroutines
- LiveData for reactive UI updates
- Type converters for Date objects
- Automatic database migrations

### UI/UX
- Material Design 3 components
- Dark/light theme support
- Responsive layouts
- Smooth animations
- Accessibility features

## Future Enhancements

- [ ] Recurring reminders
- [ ] Categories/tags for reminders
- [ ] Cloud sync
- [ ] Widget support
- [ ] Custom notification sounds
- [ ] Reminder sharing
- [ ] Advanced statistics
- [ ] Achievement system

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support, please open an issue on GitHub or contact the development team. 