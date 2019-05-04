description = "SciPaMaTo-Common :: Utilities Project"

plugins {
    `java-library`
}

dependencies {
    implementation(Lib.commonsLang3())
    implementation(Lib.commonsIo())
    testImplementation(Lib.spring("core"))
}

tasks {
    val testJar by creating(Jar::class) {
        archiveClassifier.set("tests")
        from(sourceSets.test.get().allJava)
    }

    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    val javadocJar by creating(Jar::class) {
        dependsOn.add(javadoc)
        archiveClassifier.set("javadoc")
        from(javadoc)
    }

    artifacts {
        archives(testJar)
        archives(sourcesJar)
        archives(javadocJar)
        archives(jar)
    }
}