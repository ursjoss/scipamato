package ch.difty.scipamato.publ.persistence

import ch.difty.scipamato.common.logger
import ch.difty.scipamato.publ.persistence.keyword.JooqKeywordRepo
import org.amshove.kluent.shouldContainAll
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
        keywords.map { it.name } shouldContainAll listOf("Aerosol", "Aktivität, eingeschränkte", "Allergie")
        keywords.forEach { c -> log.debug(c.toString()) }
    }

    @Test
    fun findingAllKeywords1InEnglish() {
        val keywords = repo.findKeywords("en")
        keywords.map { it.name } shouldContainAll listOf("Aerosol", "Allergies", "Restricted activity")
        keywords.forEach { c -> log.debug(c.toString()) }
    }

    @Suppress("SpellCheckingInspection")
    @Test
    fun findingAllKeywords1InFrench() {
        @Suppress("SpellCheckingInspection")
        val keywords = repo.findKeywords("fr")
        keywords.map { it.name } shouldContainAll listOf("Activités réduites", "Aérosol", "Allergie")
        keywords.forEach { c -> log.debug(c.toString()) }
    }
}
