# AudiOasis
LBYCPA2 Project

# AudiOasis

AudiOasis is a simple desktop music player built with JavaFX. It lets you load songs from your filesystem, manage a playback queue, create playlists, and control playback using an intuitive UI.
---

## Features

### Playback & Queue

- Add **individual songs** via a file chooser.
- Add **entire folders** of audio files at once.
- **Play / Pause** the current track.
- **Next** and **Back** controls:
  - `Next` moves forward in the queue.
  - `Back` replays previous songs using a history stack.
- **Repeat mode**:
  - When enabled, finished songs are moved to the end of the queue.
- **Shuffle queue**:
  - Randomizes the current queue order.
- Shows **“Now Playing”** track title.
- Displays **album art** if available in the audio metadata.

### Queue & Sorting

- Visual **queue display** using a `ListView<Song>`.
- **Add** and **remove** songs from the queue.
- **Clear** the entire queue.
- Sort queue by:
  - **Title** (ascending / descending toggle).
  - **Artist** (ascending / descending toggle).
- Sorting uses a custom `MergeSort` implementation with `Comparator<Song>`.

### Playlists

- Create **named playlists**.
- Add selected songs from the queue to a playlist.
- Remove playlists.
- Load a playlist into the **active queue**:
  - Replaces current queue with the playlist’s songs.
  - Starts playback from the first track in that playlist.

### Audio Controls

- **Seek slider**:
  - Shows track progress as a percentage.
  - Drag to seek within the current track.
- **Volume slider**:
  - Adjust playback volume in real time.
- Automatic binding between UI controls and the `MediaPlayer`.

### Supported Formats

Based on the file filters and checks in `MainController`, AudiOasis supports at least:

- `.mp3`
- `.wav`
- `.aac`
- `.m4a`
- `.flac` (recognized when adding from folders)

Support ultimately depends on your JavaFX media capabilities on your platform.

---

## Technology Stack

- **Language:** Java
- **UI Framework:** JavaFX
- **Media:** `javafx.scene.media.Media` and `MediaPlayer`
- **Collections & Data Structures:**
  - `Deque<Song>` for the playback queue
  - `Stack<Song>` for back navigation
  - `ObservableList` for UI binding in JavaFX

---

## Project Structure (Partial)

Key classes referenced by `MainController`:

- `com.project.MainController`
  - Handles all UI events and playback control.
- `com.project.Song`
  - Wraps a `Media` object and exposes metadata such as title and artist.
- `com.project.Playlist`
  - Holds a list of `Song` objects and a title.
- `com.project.MergeSort`
  - Custom merge sort implementation used to sort arrays of `Song`.

> If these files are not yet documented, consider adding Javadoc comments for public methods and fields.

---

## Getting Started

### Prerequisites

- **Java**: JDK 17 or higher (adjust to your project’s actual target).
- **JavaFX**: SDK matching your JDK version.
- A JavaFX-capable build setup (e.g., Maven, Gradle, or manual module/classpath configuration).

### Cloning the Repository

```bash
git clone https://github.com/Endomou/AudiOasis.git
cd AudiOasis
```

### Building & Running

How you run AudiOasis will depend on your build system. Below are generic options you can adapt.

#### Using Gradle (example)

```bash
./gradlew run
```

Make sure your `build.gradle` includes JavaFX dependencies and the correct main class.

#### Using Maven (example)

```bash
mvn clean javafx:run
```

Ensure `pom.xml` is configured with the `javafx-maven-plugin` and the appropriate JavaFX modules.

#### Running Manually

If you are compiling manually:

1. Add JavaFX modules to the module path:
   - `javafx-base`
   - `javafx-controls`
   - `javafx-graphics`
   - `javafx-media`
   - `javafx-fxml`
2. Run your `Main` class that loads the FXML associated with `MainController`.

Example (Linux/macOS layout):

```bash
javac --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml,javafx.media -d out src/main/java/com/project/*.java

java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml,javafx.media -cp out com.project.Main
```

Replace `com.project.Main` and paths with the actual entry point and locations in your project.

---

## How to Use

1. **Launch** the application.
2. **Add songs**:
   - Click the button wired to `addSong()` to choose a single file.
   - Or click the button wired to `addFolder()` to add all audio files in a directory.
3. **Play music**:
   - Use the Play/Pause button (triggering `playMusic()`).
4. **Navigate**:
   - Use Next (`nextTrack()`) and Back (`backTrack()`) controls.
5. **Repeat / Shuffle**:
   - Toggle `Repeat` via the control bound to `setRepeat()`.
   - Shuffle queue with the button bound to `shuffleQueue()`.
6. **Volume & Seek**:
   - Adjust volume with the volume slider.
   - Drag the seek slider to move to a different part of the current track.
7. **Playlists**:
   - Create playlists via `createPlaylist()`.
   - Add a selected song to a selected playlist (`addToPlaylist()`).
   - Load a playlist into the queue (`playlistToQueue()`).
   - Delete playlists (`deletePlaylist()`).

---

## Error Handling & Alerts

AudiOasis uses JavaFX `Alert` dialogs to notify the user about common issues:

- Trying to play with an **empty queue**.
- Sorting with **no or only one** song in the queue.
- Attempting actions with **no song** or **no playlist** selected.
- Attempting to shuffle an **empty queue**.
- Loading a playlist with **no songs**.

These alerts help guide the user to correct actions.

---

## Known Limitations / Possible Improvements

- **Null checks for metadata**: Some metadata (e.g., title, image) may be absent; at present, the code assumes certain fields exist.
- **Volume range**: `volumeSlider` uses a raw value; mapping it to a `0.0–1.0` MediaPlayer range and adding labels could improve UX.
- **File type detection**: `isAudio()` currently checks extensions via `String.contains`; switching to `endsWith` and case-insensitive checks would be more robust.
- **State persistence**: Queue and playlists are in-memory only. Adding **save/load** functionality would persist user data.
- **Keyboard shortcuts**: Adding hotkeys for Play/Pause, Next, Back, etc., would enhance usability.

---

## Contributing

Contributions are welcome. Typical contribution workflow:

1. Fork the repository.
2. Create a feature branch:
   ```bash
   git checkout -p -b feature/my-improvement
   ```
3. Make your changes.
4. Add tests or manual test notes where appropriate.
5. Open a Pull Request describing:
   - What you changed.
   - How you tested it.

---


## Acknowledgements

- [JavaFX](https://openjfx.io/) for UI and media playback.
- All contributors and libraries that make this project possible.
