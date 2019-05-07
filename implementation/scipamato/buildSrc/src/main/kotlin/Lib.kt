import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.PluginAware
import org.gradle.kotlin.dsl.apply

object Lib {

    // TODO find a way to utilize the dependencyManagement provided by the dependency-management-plugin using the spring BOM
    // so we don't explicitly need to manage the versions here
    //region:dependencies

    // Spring

    private const val springBootVersion = "2.1.4.RELEASE"
    fun springBootStarter(module: String) = springBoot("starter-$module")
    fun springBoot(module: String) = Dep("org.springframework.boot", "spring-boot-$module", springBootVersion)
    fun spring(module: String) = Dep("org.springframework", "spring-$module", "5.1.6.RELEASE")
    fun springCloud(module: String) = Dep("org.springframework.cloud", "spring-cloud-starter-$module", "2.1.1.RELEASE")
    fun springBootAdmin() = Dep("de.codecentric", "spring-boot-admin-starter-client", "2.1.4")


    // Lombok

    @Deprecated("convert to kotlin", ReplaceWith("kotlin data classes, kotlin-logging"))
    fun lombok() = Dep("org.projectlombok", "lombok", "1.18.6")


    // Logging

    fun slf4j() = Dep("org.slf4j", "slf4j-api", "1.7.26")
    fun logback() = Dep("ch.qos.logback", "logback-core", "1.2.3")


    // DB

    fun jOOQ(module: String = "jooq") = Dep("org.jooq", module, "3.11.10")
    fun flyway() = Dep("org.flywaydb", "flyway-core", "5.2.4")
    fun postgres() = Dep("org.postgresql", "postgresql", "42.2.5")


    // Cloud
    fun openfeign(module: String) = Dep("io.github.openfeign", "feign-$module", "10.1.0")


    // Wicket

    fun springBootStarterWicket() = Dep("com.giffing.wicket.spring.boot.starter", "wicket-spring-boot-starter", "2.1.5")
    fun wicket(module: String) = Dep("org.apache.wicket", "wicket-$module", "8.4.0")
    fun wicketStuff(module: String) = Dep("org.wicketstuff", "wicketstuff-$module", "8.4.0")
    fun wicketBootstrap(module: String) = Dep("de.agilecoders.wicket", "wicket-bootstrap-$module", "2.0.9")
    fun fontAwesome() = Dep("org.webjars", "font-awesome", "5.7.1")


    // Utility libraries

    fun commonsLang3() = Dep("org.apache.commons", "commons-lang3", "3.8.1")
    fun commonsIo() = Dep("commons-io", "commons-io", "2.6")
    fun commonsCollection() = Dep("org.apache.commons", "commons-collections4", "4.3")
    fun jool() = Dep("org.jooq", "jool-java-8", "0.9.14")
    fun bval() = Dep("org.apache.bval", "bval-jsr", "2.0.0")
    fun validationApi() = Dep("javax.validation", "validation-api", "2.0.1.Final")
    fun javaxActivation() = Dep("com.sun.activation", "javax.activation", "1.2.0")

    // caching
    fun cacheApi() = Dep("javax.cache", "cache-api", "1.1.0")

    fun ehcache() = Dep("org.ehcache", "ehcache", "3.6.3")

    fun jaxbApi() = Dep("javax.xml.bind", "jaxb-api", "2.3.1")
    fun jaxb(module: String) = Dep("com.sun.xml.bind", "jaxb-$module", "2.3.0.1")
    fun jaxbRuntime() = Dep("org.glassfish.jaxb", "jaxb-runtime", "2.3.1")


    // Test Libraries

    fun junit5(module: String = "") = Dep("org.junit.jupiter", "junit-jupiter${if (module.isNotBlank()) "-$module" else ""}", "5.4.2")
    fun mockito2(module: String) = Dep("org.mockito", "mockito-$module", "2.23.4")
    fun assertj() = Dep("org.assertj", "assertj-core", "3.11.1")
    fun equalsverifier() = Dep("nl.jqno.equalsverifier", "equalsverifier", "3.1.8")

    fun servletApi() = Dep("javax.servlet", "javax.servlet-api", "4.0.1")

    //endregion

    //region:plugins

    fun kotlinPlugin() = Plugin("jvm", "1.3.31")

    fun springBootPlugin() = Plugin("org.springframework.boot", springBootVersion)
    fun springDependencyManagementPlugin() = Plugin("io.spring.dependency-management")

    //endregion
}

class Dep(val group: String, val name: String, val version: String? = null) {
    val id: String
        get() {
            val versionPart = if (version != null) ":$version" else ""
            return "$group:$name$versionPart"
        }
}

class Plugin(val id: String, val version: String? = null) {
    val idVersion: String get() = "$id${if (version != null) ":$version" else ""}"
}

fun PluginAware.apply(plugin: Plugin) = apply(null, plugin.idVersion, null)

fun DependencyHandler.annotationProcessor(dependencyNotation: Dep): Dependency? = add("annotationProcessor", dependencyNotation.id)
fun DependencyHandler.api(dependencyNotation: Dep): Dependency? = add("api", dependencyNotation.id)
fun DependencyHandler.compileOnly(dependencyNotation: Dep): Dependency? = add("compileOnly", dependencyNotation.id)
fun DependencyHandler.implementation(dependencyNotation: Dep): Dependency? = add("implementation", dependencyNotation.id)
fun DependencyHandler.runtimeOnly(dependencyNotation: Dep): Dependency? = add("runtimeOnly", dependencyNotation.id)

fun DependencyHandler.testApi(dependencyNotation: Dep): Dependency? = add("testApi", dependencyNotation.id)
fun DependencyHandler.testAnnotationProcessor(dependencyNotation: Dep): Dependency? = add("testAnnotationProcessor", dependencyNotation.id)
fun DependencyHandler.testCompile(dependencyNotation: Dep): Dependency? = add("testCompile", dependencyNotation.id)
fun DependencyHandler.testRuntimeOnly(dependencyNotation: Dep): Dependency? = add("testRuntimeOnly", dependencyNotation.id)