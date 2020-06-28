package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.logger
import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition
import ch.difty.scipamato.core.entity.codeclass.CodeClassFilter
import ch.difty.scipamato.core.entity.codeclass.CodeClassTranslation
import ch.difty.scipamato.core.persistence.codeclass.JooqCodeClassRepo
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
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

private const val CODE_CLASS_COUNT = 8

private var log = logger()

@JooqTest
@Testcontainers
@Suppress("TooManyFunctions", "FunctionName", "MagicNumber", "SpellCheckingInspection", "DuplicatedCode")
internal open class JooqCodeClassRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqCodeClassRepo

    @Test
    fun findingAllCodesClassesInGerman() {
        val ccs = repo.find("de")
        ccs shouldHaveSize CODE_CLASS_COUNT
        ccs.forEach { cc -> log.debug(cc.toString()) }
    }

    @Test
    fun findingAllCodesClassesInEnglish() {
        val ccs = repo.find("en")
        ccs shouldHaveSize CODE_CLASS_COUNT
        ccs.forEach { cc -> log.debug(cc.toString()) }
    }

    @Test
    fun findingAllCodesClassesInFrench() {
        val ccs = repo.find("fr")
        ccs shouldHaveSize CODE_CLASS_COUNT
        ccs.forEach { cc -> log.debug(cc.toString()) }
    }

    @Test
    fun findingCodeClassDefinitions_withUnspecifiedFilter_findsAllDefinitions() {
        val ccds = repo.findPageOfCodeClassDefinitions(
            CodeClassFilter(),
            PaginationRequest(0, 8, Sort.Direction.ASC, "name")
        )

        ccds shouldHaveSize CODE_CLASS_COUNT

        var ccd = ccds[0]
        ccd.name shouldBeEqualTo "Kollektiv"
        ccd.getNameInLanguage("de")?.shouldStartWith("Kollektiv")
        ccd.getNameInLanguage("en")?.shouldStartWith("Study Population")
        ccd.getNameInLanguage("fr")?.shouldStartWith("Population")

        ccd = ccds[1]
        ccd.name shouldBeEqualTo "Region"
        ccd.getNameInLanguage("de")?.shouldStartWith("Region")
        ccd.getNameInLanguage("en")?.shouldStartWith("Region")
        ccd.getNameInLanguage("fr")?.shouldStartWith("Région")
    }

    @Test
    fun findingCodeClassDefinitions_sortingByUndefinedField_doesNotSort() {
        val ccds = repo.findPageOfCodeClassDefinitions(
            CodeClassFilter(),
            PaginationRequest(0, 8, Sort.Direction.ASC, "foobar")
        )

        ccds shouldHaveSize CODE_CLASS_COUNT
        ccds[0].name shouldBeEqualTo "Schadstoffe"
        ccds[1].name shouldBeEqualTo "Region"
    }

    @Test
    fun findingCodeClassDefinitions_withUnspecifiedFilter_withReverseSortByTranslations() {
        val filter = CodeClassFilter()
        filter.descriptionMask = "en"
        val ccds = repo.findPageOfCodeClassDefinitions(
            filter,
            PaginationRequest(0, 8, Sort.Direction.DESC, "translationsAsString")
        )

        ccds shouldHaveSize 3

        ccds.first().name shouldBeEqualTo "Zielgrössen"
    }

    @Test
    fun findingCodeClassDefinitions_withFilterMatchingSingleGermanName_findsOne() {
        val filter = CodeClassFilter()
        filter.nameMask = "Zeitdauer"
        val ccds = repo.findPageOfCodeClassDefinitions(
            filter,
            PaginationRequest(Sort.Direction.ASC, "name")
        )

        ccds shouldHaveSize 1

        val ntd = ccds.first()
        ntd.name shouldBeEqualTo "Zeitdauer"
        ntd.getNameInLanguage("de") shouldBeEqualTo "Zeitdauer"
        ntd.getNameInLanguage("en") shouldBeEqualTo "Duration of Exposure"
        ntd.getNameInLanguage("fr") shouldBeEqualTo "Durée de l'exposition"
    }

    @Test
    fun findingCodeClassDefinitions_haveVersionFieldsPopulated() {
        val filter = CodeClassFilter()
        filter.nameMask = "Zeitdauer"
        val ccds = repo.findPageOfCodeClassDefinitions(
            filter,
            PaginationRequest(Sort.Direction.DESC, "name")
        )

        ccds shouldHaveSize 1

        val ccd = ccds.first()

        ccd.version shouldBeEqualTo 1
        ccd.created.shouldBeNull()
        ccd.lastModified.shouldBeNull()

        val tr = ccd.getTranslations().first()
        tr.version shouldBeEqualTo 1
        tr.created.shouldBeNull()
        tr.lastModified.shouldBeNull()
    }

    @Test
    fun countingCodeClasses_witNullFilter_findsAllDefinitions() {
        repo.countByFilter(null) shouldBeEqualTo CODE_CLASS_COUNT
    }

    @Test
    fun countingCodeClasses_withUnspecifiedFilter_findsAllDefinitions() {
        repo.countByFilter(CodeClassFilter()) shouldBeEqualTo CODE_CLASS_COUNT
    }

    @Test
    fun countingCodeClasses_withFilter_findsAllMatchingDefinitions() {
        val filter = CodeClassFilter()
        filter.nameMask = "en"
        repo.countByFilter(filter) shouldBeEqualTo 3
    }

    @Test
    fun countingCodeClasses_withNonMatchingFilter_findsNone() {
        val filter = CodeClassFilter()
        filter.nameMask = "foobar"
        repo.countByFilter(filter) shouldBeEqualTo 0
    }

    @Test
    fun gettingMainLanguage() {
        repo.mainLanguage shouldBeEqualTo "de"
    }

    @Test
    fun findingMainLanguage() {
        val ccd = repo.newUnpersistedCodeClassDefinition()

        ccd.mainLanguageCode shouldBeEqualTo "de"
        ccd.name shouldBeEqualTo "n.a."
        ccd.getNameInLanguage("de").shouldBeNull()

        val translations = ccd.getTranslations()
        translations.map { it.langCode } shouldContainSame listOf("de", "en", "fr")
        translations.map { it.id } shouldContainAll listOf(null, null, null)
        translations.map { it.name } shouldContainAll listOf(null, null, null)
    }

    @Test
    fun findingCodeClassDefinition_withNonExistingId_returnsNull() {
        repo.findCodeClassDefinition(800).shouldBeNull()
    }

    @Test
    fun findingCodeClassDefinition_withExistingId_loadsWithAllLanguages() {
        val existing = repo.findCodeClassDefinition(1)
            ?: fail { "could not retrievee code class definition with id 1" }

        existing.name.shouldStartWith("Schadstoffe")
        existing.getTranslations() shouldHaveSize 3
        existing.getNameInLanguage("de")?.shouldStartWith("Schadstoffe")
        existing.getNameInLanguage("en")?.shouldStartWith("Exposure Agent")
        existing.getNameInLanguage("fr")?.shouldStartWith("Polluant nocif")
    }

    @Test
    @Suppress("LocalVariableName", "VariableNaming")
    fun savingNewRecord_savesRecordAndRefreshesId() {
        val ct_de = CodeClassTranslation(null, "de", "foo_de", "Kommentar", 0)
        val ct_en = CodeClassTranslation(null, "en", "foo1_en", null, 0)
        val ct_fr = CodeClassTranslation(null, "fr", "foo1_fr", null, 0)
        val ccd = CodeClassDefinition(10, "de", 1, ct_de, ct_en, ct_fr)

        ccd.getTranslations().map { it.id } shouldContainAll listOf(null, null, null)

        val saved = repo.saveOrUpdate(ccd)

        saved.name shouldBeEqualTo "foo_de"
        saved.getTranslations().map { it.version } shouldContainAll listOf(1, 1, 1)
    }

    @Test
    fun updatingRecord() {
        val ccd = repo.findCodeClassDefinition(1)
            ?: fail { "unable to retrieve code class definition with id 1" }

        ccd.name shouldBeEqualTo "Schadstoffe"
        ccd.getTranslations() shouldHaveSize 3
        ccd.getNameInLanguage("de") shouldBeEqualTo "Schadstoffe"
        ccd.getNameInLanguage("en") shouldBeEqualTo "Exposure Agent"
        ccd.getNameInLanguage("fr") shouldBeEqualTo "Polluant nocif"
        ccd.getTranslations("de").first().version shouldBeEqualTo 1
        ccd.getTranslations("en").first().version shouldBeEqualTo 1

        ccd.setNameInLanguage("de", "ss")
        ccd.setNameInLanguage("fr", "pn")

        val updated = repo.saveOrUpdate(ccd)

        updated.getTranslations() shouldHaveSize 3
        updated.getNameInLanguage("de") shouldBeEqualTo "ss"
        updated.getNameInLanguage("en") shouldBeEqualTo "Exposure Agent"
        updated.getNameInLanguage("fr") shouldBeEqualTo "pn"

        updated.version shouldBeEqualTo 2
        updated.getTranslations("de").first().version shouldBeEqualTo 2
        updated.getTranslations("en").first().version shouldBeEqualTo 2
        updated.getTranslations("fr").first().version shouldBeEqualTo 2
    }

    @Test
    fun deleting_withNonExistingId_returnsNull() {
        repo.delete(-1, 1).shouldBeNull()
    }

    @Suppress("TooGenericExceptionCaught")
    @Test
    fun deleting_withExistingId_butWrongVersion_throwsOptimisticLockingException() {
        invoking { repo.delete(1, -1) } shouldThrow OptimisticLockingException::class withMessage
            "Record in table 'code_class' has been modified prior to the delete attempt. Aborting...."
    }

    @Test
    fun deleting_withExistingIdAndVersion_deletes() {
        // insert new record to the database and verify it's there
        val ccd = CodeClassDefinition(10, "de", 1)
        val persisted = repo.saveOrUpdate(ccd)
        val id = persisted.id ?: fail { "id should not be null" }
        val version = persisted.version
        repo.findCodeClassDefinition(id).shouldNotBeNull()

        // delete the record
        val deleted = repo.delete(id, version) ?: fail { "Unable to delete the record" }
        deleted.id shouldBeEqualTo id

        // verify the record is not there anymore
        repo.findCodeClassDefinition(id).shouldBeNull()
    }
}
