import nl.javadude.gradle.plugins.license.LicenseMetadata
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STARTED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.unbrokendome.gradle.plugins.testsets.TestSetsPlugin

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.plugin.kotlin)
        classpath(libs.plugin.reckon)
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    kotlin("plugin.spring") version libs.versions.kotlin.get() apply false
    id("scipamato-collect-sarif")
    alias(libs.plugins.springBoot).apply(false)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.lombok)
    idea
    jacoco
    alias(libs.plugins.testSets)
    alias(libs.plugins.detekt)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.reckon)
    alias(libs.plugins.licenseReport)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
}

extra["spring.cloudVersion"] = libs.versions.springCloud.get()
extra["jooq.version"] = libs.versions.jooq.get()
extra["flyway.version"] = libs.versions.flyway.get()

dependencyManagement {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("spring.cloudVersion")}")
    }
}

val testModuleDirs = setOf("common/common-test", "common/common-persistence-jooq-test")
val testModules = testModuleDirs.map { it.substringAfter("/") }
val testPackages = testModuleDirs.map { "$it/**/*" }
val generatedPackages: Set<String> = setOf(
    "**/ch/difty/scipamato/core/db/**",
    "**/ch/difty/scipamato/core/pubmed/api/**",
    "**/ch/difty/scipamato/publ/db/**"
)

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

val jacocoTestReportFile = "$buildDir/reports/jacoco/test/jacocoTestReport.xml"
val jacocoTestPattern = "**/build/jacoco/*.exec"
val genPkg = generatedPackages.joinToString(",")

sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.projectKey", "ursjoss_${project.name}")
        property("sonar.organization", "ursjoss-github")
        property("sonar.exclusions", "**/ch/difty/scipamato/publ/web/themes/markup/html/publ/**/*,$genPkg")
        property("sonar.coverage.exclusions", (generatedPackages + testPackages).joinToString(","))
        property("sonar.coverage.jacoco.xmlReportPaths", jacocoTestReportFile)
        property("sonar.kotlin.detekt.reportPaths", "$buildDir/reports/detekt/detekt.xml")
    }
}

reckon {
    stages("rc", "final")
    setScopeCalc(calcScopeFromProp().or(calcScopeFromCommitMessages()))
    setStageCalc(calcStageFromProp())
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
    apply<TestSetsPlugin>()
    apply<JacocoPlugin>()

    testSets {
        register("integrationTest") {
            dirName = "integration-test"
        }
        register("adhocTest") {
            dirName = "adhoc-test"
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
        testImplementation(rootProject.libs.kotest.framework.api)
        testImplementation(rootProject.libs.kotest.property)
        testImplementation(rootProject.libs.kluent) {
            exclude("org.mockito", "mockito-core")
            exclude("com.nhaarman.mockitokotlin2", "mockito-kotlin")
        }
        testImplementation(rootProject.libs.mockk)
        testImplementation(rootProject.libs.springMockk)

        testRuntimeOnly(rootProject.libs.kotest.runner.junit5)
    }

    val kotlinVersion = rootProject.libs.versions.kotlin.get()
    val kotlinApiLangVersion = kotlinVersion.subSequence(0, 3).toString()
    val jvmTargetVersion = rootProject.libs.versions.java.get()
    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            sourceCompatibility = jvmTargetVersion
            targetCompatibility = jvmTargetVersion
        }
        withType<KotlinCompile> {
            kotlinOptions {
                apiVersion = kotlinApiLangVersion
                languageVersion = kotlinApiLangVersion
                jvmTarget = jvmTargetVersion
                freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")
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
            useJUnitPlatform {
                includeEngines("junit-jupiter", "kotest")
            }
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
        val integrationTest by existing {
            description = "Runs the integration tests."
            dependsOn(test)
        }
        named("check") {
            dependsOn(integrationTest)
        }

        register("version") {
            doLast {
                println(project.version)
            }
        }
    }
}

tasks {
    val jvmTargetVersion = libs.versions.java.get()
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = jvmTargetVersion
        }
    }
}

downloadLicenses {
    dependencyConfiguration = "runtimeClasspath"
    includeProjectDependencies = false

    val apacheLicense = LicenseMetadata("Apache License 2.0", "https://spdx.org/licenses/Apache-2.0.html")
    val bsd1License = LicenseMetadata("BSD 1-Clause License", "https://spdx.org/licenses/BSD-1-Clause.html")
    val bsd2License = LicenseMetadata("BSD 2-Clause \"Simplified\" License", "https://spdx.org/licenses/BSD-2-Clause.html")
    val bsd3License = LicenseMetadata("BSD 3-Clause \"New\" or \"Revised\" License", "https://spdx.org/licenses/BSD-3-Clause.html")
    val cc0License = LicenseMetadata("Creative Commons Zero v1.0 Universal", "https://spdx.org/licenses/CC0-1.0.html")
    val ccby30License = LicenseMetadata("Creative Commons Attribution 3.0 Unported", "https://spdx.org/licenses/CC-BY-3.0.html")
    val cddl11License = LicenseMetadata("Common Development and Distribution License 1.1", "https://spdx.org/licenses/CDDL-1.1.html")
    val edlLicense = LicenseMetadata("Eclipse Distribution License - v 1.0", "https://www.eclipse.org/org/documents/edl-v10.php")
    val epl1License = LicenseMetadata("Eclipse Public License 1.0", "https://spdx.org/licenses/EPL-1.0.html")
    val epl2License = LicenseMetadata("Eclipse Public License 2.0", "https://spdx.org/licenses/EPL-2.0.html")
    val glpl21License = LicenseMetadata("GNU Lesser General Public License v2.1 only", "https://spdx.org/licenses/LGPL-2.1-only.html")
    val gpl2wcpeLicense = LicenseMetadata("GNU General Public License v2.0 w/Classpath exception", "https://spdx.org/licenses/GPL-2.0-with-classpath-exception.html")
    val gpl3onlyLicense = LicenseMetadata("GNU General Public License v3.0 only", "https://spdx.org/licenses/GPL-3.0-only.html")
    val mitLicense = LicenseMetadata("MIT License", "https://spdx.org/licenses/MIT.html")
    val mpl11License = LicenseMetadata("Mozilla Public License 1.1", "https://spdx.org/licenses/MPL-1.1.html")

    licenses = mapOf(
        "com.google.elemental2:elemental2-core:1.1.0" to apacheLicense,
    )

    aliases = mapOf(
        apacheLicense to listOf(
            "ASF 2.0",
            "Apache 2",
            "Apache 2.0",
            "Apache License 2.0",
            "Apache License Version 2.0",
            "Apache License, Version 2.0",
            "Apache License, version 2.0",
            "Apache-2.0",
            "The Apache License, Version 2.0",
            "The Apache Software License, Version 2.0",
        ),
        bsd1License to listOf(
            "BSD 1-Clause License",
            "BSD licence",
            "BSD-1-Clause",
        ),
        bsd2License to listOf(
            "BSD 2-Clause \"Simplified\" Licensee",
            "BSD-2-Clause",
            "The BSD License",
        ),
        bsd3License to listOf(
            "BSD 3-Clause \"New\" or \"Revised\" License",
            "BSD License 3",
            "BSD-3-Clause",
        ),
        cc0License to listOf(
            "CC0",
            "CC0-1.0",
            "Creative Commons Zero v1.0 Universal",
            "Public Domain, per Creative Commons CC0",
        ),
        ccby30License to listOf(
            "CC BY 3.0",
            "CC-BY-3.0",
            "Creative Commons Attribution 3.0 Unported",
        ),
        cddl11License to listOf(
            "CDDL 1.1",
            "CDDL-1.1",
            "CDDL/GPLv2+CE",
            "Common Development and Distribution License 1.1",
        ),
        edlLicense to listOf(
            "EDL 1.0",
            "Eclipse Distribution License - v 1.0",
        ),
        epl1License to listOf(
            "EPL-1.0",
            "Eclipse Public License (EPL)",
            "Eclipse Public License - v 1.0",
            "Eclipse Public License 1.0",
        ),
        epl2License to listOf(
            "EPL 2.0",
            "EPL-2.0",
            "Eclipse Public License - v 2.0",
            "Eclipse Public License 2.0",
            "Eclipse Public License v2.0",
        ),
        gpl2wcpeLicense to listOf(
            "GNU General Public License v2.0 w/Classpath exception",
            "GNU General Public License, version 2 with the GNU Classpath Exception",
            "GPL-2.0-with-classpath-exception",
            "GPL2 w/ CPE",
        ),
        glpl21License to listOf(
            "GNU Lesser General Public Licence",
            "GNU Lesser General Public License v2.1 only",
            "GNU Lesser General Public License",
            "LGPL-2.1-onlyy",
        ),
        gpl3onlyLicense to listOf(
            "GNU General Public License v3.0 only",
            "GPL-3.0-only",
            "Gnu General Public License, Version 3",
        ),
        mitLicense to listOf(
            "MIT License",
            "MIT",
            "The MIT License",
        ),
        mpl11License to listOf(
            "MPL-1.1",
            "Mozilla Public License 1.1",
            "Mozilla Public License",
        ),
    )

}

fun Project.isWebProject() = path.endsWith("web")
