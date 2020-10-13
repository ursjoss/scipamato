@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.FrozenDateTimeService
import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.core.pubmed.PubmedArticleFacade
import ch.difty.scipamato.core.pubmed.PubmedArticleResult
import ch.difty.scipamato.core.pubmed.PubmedArticleService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Primary

const val RECORD_COUNT_PREPOPULATED = 36
const val MAX_ID_PREPOPULATED = 41L

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
    open fun applicationProperties(): ApplicationProperties =
        object : ApplicationProperties {
            override val buildVersion get() = "vxy"
            override val defaultLocalization get() = "de"
            override val brand get() = "scipamato"
            override val titleOrBrand get() = brand
            override val pubmedBaseUrl get() = "https://pubmed/"
            override val cmsUrlSearchPage get() = "http://localhost/paper/number"
            override val redirectFromPort get() = 8080
            override val multiSelectBoxActionBoxWithMoreEntriesThan get() = 4
        }

    @Bean
    open fun noopPubmedArticleService() = object : PubmedArticleService {
        override fun getPubmedArticleWithPmid(pmId: Int): PubmedArticleResult =
            PubmedArticleResult(null, null, null)

        override fun getPubmedArticleWithPmidAndApiKey(pmId: Int, apiKey: String) =
            PubmedArticleResult(null, null, null)

        override fun extractArticlesFrom(content: String) = listOf<PubmedArticleFacade>()
    }

    @Bean("frozenDateTimeService")
    @Primary
    open fun dateTimeService(): DateTimeService = FrozenDateTimeService()
}
