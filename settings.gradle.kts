include(":src:en:kenganmanga")
project(":src:en:kenganmanga").projectDir = file("src/en/kenganmanga")

// Ce script va chercher automatiquement toutes les librairies dans le dossier lib
File(rootDir, "lib").listFiles()?.forEach {
    if (it.isDirectory && (File(it, "build.gradle.kts").exists() || File(it, "build.gradle").exists())) {
        include(":lib:${it.name}")
    }
}
