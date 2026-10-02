# MiniTodo

## Objective

A small university demonstration of UI state restoration, JSON file export,
CRUD, Room, reactive updates with LiveData, and SQLite persistence.
The interface and project documentation are in English.
Application code uses Java and XML layouts.

## Stack

Android (minimum API 26), Java 17, Room 2.8.5, SQLite, LiveData and ViewModel
2.9.0, RecyclerView 1.4.0, Activity 1.10.1, and native Android widgets.
The existing Gradle and Android plugin versions are retained.
No server or network permission is needed.

## Architecture

```text
UI: MainActivity + TaskAdapter
↓
TaskViewModel
↓
TaskRepository
↓
TaskDao
↓
Room
↓
SQLite / tasks.db
```

The Repository retains the LiveData returned by the DAO. The ViewModel exposes
that same object, and MainActivity observes it with its lifecycle. When the table
changes, Room reruns the query. The observer submits the list to ListAdapter
and calculates the counter without an additional query.

Writes run on a single-thread ExecutorService in the Repository. This queue is
shared for the lifetime of the process and survives Activity recreation.
Room performs observable reads in the background.
Updates use a copy of Task with the same id, allowing DiffUtil to compare old and
new values without mutating the displayed list.
The CheckBox listener is detached before setChecked() and reattached afterward.

## CRUD

| Action | DAO | Interface |
| --- | --- | --- |
| Create | @Insert | Enter a title and press ADD |
| Read | @Query | LiveData list, ordered by descending id |
| Update | @Update | Check/uncheck or edit the title |
| Delete | @Delete | Confirm DELETE |

Titles are trimmed with trim(), and blank titles are rejected.
Canceling a dialog leaves the database unchanged.

## Project structure

Under `app/src/main/java/com/example/persistencedemo/`:

- `data/Task.java`: entity and copy method for updates.
- `data/TaskDao.java`: the four CRUD operations.
- `data/AppDatabase.java`: Room singleton, schema version 1, applicationContext.
- `data/TaskRepository.java`: DAO access, LiveData, and writes on ExecutorService.
- `viewmodel/TaskViewModel.java`: title validation and delegation to the Repository.
- `ui/MainActivity.java`: LiveData observation, counter, task creation, and dialogs.
- `ui/TaskAdapter.java`: ListAdapter, DiffUtil, and row callbacks.

The three layouts are in `app/src/main/res/layout/`; English strings are in
`app/src/main/res/values/strings.xml`. The Room annotationProcessor exports the
schema to `app/schemas/com.example.persistencedemo.data.AppDatabase/1.json`.

## Database schema

The `tasks` table has exactly three columns:

| Column | Java | SQLite |
| --- | --- | --- |
| id | long, auto-generated primary key | INTEGER NOT NULL, PRIMARY KEY AUTOINCREMENT |
| title | non-null String | TEXT NOT NULL |
| completed | boolean | INTEGER NOT NULL (0 or 1) |

`AppDatabase.getInstance(context)` builds Room with the filename `tasks.db`.
The file is opened or created on first access in private application storage:
`/data/user/0/com.example.persistencedemo/databases/tasks.db`.
Once a write finishes, its data survives Activity recreation, closing the app,
and force-stop. Uninstalling the app or clearing storage deletes the data.

## Build and run

Open **this existing folder** in Android Studio, sync Gradle, and run the app
configuration on an API 26+ device.
For Database Inspector, use an **API 26+** emulator or device with system SQLite
and the **debug** build variant.

```sh
./gradlew assembleDebug testDebugUnitTest lintDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`.
The Java version running Gradle must support the existing wrapper;
the Android plugin requires Java 17 or later.

## Demo procedure

### UI state and JSON file persistence

1. Type "Finish the presentation" in the main **New task** field without pressing ADD.
2. Rotate the device to landscape, then back to portrait: the unfinished text remains.
   Android saves/restores the EditText view state using its stable `taskInput` ID;
   no custom saved-state code is needed. The draft is not a row in Room and is
   not guaranteed to survive force-stop or a fresh launch.
3. Press ADD and wait for the task to appear. Add another task and check one task.
4. Press **EXPORT JSON**. In Android's document picker, choose **Downloads**, keep
   `tasks.json` (or choose another name), and press **SAVE**.
5. Wait for "Tasks exported to the selected file." Open the chosen file using
   the device's Files app and a text viewer, or pull it onto your computer:
   `adb pull /sdcard/Download/tasks.json` (for the emulator's Downloads destination).
6. Show the JSON array: each task includes numeric `id`, string `title`, and
   boolean `completed`. Exporting an empty list produces `[]`.

The system picker uses `CreateDocument("application/json")`; the user selects
the location and filename. Export writes UTF-8, indented JSON through the
ContentResolver on a background thread, using the latest observed task list
when the picker returns. Unsaved input is excluded. The button becomes enabled
once Room supplies the first list. Canceling the picker does not export;
write failures show an error message. No storage permission is required.
The file is a snapshot: later database changes do not update it.
The existing Room layers, database write executor, and UML structure are unchanged.

### CRUD and SQLite persistence

1. Launch MiniTodo.
2. Add "Prepare the lab".
3. Add "Read the course notes"; observe the counter and task order.
4. Open **View > Tool Windows > App Inspection > Database Inspector**.
5. Select the `com.example.persistencedemo` process, open `tasks.db`,
   and select the `tasks` table.
6. Show `id`, `title`, and `completed`. Enable live updates or refresh the
   table after each action.
7. Check a task: `completed` becomes 1 and the counter changes.
   Uncheck it: the value returns to 0.
8. Edit its title and save: the id stays the same and title changes.
9. Try a blank title: validation appears and nothing is written.
10. Delete the other task after confirmation: its row disappears.
11. Rotate the device: the data and counter reappear.
12. Close and reopen MiniTodo: the task remains.
13. Wait until the update appears in the list, then select
    **Settings > Apps > MiniTodo > Force stop**.
14. Relaunch MiniTodo: the title and checkbox state remain saved.
    Reconnect Database Inspector to the new process if necessary.

## UML

PlantUML sources matching the implementation:

- [Classes](docs/uml/minitodo-class-diagram.puml)
- [Add task](docs/uml/sequence-add-task.puml)
- [Update task](docs/uml/sequence-update-task.puml)
- [Delete task](docs/uml/sequence-delete-task.puml)

The diagrams distinguish SQLite, Room, and LiveData notifications.
SQLite does not call the Activity directly.

If PlantUML is available:

```sh
plantuml -tsvg -o rendered docs/uml/*.puml
```

SVG and PNG exports of all four diagrams are available in
[docs/uml/rendered/](docs/uml/rendered/). They were generated using the PlantUML
JAR bundled with the VS Code PlantUML extension. The class diagram uses the
built-in Smetana layout engine, so Graphviz is not required.

In VS Code, press **Ctrl+Shift+P**, select **PlantUML: Export Workspace Diagrams**,
and choose **svg** or **png**. The extension's export directory is controlled by
the `plantuml.exportOutDir` setting. SVG is useful for sharp text when zooming;
PNG is useful for inserting into documents.

## Important note

No migration is needed: this rebuilt demonstration starts directly with its final
schema at **version 1**. If an older PersistenceDemo installation has a version 2
database, **uninstall the old app or clear its storage once** before launching
MiniTodo for the first time. This deletes the old data.
There is no migration or automatic destructive reset.

## Validation

For the JSON-export addition, `assembleDebug` and `lintDebug` passed with zero
lint errors (two existing SDK/dependency warnings). API 29 emulator verification
confirmed main-input restoration across landscape/portrait rotation, document
creation in Downloads, and exported JSON containing all three task fields.
Force-stop/relaunch retained the saved tasks and completion state.

- `assembleDebug` and `lintDebug` passed with zero lint errors. Four warnings
  concern only the target SDK and newer dependency versions.
- `testDebugUnitTest` ran: no existing unit test suite was present (NO-SOURCE).
- API 29 emulator checks covered creation, descending order, completion toggles,
  title editing, canceling and confirming deletion, blank-title rejection,
  rotation, APK updates, and force-stop.
- Reading the SQLite file **with its WAL** after stopping the process confirmed
  schema version 1 and persisted data.
- English interface checks covered buttons, dialogs, errors, accessibility
  descriptions, the empty state, and the counter.
- The old emulator database was preserved in the private directory
  `databases-before-minitodo-v2/` before creating the version 1 database.
