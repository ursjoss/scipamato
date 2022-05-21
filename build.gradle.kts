import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import nl.javadude.gradle.plugins.license.LicenseMetadata
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STARTED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.sonarqube.gradle.SonarQubeTask
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.unbrokendome.gradle.plugins.testsets.TestSetsPlugin

plugins {
    Lib.springBootPlugin().run { id(id) version version } apply false
    Lib.springDependencyManagementPlugin().run { id(id) version version }
    Lib.kotlinJvmPlugin().run { kotlin(id) version version }
    Lib.kotlinSpringPlugin().run { kotlin(id) version version } apply false
    Lib.lombokPlugin().run { id(id) version version }
    idea
    jacoco
    Lib.testSetsPlugin().run { id(id) version version }
    Lib.detektPlugin().run { id(id) version version }
    Lib.sonarqubePlugin().run { id(id) version version }
    Lib.reckonPlugin().run { id(id) version version }
    Lib.versionsPlugin().run { id(id) version version }
    Lib.licensePlugin("report").run { id(id) version version }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

extra["spring.cloudVersion"] = Lib.springCloudVersion
extra["jooq.version"] = Lib.jooqVersion
extra["flyway.version"] = Lib.flywayVersion

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

val jacocoTestReportFile = "$buildDir/reports/jacoco/test/jacocoTestReport.xml"
val jacocoTestPattern = "**/build/jacoco/*.exec"
val genPkg = generatedPackages.joinToString(",")

sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.projectKey", "ursjoss_scipamato")
        property("sonar.organization", "ursjoss-github")
        property("sonar.exclusions", "**/ch/difty/scipamato/publ/web/themes/markup/html/publ/**/*,$genPkg")
        property("sonar.coverage.exclusions", (generatedPackages + testPackages).joinToString(","))
        property("sonar.coverage.jacoco.xmlReportPaths", jacocoTestReportFile)
        property("sonar.kotlin.detekt.reportPaths", "build/reports/detekt/detekt.xml")
    }
}

reckon {
    scopeFromProp()
    snapshotFromProp()
}

allprojects {
    group = "ch.difty"

    repositories {
        mavenCentral()
        jcenter()
        maven { url = uri("https://jaspersoft.jfrog.io/jaspersoft/third-party-ce-artifacts") }
        maven { url = uri("https://repo.spring.io/milestone") }
    }
}

lombok {
    config.put("lombok.extern.findbugs.addSuppressFBWarnings", "false")
}

subprojects {
    apply<SpringBootPlugin>()
    apply(plugin = Lib.springDependencyManagementPlugin().id)
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply<JavaPlugin>()
    apply<IdeaPlugin>()
    apply<TestSetsPlugin>()
    apply<JacocoPlugin>()
    apply<DetektPlugin>()

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
        implementation(Lib.kotlin("reflect"))

        compileOnly(Lib.lombok())
        annotationProcessor(Lib.lombok())

        api(Lib.slf4j())
        implementation(Lib.kotlinLogging())
        runtimeOnly(Lib.logback())

        compileOnly(Lib.jsr305())

        testImplementation(Lib.springBootStarter("test").id) {
            exclude("junit", "junit")
            exclude("org.skyscreamer", "jsonassert")
            exclude("org.mockito", "mockito-core")
            exclude("org.mockito", "mockito-junit-jupiter")
            exclude("org.hamcrest", "hamcrest")
            exclude("org.assertj", "assertj-core")
        }
        testImplementation(Lib.spek("dsl-jvm"))
        testImplementation(Lib.kluent().id) {
            exclude("org.mockito", "mockito-core")
            exclude("com.nhaarman.mockitokotlin2", "mockito-kotlin")
        }
        testImplementation(Lib.mockk())
        testImplementation(Lib.springMockk())
        testImplementation(Lib.kwik("evaluator"))
        testImplementation(Lib.kwik("generator-stdlib"))

        testRuntimeOnly(Lib.spek("runner-junit5"))
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
        val deleteOutFolderTask by registering(Delete::class) {
            delete("out")
        }
        named("clean") {
            dependsOn(deleteOutFolderTask)
        }
        withType<Test> {
            maxHeapSize = "2g"
            @Suppress("UnstableApiUsage")
            useJUnitPlatform {
                includeEngines("junit-jupiter", "spek2")
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

        jacocoTestReport {
            sourceSets(sourceSets["main"])
            executionData(fileTree(project.rootDir.absolutePath).include(jacocoTestPattern))
        }
        withType<JacocoReport> {
            enabled = project.name.mayHaveTestCoverage()
            @Suppress("UnstableApiUsage")
            reports {
                xml.isEnabled = true
                html.isEnabled = false
                csv.isEnabled = false
            }
            afterEvaluate {
                classDirectories.setFrom(
                    files(
                        classDirectories.files.map {
                            fileTree(it) {
                                exclude(generatedPackages)
                            }
                        }
                    )
                )
            }
            dependsOn(check)
        }

        register("version") {
            doLast {
                println(project.version)
            }
        }
    }
}

tasks {
    val jvmTarget = JavaVersion.VERSION_11.toString()

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            this.jvmTarget = jvmTarget
        }
    }
    withType<Detekt> {
        allRules = true
        buildUponDefaultConfig = true
        config.setFrom(files("${rootProject.projectDir}/config/detekt/detekt.yml"))
        baseline.set(file("detekt-baseline.xml"))
        this.jvmTarget = jvmTarget
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
    detektMain {
        reports {
            xml {
                outputLocation.set(file("build/reports/detekt/customPath.xml"))
                required.set(true)
            }
        }
    }
    val projectsWithCoverage = subprojects.filter { it.name.mayHaveTestCoverage() }
    withType<SonarQubeTask> {
        description = "Push jacoco analysis to sonarcloud."
        group = "Verification"
        dependsOn(projectsWithCoverage.map { it.tasks.getByName("jacocoTestReport") })
        dependsOn(subprojects.map { it.tasks.getByName("detekt") })
    }

    projectsWithCoverage.forEach { project ->
        project.jacoco {
            toolVersion = Lib.jacocoToolVersion
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
        "ch.difty.kris:kris-core:0.3.3" to mitLicense,
        "com.google.elemental2:elemental2-core:1.0.0-RC1" to apacheLicense,
        "org.codehaus.jettison:jettison:1.2" to apacheLicense,
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

fun String.mayHaveTestCoverage(): Boolean = this !in testModules
fun Project.isWebProject() = path.endsWith("web")
