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
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeNull
import org.amshove.kluent.shouldStartWith
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
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
        codesOfClass1 shouldHaveSize 21
        codesOfClass1.forEach { c -> log.debug(c.toString()) }
    }

    @Test
    fun findingAllCodes2InEnglish() {
        val codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC2, "en")
        codesOfClass1 shouldHaveSize 2
        codesOfClass1.forEach { c -> log.debug(c.toString()) }
    }

    @Test
    fun findingAllCodes3InEnglish() {
        val codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC3, "fr")
        codesOfClass1 shouldHaveSize 14
        codesOfClass1.forEach { c -> log.debug(c.toString()) }
    }

    @Test
    fun findingCodeDefinitions_withUnspecifiedFilter_findsAllDefinitions() {
        val cds = repo.findPageOfCodeDefinitions(
            CodeFilter(),
            PaginationRequest(0, 10, Sort.Direction.ASC, "name")
        )

        cds shouldHaveSize 10

        var cd = cds[0]
        cd.code shouldBeEqualTo "4Y"
        cd.codeClass?.id shouldBeEqualTo 4
        cd.name shouldBeEqualTo "Absenzen, eingeschränkte Aktivität"
        cd.sort shouldBeEqualTo 17
        cd.isInternal.shouldBeTrue()
        cd.getNameInLanguage("de")?.shouldStartWith("Absenzen,")
        cd.getNameInLanguage("en")?.shouldStartWith("Absenteeism")
        cd.getNameInLanguage("fr")?.shouldStartWith("Absentéisme")

        cd = cds[1]
        cd.code shouldBeEqualTo "7Z"
        cd.codeClass?.id shouldBeEqualTo 7
        cd.name shouldBeEqualTo "Alle"
        cd.sort shouldBeEqualTo 4
        cd.isInternal.shouldBeTrue()
        cd.getNameInLanguage("de")?.shouldStartWith("Alle")
        cd.getNameInLanguage("en")?.shouldStartWith("All")
        cd.getNameInLanguage("fr")?.shouldStartWith("tous")
    }

    @Test
    fun findingCodeDefinitions_withFilterMatchingSingleGermanName_findsOne() {
        val filter = CodeFilter()
        filter.nameMask = "Experimentelle Studie unter Belastung / Arbeit"
        val kds = repo.findPageOfCodeDefinitions(
            filter,
            PaginationRequest(Sort.Direction.ASC, "name")
        )

        kds shouldHaveSize 1

        val ntd = kds[0]
        ntd.code shouldBeEqualTo "5A"
        ntd.name shouldBeEqualTo "Experimentelle Studie unter Belastung / Arbeit"
        ntd.getNameInLanguage("de") shouldBeEqualTo "Experimentelle Studie unter Belastung / Arbeit"
        ntd.getNameInLanguage("en") shouldBeEqualTo "Experimental study under exercising conditions"
        ntd.getNameInLanguage("fr") shouldBeEqualTo "Etude expérimentale dans des conditions exercicantes"
    }

    @Test
    fun findingCodeDefinitions_haveVersionFieldsPopulated() {
        val filter = CodeFilter()
        filter.nameMask = "Experimentelle Studie unter Belastung / Arbeit"
        val kds = repo.findPageOfCodeDefinitions(filter, PaginationRequest(Sort.Direction.ASC, "name"))

        kds shouldHaveSize 1

        val cd = kds[0]

        cd.version shouldBeEqualTo 1
        cd.created.shouldBeNull()
        cd.lastModified.shouldBeNull()

        val tr = cd.getTranslations().first()
        tr.version shouldBeEqualTo 1
        tr.created.shouldBeNull()
        tr.lastModified.shouldBeNull()
    }

    @Test
    fun countingCodes_witNullFilter_findsAllDefinitions() {
        repo.countByFilter(null) shouldBeEqualTo 82
    }

    @Test
    fun countingCodes_withUnspecifiedFilter_findsAllDefinitions() {
        repo.countByFilter(CodeFilter()) shouldBeEqualTo 82
    }

    @Test
    fun countingCodes_withFilter_findsAllMatchingDefinitions() {
        val filter = CodeFilter()
        filter.nameMask = "es"
        repo.countByFilter(filter) shouldBeEqualTo 44
    }

    @Test
    fun countingCodes_withNonMatchingFilter_findsNone() {
        val filter = CodeFilter()
        filter.nameMask = "foobar"
        repo.countByFilter(filter) shouldBeEqualTo 0
    }

    @Test
    fun gettingMainLanguage() {
        repo.mainLanguage shouldBeEqualTo "de"
    }

    @Test
    fun findingMainLanguage() {
        val cd = repo.newUnpersistedCodeDefinition()

        cd.code.shouldBeNull()
        cd.mainLanguageCode shouldBeEqualTo "de"
        cd.codeClass.shouldBeNull()
        cd.sort shouldBeEqualTo 1
        cd.isInternal.shouldBeFalse()
        cd.name shouldBeEqualTo "n.a."
        cd.getNameInLanguage("de").shouldBeNull()

        val translations = cd.getTranslations()
        translations.map { it.langCode } shouldContainSame listOf("de", "en", "fr")
        translations.map { it.id } shouldContainAll listOf(null, null, null)
        translations.map { it.name } shouldContainAll listOf(null, null, null)
    }

    @Test
    fun findingCodeDefinition_withNonExistingId_returnsNull() {
        repo.findCodeDefinition("foo").shouldBeNull()
    }

    @Test
    fun findingCodeDefinition_withExistingId_loadsWithAllLanguages() {
        val existing = repo.findCodeDefinition("4Y") ?: fail { "Unable to find code 4Y" }

        existing.code shouldBeEqualTo "4Y"
        existing.name shouldStartWith "Absenzen"
        existing.getTranslations() shouldHaveSize 3
        existing.getNameInLanguage("de")?.shouldStartWith("Absenzen,")
        existing.getNameInLanguage("en")?.shouldStartWith("Absenteeism")
        existing.getNameInLanguage("fr")?.shouldStartWith("Absentéisme")
    }

    @Test
    @Suppress("LocalVariableName", "VariableNaming")
    fun savingNewRecord_savesRecordAndRefreshesId() {
        val ct_de = CodeTranslation(null, "de", "foo_de", "Kommentar", 0)
        val ct_en = CodeTranslation(null, "en", "foo1_en", null, 0)
        val ct_fr = CodeTranslation(null, "fr", "foo1_fr", null, 0)
        val cc = CodeClass(2, "cc2", "d2")
        val cd = CodeDefinition("2Z", "de", cc, 0, false, 1, ct_de, ct_en, ct_fr)

        cd.getTranslations().map { it.id } shouldContainAll listOf(null, null, null)

        val saved = repo.saveOrUpdate(cd) ?: fail { "Unable to save code" }

        saved.code shouldBeEqualTo "2Z"
        saved.name shouldBeEqualTo "foo_de"
        saved.getTranslations().map { it.version } shouldContainAll listOf(1, 1, 1)
    }

    @Test
    fun updatingRecord() {
        val cd = repo.findCodeDefinition("2R") ?: fail { "Unable to find code 2R" }

        cd.code shouldBeEqualTo "2R"
        cd.name shouldBeEqualTo "Europa"
        cd.getTranslations() shouldHaveSize 3
        cd.getNameInLanguage("de") shouldBeEqualTo "Europa"
        cd.getNameInLanguage("en") shouldBeEqualTo "Europe"
        cd.getNameInLanguage("fr") shouldBeEqualTo "Europe"
        cd.getTranslations("de").first().version shouldBeEqualTo 1
        cd.getTranslations("en").first().version shouldBeEqualTo 1

        cd.setNameInLanguage("de", "eu")
        cd.setNameInLanguage("fr", "foo")

        val updated = repo.saveOrUpdate(cd) ?: fail { "Unable to save or update code" }

        updated.code shouldBeEqualTo "2R"
        updated.getTranslations() shouldHaveSize 3
        updated.getNameInLanguage("de") shouldBeEqualTo "eu"
        updated.getNameInLanguage("en") shouldBeEqualTo "Europe"
        updated.getNameInLanguage("fr") shouldBeEqualTo "foo"

        updated.version shouldBeEqualTo 2
        updated.getTranslations("de").first().version shouldBeEqualTo 2
        updated.getTranslations("en").first().version shouldBeEqualTo 2
        updated.getTranslations("fr").first().version shouldBeEqualTo 2
    }

    @Test
    fun deleting_withNonExistingId_returnsNull() {
        repo.delete("ZZ", 1).shouldBeNull()
    }

    @Suppress("TooGenericExceptionCaught")
    @Test
    fun deleting_withExistingId_butWrongVersion_throwsOptimisticLockingException() {
        invoking { repo.delete("1A", -1) } shouldThrow OptimisticLockingException::class withMessage
            "Record in table 'code' has been modified prior to the delete attempt. Aborting...."
    }

    @Test
    fun deleting_withExistingIdAndVersion_deletes() {
        // insert new record to the database and verify it's there
        val cd = CodeDefinition("2Z", "de", CodeClass(2, "cc2", "d2"), 3, true, null)
        val persisted = repo.saveOrUpdate(cd) ?: fail { "Unable to save or update code" }
        val code = persisted.code ?: fail { "code should not be null" }
        val version = persisted.version
        repo.findCodeDefinition(code).shouldNotBeNull()

        // delete the record
        val deleted = repo.delete(code, version) ?: fail { "Unable to delete code" }
        deleted.code shouldBeEqualTo code

        // verify the record is not there anymore
        repo.findCodeDefinition(code).shouldBeNull()
    }

    @Test
    fun gettingCodeClass1_inAKnownLanguage() {
        val cc1 = repo.getCodeClass1("en")
        cc1.id shouldBeEqualTo 1
        cc1.name shouldBeEqualTo "Exposure Agent"
    }

    @Test
    fun gettingCodeClass1_inNotKnownLanguage() {
        val cc1 = repo.getCodeClass1("foo")
        cc1.id shouldBeEqualTo 1
        cc1.name shouldBeEqualTo "not translated"
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

        cds shouldHaveSize 10

        val cd = cds[0]
        cd.code shouldBeEqualTo code
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

        cds shouldHaveSize 10

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

        cds shouldHaveSize count

        val cd = cds[0]
        cd.code shouldBeEqualTo code
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
