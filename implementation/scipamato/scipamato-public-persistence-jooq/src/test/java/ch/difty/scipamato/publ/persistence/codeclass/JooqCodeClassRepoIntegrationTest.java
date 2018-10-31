package ch.difty.scipamato.publ.persistence.codeclass;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.publ.persistence.JooqTransactionalIntegrationTest;

public class JooqCodeClassRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    @Autowired
    private JooqCodeClassRepo repo;

    @Test
    public void finding_inEnglish_returnsLocalizedCodeClasses() {
        assertThat(repo.find("en"))
            .extracting("name")
            .containsExactly("Exposure Agent", "Region", "Study Population", "Health Outcome", "Study Design",
                "Species", "Duration of Exposure", "Setting");
    }

    @Test
    public void finding_inFrench_returnsLocalizedCodeClasses() {
        assertThat(repo.find("fr"))
            .extracting("name")
            .containsExactly("Polluant nocif", "Région", "Echantillons des personnes", "Effets physiologique ou nocifs",
                "Typ d'étude", "Espèces investigées", "Durée de l'exposition", "Site d'exposition");
    }

    @Test
    public void finding_inGerman_returnsLocalizedCodeClasses() {
        assertThat(repo.find("de"))
            .extracting("name")
            .containsExactly("Schadstoffe", "Region", "Kollektiv", "Zielgrössen", "Studientyp", "Spezies", "Zeitdauer",
                "Umgebung");
    }
}
