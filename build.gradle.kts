plugins {
    id("org.jetbrains.intellij") version "1.10.0"
    id("java")
    kotlin("jvm") version "1.7.21"
}

group = "com.maaxgr.intellij"
version = "1.0.14"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit", "junit", "4.12")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2022.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_17
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
