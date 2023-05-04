import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.detekt)
}

dependencies {
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.detekt)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    }
}

detekt {
    buildUponDefaultConfig = true
    config.from(file("../../config/detekt/detekt.yml"))
}

gradlePlugin {
    plugins {
        create("scipamato-detekt") {
            id = "scipamato-detekt"
            implementationClass = "ScipamatoDetektPlugin"
        }
        create("scipamato-collect-sarif") {
            id = "scipamato-collect-sarif"
            implementationClass = "CollectSarifPlugin"
        }
        create("scipamato-jacoco") {
            id = "scipamato-jacoco"
            implementationClass = "ScipamatoJacocoPlugin"
        }
        create("scipamato-adhoc-test") {
            id = "scipamato-adhoc-test"
            implementationClass = "ScipamatoAdhocTestPlugin"
        }
        create("scipamato-integration-test") {
            id = "scipamato-integration-test"
            implementationClass = "ScipamatoIntegrationTestPlugin"
        }
    }
}
