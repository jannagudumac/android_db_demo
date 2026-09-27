plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.persistencedemo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.persistencedemo"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.activity:activity:1.10.1")
    implementation("androidx.lifecycle:lifecycle-livedata:2.9.0")
    implementation("androidx.room:room-runtime:2.8.5")
    annotationProcessor("androidx.room:room-compiler:2.8.5")
}
