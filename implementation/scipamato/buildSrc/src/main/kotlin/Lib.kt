object Lib {

    // Spring

    fun springBootStarter(module: String) = "org.springframework.boot:spring-boot-starter-$module:2.1.4.RELEASE"
    fun spring(module: String) = "org.springframework:spring-$module:5.1.6.RELEASE"


    // Lombok

    @Deprecated("convert to kotlin", ReplaceWith("kotlin data classes, kotlin-logging"))
    fun lombok() = "org.projectlombok:lombok:1.18.6"


    // Logging

    fun slf4j() = "org.slf4j:slf4j-api:1.7.26"
    fun logback() = "ch.qos.logback:logback-core:1.2.3"


    // Utility libraries

    fun commonsLang3() = "org.apache.commons:commons-lang3:3.8.1"
    fun commonsIo() = "commons-io:commons-io:2.6"


    // Test Libraries

    fun junit5(module: String = "") = "org.junit.jupiter:junit-jupiter${if (module.isNotBlank()) "-$module" else ""}:5.4.2"
    fun mockito2(module: String) = "org.mockito:mockito-$module:2.23.4"
    fun assertj() = "org.assertj:assertj-core:3.11.1"
    fun equalsverifier() = "nl.jqno.equalsverifier:equalsverifier:3.1.8"
}