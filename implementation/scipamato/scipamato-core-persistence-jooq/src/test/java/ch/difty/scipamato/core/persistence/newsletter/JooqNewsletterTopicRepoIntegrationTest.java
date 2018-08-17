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

    @Test
    public void findingNewsletterTopicDefinitions_withUnspecifiedFilter_findsAllDefinitions() {
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
    public void findingNewsletterTopicDefinitions_haveVersionFieldsPopulated() {
        final NewsletterTopicFilter filter = new NewsletterTopicFilter();
        filter.setTitleMask("Partikel");
        final List<NewsletterTopicDefinition> ntds = repo.findPageOfNewsletterTopicDefinitions(filter,
            new PaginationRequest(Sort.Direction.ASC, "title"));

        assertThat(ntds).hasSize(1);

        NewsletterTopicDefinition ntd = ntds.get(0);

        assertThat(ntd.getVersion()).isEqualTo(1);
        assertThat(ntd.getCreated()).isNull();
        assertThat(ntd.getCreatedBy()).isNull();
        assertThat(ntd.getLastModified()).isNull();
        assertThat(ntd.getLastModifiedBy()).isNull();

        Collection<NewsletterTopicTranslation> translations = ntd
            .getTranslations()
            .values();
        assertThat(translations).isNotEmpty();
        NewsletterTopicTranslation tr = translations
            .iterator()
            .next();
        assertThat(tr.getVersion()).isEqualTo(1);
        assertThat(tr.getCreated()).isNull();
        assertThat(tr.getCreatedBy()).isNull();
        assertThat(tr.getLastModified()).isNull();
        assertThat(tr.getLastModifiedBy()).isNull();
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

    @Test
    public void addingRecord_savesRecordAndRefreshesId() {
        final NewsletterTopicTranslation ntt_de = new NewsletterTopicTranslation(null, "de", "foo_de", 0);
        final NewsletterTopicTranslation ntt_en = new NewsletterTopicTranslation(null, "en", "foo1_en", 0);
        final NewsletterTopicTranslation ntt_fr = new NewsletterTopicTranslation(null, "fr", "foo1_fr", 0);
        final NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(null, "de", 0, ntt_de, ntt_en, ntt_fr);

        assertThat(ntd.getId()).isNull();
        assertThat(ntd
            .getTranslations()
            .values())
            .extracting("id")
            .containsExactly(null, null, null);

        NewsletterTopicDefinition saved = repo.add(ntd);

        assertThat(saved).isNotNull();
        assertThat(saved.getId())
            .isNotNull()
            .isGreaterThan(0);
        assertThat(saved.getTitle()).isEqualTo("foo_de");
        assertThat(saved
            .getTranslations()
            .size()).isEqualTo(3);
        assertThat(saved
            .getTranslations()
            .values())
            .extracting("version")
            .containsExactly(1, 1, 1);
    }

    @Test
    public void updatingRecord() {
        final NewsletterTopicFilter filter = new NewsletterTopicFilter();
        filter.setTitleMask("Partikel");
        final NewsletterTopicDefinition ntd = repo
            .findPageOfNewsletterTopicDefinitions(filter, new PaginationRequest(Sort.Direction.ASC, "title"))
            .get(0);

        assertThat(ntd).isNotNull();
        assertThat(ntd.getId()).isEqualTo(1);
        assertThat(ntd.getTranslations()).hasSize(3);
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Ultrafeine Partikel");
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Ultrafine Particles");
        assertThat(ntd.getTitleInLanguage("fr")).isNull();
        assertThat(ntd
            .getTranslations()
            .get("de")
            .getVersion()).isEqualTo(1);
        assertThat(ntd
            .getTranslations()
            .get("en")
            .getVersion()).isEqualTo(1);

        ntd.setTitleInLanguage("de", "ufp");
        ntd.setTitleInLanguage("fr", "foo");

        NewsletterTopicDefinition updated = repo.update(ntd);

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(1);
        assertThat(updated.getTranslations()).hasSize(3);
        assertThat(updated.getTitleInLanguage("de")).isEqualTo("ufp");
        assertThat(updated.getTitleInLanguage("en")).isEqualTo("Ultrafine Particles");
        assertThat(updated.getTitleInLanguage("fr")).isEqualTo("foo");

        assertThat(updated.getVersion()).isEqualTo(2);
        assertThat(updated
            .getTranslations()
            .get("de")
            .getVersion()).isEqualTo(2);
        assertThat(updated
            .getTranslations()
            .get("en")
            .getVersion()).isEqualTo(2);
        assertThat(updated
            .getTranslations()
            .get("fr")
            .getVersion()).isEqualTo(1);
    }

}
