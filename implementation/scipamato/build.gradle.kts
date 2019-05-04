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

    dependencies {
        api("org.slf4j:slf4j-api:1.7.26")
        implementation("ch.qos.logback:logback-core:1.2.3")
        compileOnly("org.projectlombok:lombok:1.18.6")
        annotationProcessor("org.projectlombok:lombok:1.18.6")
        testCompile("org.springframework.boot:spring-boot-starter-test:2.1.4.RELEASE")
        testCompile("org.junit.jupiter:junit-jupiter:5.4.2")
        testCompile("org.junit.jupiter:junit-jupiter-params:5.4.2")
        testCompile("org.mockito:mockito-core:2.23.4")
        testCompile("org.mockito:mockito-junit-jupiter:2.23.4")
        testCompile("org.assertj:assertj-core:3.11.1")
        testCompile("nl.jqno.equalsverifier:equalsverifier:3.1.8")
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
    }
}