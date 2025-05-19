plugins {
    kotlin("jvm") version "2.1.20-RC2"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "io.github.surang_volkov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(23))
}

tasks.jar {
    destinationDirectory = file("F:\\works\\games\\servers\\plugin tester\\plugins")
    manifest {
        attributes["Main-Class"] = "io.github.surang_volkov.minecivilization.MineCivilization"
        attributes["paperweight-mappings-namespace"] = "mojang"
    }
}

// if you have shadowJar configured
tasks.shadowJar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }
}

tasks.build {
    dependsOn("jar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}
