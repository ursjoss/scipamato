plugins {
    `java-library`
}

dependencies {
    compile("org.apache.commons:commons-lang3:3.8.1")
    compile("commons-io:commons-io:2.6")
    testCompile("org.springframework:spring-core:5.1.6.RELEASE")
}

description = "SciPaMaTo-Common :: Utilities Project"

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