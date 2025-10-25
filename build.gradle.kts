import com.github.jk1.license.render.ReportRenderer
import com.github.jk1.license.render.InventoryHtmlReportRenderer
import com.github.jk1.license.filter.DependencyFilter
import com.github.jk1.license.filter.LicenseBundleNormalizer
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STARTED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.sonarqube.gradle.SonarQubePlugin
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.plugin.kotlin)
    }
}

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    kotlin("plugin.spring") version libs.versions.kotlin.get() apply false
    id("scipamato-collect-sarif")
    alias(libs.plugins.springBoot).apply(false)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.lombok)
    idea
    jacoco
    alias(libs.plugins.kotest)
    alias(libs.plugins.detekt)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.licenseReport)
}

extra["spring.cloudVersion"] = libs.versions.springCloud.get()

dependencyManagement {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("spring.cloudVersion")}")
    }
}

val testModuleDirs = setOf("common/common-test", "common/common-persistence-jooq-test")
val testPackages = testModuleDirs.map { "$it/**/*" }
val generatedPackages: Set<String> = setOf(
    "**/ch/difty/scipamato/core/db/**",
    "**/ch/difty/scipamato/core/pubmed/api/**",
    "**/ch/difty/scipamato/publ/db/**"
)

testing {
    suites {
        @Suppress("UnstableApiUsage", "unused")
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

val jacocoTestPattern = "**/build/jacoco/*.exec"
val genPkg = generatedPackages.joinToString(",")

sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.projectKey", "ursjoss_${project.name}")
        property("sonar.organization", "ursjoss-github")
        property("sonar.exclusions", "**/ch/difty/scipamato/publ/web/themes/markup/html/publ/**/*,$genPkg")
        property("sonar.coverage.exclusions", (generatedPackages + testPackages).joinToString(","))
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    }
}

subprojects {
    apply<SpringBootPlugin>()
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply<ScipamatoDetektPlugin>()
    apply<CollectSarifPlugin>()
    apply<ScipamatoJacocoPlugin>()
    apply<JavaPlugin>()
    apply<IdeaPlugin>()
    apply<JacocoPlugin>()
    apply<SonarQubePlugin>()

    sonarqube {
        properties {
            property(
                "sonar.kotlin.detekt.reportPaths",
                layout.buildDirectory.get().asFile.resolve("reports/detekt/detekt.xml")
            )
            property(
                "sonar.coverage.jacoco.xmlReportPaths",
                layout.buildDirectory.get().asFile.resolve("reports/jacoco/test/jacocoTestReport.xml")
            )
        }
    }

    testing {
        suites {
            @Suppress("UnstableApiUsage", "unused")
            val test by getting(JvmTestSuite::class) {
                useJUnitJupiter()
            }
        }
    }

    // Breaks running the project from the IntelliJ Run Dashboard - disabling for now
    // idea {
    //     module {
    //         // https://structure101.com/2018/12/01/structure101-workspace-intellij-idea-and-gradle/
    //         outputDir = file("$buildDir/classes/java/main")
    //         testOutputDir = file("$buildDir/classes/java/test")
    //         inheritOutputDirs = false
    //     }
    // }

    if (!isWebProject()) {
        apply(plugin = "java-library")
    }

    dependencies {
        implementation(rootProject.libs.kotlin.reflect)

        compileOnly(rootProject.libs.lombok)
        annotationProcessor(rootProject.libs.lombok)

        api(rootProject.libs.slf4j.api)
        implementation(rootProject.libs.kotlinLogging)
        runtimeOnly(rootProject.libs.logback.core)

        compileOnly(rootProject.libs.jsr305)

        testImplementation(rootProject.libs.spring.boot.starter.test) {
            exclude("junit", "junit")
            exclude("org.skyscreamer", "jsonassert")
            exclude("org.mockito", "mockito-core")
            exclude("org.mockito", "mockito-junit-jupiter")
            exclude("org.hamcrest", "hamcrest")
            exclude("org.assertj", "assertj-core")
        }
        testImplementation(rootProject.libs.kotest.framework.engine)
        testImplementation(rootProject.libs.kotest.property)
        testImplementation(rootProject.libs.kluent) {
            exclude("org.mockito", "mockito-core")
            exclude("com.nhaarman.mockitokotlin2", "mockito-kotlin")
        }
        testImplementation(rootProject.libs.mockk)
        testImplementation(rootProject.libs.springMockk)

        testRuntimeOnly(rootProject.libs.kotest.runner.junit5)
    }

    tasks {
        named("compileKotlin", org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask::class.java) {
            compilerOptions {
                freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            }
        }
        val deleteOutFolderTask by registering(Delete::class) {
            delete("out")
        }
        named("clean") {
            dependsOn(deleteOutFolderTask)
        }
        withType<Test> {
            maxHeapSize = "2g"
            failFast = true
            testLogging {
                events = setOf(STARTED, FAILED, PASSED, SKIPPED)
                showStackTraces = true
                exceptionFormat = TestExceptionFormat.FULL
            }
        }
        withType<Jar> {
            enabled = !isWebProject()
        }
        withType<BootJar> {
            enabled = false
        }

        register("version") {
            doLast {
                println(project.version)
            }
        }

        register("printSourceSetInformation") {
            doLast {
                sourceSets.forEach { srcSet ->
                    println("[" + srcSet.name + "]")
                    println("-->Source directories: " + srcSet.allJava.srcDirs)
                    println("-->Output directories: " + srcSet.output.classesDirs.files)
                    println("-->Compile classpath:")
//                    srcSet.compileClasspath.files.forEach { println("  " + it.path) }
                    println("")
                }
            }
        }
    }

    // see https://github.com/detekt/detekt/issues/6198
    configurations.matching { it.name == "detekt" }.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                useVersion(libs.versions.detektKotlinVersion.get())
            }
        }
    }
}

tasks.named("compileKotlin", org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask::class.java) {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

licenseReport {
    renderers = arrayOf<ReportRenderer>(InventoryHtmlReportRenderer("report.html", "Backend"))
    filters = arrayOf<DependencyFilter>(LicenseBundleNormalizer())
}

fun Project.isWebProject() = path.endsWith("web")
