package ch.difty.scipamato.core.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Collection;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code_class.CodeClassDefinition;
import ch.difty.scipamato.core.entity.code_class.CodeClassFilter;
import ch.difty.scipamato.core.entity.code_class.CodeClassTranslation;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.persistence.codeclass.JooqCodeClassRepo;

@SuppressWarnings("SpellCheckingInspection")
@Slf4j
@JooqTest
@Testcontainers
class JooqCodeClassRepoIntegrationTest {

    private static final int CODE_CLASS_COUNT = 8;

    @Autowired
    private JooqCodeClassRepo repo;

    @Test
    void findingAllCodesClassesInGerman() {
        List<CodeClass> ccs = repo.find("de");
        assertThat(ccs)
            .isNotEmpty()
            .hasSize(CODE_CLASS_COUNT);
        ccs.forEach((cc) -> log.debug(cc.toString()));
    }

    @Test
    void findingAllCodesClassesInEnglish() {
        List<CodeClass> ccs = repo.find("en");
        assertThat(ccs)
            .isNotEmpty()
            .hasSize(CODE_CLASS_COUNT);
        ccs.forEach((cc) -> log.debug(cc.toString()));
    }

    @Test
    void findingAllCodesClassesInFrench() {
        List<CodeClass> ccs = repo.find("fr");
        assertThat(ccs)
            .isNotEmpty()
            .hasSize(CODE_CLASS_COUNT);
        ccs.forEach((cc) -> log.debug(cc.toString()));
    }

    @Test
    void findingCodeClassDefinitions_withUnspecifiedFilter_findsAllDefinitions() {
        final List<CodeClassDefinition> ccds = repo.findPageOfCodeClassDefinitions(new CodeClassFilter(),
            new PaginationRequest(0, 8, Sort.Direction.ASC, "name"));

        assertThat(ccds).hasSize(8);

        CodeClassDefinition ccd = ccds.get(0);
        assertThat(ccd.getName()).isEqualTo("Kollektiv");
        assertThat(ccd.getNameInLanguage("de")).startsWith("Kollektiv");
        assertThat(ccd.getNameInLanguage("en")).startsWith("Study Population");
        assertThat(ccd.getNameInLanguage("fr")).startsWith("Population");

        ccd = ccds.get(1);
        assertThat(ccd.getName()).isEqualTo("Region");
        assertThat(ccd.getNameInLanguage("de")).startsWith("Region");
        assertThat(ccd.getNameInLanguage("en")).startsWith("Region");
        assertThat(ccd.getNameInLanguage("fr")).startsWith("Région");
    }

    @Test
    void findingCodeClassDefinitions_sortingByUndefinedField_doesNotSort() {
        final List<CodeClassDefinition> ccds = repo.findPageOfCodeClassDefinitions(new CodeClassFilter(),
            new PaginationRequest(0, 8, Sort.Direction.ASC, "foobar"));

        assertThat(ccds).hasSize(8);

        CodeClassDefinition ccd = ccds.get(0);
        assertThat(ccd.getName()).isEqualTo("Schadstoffe");

        ccd = ccds.get(1);
        assertThat(ccd.getName()).isEqualTo("Region");
    }

    @Test
    void findingCodeClassDefinitions_withUnspecifiedFilter_withReverseSortByTranslations() {
        final CodeClassFilter filter = new CodeClassFilter();
        filter.setDescriptionMask("en");
        final List<CodeClassDefinition> ccds = repo.findPageOfCodeClassDefinitions(filter,
            new PaginationRequest(0, 8, Sort.Direction.DESC, "translationsAsString"));

        assertThat(ccds).hasSize(3);

        CodeClassDefinition ccd = ccds.get(0);
        assertThat(ccd.getName()).isEqualTo("Zielgrössen");
    }

    @Test
    void findingCodeClassDefinitions_withFilterMatchingSingleGermanName_findsOne() {
        final CodeClassFilter filter = new CodeClassFilter();
        filter.setNameMask("Zeitdauer");
        final List<CodeClassDefinition> ccds = repo.findPageOfCodeClassDefinitions(filter,
            new PaginationRequest(Sort.Direction.ASC, "name"));

        assertThat(ccds).hasSize(1);

        CodeClassDefinition ntd = ccds.get(0);
        assertThat(ntd.getName()).isEqualTo("Zeitdauer");
        assertThat(ntd.getNameInLanguage("de")).isEqualTo("Zeitdauer");
        assertThat(ntd.getNameInLanguage("en")).isEqualTo("Duration of Exposure");
        assertThat(ntd.getNameInLanguage("fr")).isEqualTo("Durée de l'exposition");
    }

    @Test
    void findingCodeClassDefinitions_haveVersionFieldsPopulated() {
        final CodeClassFilter filter = new CodeClassFilter();
        filter.setNameMask("Zeitdauer");
        final List<CodeClassDefinition> ccds = repo.findPageOfCodeClassDefinitions(filter,
            new PaginationRequest(Sort.Direction.DESC, "name"));

        assertThat(ccds).hasSize(1);

        CodeClassDefinition ccd = ccds.get(0);

        assertThat(ccd.getVersion()).isEqualTo(1);
        assertThat(ccd.getCreated()).isNull();
        assertThat(ccd.getLastModified()).isNull();

        Collection<CodeClassTranslation> translations = ccd
            .getTranslations()
            .values();
        assertThat(translations).isNotEmpty();
        CodeClassTranslation tr = translations
            .iterator()
            .next();
        assertThat(tr.getVersion()).isEqualTo(1);
        assertThat(tr.getCreated()).isNull();
        assertThat(tr.getLastModified()).isNull();
    }

    @Test
    void countingCodeClasses_witNullFilter_findsAllDefinitions() {
        assertThat(repo.countByFilter(null)).isEqualTo(8);
    }

    @Test
    void countingCodeClasses_withUnspecifiedFilter_findsAllDefinitions() {
        assertThat(repo.countByFilter(new CodeClassFilter())).isEqualTo(8);
    }

    @Test
    void countingCodeClasses_withFilter_findsAllMatchingDefinitions() {
        final CodeClassFilter filter = new CodeClassFilter();
        filter.setNameMask("en");
        assertThat(repo.countByFilter(filter)).isEqualTo(3);
    }

    @Test
    void countingCodeClasses_withNonMatchingFilter_findsNone() {
        final CodeClassFilter filter = new CodeClassFilter();
        filter.setNameMask("foobar");
        assertThat(repo.countByFilter(filter)).isEqualTo(0);
    }

    @Test
    void gettingMainLanguage() {
        assertThat(repo.getMainLanguage()).isEqualTo("de");
    }

    @Test
    void findingMainLanguage() {
        CodeClassDefinition ccd = repo.newUnpersistedCodeClassDefinition();

        assertThat(ccd.getMainLanguageCode()).isEqualTo("de");
        assertThat(ccd.getName()).isEqualTo("n.a.");
        assertThat(ccd.getNameInLanguage("de")).isNull();
        assertThat(ccd
            .getTranslations()
            .asMap()).hasSize(3);

        final Collection<CodeClassTranslation> translations = ccd
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
    void findingCodeClassDefinition_withNonExistingId_returnsNull() {
        assertThat(repo.findCodeClassDefinition(800)).isNull();
    }

    @Test
    void findingCodeClassDefinition_withExistingId_loadsWithAllLanguages() {
        final CodeClassDefinition existing = repo.findCodeClassDefinition(1);

        assertThat(existing).isNotNull();
        assertThat(existing.getName()).startsWith("Schadstoffe");
        assertThat(existing
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(existing.getNameInLanguage("de")).startsWith("Schadstoffe");
        assertThat(existing.getNameInLanguage("en")).startsWith("Exposure Agent");
        assertThat(existing.getNameInLanguage("fr")).startsWith("Polluant nocif");
    }

    @Test
    void savingNewRecord_savesRecordAndRefreshesId() {
        final CodeClassTranslation ct_de = new CodeClassTranslation(null, "de", "foo_de", "Kommentar", 0);
        final CodeClassTranslation ct_en = new CodeClassTranslation(null, "en", "foo1_en", null, 0);
        final CodeClassTranslation ct_fr = new CodeClassTranslation(null, "fr", "foo1_fr", null, 0);
        final CodeClassDefinition ccd = new CodeClassDefinition(10, "de", 1, ct_de, ct_en, ct_fr);

        assertThat(ccd
            .getTranslations()
            .values())
            .extracting("id")
            .containsExactly(null, null, null);

        CodeClassDefinition saved = repo.saveOrUpdate(ccd);

        assertThat(saved).isNotNull();
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
    void updatingRecord() {
        final CodeClassDefinition ccd = repo.findCodeClassDefinition(1);

        assertThat(ccd).isNotNull();
        assertThat(ccd.getName()).isEqualTo("Schadstoffe");
        assertThat(ccd
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(ccd.getNameInLanguage("de")).isEqualTo("Schadstoffe");
        assertThat(ccd.getNameInLanguage("en")).isEqualTo("Exposure Agent");
        assertThat(ccd.getNameInLanguage("fr")).isEqualTo("Polluant nocif");
        assertThat(ccd
            .getTranslations()
            .get("de")
            .get(0)
            .getVersion()).isEqualTo(1);
        assertThat(ccd
            .getTranslations()
            .get("en")
            .get(0)
            .getVersion()).isEqualTo(1);

        ccd.setNameInLanguage("de", "ss");
        ccd.setNameInLanguage("fr", "pn");

        CodeClassDefinition updated = repo.saveOrUpdate(ccd);

        assertThat(updated).isNotNull();
        assertThat(updated
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(updated.getNameInLanguage("de")).isEqualTo("ss");
        assertThat(updated.getNameInLanguage("en")).isEqualTo("Exposure Agent");
        assertThat(updated.getNameInLanguage("fr")).isEqualTo("pn");

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
    void deleting_withNonExistingId_returnsNull() {
        assertThat(repo.delete(-1, 1)).isNull();
    }

    @Test
    void deleting_withExistingId_butWrongVersion_throwsOptimisticLockingException() {
        try {
            repo.delete(1, -1);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException.class)
                .hasMessage("Record in table 'code_class' has been modified prior to the delete attempt. Aborting....");
        }
    }

    @Test
    void deleting_withExistingIdAndVersion_deletes() {
        // insert new record to the database and verify it's there
        CodeClassDefinition ccd = new CodeClassDefinition(10, "de", 1);
        CodeClassDefinition persisted = repo.saveOrUpdate(ccd);
        final Integer id = persisted.getId();
        final int version = persisted.getVersion();
        assertThat(repo.findCodeClassDefinition(id)).isNotNull();

        // delete the record
        CodeClassDefinition deleted = repo.delete(id, version);
        assertThat(deleted).isNotNull();
        assertThat(deleted.getId()).isEqualTo(id);

        // verify the record is not there anymore
        assertThat(repo.findCodeClassDefinition(id)).isNull();
    }

}
