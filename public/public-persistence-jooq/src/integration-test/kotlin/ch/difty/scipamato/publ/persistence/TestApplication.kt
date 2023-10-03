package ch.difty.scipamato.publ.persistence

import ch.difty.scipamato.publ.config.ApplicationPublicProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["ch.difty.scipamato"])
open class TestApplication {

    @Suppress("SpreadOperator")
    fun main(args: Array<String>) {
        SpringApplicationBuilder()
            .sources(TestApplication::class.java)
            .run(*args)
    }

    @Bean
    open fun applicationProperties() = object : ApplicationPublicProperties {
        override val buildVersion get() = "vxy"
        override val defaultLocalization get() = "de"
        override val brand get() = "scipamato"
        override val titleOrBrand get() = brand
        override val pubmedBaseUrl get() = "https://pubmed/"
        override val redirectFromPort get() = 8081
        override val multiSelectBoxActionBoxWithMoreEntriesThan get() = 4
        override val isCommercialFontPresent = false
        override val isSassUsedOverCss = false
        override val isNavbarVisibleByDefault = false
        override val cmsUrlSearchPage: String?
            get() = null
        override val cmsUrlNewStudyPage: String? = null
        override val authorsAbbreviatedMaxLength = 50
        override val isResponsiveIframeSupportEnabled = false
        override val managementUserName = "admin"
        override val managementUserPassword = "admin"
        override val numberOfPreviousNewslettersInArchive = 14
    }
}
