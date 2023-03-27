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
class ScipamatoAdhocTestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<JvmTestSuitePlugin>()
            configure<TestingExtension> {
                suites {
                    register("adhocTest", JvmTestSuite::class) {
                        sources.java.srcDir("src/adhoc-test/kotlin")
                        dependencies {
                            implementation(project())
                        }
                    }
                }
            }
            configurations.named("adhocTestImplementation") {
                extendsFrom(configurations.named("testImplementation").get())
            }
        }
    }
}
