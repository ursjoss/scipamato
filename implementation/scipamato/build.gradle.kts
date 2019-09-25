import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
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
    Lib.sonarqubePlugin().run { id(id) version version }
}

java {
    version = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}


extra["spring.cloudVersion"] = Lib.springCloudVersion
extra["mockito.version"] = Lib.mockitoVersion
extra["jooq.version"] = Lib.jooqVersion

dependencyManagement {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("spring.cloudVersion")}")
    }
}

val testModuleDirs = setOf("common/test", "common/persistence-jooq-test")
val testModules = testModuleDirs.map { it.replaceFirst("/", "-") }
val testPackages = testModuleDirs.map { "$it/**/*" }
val generatedPackages: Set<String> = setOf(
        "**/ch/difty/scipamato/core/db/**",
        "**/ch/difty/scipamato/core/pubmed/api/**",
        "**/ch/difty/scipamato/publ/db/**"
)

val jacocoTestReportFile = "$buildDir/reports/jacoco/test/jacocoTestReport.xml"
val jacocoTestPattern = "**/build/jacoco/*.exec"

sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.projectKey", "ursjoss_scipamato")
        property("sonar.organization", "ursjoss-github")
        property("sonar.exclusions", "**/ch/difty/scipamato/publ/web/themes/markup/html/publ/**/*,${generatedPackages.joinToString(",")}")
        property("sonar.coverage.exclusions", (generatedPackages + testPackages).joinToString(","))
        property("sonar.coverage.jacoco.xmlReportPaths", jacocoTestReportFile)
    }
}


allprojects {
    group = "ch.difty"
    version = "1.3.1-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://dl.bintray.com/mockito/maven/") }
        maven { url = uri("http://jaspersoft.jfrog.io/jaspersoft/third-party-ce-artifacts") }
    }
}

subprojects {
    apply<SpringBootPlugin>()
    apply(plugin = Lib.springDependencyManagementPlugin().id)
    apply<KotlinPlatformJvmPlugin>()
    apply<JavaPlugin>()
    apply<IdeaPlugin>()
    apply<TestSetsPlugin>()
    apply<JacocoPlugin>()

    testSets {
        val testLib by libraries.creating {
            dirName = "testLib"
        }

        named("unitTest") {
            imports(testLib)
        }

        register("integrationTest") {
            dirName = "intTest"
            imports(testLib)
        }

        register("adhocTest") {
            dirName = "adhocTest"
            imports(testLib)
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
        implementation(Lib.kotlin("stdlib-jdk8"))
        implementation(Lib.kotlin("reflect"))

        compileOnly(Lib.lombok())
        annotationProcessor(Lib.lombok())

        api(Lib.slf4j())
        implementation(Lib.kotlinLogging())
        runtimeOnly(Lib.logback())

        compileOnly(Lib.jsr305())
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
            useJUnitPlatform()
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
                classDirectories.setFrom((files(classDirectories.files.map {
                    fileTree(it) {
                        exclude(generatedPackages)
                    }
                })))
            }
            dependsOn(check)
        }
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
    val projectsWithCoverage = subprojects.filter { it.name.mayHaveTestCoverage() }
    withType<SonarQubeTask> {
        description = "Push jacoco analysis to sonarcloud."
        group = "Verification"
        dependsOn(
                projectsWithCoverage.map { it.tasks.getByName("jacocoTestReport") }
        )
    }
    withType<Test> {
        failFast = true
        testLogging {
            events = setOf(STARTED, FAILED, PASSED, SKIPPED)
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}

fun String.mayHaveTestCoverage(): Boolean = this !in testModules

fun Project.isWebProject() = path.endsWith("web")
