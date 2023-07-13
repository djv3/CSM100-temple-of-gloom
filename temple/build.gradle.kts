/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/8.1/userguide/building_java_projects.html
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    id("io.freefair.lombok") version "8.0.1"
    id("net.ltgt.errorprone") version "latest.release"
    id("com.diffplug.spotless") version "6.19.0"
}


repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("junit:junit:4.13.1")
    errorprone("com.google.errorprone:error_prone_core:latest.release")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(20))
    }
}

application {
    // Define the main class for the application.
    mainClass.set(project.findProperty("chooseMain").toString())
}

dependencies {
    testImplementation("junit:junit:4.13.1")
    testImplementation(platform("org.junit:junit-bom:5.9.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Mockito dependencies
    testImplementation("org.mockito:mockito-core:4.0.0")
}

tasks.test {
    useJUnitPlatform()
}
