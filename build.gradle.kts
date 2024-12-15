import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    val kotlinVersion = "2.1.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("org.sirekanyan.version-checker") version "1.0.13"
    application
}

group = "com.sirekanyan"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal {
        content {
            includeModule("org.sirekanyan", "telegram-bots")
        }
    }
}

dependencies {
    implementation("org.sirekanyan:telegram-bots:8.0.0")
    implementation("org.telegram:telegrambots-client:8.0.0")
    implementation("io.ktor:ktor-client-cio:3.0.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.57.0")
    implementation("org.jetbrains.lets-plot:lets-plot-common:4.5.2")
    implementation("org.jetbrains.lets-plot:lets-plot-image-export:4.5.2")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:4.9.2")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation("junit:junit:4.13.2")
}

application {
    mainClass.set("com.sirekanyan.andersrobot.Main")
    if (hasProperty("debug")) {
        applicationDefaultJvmArgs = listOf("-Ddebug")
    }
}

distributions {
    main {
        contents {
            from("bot.properties")
            from("data") { into("data") }
        }
    }
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
        allWarningsAsErrors = true
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
