package ch.difty.scipamato.publ.persistence.newstudies;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.publ.entity.NewStudy;
import ch.difty.scipamato.publ.entity.NewStudyPageLink;
import ch.difty.scipamato.publ.entity.NewStudyTopic;
import ch.difty.scipamato.publ.entity.Newsletter;
import ch.difty.scipamato.publ.persistence.JooqBaseIntegrationTest;

@SuppressWarnings("SpellCheckingInspection")
public class JooqNewStudyRepoIntegrationTest extends JooqBaseIntegrationTest {

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
            .containsExactly(8924L, 8993L);
        assertThat(result
            .get(1)
            .getStudies())
            .extracting("number")
            .containsExactly(8973L, 8983L, 8984L);
        assertThat(result
            .get(2)
            .getStudies())
            .extracting("number")
            .containsExactly(8933L, 8897L, 8861L);
        assertThat(result
            .get(3)
            .getStudies())
            .extracting("number")
            .containsExactly(8916L, 8934L);

        NewStudy ns = result
            .get(0)
            .getStudies()
            .get(0);
        assertThat(ns.getSort()).isEqualTo(1);
        assertThat(ns.getNumber()).isEqualTo(8924L);
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
    public void findingArchivedNewsletters_with14ToFind_returnsUpTo14() {
        final List<Newsletter> results = repo.findArchivedNewsletters(14, "en");

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
    public void findingArchivedNewsletters_withOneToFind_returnsOne() {
        final List<Newsletter> results = repo.findArchivedNewsletters(1, "en");

        assertThat(results).hasSize(1);
        assertThat(results)
            .extracting(n -> n.getMonthName("en"))
            .containsExactly("June 2018");
        assertThat(results)
            .extracting(n -> n.getMonthName("fr"))
            .containsExactly("juin 2018");
        assertThat(results)
            .extracting(n -> n.getMonthName("de"))
            .containsExactly("Juni 2018");
    }

    @Test
    public void findingNewStudyPageLinks_withEnglish() {
        final List<NewStudyPageLink> results = repo.findNewStudyPageLinks("en");

        assertThat(results).hasSize(2);
        assertThat(results)
            .extracting(NewStudyPageLink.NewStudyPageLinkFields.LANG_CODE.getName())
            .containsOnly("en");
        assertThat(results)
            .extracting(NewStudyPageLink.NewStudyPageLinkFields.SORT.getName())
            .containsExactly(1, 2);
        assertThat(results)
            .extracting(NewStudyPageLink.NewStudyPageLinkFields.TITLE.getName())
            .containsExactly("Search", "Project Repository");
        assertThat(results)
            .extracting(NewStudyPageLink.NewStudyPageLinkFields.URL.getName())
            .containsExactly("https://duckduckgo.com/", "https://github.com/ursjoss/scipamato");
    }

    @Test
    public void findingNewStudyPageLinks_withGerman() {
        final List<NewStudyPageLink> results = repo.findNewStudyPageLinks("de");

        assertThat(results).hasSize(2);
        assertThat(results)
            .extracting(NewStudyPageLink.NewStudyPageLinkFields.LANG_CODE.getName())
            .containsOnly("de");
        assertThat(results)
            .extracting(NewStudyPageLink.NewStudyPageLinkFields.TITLE.getName())
            .containsExactly("Web Suche", "Projekt Code");
    }

    @Test
    public void findingIdOfNewsletterWithIssue_forExistingNewsletter_findsIt() {
        final Optional<Integer> idOpt = repo.findIdOfNewsletterWithIssue("2018/06");
        assertThat(idOpt).isPresent();
        //noinspection OptionalGetWithoutIsPresent
        assertThat(idOpt.get()).isEqualTo(2);
    }

    @Test
    public void findingIdOfNewsletterWithIssue_forNonExistingNewsletter_returnsEmptyOptional() {
        final Optional<Integer> idOpt = repo.findIdOfNewsletterWithIssue("2018/06xxx");
        assertThat(idOpt).isNotPresent();
    }
}