# PersistenceDemo (Java + Room)

Petit projet Android pour la démonstration du cours « Bases de données embarquées et persistance ».

## Ouvrir dans Android Studio

Le code de l'application est entièrement en Java. Android Studio utilise Gradle pour construire un APK et télécharge les dépendances depuis les dépôts Maven (`google()` et `mavenCentral()`).

1. Dans Android Studio, créer un projet **Empty Views Activity** en **Java**, package `com.example.persistencedemo`, `minSdk 26` ou supérieur. Laisser Android Studio créer son Gradle Wrapper.
2. Remplacer les fichiers `app/src/main` du projet créé par ceux de cette archive. Conserver les fichiers du Gradle Wrapper créés par Android Studio.
3. Dans `app/build.gradle.kts`, reprendre les dépendances et `annotationProcessor` du fichier fourni. Si le projet généré utilise `app/build.gradle` (Groovy), les équivalents sont dans `DEPENDENCIES_GROOVY.txt`.
4. Cliquer **Sync Project with Gradle Files**, puis lancer l'application sur un émulateur **API 26+**.

Le fichier `app/build.gradle.kts` fourni est un exemple complet pour un projet dont le plugin Android `com.android.application` est déclaré au niveau racine. Les versions choisies sont fixées pour ce kit ; le projet généré par votre Android Studio peut utiliser un plugin Android plus récent.

## Démo en classe (3 à 4 minutes)

1. Lancer l'application, ajouter `préparer le TP`.
2. Ouvrir **View > Tool Windows > App Inspection > Database Inspector**. Sélectionner le processus `com.example.persistencedemo`, puis `tasks.db` et `tasks`.
3. Lire la ligne (`title`, `completed=0`). Cocher la tâche dans l'application, puis regarder `completed=1` dans la table. Rafraîchir la vue de la table si nécessaire.
4. Dans l'émulateur, ouvrir **Settings > Apps > PersistenceDemo > Force stop** ; relancer l'application. La tâche réapparaît. Ne pas choisir **Clear storage**.
5. Facultatif : dans Logcat, filtrer par `PersistenceDemo` pour lire `inserted id=...` ou `completed id=...`.

**Question à poser à la classe :** quel constat prouve réellement la persistance ? Le log seul montre le passage dans le code. La ligne dans SQLite après relance fournit la preuve utile.

## Structure

- `Task.java` : une ligne de la table.
- `TaskDao.java` : requête observable, insertion et mise à jour.
- `AppDb.java` : configuration Room et migration 1 → 2.
- `MainActivity.java` : interface simple, observation LiveData et écritures sur un ExecutorService.

La migration figure dans le code, mais l'installation fraîche crée directement la version 2. Pour démontrer la migration en direct, il faudrait d'abord installer une version 1 qui crée l'ancien schéma, puis mettre à jour vers cette version 2 sans désinstaller l'application. Pour la présentation courte, montrer le code de migration sur la slide suffit.

Le code a été vérifié par inspection statique ; l'APK n'a pas été compilé dans cet environnement Android Studio. Faites un essai complet sur votre émulateur avant le cours.

## Sources

- https://developer.android.com/training/data-storage/room/v2
- https://developer.android.com/studio/inspect/database
- https://developer.android.com/training/data-storage/room/migrating-db-versions
