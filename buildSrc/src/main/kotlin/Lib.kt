import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import java.io.File
import java.util.Properties

@Suppress("TooManyFunctions", "MaximumLineLength", "MemberVisibilityCanBePrivate", "SpellCheckingInspection")
object Lib {

    //region:dependencyVersions
    private const val kotlinVersion = "1.3.72"

    private const val springBootVersion = "2.2.7.RELEASE"
    private const val springBootAdminVersion = "2.2.2"
    const val springCloudVersion = "Hoxton.SR4"
    private const val springCloudStarterVersion = "2.2.2.RELEASE"

    private const val wicketSpringBootStarterVersion = "2.1.9"
    private const val wicketVersion = "8.8.0"
    private const val wicketstuffVersion = "8.8.0"
    private const val wicketJqueryUiVersion = "8.6.0"
    private const val wicketBootstrapVersion = "2.0.13"
    private const val jasperReportVersion = "6.12.2"
    private const val krisVersion = "0.2.4"
    const val jooqVersion = "3.12.4"

    private const val kotlinLoggingVersion = "1.7.9"

    private const val openfeignVersion = "10.10.1"

    private const val jaxbCoreVersion = "2.3.0.1"
    private const val jaxbImplVersion = "2.3.2"
    private const val javaxActivationVersion = "1.2.0"
    private const val javaxElApiVersion = "3.0.1-b06"
    private const val javaxElVersion = "3.0.1-b11"

    private const val fontAwesomeVersion = "5.12.0"

    private const val equalsverifierVersion = "3.1.13"

    private const val junit5Version = "5.6.2"

    private const val testcontainersVersion = "1.14.1"

    @Suppress("MemberVisibilityCanBePrivate")
    const val mockitoVersion = "3.3.3"
    private const val mockitoKotlinVersion = "2.2.0"

    private const val spekVersion = "2.0.10"
    private const val kwikVersion = "0.6.0"
    private const val kluentVersion = "1.61"
    private const val mockkVersion = "1.10.0"
    private const val springMockkVersion = "2.0.1"

    private const val jsr305Version = "3.0.2"
    //endregion

    //region:pluginVersions
    private const val springDependencyManagementPluginVersion = "1.0.9.RELEASE"
    private const val lombokPluginVersion = "5.0.1"
    private const val jooqModelatorPluginVersion = "3.6.0"
    private const val reckonPluginVersion = "0.12.0"
    private const val jaxbPluginVersion = "4.3.1"
    private const val testSetsPluginVersion = "3.0.1"
    private const val sonarqubePluginVersion = "2.8"
    private const val detektPluginVersion = "1.8.0"
    //endregion

    //region:dependencies

    // Kotlin

    fun kotlin(module: String) = Dep("org.jetbrains.kotlin:kotlin-$module", kotlinVersion)

    // Spring

    fun springBoot(module: String) = Dep("org.springframework.boot", "spring-boot-$module", springBootVersion)
    fun springBootStarter(module: String) = springBoot("starter-$module")
    fun spring(module: String) = Dep("org.springframework", "spring-$module")
    fun springSecurity(module: String) = Dep("org.springframework.security", "spring-security-$module")
    fun springBootAdmin() = Dep("de.codecentric", "spring-boot-admin-starter-client", springBootAdminVersion)

    // Lombok

    @Deprecated("convert to kotlin", ReplaceWith("kotlin data classes, kotlin-logging"))
    fun lombok() = Dep("org.projectlombok", "lombok")

    // Logging

    fun slf4j() = Dep("org.slf4j", "slf4j-api")
    fun kotlinLogging() = Dep("io.github.microutils", "kotlin-logging", kotlinLoggingVersion)
    fun logback() = Dep("ch.qos.logback", "logback-core")

    // DB

    fun jOOQ(module: String = "jooq") = Dep("org.jooq", module, jooqVersion)
    fun flyway() = Dep("org.flywaydb", "flyway-core")
    fun postgres() = Dep("org.postgresql", "postgresql")

    // Cloud

    fun springCloud(module: String) = Dep("org.springframework.cloud", "spring-cloud-starter-$module", springCloudStarterVersion)
    fun openfeign(module: String) = Dep("io.github.openfeign", "feign-$module", openfeignVersion)

    // Wicket

    fun springBootStarterWicket() = Dep("com.giffing.wicket.spring.boot.starter", "wicket-spring-boot-starter", wicketSpringBootStarterVersion)
    fun wicket(module: String) = Dep("org.apache.wicket", "wicket-$module", wicketVersion)
    fun wicketStuff(module: String) = Dep("org.wicketstuff", "wicketstuff-$module", wicketstuffVersion)
    fun wicketBootstrap(module: String) = Dep("de.agilecoders.wicket", "wicket-bootstrap-$module", wicketBootstrapVersion)
    fun wicketJqueryUi(module: String = "") = Dep("com.googlecode.wicket-jquery-ui", "wicket-jquery-ui${if (module.isNotBlank()) "-$module" else ""}", wicketJqueryUiVersion)
    fun fontAwesome() = Dep("org.webjars", "font-awesome", fontAwesomeVersion)
    fun jasperreports(module: String = "") = Dep("net.sf.jasperreports", "jasperreports${if (module.isNotBlank()) "-$module" else ""}", jasperReportVersion)
    fun kris(module: String) = Dep("ch.difty.kris", "kris-$module", krisVersion)

    // JSR 303 bean validation provider implementation

    fun validationApi() = Dep("javax.validation", "validation-api")
    fun hibernateValidator() = Dep("org.hibernate.validator", "hibernate-validator")
    fun javaxElApi() = Dep("javax.el", "javax.el-api", javaxElApiVersion)
    fun javaxElImpl() = Dep("org.glassfish", "javax.el", javaxElVersion)

    // Utility libraries

    fun javaxActivation() = Dep("com.sun.activation", "javax.activation", javaxActivationVersion)
    fun javaxActivationApi() = Dep("javax.activation", "javax.activation-api", javaxActivationVersion)

    // Caching: JCache with ehcache as cache provider

    fun cacheApi() = Dep("javax.cache", "cache-api")
    fun ehcache() = Dep("org.ehcache", "ehcache")

    fun jaxbApi() = Dep("javax.xml.bind", "jaxb-api")
    fun jaxbRuntime() = Dep("org.glassfish.jaxb", "jaxb-runtime")
    fun jaxbCore() = Dep("com.sun.xml.bind", "jaxb-core", jaxbCoreVersion)
    fun jaxb(module: String) = Dep("com.sun.xml.bind", "jaxb-$module", jaxbImplVersion)
    fun jacksonKotlin() = Dep("com.fasterxml.jackson.module", "jackson-module-kotlin")

    // Test Libraries

    fun junit5(module: String = "") = Dep("org.junit.jupiter", "junit-jupiter${if (module.isNotBlank()) "-$module" else ""}", junit5Version)

    fun testcontainers(module: String) = Dep("org.testcontainers", module, testcontainersVersion)
    fun equalsverifier() = Dep("nl.jqno.equalsverifier", "equalsverifier", equalsverifierVersion)
    fun spek(module: String) = Dep("org.spekframework.spek2", "spek-$module", spekVersion)
    fun kluent() = Dep("org.amshove.kluent", "kluent", kluentVersion)
    fun mockk() = Dep("io.mockk", "mockk", mockkVersion)
    fun springMockk() = Dep("com.ninja-squad", "springmockk", springMockkVersion)
    fun kwik(module: String) = Dep("com.github.jcornaz.kwik", "kwik-$module-jvm", kwikVersion)

    fun servletApi() = Dep("javax.servlet", "javax.servlet-api")

    fun jsr305() = Dep("com.google.code.findbugs", "jsr305", jsr305Version)

    //endregion

    //region:plugins

    fun kotlinJvmPlugin() = Plugin("jvm", kotlinVersion)
    fun kotlinSpringPlugin() = Plugin("plugin.spring", kotlinVersion)

    fun springBootPlugin() = Plugin("org.springframework.boot", springBootVersion)
    fun springDependencyManagementPlugin() = Plugin("io.spring.dependency-management", springDependencyManagementPluginVersion)

    fun lombokPlugin() = Plugin("io.freefair.lombok", lombokPluginVersion)

    fun jooqModelatorPlugin() = Plugin("ch.ayedo.jooqmodelator", jooqModelatorPluginVersion)

    fun testSetsPlugin() = Plugin("org.unbroken-dome.test-sets", testSetsPluginVersion)

    fun reckonPlugin() = Plugin("org.ajoberstar.reckon", reckonPluginVersion)

    fun jaxbPlugin() = Plugin("com.intershop.gradle.jaxb", jaxbPluginVersion)

    fun detektPlugin() = Plugin("io.gitlab.arturbosch.detekt", detektPluginVersion)

    fun sonarqubePlugin() = Plugin("org.sonarqube", sonarqubePluginVersion)
    //endregion
}

class Dep(private val group: String, private val name: String, private val version: String? = null) {
    val id: String
        get() {
            val versionPart = if (version != null) ":$version" else ""
            return "$group:$name$versionPart"
        }
}

class Plugin(val id: String, val version: String? = null) {
    val idVersion: String get() = "$id${if (version != null) ":$version" else ""}"
}

fun DependencyHandler.annotationProcessor(dependencyNotation: Dep): Dependency? = add("annotationProcessor", dependencyNotation.id)
fun DependencyHandler.api(dependencyNotation: Dep): Dependency? = add("api", dependencyNotation.id)
fun DependencyHandler.compileOnly(dependencyNotation: Dep): Dependency? = add("compileOnly", dependencyNotation.id)
fun DependencyHandler.implementation(dependencyNotation: Dep): Dependency? = add("implementation", dependencyNotation.id)
fun DependencyHandler.runtimeOnly(dependencyNotation: Dep): Dependency? = add("runtimeOnly", dependencyNotation.id)

fun DependencyHandler.testAnnotationProcessor(dependencyNotation: Dep): Dependency? = add("testAnnotationProcessor", dependencyNotation.id)
fun DependencyHandler.testImplementation(dependencyNotation: Dep): Dependency? = add("testImplementation", dependencyNotation.id)
fun DependencyHandler.testRuntimeOnly(dependencyNotation: Dep): Dependency? = add("testRuntimeOnly", dependencyNotation.id)
fun DependencyHandler.testLibImplementation(dependencyNotation: Dep): Dependency? = add("testLibImplementation", dependencyNotation.id)
fun DependencyHandler.testLibAnnotationProcessor(dependencyNotation: Dep): Dependency? = add("testLibAnnotationProcessor", dependencyNotation.id)
fun DependencyHandler.adhocTestImplementation(dependencyNotation: Dep): Dependency? = add("adhocTestImplementation", dependencyNotation.id)
fun DependencyHandler.integrationTestImplementation(dependencyNotation: Dep): Dependency? = add("integrationTestImplementation", dependencyNotation.id)
fun DependencyHandler.integrationTestAnnotationProcessor(dependencyNotation: Dep): Dependency? = add("integrationTestAnnotationProcessor", dependencyNotation.id)
fun DependencyHandler.integrationTestRuntimeOnly(dependencyNotation: Dep): Dependency? = add("integrationTestRuntimeOnly", dependencyNotation.id)
fun DependencyHandler.jaxb(dependencyNotation: Dep): Dependency? = add("jaxb", dependencyNotation.id)
fun DependencyHandler.jooqModelatorRuntime(dependencyNotation: Dep): Dependency? = add("jooqModelatorRuntime", dependencyNotation.id)
fun DependencyHandler.developmentOnly(dependencyNotation: Dep): Dependency? = add("developmentOnly", dependencyNotation.id)

fun File.asProperties() = Properties().apply {
    inputStream().use { fis ->
        load(fis)
    }
}
