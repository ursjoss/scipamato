plugins {
    java
}


allprojects {
    group = "ch.difty"
    version = "1.1.7-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    java {
        version = JavaVersion.VERSION_11
    }
}

subprojects {

    dependencies {
        compile("org.slf4j:slf4j-api")
        compile("ch.qos.logback:logback-core")
        testCompile("org.springframework.boot:spring-boot-starter-test")
        testCompile("org.junit.jupiter:junit-jupiter")
        testCompile("org.junit.jupiter:junit-jupiter-params")
        testCompile("org.mockito:mockito-core")
        testCompile("org.mockito:mockito-junit-jupiter")
        testCompile("org.assertj:assertj-core")
        testCompile("nl.jqno.equalsverifier:equalsverifier")
    }

    configurations.all {
    }

    tasks.withType(JavaCompile::class) {
        options.encoding = "UTF-8"
    }
}
