package ch.difty.scipamato.core.persistence.keyword;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation;
import ch.difty.scipamato.core.persistence.JooqBaseIntegrationTest;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

public class JooqKeywordRepoIntegrationTest extends JooqBaseIntegrationTest {

    @Autowired
    private JooqKeywordRepo repo;

    @Test
    public void findingAll() {
        assertThat(repo.findAll("en")).hasSize(3);
    }

    @Test
    public void findingKeywordDefinitions_withUnspecifiedFilter_findsAllDefinitions() {
        final List<KeywordDefinition> kds = repo.findPageOfKeywordDefinitions(new KeywordFilter(),
            new PaginationRequest(Sort.Direction.ASC, "name"));

        assertThat(kds).hasSize(3);

        KeywordDefinition ntd = kds.get(0);
        assertThat(ntd.getId()).isEqualTo(1);
        assertThat(ntd.getName()).isEqualTo("Aerosol");
        assertThat(ntd.getSearchOverride()).isNull();
        assertThat(ntd.getNameInLanguage("de")).isEqualTo("Aerosol");
        assertThat(ntd.getNameInLanguage("en")).isEqualTo("Aerosol");
        assertThat(ntd.getNameInLanguage("fr")).isEqualTo("Aérosol");

        ntd = kds.get(1);
        assertThat(ntd.getId()).isEqualTo(2);
        assertThat(ntd.getName()).isEqualTo("Aktivität, eingeschränkte");
        assertThat(ntd.getSearchOverride()).isEqualTo("Aktivität");
        assertThat(ntd.getNameInLanguage("de")).isEqualTo("Aktivität, eingeschränkte");
        assertThat(ntd.getNameInLanguage("en")).isEqualTo("Restricted activity");
        assertThat(ntd.getNameInLanguage("fr")).isEqualTo("Activités réduites");

        ntd = kds.get(2);
        assertThat(ntd.getId()).isEqualTo(3);
        assertThat(ntd.getName()).isEqualTo("Allergie (not Atopie)");
        assertThat(ntd.getSearchOverride()).isNull();
        assertThat(ntd.getNameInLanguage("de")).isEqualTo("Allergie (not Atopie)");
        assertThat(ntd.getNameInLanguage("en")).isEqualTo("Allergies");
        assertThat(ntd.getNameInLanguage("fr")).isEqualTo("Allergie");
    }

    @Test
    public void findingKeywordDefinitions_withFilterMatchingSingleGermanTitle_findsOne() {
        final KeywordFilter filter = new KeywordFilter();
        filter.setNameMask("Allergie (not Atopie)");
        final List<KeywordDefinition> kds = repo.findPageOfKeywordDefinitions(filter,
            new PaginationRequest(Sort.Direction.ASC, "name"));

        assertThat(kds).hasSize(1);

        KeywordDefinition ntd = kds.get(0);
        assertThat(ntd.getId()).isEqualTo(3);
        assertThat(ntd.getName()).isEqualTo("Allergie (not Atopie)");
        assertThat(ntd.getSearchOverride()).isNull();
        assertThat(ntd.getNameInLanguage("de")).isEqualTo("Allergie (not Atopie)");
        assertThat(ntd.getNameInLanguage("en")).isEqualTo("Allergies");
        assertThat(ntd.getNameInLanguage("fr")).isEqualTo("Allergie");
    }

    @Test
    public void findingKeywordDefinitions_haveVersionFieldsPopulated() {
        final KeywordFilter filter = new KeywordFilter();
        filter.setNameMask("Allergie");
        final List<KeywordDefinition> kds = repo.findPageOfKeywordDefinitions(filter,
            new PaginationRequest(Sort.Direction.ASC, "name"));

        assertThat(kds).hasSize(1);

        KeywordDefinition ntd = kds.get(0);

        assertThat(ntd.getVersion()).isEqualTo(1);
        assertThat(ntd.getCreated()).isNull();
        assertThat(ntd.getLastModified()).isNull();

        Collection<KeywordTranslation> translations = ntd
            .getTranslations()
            .values();
        assertThat(translations).isNotEmpty();
        KeywordTranslation tr = translations
            .iterator()
            .next();
        assertThat(tr.getVersion()).isEqualTo(1);
        assertThat(tr.getCreated()).isNull();
        assertThat(tr.getLastModified()).isNull();
    }

    @Test
    public void findingKeywordDefinitions_withFilterMatchingSeveral() {
        final KeywordFilter filter = new KeywordFilter();
        filter.setNameMask("er");
        final List<KeywordDefinition> kds = repo.findPageOfKeywordDefinitions(filter,
            new PaginationRequest(Sort.Direction.ASC, "name"));

        assertThat(kds).hasSize(2);

        KeywordDefinition ntd = kds.get(0);
        assertThat(ntd.getId()).isEqualTo(1);
        assertThat(ntd.getName()).isEqualTo("Aerosol");
        assertThat(ntd.getNameInLanguage("de")).isEqualTo("Aerosol");
        assertThat(ntd.getNameInLanguage("en")).isEqualTo("Aerosol");
        assertThat(ntd.getNameInLanguage("fr")).isEqualTo("Aérosol");

        ntd = kds.get(1);
        assertThat(ntd.getId()).isEqualTo(3);
        assertThat(ntd.getName()).isEqualTo("Allergie (not Atopie)");
        assertThat(ntd.getNameInLanguage("de")).isEqualTo("Allergie (not Atopie)");
        assertThat(ntd.getNameInLanguage("en")).isEqualTo("Allergies");
        assertThat(ntd.getNameInLanguage("fr")).isEqualTo("Allergie");
    }

    @Test
    public void countingKeywords_witNullFilter_findsAllDefinitions() {
        assertThat(repo.countByFilter(null)).isEqualTo(3);
    }

    @Test
    public void countingKeywords_withUnspecifiedFilter_findsAllDefinitions() {
        assertThat(repo.countByFilter(new KeywordFilter())).isEqualTo(3);
    }

    @Test
    public void countingKeywords_withFilter_findsAllMatchingDefinitions() {
        final KeywordFilter filter = new KeywordFilter();
        filter.setNameMask("es");
        assertThat(repo.countByFilter(filter)).isEqualTo(2);
    }

    @Test
    public void countingKeywords_withNaFilter_findsThem() {
        final KeywordFilter filter = new KeywordFilter();
        filter.setNameMask("n.a.");
        assertThat(repo.countByFilter(filter)).isEqualTo(0);
    }

    @Test
    public void countingKeywords_withNonMatchingFilter_findsNone() {
        final KeywordFilter filter = new KeywordFilter();
        filter.setNameMask("foobar");
        assertThat(repo.countByFilter(filter)).isEqualTo(0);
    }

    @Test
    public void gettingMainLanguage() {
        assertThat(repo.getMainLanguage()).isEqualTo("de");
    }

    @Test
    public void findingMainLanguage() {
        KeywordDefinition ntd = repo.newUnpersistedKeywordDefinition();

        assertThat(ntd.getId()).isNull();
        assertThat(ntd.getMainLanguageCode()).isEqualTo("de");
        assertThat(ntd.getName()).isEqualTo("n.a.");
        assertThat(ntd.getNameInLanguage("de")).isNull();
        assertThat(ntd
            .getTranslations()
            .asMap()).hasSize(3);

        final Collection<KeywordTranslation> translations = ntd
            .getTranslations()
            .values();
        assertThat(translations)
            .extracting("langCode")
            .containsOnly("de", "en", "fr");
        assertThat(translations)
            .extracting("id")
            .containsExactly(null, null, null);
        assertThat(translations)
            .extracting("name")
            .containsExactly(null, null, null);
    }

    @Test
    public void findingKeywordDefinition_withNonExistingId_returnsNull() {
        assertThat(repo.findKeywordDefinitionById(-1)).isNull();
    }

    @Test
    public void findingKeywordDefinition_withExistingId_loadsWithAllLanguages() {
        final KeywordDefinition existing = repo.findKeywordDefinitionById(1);

        assertThat(existing).isNotNull();
        assertThat(existing.getId()).isEqualTo(1);
        assertThat(existing.getName()).isEqualTo("Aerosol");
        assertThat(existing
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(existing.getNameInLanguage("de")).isEqualTo("Aerosol");
        assertThat(existing.getNameInLanguage("en")).isEqualTo("Aerosol");
        assertThat(existing.getNameInLanguage("fr")).isEqualTo("Aérosol");
    }

    @Test
    public void insertingRecord_savesRecordAndRefreshesId() {
        final KeywordTranslation kt_de = new KeywordTranslation(null, "de", "foo_de", 0);
        final KeywordTranslation kt_en = new KeywordTranslation(null, "en", "foo1_en", 0);
        final KeywordTranslation kt_fr = new KeywordTranslation(null, "fr", "foo1_fr", 0);
        final KeywordDefinition ntd = new KeywordDefinition(null, "de", 0, kt_de, kt_en, kt_fr);

        assertThat(ntd.getId()).isNull();
        assertThat(ntd
            .getTranslations()
            .values())
            .extracting("id")
            .containsExactly(null, null, null);

        KeywordDefinition saved = repo.insert(ntd);

        assertThat(saved).isNotNull();
        assertThat(saved.getId())
            .isNotNull()
            .isGreaterThan(0);
        assertThat(saved.getName()).isEqualTo("foo_de");
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
        final KeywordDefinition ntd = repo.findKeywordDefinitionById(2);

        assertThat(ntd).isNotNull();
        assertThat(ntd.getId()).isEqualTo(2);
        assertThat(ntd.getSearchOverride()).isEqualTo("Aktivität");
        assertThat(ntd
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(ntd.getNameInLanguage("de")).isEqualTo("Aktivität, eingeschränkte");
        assertThat(ntd.getNameInLanguage("en")).isEqualTo("Restricted activity");
        assertThat(ntd.getNameInLanguage("fr")).isEqualTo("Activités réduites");
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

        ntd.setSearchOverride("a");
        ntd.setNameInLanguage("de", "ae");
        ntd.setNameInLanguage("fr", "ar");

        KeywordDefinition updated = repo.update(ntd);

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(2);
        assertThat(updated.getSearchOverride()).isEqualTo("a");
        assertThat(updated
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(updated.getNameInLanguage("de")).isEqualTo("ae");
        assertThat(updated.getNameInLanguage("en")).isEqualTo("Restricted activity");
        assertThat(updated.getNameInLanguage("fr")).isEqualTo("ar");

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
            .getVersion()).isEqualTo(2);
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
                .hasMessage("Record in table 'keyword' has been modified prior to the delete attempt. Aborting....");
        }
    }

    @Test
    public void deleting_withExistingIdAndVersion_deletes() {
        // insert new record to the database and verify it's there
        KeywordDefinition ntd = new KeywordDefinition(null, "de", null);
        KeywordDefinition persisted = repo.insert(ntd);
        final int id = persisted.getId();
        final int version = persisted.getVersion();
        assertThat(repo.findKeywordDefinitionById(id)).isNotNull();

        // delete the record
        KeywordDefinition deleted = repo.delete(id, version);
        assertThat(deleted).isNotNull();
        assertThat(deleted.getId()).isEqualTo(id);

        // verify the record is not there anymore
        assertThat(repo.findKeywordDefinitionById(id)).isNull();
    }

    @Test
    public void canLoadKeywordWithMultipleTranslationsInOneLanguage() {
        final KeywordDefinition kd = repo.findKeywordDefinitionById(3);
        assertThat(kd).isNotNull();
        assertThat(kd.getTranslationsAsString()).isEqualTo(
            "DE: 'Allergie (not Atopie)','Allergie'; EN: 'Allergies'; FR: 'Allergie'");
    }

    @Test
    public void findingKeywordDefinitions_sortedByName() {
        assertSortedList("name", 3);
    }

    @Test
    public void findingKeywordDefinitions_sortedByUndefinedProperty() {
        assertSortedList("whatever", 1);
    }

    private void assertSortedList(final String sortProperty, final Integer id) {
        final List<KeywordDefinition> cds = repo.findPageOfKeywordDefinitions(new KeywordFilter(),
            new PaginationRequest(0, 10, Sort.Direction.DESC, sortProperty));

        assertThat(cds).hasSize(3);

        KeywordDefinition cd = cds.get(0);
        assertThat(cd.getId()).isEqualTo(id);
    }

}
