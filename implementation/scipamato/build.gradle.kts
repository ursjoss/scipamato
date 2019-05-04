import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.31"
}

java {
    version = JavaVersion.VERSION_11
}

allprojects {
    group = "ch.difty"
    version = "1.1.7-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    if (!path.endsWith("-web"))
        apply(plugin = "java-library")


    dependencies {
        compileOnly(Lib.lombok())
        annotationProcessor(Lib.lombok())

        implementation(Lib.slf4j())
        implementation(Lib.logback())
    }

    configurations.all {
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
        withType<Test> {
            @Suppress("UnstableApiUsage")
            useJUnitPlatform()
        }
    }
}