plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("kapt") version "2.3.10"
    id("com.gradleup.shadow") version "9.4.0"
}

group = "dev.akkih"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.minestom:minestom:2026.07.22-26.2")
    implementation("dev.hollowcube:polar:1.16.0")
    implementation("info.picocli:picocli:4.7.7")
    kapt("info.picocli:picocli-codegen:4.7.7")
}

kotlin {
    jvmToolchain(25)
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        manifest { attributes["Main-Class"] = "dev.akkih.MainKt" }

        mergeServiceFiles()

        minimize {
            exclude(dependency("net.minestom:minestom:.*"))
            exclude(dependency("net.kyori:.*"))
            exclude(dependency("org.slf4j:.*"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}

kapt {
    arguments {
        arg("project", "${project.group}/${project.name}")
    }
}