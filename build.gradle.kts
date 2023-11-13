// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.45")
    }
}


plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.45" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
