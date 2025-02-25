import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JvmTestSuitePlugin
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register
import org.gradle.testing.base.TestingExtension

@Suppress("unused", "UnstableApiUsage")
class ScipamatoIntegrationTestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<JvmTestSuitePlugin>()
            val test = tasks.named("test")
            configure<TestingExtension> {
                suites {
                    register("integrationTest", JvmTestSuite::class) {
                        sources {
                            java.srcDir("src/integration-test/kotlin")
                            resources.srcDir("src/integration-test/resources")
                        }
                        dependencies {
                            implementation(project())
                        }
                        targets {
                            all {
                                testTask.configure {
                                    shouldRunAfter(test)
                                }
                            }
                        }
                    }
                }
            }
            configurations.named("integrationTestImplementation") {
                extendsFrom(configurations.named("testImplementation").get())
            }
            tasks.named("check") {
                dependsOn(tasks.named("integrationTest"))
            }
        }
    }
}
