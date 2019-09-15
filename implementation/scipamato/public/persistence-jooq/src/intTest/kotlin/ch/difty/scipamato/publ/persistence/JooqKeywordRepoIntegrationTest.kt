package ch.difty.scipamato.publ.persistence

import ch.difty.scipamato.common.logger
import ch.difty.scipamato.publ.persistence.keyword.JooqKeywordRepo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

private val log = logger()

@JooqTest
@Testcontainers
internal open class JooqKeywordRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqKeywordRepo

    @Test
    @Suppress("SpellCheckingInspection")
    fun findingAllKeywordsInGerman() {
        val keywords = repo.findKeywords("de")
        assertThat(keywords.map { it.name }).containsExactly("Aerosol", "Aktivität, eingeschränkte", "Allergie")
        keywords.forEach { c -> log.debug(c.toString()) }
    }

    @Test
    fun findingAllKeywords1InEnglish() {
        val keywords = repo.findKeywords("en")
        assertThat(keywords.map { it.name }).containsExactly("Aerosol", "Allergies", "Restricted activity")
        keywords.forEach { c -> log.debug(c.toString()) }
    }

    @Test
    fun findingAllKeywords1InFrench() {
        @Suppress("SpellCheckingInspection")
        val keywords = repo.findKeywords("fr")
        assertThat(keywords.map { it.name }).containsExactly("Activités réduites", "Aérosol", "Allergie")
        keywords.forEach { c -> log.debug(c.toString()) }
    }

}
