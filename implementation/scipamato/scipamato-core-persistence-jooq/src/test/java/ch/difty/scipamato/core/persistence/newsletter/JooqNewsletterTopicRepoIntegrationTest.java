package ch.difty.scipamato.core.persistence.newsletter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation;
import ch.difty.scipamato.core.persistence.JooqTransactionalIntegrationTest;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

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
    public void findingNewsletterTopicDefinitions_withFilterMatchingNa_findsSomeWithMissingTranslations() {
        final NewsletterTopicFilter filter = new NewsletterTopicFilter();
        filter.setTitleMask("n.a.");
        final List<NewsletterTopicDefinition> ntds = repo.findPageOfNewsletterTopicDefinitions(filter,
            new PaginationRequest(Sort.Direction.ASC, "title"));

        assertThat(ntds).hasSize(3);

        NewsletterTopicDefinition ntd = ntds.get(0);
        assertThat(ntd.getId()).isEqualTo(3);
        assertThat(ntd.getTitle()).isEqualTo("Gesundheitsfolgenabschätzung");
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Gesundheitsfolgenabschätzung");
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
        assertThat(ntd.getLastModified()).isNull();

        Collection<NewsletterTopicTranslation> translations = ntd
            .getTranslations()
            .values();
        assertThat(translations).isNotEmpty();
        NewsletterTopicTranslation tr = translations
            .iterator()
            .next();
        assertThat(tr.getVersion()).isEqualTo(1);
        assertThat(tr.getCreated()).isNull();
        assertThat(tr.getLastModified()).isNull();
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
    public void countingNewsletterTopics_withUnspecifiedFilter_findsAllDefinitions() {
        assertThat(repo.countByFilter(new NewsletterTopicFilter())).isEqualTo(3);
    }

    @Test
    public void countingNewsletterTopics_withFilter_findsAllMatchingDefinitions() {
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
        assertThat(ntd
            .getTranslations()
            .asMap()).hasSize(3);

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
    public void findingNewsletterTopicDefinition_withNonExistingId_returnsNull() {
        assertThat(repo.findNewsletterTopicDefinitionById(-1)).isNull();
    }

    @Test
    public void findingNewsletterTopicDefinition_withExistingId_loadsWithAllLanguages() {
        final NewsletterTopicDefinition existing = repo.findNewsletterTopicDefinitionById(1);

        assertThat(existing).isNotNull();
        assertThat(existing.getId()).isEqualTo(1);
        assertThat(existing.getTitle()).isEqualTo("Ultrafeine Partikel");
        assertThat(existing
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(existing.getTitleInLanguage("de")).isEqualTo("Ultrafeine Partikel");
        assertThat(existing.getTitleInLanguage("en")).isEqualTo("Ultrafine Particles");
        assertThat(existing.getTitleInLanguage("fr")).isNull();
    }

    @Test
    public void insertingRecord_savesRecordAndRefreshesId() {
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

        NewsletterTopicDefinition saved = repo.insert(ntd);

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
        final NewsletterTopicDefinition ntd = repo.findNewsletterTopicDefinitionById(1);

        assertThat(ntd).isNotNull();
        assertThat(ntd.getId()).isEqualTo(1);
        assertThat(ntd
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Ultrafeine Partikel");
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Ultrafine Particles");
        assertThat(ntd.getTitleInLanguage("fr")).isNull();
        assertThat(ntd
            .getTranslations()
            .get("de")
            .get(0)
            .getVersion()).isEqualTo(1);
        assertThat(ntd
            .getTranslations()
            .get("en")
            .get(0)
            .getVersion()).isEqualTo(1);

        ntd.setTitleInLanguage("de", "ufp");
        ntd.setTitleInLanguage("fr", "foo");

        NewsletterTopicDefinition updated = repo.update(ntd);

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(1);
        assertThat(updated
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(updated.getTitleInLanguage("de")).isEqualTo("ufp");
        assertThat(updated.getTitleInLanguage("en")).isEqualTo("Ultrafine Particles");
        assertThat(updated.getTitleInLanguage("fr")).isEqualTo("foo");

        assertThat(updated.getVersion()).isEqualTo(2);
        assertThat(updated
            .getTranslations()
            .get("de")
            .get(0)
            .getVersion()).isEqualTo(2);
        assertThat(updated
            .getTranslations()
            .get("en")
            .get(0)
            .getVersion()).isEqualTo(2);
        assertThat(updated
            .getTranslations()
            .get("fr")
            .get(0)
            .getVersion()).isEqualTo(1);
    }

    @Test
    public void deleting_withNonExistingId_returnsNull() {
        assertThat(repo.delete(-1, 1)).isNull();
    }

    @Test
    public void deleting_withExistingId_butWrongVersion_throwsOptimisticLockingException() {
        try {
            repo.delete(1, -1);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException.class)
                .hasMessage(
                    "Record in table 'newsletter_topic' has been modified prior to the delete attempt. Aborting....");
        }
    }

    @Test
    public void deleting_withExistingIdAndVersion_deletes() {
        // insert new record to the database and verify it's there
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(null, "de", null);
        NewsletterTopicDefinition persisted = repo.insert(ntd);
        final int id = persisted.getId();
        final int version = persisted.getVersion();
        assertThat(repo.findNewsletterTopicDefinitionById(id)).isNotNull();

        // delete the record
        NewsletterTopicDefinition deleted = repo.delete(id, version);
        assertThat(deleted).isNotNull();
        assertThat(deleted.getId()).isEqualTo(id);

        // verify the record is not there anymore
        assertThat(repo.findNewsletterTopicDefinitionById(id)).isNull();
    }

    @Test
    public void findingPersistedSortedNewsletterTopicsForNewsletterWithId() {
        assertThat(repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(1)).isEmpty();
    }

    @Test
    public void findingAllSortedNewsletterTopicsForNewsletterWithId() {
        assertThat(repo.findAllSortedNewsletterTopicsForNewsletterWithId(1)).hasSize(3);
    }

    @Test
    public void removingObsoleteNewsletterTopicsFromSort() {
        repo.removeObsoleteNewsletterTopicsFromSort(1);
        // TODO currently only asserting that the method runs without failure. Need test data and actually assert the behavior
    }

    @Test
    public void savingSortedNewsletterTopics() {
        final int newsletterId = 1;

        final List<NewsletterNewsletterTopic> initialRecords = repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(
            newsletterId);
        assertThat(initialRecords).isEmpty();

        final List<NewsletterNewsletterTopic> topics = new ArrayList<>();
        topics.add(new NewsletterNewsletterTopic(newsletterId, 1, 1, "foo"));
        topics.add(new NewsletterNewsletterTopic(newsletterId, 2, 2, "bar"));
        topics.add(new NewsletterNewsletterTopic(newsletterId + 1, 3, 3, "baz"));

        repo.saveSortedNewsletterTopics(newsletterId, topics);

        final List<NewsletterNewsletterTopic> newRecords = repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(
            newsletterId);
        assertThat(newRecords)
            .extracting("sort")
            .containsExactly(1, 2);

        repo.removeObsoleteNewsletterTopicsFromSort(newsletterId);

        assertThat(repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)).isEmpty();
    }

    @Test
    public void findingNewsletterTopicDefinitions_sortedByName() {
        assertSortedList("name", 1);
    }

    @Test
    public void findingNewsletterTopicDefinitions_sortedByUndefinedProperty() {
        assertSortedList("whatever", 1);
    }

    private void assertSortedList(final String sortProperty, final Integer id) {
        final List<NewsletterTopicDefinition> cds = repo.findPageOfNewsletterTopicDefinitions(
            new NewsletterTopicFilter(), new PaginationRequest(0, 10, Sort.Direction.DESC, sortProperty));

        assertThat(cds).hasSize(3);

        NewsletterTopicDefinition ntd = cds.get(0);
        assertThat(ntd.getId()).isEqualTo(id);
    }
}
