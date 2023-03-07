# Note app

## Screenshots
![screenshot-1](https://raw.githubusercontent.com/actiangent/note-app/main/screenshots/screenshot-1.png)
![screenshot-2](https://raw.githubusercontent.com/actiangent/note-app/main/screenshots/screenshot-2.png)

## Project Overview

A simple note taking app that allows users to create and manage notes. 
Inspired by [android/architecture-samples](https://github.com/android/architecture-samples) to 
showcase good android app architecture as described [here](https://developer.android.com/topic/architecture).

### Architecture

* Single-activity architecture, using [Navigation Component](https://developer.android.com/guide/navigation).
* Encapsulates UI states and business logic using [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* Reactive UIs using [Flow](https://developer.android.com/kotlin/flow) and [coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for asynchronous operations.
* A data layer with a repository.
* Database using [Room](https://developer.android.com/jetpack/androidx/releases/room) for SQLite wrapper.
* A collection of unit, integration and end-to-end **tests**.
* Dependency injection using [Hilt](https://developer.android.com/training/dependency-injection/hilt-android).

### Variation
In its different branches you will find the identical app implemented using Views (xml) and
[Jetpack Compose](https://developer.android.com/jetpack/compose)

| Branch                                                         | Description                               |
|----------------------------------------------------------------|-------------------------------------------|
| [main](https://github.com/actiangent/note-app/tree/main)       | note app implemented with views (xml)     |
| [compose](https://github.com/actiangent/note-app/tree/compose) | note app implemented with Jetpack Compose |

## Opening Project

To open one of the note app two variations

Clone this repository: 
```
git clone git@github.com:actiangent/note-app.git
```

Checkout one of the branches. For example, to checkout the Compose implementation:
```
git checkout compose
```

Finally, open the `note-app` directory in Android Studio.
