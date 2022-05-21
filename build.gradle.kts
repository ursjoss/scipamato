import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
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
    setScopeCalc(calcScopeFromProp().or(calcScopeFromCommitMessages()))
    setStageCalc(calcStageFromProp())
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
}

fun String.mayHaveTestCoverage(): Boolean = this !in testModules
fun Project.isWebProject() = path.endsWith("web")
