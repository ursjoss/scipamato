package ch.difty.scipamato.publ.persistence

import ch.difty.scipamato.publ.persistence.newstudies.NewStudyRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@JooqTest
@Testcontainers
@Suppress("FunctionName", "SpellCheckingInspection")
internal open class JooqNewStudyRepoIntegrationTest {

    @Autowired
    private lateinit var repo: NewStudyRepository

    @Test
    fun findingTopicsOfNewsletter1_inEnglish_returnsNoResults() {
        assertThat(repo.findNewStudyTopicsForNewsletter(1, "en")).isEmpty()
    }

    @Test
    fun findingTopicsOfNewsletter1_inGerman_returnsResults() {
        val result = repo.findNewStudyTopicsForNewsletter(1, "de")
        assertThat(result).hasSize(4)

        assertThat(result.map { it.sort }).containsExactly(1, 2, 3, 4)
        assertThat(result.map { it.title }).containsExactly(
            "Tiefe Belastungen", "Hirnleistung und neurodegenerative Erkrankungen",
            "Feinstaubkomponenten und PAK", "Hirnschlag"
        )

        assertThat(result[0].studies).hasSize(2)
        assertThat(result[1].studies).hasSize(3)
        assertThat(result[2].studies).hasSize(3)
        assertThat(result[3].studies).hasSize(2)

        assertThat(result[0].studies.map { it.number }).containsExactly(8924L, 8993L)
        assertThat(result[1].studies.map { it.number }).containsExactly(8973L, 8983L, 8984L)
        assertThat(result[2].studies.map { it.number }).containsExactly(8933L, 8897L, 8861L)
        assertThat(result[3].studies.map { it.number }).containsExactly(8916L, 8934L)

        val ns = result[0].studies.first()
        assertThat(ns.sort).isEqualTo(1)
        assertThat(ns.number).isEqualTo(8924L)
        assertThat(ns.year).isEqualTo(2017)
        assertThat(ns.authors).isEqualTo("Di et al.")
        assertThat(ns.reference).isEqualTo("(Di et al.; 2017)")
        assertThat(ns.headline).startsWith("USA: Grosse Kohortenstudie zeigt, dass auch ein PM2.5-Grenzwert von 12")
        assertThat(ns.description).startsWith("Registerkohortenstudie in den USA zur Untersuchung, ob die Sterblichkeit")
    }

    @Test
    fun findingMostRecentNewsletterId() {
        assertThat(repo.findMostRecentNewsletterId()).isPresent.hasValue(2)
    }

    @Test
    fun findingArchivedNewsletters_with14ToFind_returnsUpTo14() {
        val results = repo.findArchivedNewsletters(14, "en")
        assertThat(results).hasSize(2)
        assertThat(results.map { it.getMonthName("en") }).containsExactly("June 2018", "April 2018")
        assertThat(results.map { it.getMonthName("fr") }).containsExactly("juin 2018", "avril 2018")
        assertThat(results.map { it.getMonthName("de") }).containsExactly("Juni 2018", "April 2018")
    }

    @Test
    fun findingArchivedNewsletters_withOneToFind_returnsOne() {
        val results = repo.findArchivedNewsletters(1, "en")

        assertThat(results).hasSize(1)
        assertThat(results.map { it.getMonthName("en") }).containsExactly("June 2018")
        assertThat(results.map { it.getMonthName("fr") }).containsExactly("juin 2018")
        assertThat(results.map { it.getMonthName("de") }).containsExactly("Juni 2018")
    }

    @Test
    fun findingNewStudyPageLinks_withEnglish() {
        val results = repo.findNewStudyPageLinks("en")

        assertThat(results).hasSize(2)
        assertThat(results.map { it.langCode }).containsOnly("en")
        assertThat(results.map { it.sort }).containsExactly(1, 2)
        assertThat(results.map { it.title }).containsExactly("Search", "Project Repository")
        assertThat(results.map { it.url }).containsExactly("https://duckduckgo.com/", "https://github.com/ursjoss/scipamato")
    }

    @Test
    fun findingNewStudyPageLinks_withGerman() {
        val results = repo.findNewStudyPageLinks("de")

        assertThat(results).hasSize(2)
        assertThat(results.map { it.langCode }).containsOnly("de")
        assertThat(results.map { it.title }).containsExactly("Web Suche", "Projekt Code")
    }

    @Test
    fun findingIdOfNewsletterWithIssue_forExistingNewsletter_findsIt() {
        val idOpt = repo.findIdOfNewsletterWithIssue("2018/06")
        assertThat(idOpt).isPresent
        assertThat(idOpt.get()).isEqualTo(2)
    }

    @Test
    fun findingIdOfNewsletterWithIssue_forNonExistingNewsletter_returnsEmptyOptional() {
        val idOpt = repo.findIdOfNewsletterWithIssue("2018/06xxx")
        assertThat(idOpt).isNotPresent
    }
}