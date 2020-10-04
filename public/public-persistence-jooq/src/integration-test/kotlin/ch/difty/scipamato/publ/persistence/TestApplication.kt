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
        override fun isCommercialFontPresent() = false
        override fun isLessUsedOverCss() = false
        override fun isNavbarVisibleByDefault() = false
        override val cmsUrlSearchPage: String?
            get() = null
        override fun getCmsUrlNewStudyPage(): String? = null
        override fun getAuthorsAbbreviatedMaxLength() = 50
        override fun isResponsiveIframeSupportEnabled() = false
        override fun getManagementUserName() = "admin"
        override fun getManagementUserPassword() = "admin"
        override fun getNumberOfPreviousNewslettersInArchive() = 14
    }
}
