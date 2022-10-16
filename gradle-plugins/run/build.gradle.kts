import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.detekt)
}

dependencies {
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.detekt)
}

tasks.withType(KotlinJvmCompile::class.java).configureEach {
    kotlinOptions.jvmTarget = libs.versions.java.get()
}

detekt {
    buildUponDefaultConfig = true
    config.from(file("../../config/detekt/detekt.yml"))
}

gradlePlugin {
    plugins {
        create("application-properties-filter") {
            id = "application-properties-filter"
            implementationClass = "ApplicationPropertiesFilterPlugin"
        }
    }
}
