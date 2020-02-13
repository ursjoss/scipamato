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
        override fun getBuildVersion() = "vxy"
        override fun getDefaultLocalization() = "de"
        override fun getBrand() = "scipamato"
        override fun getTitleOrBrand() = brand
        override fun getPubmedBaseUrl() = "https://pubmed/"
        override fun getRedirectFromPort() = 8081
        override fun getMultiSelectBoxActionBoxWithMoreEntriesThan() = 4
        override fun isCommercialFontPresent() = false
        override fun isLessUsedOverCss() = false
        override fun isNavbarVisibleByDefault() = false
        override fun getCmsUrlSearchPage(): String? = null
        override fun getCmsUrlNewStudyPage(): String? = null
        override fun getAuthorsAbbreviatedMaxLength() = 50
        override fun isResponsiveIframeSupportEnabled() = false
        override fun getManagementUserName() = "admin"
        override fun getManagementUserPassword() = "admin"
        override fun getNumberOfPreviousNewslettersInArchive() = 14
    }
}
