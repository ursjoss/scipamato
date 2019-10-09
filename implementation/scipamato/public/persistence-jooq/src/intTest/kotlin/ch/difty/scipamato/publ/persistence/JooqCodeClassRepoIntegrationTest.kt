package ch.difty.scipamato.publ.persistence

import ch.difty.scipamato.publ.persistence.codeclass.JooqCodeClassRepo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@JooqTest
@Testcontainers
@Suppress("FunctionName")
internal open class JooqCodeClassRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqCodeClassRepo

    @Test
    fun finding_inEnglish_returnsLocalizedCodeClasses() {
        assertThat(repo.find("en").map { it.name }).containsExactly(
            "Exposure Agent", "Region", "Study Population", "Health Outcome",
            "Study Design", "Species", "Duration of Exposure", "Setting"
        )
    }

    @Test
    @Suppress("SpellCheckingInspection")
    fun finding_inFrench_returnsLocalizedCodeClasses() {
        assertThat(repo.find("fr").map { it.name }).containsExactly(
            "Polluant nocif", "Région", "Population", "Effets physiologique ou nocifs",
            "Type d'étude", "Espèces investigées", "Durée de l'exposition", "Site d'exposition"
        )
    }

    @Test
    @Suppress("SpellCheckingInspection")
    fun finding_inGerman_returnsLocalizedCodeClasses() {
        assertThat(repo.find("de").map { it.name }).containsExactly(
            "Schadstoffe", "Region", "Kollektiv", "Zielgrössen", "Studientyp", "Spezies", "Zeitdauer", "Umgebung"
        )
    }
}
