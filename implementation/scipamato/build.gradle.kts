import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    Lib.kotlinPlugin().run { kotlin(id) version version }
    java
    Lib.springBootPlugin().run { id(id) version version } apply false
    idea
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

    tasks {
        withType<BootJar> {
            enabled = false
        }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = Lib.springDependencyManagementPlugin())

    if (!path.isWebProject())
        apply(plugin = "java-library")

    dependencies {
        compileOnly(Lib.lombok())
        annotationProcessor(Lib.lombok())

        api(Lib.slf4j())
        runtimeOnly(Lib.logback())
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
        withType<Jar> {
            enabled = !path.isWebProject()
        }
    }
}

fun String.isWebProject() = endsWith("-web")