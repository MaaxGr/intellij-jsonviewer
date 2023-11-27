plugins {
    id("org.jetbrains.intellij") version "1.16.0"
    id("java")
    kotlin("jvm") version "1.9.21"
}

group = "com.maaxgr.intellij"
version = "1.0.19"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2022.3")
    updateSinceUntilBuild.set(false)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.buildSearchableOptions {
    enabled = false
}

tasks.test {
    useJUnit()

    maxHeapSize = "1G"
}

buildscript {
    repositories {
        maven("https://www.jetbrains.com/intellij-repository/releases")
    }
}
