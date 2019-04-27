package ch.difty.scipamato.publ.persistence.codeclass;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.publ.persistence.JooqBaseIntegrationTest;

@SuppressWarnings("SpellCheckingInspection")
class JooqCodeClassRepoIntegrationTest extends JooqBaseIntegrationTest {

    @Autowired
    private JooqCodeClassRepo repo;

    @Test
    void finding_inEnglish_returnsLocalizedCodeClasses() {
        assertThat(repo.find("en"))
            .extracting("name")
            .containsExactly("Exposure Agent", "Region", "Study Population", "Health Outcome", "Study Design",
                "Species", "Duration of Exposure", "Setting");
    }

    @Test
    void finding_inFrench_returnsLocalizedCodeClasses() {
        assertThat(repo.find("fr"))
            .extracting("name")
            .containsExactly("Polluant nocif", "Région", "Population", "Effets physiologique ou nocifs", "Type d'étude",
                "Espèces investigées", "Durée de l'exposition", "Site d'exposition");
    }

    @Test
    void finding_inGerman_returnsLocalizedCodeClasses() {
        assertThat(repo.find("de"))
            .extracting("name")
            .containsExactly("Schadstoffe", "Region", "Kollektiv", "Zielgrössen", "Studientyp", "Spezies", "Zeitdauer",
                "Umgebung");
    }
}
