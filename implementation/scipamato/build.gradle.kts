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

dependencyManagement {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
}

extra["spring.cloudVersion"] = "Greenwich.SR1"

jacoco {
    toolVersion = "0.8.4"
}

val testModuleDirs = setOf("common/test", "common/persistence-jooq-test")
val testModules = testModuleDirs.map { it.replaceFirst("/", "-") }
val testPackages = testModuleDirs.map { "$it/**/*" }
val generatedPackages: Set<String> = setOf(
        "**/ch/difty/scipamato/core/db/**",
        "**/ch/difty/scipamato/core/pubmed/api/**",
        "**/ch/difty/scipamato/publ/db/**"
)
val jacocoTestReportXml = "build/reports/jacoco/test/jacocoTestReport.xml"
val jacocoTestPattern = "**/build/jacoco/*.exec"

sonarqube {
    properties {
        property("sonar.exclusions", "**/ch/difty/scipamato/publ/web/themes/markup/html/publ/**/*,${generatedPackages.joinToString(",")}")
        property("sonar.coverage.exclusions", (generatedPackages + testPackages).joinToString(","))
        property("sonar.coverage.jacoco.xmlReportPaths", jacocoTestReportXml)
    }
}


allprojects {
    group = "ch.difty"
    version = "1.2.0"

    repositories {
        mavenLocal()
        mavenCentral()
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

        val integrationTest by registering {
            dirName = "intTest"
            imports(testLib)
        }

        val adhocTest by registering {
            dirName = "adhocTest"
            imports(testLib)
        }

        val unitTest by existing {
            imports(testLib)
        }
    }

    if (!isWebProject()) {
        apply(plugin = "java-library")
    }

    dependencies {
        compileOnly(Lib.lombok())
        annotationProcessor(Lib.lombok())

        api(Lib.slf4j())
        runtimeOnly(Lib.logback())
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
            dependsOn(test)
        }
        val check by existing {
            dependsOn(integrationTest)
        }
        withType<JacocoReport> {
            enabled = project.name.needsJacocoCoverage()
            reports {
                xml.isEnabled = true
                xml.destination = file(jacocoTestReportXml)
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
    withType<SonarQubeTask> {
        dependsOn(subprojects
                .filter { it.name.needsJacocoCoverage() }
                .map { it.tasks.getByName("jacocoTestReport") }
        )
    }
}

fun String.needsJacocoCoverage(): Boolean = this !in testModules

fun Project.isWebProject() = path.endsWith("web")
