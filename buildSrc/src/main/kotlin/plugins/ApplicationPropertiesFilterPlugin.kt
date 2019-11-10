package plugins

import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.filter
import org.gradle.kotlin.dsl.named
import org.gradle.language.jvm.tasks.ProcessResources
import java.time.LocalDateTime

/**
 * Gradle plugin that takes care of replacing properties of the form `@prop@`
 * (e.g. `@project.version@`) during the [ProcessResources] gradle task for
 * `application.properties` files located in `src/main/resources`.
 */
class ApplicationPropertiesFilterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {

            tasks.named<ProcessResources>("processResources") {
                from("src/main/resources") {
                    include("**/application*.yml")
                    include("**/application*.yaml")
                    include("**/application*.properties")
                    val tokens: Map<String, Any> = project.collectProperties()
                    inputs.properties(tokens)
                    filter<ReplaceTokens>("tokens" to tokens)
                }
                into("build/resources/main")
            }
        }
    }

    private fun Project.collectProperties(): Map<String, Any> {
        val props: MutableMap<String, Any> = mutableMapOf()
        props["timestamp"] = LocalDateTime.now().toString()
        properties.forEach { prop ->
            prop.value?.let { value ->
                when (value) {
                    is Map<*, *> -> null
                    is Collection<*> -> null
                    else -> value.toString()
                }?.let { stringValue ->
                    props[prop.key] = stringValue
                    props["project.${prop.key}"] = stringValue
                }
            }
        }
        return props
    }
}
