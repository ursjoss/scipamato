package ch.difty.scipamato.core.persistence.newsletter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation;
import ch.difty.scipamato.core.persistence.JooqTransactionalIntegrationTest;

public class JooqNewsletterTopicRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    @Autowired
    private JooqNewsletterTopicRepo repo;

    @Test
    public void findingAll() {
        assertThat(repo.findAll("en")).hasSize(3);
    }

    //    @Test
    //    public void findingById_withNonExistingId_returnsNull() {
    //        assertThat(repo.findById(-1)).isNull();
    //    }
    //
    //    @Test
    //    public void findById_withExistingId_returnsRecord() {
    //        final NewsletterTopic nl = repo.findById(1);
    //        assertThat(nl).isNotNull();
    //        assertThat(nl.getId()).isEqualTo(1);
    //        assertThat(nl.getTitle()).isEqualTo("Ultrafeine Partikel");
    //    }
    //
    //    @Test
    //    public void addingRecord_savesRecordAndRefreshesId() {
    //        NewsletterTopic nlt = makeMinimalNewsletterTopic();
    //        assertThat(nlt.getId()).isNull();
    //
    //        NewsletterTopic saved = repo.add(nlt);
    //        assertThat(saved).isNotNull();
    //        assertThat(saved.getId())
    //            .isNotNull()
    //            .isGreaterThan(0);
    //        assertThat(saved.getTitle()).isEqualTo("new-title");
    //    }
    //
    //    private NewsletterTopic makeMinimalNewsletterTopic() {
    //        final NewsletterTopic nlt = new NewsletterTopic();
    //        nlt.setTitle("new-title");
    //        return nlt;
    //    }
    //
    //    @Test
    //    public void updatingRecord() {
    //        NewsletterTopic nlt = repo.add(makeMinimalNewsletterTopic());
    //        assertThat(nlt).isNotNull();
    //        assertThat(nlt.getId())
    //            .isNotNull()
    //            .isGreaterThan(0);
    //        final int id = nlt.getId();
    //        assertThat(nlt.getTitle()).isEqualTo("new-title");
    //
    //        nlt.setTitle("mod-title");
    //        repo.update(nlt);
    //        assertThat(nlt.getId()).isEqualTo(id);
    //
    //        NewsletterTopic newCopy = repo.findById(id);
    //        assertThat(newCopy).isNotEqualTo(nlt);
    //        assertThat(newCopy.getId()).isEqualTo(id);
    //        assertThat(newCopy.getTitle()).isEqualTo("mod-title");
    //    }
    //
    //    @Test
    //    public void deletingRecord() {
    //        NewsletterTopic nlt = repo.add(makeMinimalNewsletterTopic());
    //        assertThat(nlt).isNotNull();
    //        assertThat(nlt.getId())
    //            .isNotNull()
    //            .isGreaterThan(0);
    //        final int id = nlt.getId();
    //        assertThat(nlt.getTitle()).isEqualTo("new-title");
    //
    //        NewsletterTopic deleted = repo.delete(id, nlt.getVersion());
    //        assertThat(deleted.getId()).isEqualTo(id);
    //
    //        assertThat(repo.findById(id)).isNull();
    //    }

    @Test
    public void findingNewsletterTopicDefinitions_withUnspecifiedFilter_findsAllDefintions() {
        final List<NewsletterTopicDefinition> ntds = repo.findPageOfNewsletterTopicDefinitions(
            new NewsletterTopicFilter(), new PaginationRequest(Sort.Direction.ASC, "title"));

        assertThat(ntds).hasSize(3);

        NewsletterTopicDefinition ntd = ntds.get(0);
        assertThat(ntd.getId()).isEqualTo(3);
        assertThat(ntd.getTitle()).isEqualTo("Gesundheitsfolgenabschätzung");
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Gesundheitsfolgenabschätzung");
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Health Impact Assessment");
        assertThat(ntd.getTitleInLanguage("fr")).isNull();

        ntd = ntds.get(1);
        assertThat(ntd.getId()).isEqualTo(2);
        assertThat(ntd.getTitle()).isEqualTo("Sterblichkeit");
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Sterblichkeit");
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Mortality");
        assertThat(ntd.getTitleInLanguage("fr")).isNull();

        ntd = ntds.get(2);
        assertThat(ntd.getId()).isEqualTo(1);
        assertThat(ntd.getTitle()).isEqualTo("Ultrafeine Partikel");
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Ultrafeine Partikel");
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Ultrafine Particles");
        assertThat(ntd.getTitleInLanguage("fr")).isNull();
    }

    @Test
    public void findingNewsletterTopicDefinitions_withFilterMatchingSingleGermanTitle_findsOne() {
        final NewsletterTopicFilter filter = new NewsletterTopicFilter();
        filter.setTitleMask("Partikel");
        final List<NewsletterTopicDefinition> ntds = repo.findPageOfNewsletterTopicDefinitions(filter,
            new PaginationRequest(Sort.Direction.ASC, "title"));

        assertThat(ntds).hasSize(1);

        NewsletterTopicDefinition ntd = ntds.get(0);
        assertThat(ntd.getId()).isEqualTo(1);
        assertThat(ntd.getTitle()).isEqualTo("Ultrafeine Partikel");
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Ultrafeine Partikel");
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Ultrafine Particles");
        assertThat(ntd.getTitleInLanguage("fr")).isNull();
    }

    @Test
    public void findingNewsletterTopicDefinitions_withFilterMatchingSeveral() {
        final NewsletterTopicFilter filter = new NewsletterTopicFilter();
        filter.setTitleMask("es");
        final List<NewsletterTopicDefinition> ntds = repo.findPageOfNewsletterTopicDefinitions(filter,
            new PaginationRequest(Sort.Direction.ASC, "title"));

        assertThat(ntds).hasSize(2);

        NewsletterTopicDefinition ntd = ntds.get(0);
        assertThat(ntd.getId()).isEqualTo(3);
        assertThat(ntd.getTitle()).isEqualTo("Gesundheitsfolgenabschätzung");
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Gesundheitsfolgenabschätzung");
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Health Impact Assessment");
        assertThat(ntd.getTitleInLanguage("fr")).isNull();

        ntd = ntds.get(1);
        assertThat(ntd.getId()).isEqualTo(1);
        assertThat(ntd.getTitle()).isEqualTo("Ultrafeine Partikel");
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Ultrafeine Partikel");
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Ultrafine Particles");
        assertThat(ntd.getTitleInLanguage("fr")).isNull();
    }

    @Test
    public void countingNewsletterTopics_withUnspecifiedFilter_findsAllDefintions() {
        assertThat(repo.countByFilter(new NewsletterTopicFilter())).isEqualTo(3);
    }

    @Test
    public void countingNewsletterTopics_withFilter_findsAllMatchingDefintions() {
        final NewsletterTopicFilter filter = new NewsletterTopicFilter();
        filter.setTitleMask("es");
        assertThat(repo.countByFilter(filter)).isEqualTo(2);
    }

    @Test
    public void countingNewsletterTopics_withNonMatchingFilter_findsNone() {
        final NewsletterTopicFilter filter = new NewsletterTopicFilter();
        filter.setTitleMask("foobar");
        assertThat(repo.countByFilter(filter)).isEqualTo(0);
    }

    @Test
    public void gettingMainLanguage() {
        assertThat(repo.getMainLanguage()).isEqualTo("de");
    }

    @Test
    public void findingMainLanguage() {
        NewsletterTopicDefinition ntd = repo.newUnpersistedNewsletterTopicDefinition();

        assertThat(ntd.getId()).isNull();
        assertThat(ntd.getMainLanguageCode()).isEqualTo("de");
        assertThat(ntd.getTitle()).isEqualTo("n.a.");
        assertThat(ntd.getTitleInLanguage("de")).isNull();
        assertThat(ntd.getTranslations()).hasSize(3);

        final Collection<NewsletterTopicTranslation> translations = ntd
            .getTranslations()
            .values();
        assertThat(translations)
            .extracting("langCode")
            .containsOnly("de", "en", "fr");
        assertThat(translations)
            .extracting("id")
            .containsExactly(null, null, null);
        assertThat(translations)
            .extracting("title")
            .containsExactly(null, null, null);
    }
}
