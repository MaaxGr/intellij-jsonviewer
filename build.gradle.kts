plugins {
    id("org.jetbrains.intellij") version "0.4.18"
    java
    kotlin("jvm") version "1.3.72"
}

group = "com.maaxgr.intellij"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    testImplementation("junit", "junit", "4.12")
    implementation("org.koin", "koin-core", "2.1.5")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2020.1"
}
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnit()

    maxHeapSize = "1G"
}

tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    changeNotes("""
      Add change notes here.<br>
      <em>most HTML tags may be used</em>""")
}
