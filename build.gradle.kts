plugins {
    id("org.jetbrains.intellij") version "0.4.21"
    java
    kotlin("jvm") version "1.3.72"
}

group = "com.maaxgr.intellij"
version = "1.0.7"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    testImplementation("junit", "junit", "4.12")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2020.3"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.buildSearchableOptions {
    enabled = false
}

tasks.test {
    useJUnit()

    maxHeapSize = "1G"
}