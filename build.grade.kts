plugins {
    // Ici on prépare les outils sans les activer tout de suite
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        // Indispensable pour les dépendances Tachiyomi
        maven { url = uri("https://jitpack.io") }
    }
}
