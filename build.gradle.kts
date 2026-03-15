plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("kapt") version "2.3.10"
    id("com.gradleup.shadow") version "9.4.0"
}

group = "dev.akkih"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.minestom:minestom:2026.03.03-1.21.11")
    implementation("dev.hollowcube:polar:1.15.0")
    implementation("info.picocli:picocli:4.7.7")
    kapt("info.picocli:picocli-codegen:4.7.7")
}

kotlin {
    jvmToolchain(25)
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "dev.akkih.MainKt"
        }
    }

    shadowJar {
//        minimize()
        archiveClassifier.set("")
    }
}

kapt {
    arguments {
        arg("project", "${project.group}/${project.name}")
    }
}