package ch.difty.scipamato.publ.persistence

import ch.difty.scipamato.publ.persistence.newstudies.NewStudyRepository
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldStartWith
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@JooqTest
@Testcontainers
@Suppress("FunctionName", "SpellCheckingInspection", "MagicNumber")
internal open class JooqNewStudyRepoIntegrationTest {

    @Autowired
    private lateinit var repo: NewStudyRepository

    @Test
    fun findingTopicsOfNewsletter1_inEnglish_returnsNoResults() {
        repo.findNewStudyTopicsForNewsletter(1, "en").shouldBeEmpty()
    }

    @Test
    fun findingTopicsOfNewsletter1_inGerman_returnsResults() {
        val result = repo.findNewStudyTopicsForNewsletter(1, "de")
        result shouldHaveSize 4

        result.map { it.sort } shouldContainAll listOf(1, 2, 3, 4)
        result.map { it.title } shouldContainAll listOf(
            "Tiefe Belastungen", "Hirnleistung und neurodegenerative Erkrankungen",
            "Feinstaubkomponenten und PAK", "Hirnschlag"
        )

        result[0].studies shouldHaveSize 2
        result[1].studies shouldHaveSize 3
        result[2].studies shouldHaveSize 3
        result[3].studies shouldHaveSize 2

        result[0].studies.map { it.number } shouldContainAll listOf(8924L, 8993L)
        result[1].studies.map { it.number } shouldContainAll listOf(8973L, 8983L, 8984L)
        result[2].studies.map { it.number } shouldContainAll listOf(8933L, 8897L, 8861L)
        result[3].studies.map { it.number } shouldContainAll listOf(8916L, 8934L)

        val ns = result[0].studies.first()
        ns.sort shouldBeEqualTo 1
        ns.number shouldBeEqualTo 8924L
        ns.year shouldBeEqualTo 2017
        ns.authors shouldBeEqualTo "Di et al."
        ns.reference shouldBeEqualTo "(Di et al.; 2017)"
        ns.headline?.shouldStartWith("USA: Grosse Kohortenstudie zeigt, dass auch ein PM2.5-Grenzwert von 12")
        ns.description?.shouldStartWith("Registerkohortenstudie in den USA zur Untersuchung, ob die Sterblichkeit")
    }

    @Test
    fun findingMostRecentNewsletterId() {
        repo.findMostRecentNewsletterId() shouldBeEqualTo 2
    }

    @Test
    fun findingArchivedNewsletters_with14ToFind_returnsUpTo14() {
        val results = repo.findArchivedNewsletters(14, "en")
        results shouldHaveSize 2
        results.map { it.getMonthName("en") } shouldContainAll listOf("June 2018", "April 2018")
        results.map { it.getMonthName("fr") } shouldContainAll listOf("juin 2018", "avril 2018")
        results.map { it.getMonthName("de") } shouldContainAll listOf("Juni 2018", "April 2018")
    }

    @Test
    fun findingArchivedNewsletters_withOneToFind_returnsOne() {
        val results = repo.findArchivedNewsletters(1, "en")

        results shouldHaveSize 1
        results.map { it.getMonthName("en") } shouldContainAll listOf("June 2018")
        results.map { it.getMonthName("fr") } shouldContainAll listOf("juin 2018")
        results.map { it.getMonthName("de") } shouldContainAll listOf("Juni 2018")
    }

    @Test
    fun findingNewStudyPageLinks_withEnglish() {
        val results = repo.findNewStudyPageLinks("en")

        results shouldHaveSize 2
        results.map { it.langCode } shouldContainAll listOf("en")
        results.map { it.sort } shouldContainAll listOf(1, 2)
        results.map { it.title } shouldContainAll
            listOf("Search", "Project Repository")
        results.map { it.url } shouldContainAll
            listOf("https://duckduckgo.com/", "https://github.com/ursjoss/scipamato")
    }

    @Test
    fun findingNewStudyPageLinks_withGerman() {
        val results = repo.findNewStudyPageLinks("de")

        results shouldHaveSize 2
        results.map { it.langCode } shouldContainAll listOf("de")
        results.map { it.title } shouldContainAll listOf("Web Suche", "Projekt Code")
    }

    @Test
    fun findingIdOfNewsletterWithIssue_forExistingNewsletter_findsIt() {
        val idOpt = repo.findIdOfNewsletterWithIssue("2018/06")
        idOpt shouldBeEqualTo 2
    }

    @Test
    fun findingIdOfNewsletterWithIssue_forNonExistingNewsletter_returnsEmptyOptional() {
        val idOpt = repo.findIdOfNewsletterWithIssue("2018/06xxx")
        idOpt.shouldBeNull()
    }
}
