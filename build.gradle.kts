plugins {
    id("org.jetbrains.intellij") version "1.5.3"
    java
    kotlin("jvm") version "1.5.10"
}

group = "com.maaxgr.intellij"
version = "1.0.12"

repositories {
    mavenCentral()
    jcenter()

}

dependencies {
    testImplementation("junit", "junit", "4.12")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2021.3")
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
