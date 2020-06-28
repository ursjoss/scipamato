package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.logger
import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.core.entity.CodeClass
import ch.difty.scipamato.core.entity.code.CodeDefinition
import ch.difty.scipamato.core.entity.code.CodeFilter
import ch.difty.scipamato.core.entity.code.CodeTranslation
import ch.difty.scipamato.core.persistence.code.JooqCodeRepo
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

private val log = logger()

@JooqTest
@Testcontainers
@Suppress("TooManyFunctions", "SpellCheckingInspection", "FunctionName", "MagicNumber", "DuplicatedCode")
internal open class JooqCodeRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqCodeRepo

    @Test
    fun findingAllCodes1InGerman() {
        val codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC1, "de")
        assertThat(codesOfClass1).hasSize(21)
        codesOfClass1.forEach { c -> log.debug(c.toString()) }
    }

    @Test
    fun findingAllCodes2InEnglish() {
        val codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC2, "en")
        assertThat(codesOfClass1).hasSize(2)
        codesOfClass1.forEach { c -> log.debug(c.toString()) }
    }

    @Test
    fun findingAllCodes3InEnglish() {
        val codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC3, "fr")
        assertThat(codesOfClass1).hasSize(14)
        codesOfClass1.forEach { c -> log.debug(c.toString()) }
    }

    @Test
    fun findingCodeDefinitions_withUnspecifiedFilter_findsAllDefinitions() {
        val cds = repo.findPageOfCodeDefinitions(
            CodeFilter(),
            PaginationRequest(0, 10, Sort.Direction.ASC, "name")
        )

        assertThat(cds).hasSize(10)

        var cd = cds[0]
        assertThat(cd.code).isEqualTo("4Y")
        assertThat(cd.codeClass?.id).isEqualTo(4)
        assertThat(cd.name).isEqualTo("Absenzen, eingeschränkte Aktivität")
        assertThat(cd.sort).isEqualTo(17)
        assertThat(cd.isInternal).isTrue()
        assertThat(cd.getNameInLanguage("de")).startsWith("Absenzen,")
        assertThat(cd.getNameInLanguage("en")).startsWith("Absenteeism")
        assertThat(cd.getNameInLanguage("fr")).startsWith("Absentéisme")

        cd = cds[1]
        assertThat(cd.code).isEqualTo("7Z")
        assertThat(cd.codeClass?.id).isEqualTo(7)
        assertThat(cd.name).isEqualTo("Alle")
        assertThat(cd.sort).isEqualTo(4)
        assertThat(cd.isInternal).isTrue()
        assertThat(cd.getNameInLanguage("de")).startsWith("Alle")
        assertThat(cd.getNameInLanguage("en")).startsWith("All")
        assertThat(cd.getNameInLanguage("fr")).startsWith("tous")
    }

    @Test
    fun findingCodeDefinitions_withFilterMatchingSingleGermanName_findsOne() {
        val filter = CodeFilter()
        filter.nameMask = "Experimentelle Studie unter Belastung / Arbeit"
        val kds = repo.findPageOfCodeDefinitions(
            filter,
            PaginationRequest(Sort.Direction.ASC, "name")
        )

        assertThat(kds).hasSize(1)

        val ntd = kds[0]
        assertThat(ntd.code).isEqualTo("5A")
        assertThat(ntd.name).isEqualTo("Experimentelle Studie unter Belastung / Arbeit")
        assertThat(ntd.getNameInLanguage("de")).isEqualTo("Experimentelle Studie unter Belastung / Arbeit")
        assertThat(ntd.getNameInLanguage("en")).isEqualTo("Experimental study under exercising conditions")
        assertThat(ntd.getNameInLanguage("fr")).isEqualTo("Etude expérimentale dans des conditions exercicantes")
    }

    @Test
    fun findingCodeDefinitions_haveVersionFieldsPopulated() {
        val filter = CodeFilter()
        filter.nameMask = "Experimentelle Studie unter Belastung / Arbeit"
        val kds = repo.findPageOfCodeDefinitions(filter, PaginationRequest(Sort.Direction.ASC, "name"))

        assertThat(kds).hasSize(1)

        val cd = kds[0]

        assertThat(cd.version).isEqualTo(1)
        assertThat(cd.created).isNull()
        assertThat(cd.lastModified).isNull()

        val tr = cd.getTranslations().first()
        assertThat(tr.version).isEqualTo(1)
        assertThat(tr.created).isNull()
        assertThat(tr.lastModified).isNull()
    }

    @Test
    fun countingCodes_witNullFilter_findsAllDefinitions() {
        assertThat(repo.countByFilter(null)).isEqualTo(82)
    }

    @Test
    fun countingCodes_withUnspecifiedFilter_findsAllDefinitions() {
        assertThat(repo.countByFilter(CodeFilter())).isEqualTo(82)
    }

    @Test
    fun countingCodes_withFilter_findsAllMatchingDefinitions() {
        val filter = CodeFilter()
        filter.nameMask = "es"
        assertThat(repo.countByFilter(filter)).isEqualTo(44)
    }

    @Test
    fun countingCodes_withNonMatchingFilter_findsNone() {
        val filter = CodeFilter()
        filter.nameMask = "foobar"
        assertThat(repo.countByFilter(filter)).isEqualTo(0)
    }

    @Test
    fun gettingMainLanguage() {
        assertThat(repo.mainLanguage).isEqualTo("de")
    }

    @Test
    fun findingMainLanguage() {
        val cd = repo.newUnpersistedCodeDefinition()

        assertThat(cd.code).isNull()
        assertThat(cd.mainLanguageCode).isEqualTo("de")
        assertThat(cd.codeClass).isNull()
        assertThat(cd.sort).isEqualTo(1)
        assertThat(cd.isInternal).isFalse()
        assertThat(cd.name).isEqualTo("n.a.")
        assertThat(cd.getNameInLanguage("de")).isNull()

        val translations = cd.getTranslations()
        assertThat(translations.map { it.langCode }).containsOnly("de", "en", "fr")
        assertThat(translations.map { it.id }).containsExactly(null, null, null)
        assertThat(translations.map { it.name }).containsExactly(null, null, null)
    }

    @Test
    fun findingCodeDefinition_withNonExistingId_returnsNull() {
        assertThat(repo.findCodeDefinition("foo")).isNull()
    }

    @Test
    fun findingCodeDefinition_withExistingId_loadsWithAllLanguages() {
        val existing = repo.findCodeDefinition("4Y") ?: fail("Unable to find code 4Y")

        assertThat(existing.code).isEqualTo("4Y")
        assertThat(existing.name).startsWith("Absenzen")
        assertThat(existing.getTranslations()).hasSize(3)
        assertThat(existing.getNameInLanguage("de")).startsWith("Absenzen,")
        assertThat(existing.getNameInLanguage("en")).startsWith("Absenteeism")
        assertThat(existing.getNameInLanguage("fr")).startsWith("Absentéisme")
    }

    @Test
    @Suppress("LocalVariableName", "VariableNaming")
    fun savingNewRecord_savesRecordAndRefreshesId() {
        val ct_de = CodeTranslation(null, "de", "foo_de", "Kommentar", 0)
        val ct_en = CodeTranslation(null, "en", "foo1_en", null, 0)
        val ct_fr = CodeTranslation(null, "fr", "foo1_fr", null, 0)
        val cc = CodeClass(2, "cc2", "d2")
        val cd = CodeDefinition("2Z", "de", cc, 0, false, 1, ct_de, ct_en, ct_fr)

        assertThat(cd.getTranslations().map { it.id }).containsExactly(null, null, null)

        val saved = repo.saveOrUpdate(cd) ?: fail("Unable to save code")

        assertThat(saved.code).isEqualTo("2Z")
        assertThat(saved.name).isEqualTo("foo_de")
        assertThat(saved.getTranslations().map { it.version }).containsExactly(1, 1, 1)
    }

    @Test
    fun updatingRecord() {
        val cd = repo.findCodeDefinition("2R") ?: fail("Unable to find code 2R")

        assertThat(cd.code).isEqualTo("2R")
        assertThat(cd.name).isEqualTo("Europa")
        assertThat(cd.getTranslations()).hasSize(3)
        assertThat(cd.getNameInLanguage("de")).isEqualTo("Europa")
        assertThat(cd.getNameInLanguage("en")).isEqualTo("Europe")
        assertThat(cd.getNameInLanguage("fr")).isEqualTo("Europe")
        assertThat(cd.getTranslations("de").first().version).isEqualTo(1)
        assertThat(cd.getTranslations("en").first().version).isEqualTo(1)

        cd.setNameInLanguage("de", "eu")
        cd.setNameInLanguage("fr", "foo")

        val updated = repo.saveOrUpdate(cd) ?: fail("Unable to save or update code")

        assertThat(updated.code).isEqualTo("2R")
        assertThat(updated.getTranslations()).hasSize(3)
        assertThat(updated.getNameInLanguage("de")).isEqualTo("eu")
        assertThat(updated.getNameInLanguage("en")).isEqualTo("Europe")
        assertThat(updated.getNameInLanguage("fr")).isEqualTo("foo")

        assertThat(updated.version).isEqualTo(2)
        assertThat(updated.getTranslations("de").first().version).isEqualTo(2)
        assertThat(updated.getTranslations("en").first().version).isEqualTo(2)
        assertThat(updated.getTranslations("fr").first().version).isEqualTo(2)
    }

    @Test
    fun deleting_withNonExistingId_returnsNull() {
        assertThat(repo.delete("ZZ", 1)).isNull()
    }

    @Suppress("TooGenericExceptionCaught")
    @Test
    fun deleting_withExistingId_butWrongVersion_throwsOptimisticLockingException() {
        try {
            repo.delete("1A", -1)
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException::class.java)
                .hasMessage("Record in table 'code' has been modified prior to the delete attempt. Aborting....")
        }
    }

    @Test
    fun deleting_withExistingIdAndVersion_deletes() {
        // insert new record to the database and verify it's there
        val cd = CodeDefinition("2Z", "de", CodeClass(2, "cc2", "d2"), 3, true, null)
        val persisted = repo.saveOrUpdate(cd) ?: fail("Unable to save or update code")
        val code = persisted.code ?: fail("code should not be null")
        val version = persisted.version
        assertThat(repo.findCodeDefinition(code)).isNotNull()

        // delete the record
        val deleted = repo.delete(code, version) ?: fail("Unable to delete code")
        assertThat(deleted.code).isEqualTo(code)

        // verify the record is not there anymore
        assertThat(repo.findCodeDefinition(code)).isNull()
    }

    @Test
    fun gettingCodeClass1_inAKnownLanguage() {
        val cc1 = repo.getCodeClass1("en")
        assertThat(cc1.id).isEqualTo(1)
        assertThat(cc1.name).isEqualTo("Exposure Agent")
    }

    @Test
    fun gettingCodeClass1_inNotKnownLanguage() {
        val cc1 = repo.getCodeClass1("foo")
        assertThat(cc1.id).isEqualTo(1)
        assertThat(cc1.name).isEqualTo("not translated")
    }

    @Test
    fun findingCodeDefinitions_sortedBySort() {
        assertSortedList("sort", "1Z")
    }

    private fun assertSortedList(sortProperty: String, code: String) {
        val cds = repo.findPageOfCodeDefinitions(
            CodeFilter(),
            PaginationRequest(0, 10, Sort.Direction.DESC, sortProperty)
        )

        assertThat(cds).hasSize(10)

        val cd = cds[0]
        assertThat(cd.code).isEqualTo(code)
    }

    @Test
    fun findingCodeDefinitions_sortedByCode() {
        assertSortedList("code", "8Z")
    }

    @Test
    fun findingCodeDefinitions_sortedByInternal() {
        val cds = repo.findPageOfCodeDefinitions(
            CodeFilter(),
            PaginationRequest(0, 10, Sort.Direction.DESC, "internal")
        )

        assertThat(cds).hasSize(10)

        // all are internal=true, no use asserting
    }

    @Test
    fun findingCodeDefinitions_sortedByTranslation() {
        assertSortedList("translationsAsString", "2N")
    }

    @Test
    fun findingCodeDefinitions_sortedBySomethingUndefined_doesNotSort() {
        assertSortedList("notexisting", "1F")
    }

    private fun assertFiltering(filter: CodeFilter, count: Int, code: String) {
        val cds = repo.findPageOfCodeDefinitions(
            filter,
            PaginationRequest(0, 100, Sort.Direction.DESC, "sort")
        )

        assertThat(cds).hasSize(count)

        val cd = cds[0]
        assertThat(cd.code).isEqualTo(code)
    }

    @Test
    fun findingCodeDefinitions_filteredByInternal() {
        val filter = CodeFilter()
        filter.internal = true
        assertFiltering(filter, 19, "1Z")
    }

    @Test
    fun findingCodeDefinitions_filteredByCodeClass() {
        val filter = CodeFilter() // internal codeclass, comment
        filter.codeClass = CodeClass(5, "whatever", "")
        assertFiltering(filter, 13, "5U")
    }

    @Test
    fun findingCodeDefinitions_filteredByComment() {
        val filter = CodeFilter() // internal codeclass, comment
        filter.commentMask = "COPD"
        assertFiltering(filter, 1, "4F")
    }
}
