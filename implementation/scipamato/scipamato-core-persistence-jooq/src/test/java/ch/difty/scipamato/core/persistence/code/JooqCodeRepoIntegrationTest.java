package ch.difty.scipamato.core.persistence.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Collection;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.entity.code.CodeTranslation;
import ch.difty.scipamato.core.persistence.JooqTransactionalIntegrationTest;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

@Slf4j
public class JooqCodeRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    @Autowired
    private JooqCodeRepo repo;

    @Test
    public void findingAllCodes1InGerman() {
        List<Code> codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC1, "de");
        assertThat(codesOfClass1)
            .isNotEmpty()
            .hasSize(21);
        codesOfClass1.forEach((c) -> log.debug(c.toString()));
    }

    @Test
    public void findingAllCodes2InEnglish() {
        List<Code> codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC2, "en");
        assertThat(codesOfClass1)
            .isNotEmpty()
            .hasSize(2);
        codesOfClass1.forEach((c) -> log.debug(c.toString()));
    }

    @Test
    public void findingAllCodes3InEnglish() {
        List<Code> codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC3, "fr");
        assertThat(codesOfClass1)
            .isNotEmpty()
            .hasSize(14);
        codesOfClass1.forEach((c) -> log.debug(c.toString()));
    }

    @Test
    public void findingCodeDefinitions_withUnspecifiedFilter_findsAllDefinitions() {
        final List<CodeDefinition> cds = repo.findPageOfCodeDefinitions(new CodeFilter(),
            new PaginationRequest(0, 10, Sort.Direction.ASC, "name"));

        assertThat(cds).hasSize(10);

        CodeDefinition cd = cds.get(0);
        assertThat(cd.getCode()).isEqualTo("4Y");
        assertThat(cd.getCodeClass())
            .extracting("id")
            .containsExactly(4);
        assertThat(cd.getName()).isEqualTo("Absenzen, eingeschränkte Aktivität");
        assertThat(cd.getSort()).isEqualTo(17);
        assertThat(cd.isInternal()).isTrue();
        assertThat(cd.getNameInLanguage("de")).startsWith("Absenzen,");
        assertThat(cd.getNameInLanguage("en")).startsWith("Absenteeism");
        assertThat(cd.getNameInLanguage("fr")).startsWith("Absentéisme");

        cd = cds.get(1);
        assertThat(cd.getCode()).isEqualTo("7Z");
        assertThat(cd.getCodeClass())
            .extracting("id")
            .containsExactly(7);
        assertThat(cd.getName()).isEqualTo("Alle");
        assertThat(cd.getSort()).isEqualTo(4);
        assertThat(cd.isInternal()).isTrue();
        assertThat(cd.getNameInLanguage("de")).startsWith("Alle");
        assertThat(cd.getNameInLanguage("en")).startsWith("All");
        assertThat(cd.getNameInLanguage("fr")).startsWith("tous");
    }

    @Test
    public void findingCodeDefinitions_withFilterMatchingSingleGermanName_findsOne() {
        final CodeFilter filter = new CodeFilter();
        filter.setNameMask("Experimentelle Studie unter Belastung / Arbeit");
        final List<CodeDefinition> kds = repo.findPageOfCodeDefinitions(filter,
            new PaginationRequest(Sort.Direction.ASC, "name"));

        assertThat(kds).hasSize(1);

        CodeDefinition ntd = kds.get(0);
        assertThat(ntd.getCode()).isEqualTo("5A");
        assertThat(ntd.getName()).isEqualTo("Experimentelle Studie unter Belastung / Arbeit");
        assertThat(ntd.getNameInLanguage("de")).isEqualTo("Experimentelle Studie unter Belastung / Arbeit");
        assertThat(ntd.getNameInLanguage("en")).isEqualTo("Experimental study under exercising conditions");
        assertThat(ntd.getNameInLanguage("fr")).isEqualTo("Etude expérimentale dans des conditions exercicantes");
    }

    @Test
    public void findingCodeDefinitions_haveVersionFieldsPopulated() {
        final CodeFilter filter = new CodeFilter();
        filter.setNameMask("Experimentelle Studie unter Belastung / Arbeit");
        final List<CodeDefinition> kds = repo.findPageOfCodeDefinitions(filter,
            new PaginationRequest(Sort.Direction.ASC, "name"));

        assertThat(kds).hasSize(1);

        CodeDefinition cd = kds.get(0);

        assertThat(cd.getVersion()).isEqualTo(1);
        assertThat(cd.getCreated()).isNull();
        assertThat(cd.getLastModified()).isNull();

        Collection<CodeTranslation> translations = cd
            .getTranslations()
            .values();
        assertThat(translations).isNotEmpty();
        CodeTranslation tr = translations
            .iterator()
            .next();
        assertThat(tr.getVersion()).isEqualTo(1);
        assertThat(tr.getCreated()).isNull();
        assertThat(tr.getLastModified()).isNull();
    }

    @Test
    public void countingCodes_withUnspecifiedFilter_findsAllDefinitions() {
        assertThat(repo.countByFilter(new CodeFilter())).isEqualTo(82);
    }

    @Test
    public void countingCodes_withFilter_findsAllMatchingDefinitions() {
        final CodeFilter filter = new CodeFilter();
        filter.setNameMask("es");
        assertThat(repo.countByFilter(filter)).isEqualTo(44);
    }

    @Test
    public void countingCodes_withNonMatchingFilter_findsNone() {
        final CodeFilter filter = new CodeFilter();
        filter.setNameMask("foobar");
        assertThat(repo.countByFilter(filter)).isEqualTo(0);
    }

    @Test
    public void gettingMainLanguage() {
        assertThat(repo.getMainLanguage()).isEqualTo("de");
    }

    @Test
    public void findingMainLanguage() {
        CodeDefinition cd = repo.newUnpersistedCodeDefinition();

        assertThat(cd.getCode()).isNull();
        assertThat(cd.getMainLanguageCode()).isEqualTo("de");
        assertThat(cd.getCodeClass()).isNull();
        assertThat(cd.getSort()).isEqualTo(1);
        assertThat(cd.isInternal()).isFalse();
        assertThat(cd.getName()).isEqualTo("n.a.");
        assertThat(cd.getNameInLanguage("de")).isNull();
        assertThat(cd
            .getTranslations()
            .asMap()).hasSize(3);

        final Collection<CodeTranslation> translations = cd
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
    public void findingCodeDefinition_withNonExistingId_returnsNull() {
        assertThat(repo.findCodeDefinition("foo")).isNull();
    }

    @Test
    public void findingCodeDefinition_withExistingId_loadsWithAllLanguages() {
        final CodeDefinition existing = repo.findCodeDefinition("4Y");

        assertThat(existing).isNotNull();
        assertThat(existing.getCode()).isEqualTo("4Y");
        assertThat(existing.getName()).startsWith("Absenzen");
        assertThat(existing
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(existing.getNameInLanguage("de")).startsWith("Absenzen,");
        assertThat(existing.getNameInLanguage("en")).startsWith("Absenteeism");
        assertThat(existing.getNameInLanguage("fr")).startsWith("Absentéisme");
    }

    @Test
    public void savingNewRecord_savesRecordAndRefreshesId() {
        final CodeTranslation ct_de = new CodeTranslation(null, "de", "foo_de", "Kommentar", 0);
        final CodeTranslation ct_en = new CodeTranslation(null, "en", "foo1_en", null, 0);
        final CodeTranslation ct_fr = new CodeTranslation(null, "fr", "foo1_fr", null, 0);
        CodeClass cc = new CodeClass(2, null, null);
        final CodeDefinition cd = new CodeDefinition("2Z", "de", cc, 0, false, 1, ct_de, ct_en, ct_fr);

        assertThat(cd
            .getTranslations()
            .values())
            .extracting("id")
            .containsExactly(null, null, null);

        CodeDefinition saved = repo.saveOrUpdate(cd);

        assertThat(saved).isNotNull();
        assertThat(saved.getCode()).isEqualTo("2Z");
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
        final CodeDefinition cd = repo.findCodeDefinition("2R");

        assertThat(cd).isNotNull();
        assertThat(cd.getCode()).isEqualTo("2R");
        assertThat(cd.getName()).isEqualTo("Europa");
        assertThat(cd
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(cd.getNameInLanguage("de")).isEqualTo("Europa");
        assertThat(cd.getNameInLanguage("en")).isEqualTo("Europe");
        assertThat(cd.getNameInLanguage("fr")).isEqualTo("Europe");
        assertThat(cd
            .getTranslations()
            .get("de")
            .get(0)
            .getVersion()).isEqualTo(1);
        assertThat(cd
            .getTranslations()
            .get("en")
            .get(0)
            .getVersion()).isEqualTo(1);

        cd.setNameInLanguage("de", "eu");
        cd.setNameInLanguage("fr", "foo");

        CodeDefinition updated = repo.saveOrUpdate(cd);

        assertThat(updated).isNotNull();
        assertThat(updated.getCode()).isEqualTo("2R");
        assertThat(updated
            .getTranslations()
            .asMap()).hasSize(3);
        assertThat(updated.getNameInLanguage("de")).isEqualTo("eu");
        assertThat(updated.getNameInLanguage("en")).isEqualTo("Europe");
        assertThat(updated.getNameInLanguage("fr")).isEqualTo("foo");

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
        assertThat(repo.delete("ZZ", 1)).isNull();
    }

    @Test
    public void deleting_withExistingId_butWrongVersion_throwsOptimisticLockingException() {
        try {
            repo.delete("1A", -1);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException.class)
                .hasMessage("Record in table 'code' has been modified prior to the delete attempt. Aborting....");
        }
    }

    @Test
    public void deleting_withExistingIdAndVersion_deletes() {
        // insert new record to the database and verify it's there
        CodeDefinition cd = new CodeDefinition("2Z", "de", new CodeClass(2, null, null), 3, true, null);
        CodeDefinition persisted = repo.saveOrUpdate(cd);
        final String code = persisted.getCode();
        final int version = persisted.getVersion();
        assertThat(repo.findCodeDefinition(code)).isNotNull();

        // delete the record
        CodeDefinition deleted = repo.delete(code, version);
        assertThat(deleted).isNotNull();
        assertThat(deleted.getCode()).isEqualTo(code);

        // verify the record is not there anymore
        assertThat(repo.findCodeDefinition(code)).isNull();
    }

    @Test
    public void gettingCodeClass1() {
        CodeClass cc1 = repo.getCodeClass1("en");
        assertThat(cc1).isNotNull();
        assertThat(cc1.getId()).isEqualTo(1);
    }

    private void assertSortedList(final String sortProperty, final String code) {
        final List<CodeDefinition> cds = repo.findPageOfCodeDefinitions(new CodeFilter(),
            new PaginationRequest(0, 10, Sort.Direction.DESC, sortProperty));

        assertThat(cds).hasSize(10);

        CodeDefinition cd = cds.get(0);
        assertThat(cd.getCode()).isEqualTo(code);
    }

    @Test
    public void findingCodeDefinitions_sortedBySort() {
        assertSortedList("sort", "1Z");
    }

    @Test
    public void findingCodeDefinitions_sortedByCode() {
        assertSortedList("code", "8Z");
    }

    private void assertFiltering(final CodeFilter filter, final int count, final String code) {
        final List<CodeDefinition> cds = repo.findPageOfCodeDefinitions(filter,
            new PaginationRequest(0, 100, Sort.Direction.DESC, "sort"));

        assertThat(cds).hasSize(count);

        CodeDefinition cd = cds.get(0);
        assertThat(cd.getCode()).isEqualTo(code);
    }

    @Test
    public void findingCodeDefinitions_filteredByInternal() {
        final CodeFilter filter = new CodeFilter();
        filter.setInternal(true);
        assertFiltering(filter, 19, "1Z");
    }

    @Test
    public void findingCodeDefinitions_filteredByCodeClass() {
        final CodeFilter filter = new CodeFilter(); // internal codeclass, comment
        filter.setCodeClass(new CodeClass(5, "whatever", ""));
        assertFiltering(filter, 13, "5U");
    }

    @Test
    public void findingCodeDefinitions_filteredByComment() {
        final CodeFilter filter = new CodeFilter(); // internal codeclass, comment
        filter.setCommentMask("COPD");
        assertFiltering(filter, 1, "4F");
    }

}
