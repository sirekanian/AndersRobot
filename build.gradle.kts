plugins {
    val kotlinVersion = "1.4.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    application
}

group = "com.sirekanyan"
version = "0.1"

repositories {
    mavenCentral()
    jcenter {
        content {
            includeGroup("org.jetbrains.exposed")
        }
    }
}

dependencies {
    implementation("org.telegram:telegrambots:4.9.1")
    implementation("io.ktor:ktor-client-cio:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.28.1")
    implementation("org.postgresql:postgresql:42.2.18")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    testImplementation("junit:junit:4.13")
}

application {
    mainClassName = "com.sirekanyan.andersrobot.Main"
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    jar {
        manifest.attributes["Main-Class"] = "com.sirekanyan.andersrobot.Main"
        from(configurations.runtimeClasspath.get().map(::zipTree))
        doLast {
            File("bot").also { file ->
                file.createNewFile()
                file.setExecutable(true)
                file.printWriter().use { writer ->
                    writer.println("#!/usr/bin/env sh")
                    writer.println("java -jar ${archiveFileName.get()} \"$@\"")
                }
            }
        }
    }
}