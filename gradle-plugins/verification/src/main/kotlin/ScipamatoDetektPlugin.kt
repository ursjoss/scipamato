import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class ScipamatoDetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.plugins.apply("io.gitlab.arturbosch.detekt")
        target.plugins.withId("io.gitlab.arturbosch.detekt") {
            val rootProject = target.rootProject

            // specify base path to make file locations relative
            target.extensions.configure<DetektExtension> {
                buildUponDefaultConfig = true
                allRules = true
                baseline = target.file("detekt-baseline.xml")
                basePath = rootProject.projectDir.absolutePath
                config.setFrom("${rootProject.projectDir}/config/detekt/detekt.yml")
            }

            // enable SARIF report
            val detektTask = target.tasks.named("detekt", Detekt::class.java)
            detektTask.configure {
                reports.sarif.required.set(true)
                reports.xml.required.set(true)
            }

            // add detekt output to inputs of ReportMergeTask
            // mustRunAfter should be used here otherwise the merged report won't be generated on fail
            rootProject.plugins.withId( "scipamato-collect-sarif") {
                rootProject.tasks.named(
                    CollectSarifPlugin.MERGE_DETEKT_TASK_NAME,
                    ReportMergeTask::class.java
                ) {
                    input.from(detektTask.map { it.sarifReportFile }.orNull)
                    mustRunAfter(detektTask)
                }
            }
        }
        target.rootProject.tasks.named("sonarqube") {
            dependsOn(target.tasks.getByName("detekt"))
        }
    }
}
