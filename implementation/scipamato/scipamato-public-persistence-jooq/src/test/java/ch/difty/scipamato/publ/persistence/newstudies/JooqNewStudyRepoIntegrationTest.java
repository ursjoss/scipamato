package ch.difty.scipamato.publ.persistence.newstudies;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.publ.entity.NewStudy;
import ch.difty.scipamato.publ.entity.NewStudyPageLink;
import ch.difty.scipamato.publ.entity.NewStudyTopic;
import ch.difty.scipamato.publ.entity.Newsletter;
import ch.difty.scipamato.publ.persistence.JooqTransactionalIntegrationTest;

public class JooqNewStudyRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    @Autowired
    private NewStudyRepository repo;

    @Test
    public void findingTopicsOfNewsletter1_inEnglish_returnsNoResults() {
        assertThat(repo.findNewStudyTopicsForNewsletter(1, "en"))
            .isNotNull()
            .isEmpty();
    }

    @Test
    public void findingTopicsOfNewsletter1_inGerman_returnsResults() {
        List<NewStudyTopic> result = repo.findNewStudyTopicsForNewsletter(1, "de");
        assertThat(result)
            .isNotNull()
            .hasSize(4);

        assertThat(result)
            .extracting("sort")
            .containsExactly(1, 2, 3, 4);
        assertThat(result)
            .extracting("title")
            .containsExactly("Tiefe Belastungen", "Hirnleistung und neurodegenerative Erkrankungen",
                "Feinstaubkomponenten und PAK", "Hirnschlag");

        assertThat(result
            .get(0)
            .getStudies()).hasSize(2);
        assertThat(result
            .get(1)
            .getStudies()).hasSize(3);
        assertThat(result
            .get(2)
            .getStudies()).hasSize(3);
        assertThat(result
            .get(3)
            .getStudies()).hasSize(2);

        assertThat(result
            .get(0)
            .getStudies())
            .extracting("number")
            .containsExactly(8924l, 8993l);
        assertThat(result
            .get(1)
            .getStudies())
            .extracting("number")
            .containsExactly(8973l, 8983l, 8984l);
        assertThat(result
            .get(2)
            .getStudies())
            .extracting("number")
            .containsExactly(8933l, 8897l, 8861l);
        assertThat(result
            .get(3)
            .getStudies())
            .extracting("number")
            .containsExactly(8916l, 8934l);

        NewStudy ns = result
            .get(0)
            .getStudies()
            .get(0);
        assertThat(ns.getSort()).isEqualTo(1);
        assertThat(ns.getNumber()).isEqualTo(8924l);
        assertThat(ns.getYear()).isEqualTo(2017);
        assertThat(ns.getAuthors()).isEqualTo("Di et al.");
        assertThat(ns.getReference()).isEqualTo("(Di et al.; 2017)");
        assertThat(ns.getHeadline()).startsWith(
            "USA: Grosse Kohortenstudie zeigt, dass auch ein PM2.5-Grenzwert von 12");
        assertThat(ns.getDescription()).startsWith(
            "Registerkohortenstudie in den USA zur Untersuchung, ob die Sterblichkeit");
    }

    @Test
    public void findingMostRecentNewsletterId() {
        assertThat(repo.findMostRecentNewsletterId())
            .isPresent()
            .hasValue(2);
    }

    @Test
    public void findingArchivedNewsletters() {
        final List<Newsletter> results = repo.findArchivedNewsletters("en");

        assertThat(results).hasSize(2);
        assertThat(results)
            .extracting(n -> n.getMonthName("en"))
            .containsExactly("June 2018", "April 2018");
        assertThat(results)
            .extracting(n -> n.getMonthName("fr"))
            .containsExactly("juin 2018", "avril 2018");
        assertThat(results)
            .extracting(n -> n.getMonthName("de"))
            .containsExactly("Juni 2018", "April 2018");
    }

    @Test
    public void findingNewStudyPageLinks_withEnglish() {
        final List<NewStudyPageLink> results = repo.findNewStudyPageLinks("en");

        assertThat(results).hasSize(2);
        assertThat(results)
            .extracting(NewStudyPageLink.NewProjectPageLinkFields.LANG_CODE.getName())
            .containsOnly("en");
        assertThat(results)
            .extracting(NewStudyPageLink.NewProjectPageLinkFields.SORT.getName())
            .containsExactly(1, 2);
        assertThat(results)
            .extracting(NewStudyPageLink.NewProjectPageLinkFields.TITLE.getName())
            .containsExactly("Search", "Project Repository");
        assertThat(results)
            .extracting(NewStudyPageLink.NewProjectPageLinkFields.URL.getName())
            .containsExactly("https://duckduckgo.com/", "https://github.com/ursjoss/scipamato");
    }

    @Test
    public void findingNewStudyPageLinks_withGerman() {
        final List<NewStudyPageLink> results = repo.findNewStudyPageLinks("de");

        assertThat(results).hasSize(2);
        assertThat(results)
            .extracting(NewStudyPageLink.NewProjectPageLinkFields.LANG_CODE.getName())
            .containsOnly("de");
        assertThat(results)
            .extracting(NewStudyPageLink.NewProjectPageLinkFields.TITLE.getName())
            .containsExactly("Web Suche", "Projekt Code");
    }
}