import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport

@Suppress("unused")
class ScipamatoJacocoPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply<JacocoPlugin>()
            val test = tasks.named("test")
            tasks.withType<JacocoReport> {
                sourceSets(project.extensions.getByType(SourceSetContainer::class.java).getByName("main"))
                executionData(fileTree(project.rootDir.absolutePath).include("**/build/jacoco/*.exec"))
                reports {
                    xml.required.set(true)
                    html.required.set(false)
                }
                dependsOn(test)
            }
            target.rootProject.tasks.named("sonar") {
                dependsOn(tasks.getByName("check"))
                dependsOn(tasks.getByName("jacocoTestReport"))
            }
        }
    }
}
