package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition
import ch.difty.scipamato.core.entity.keyword.KeywordFilter
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation
import ch.difty.scipamato.core.persistence.keyword.JooqKeywordRepo
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeNull
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@JooqTest
@Testcontainers
@Suppress("TooManyFunctions", "FunctionName", "MagicNumber", "DuplicatedCode", "SpellCheckingInspection")
internal open class JooqKeywordRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqKeywordRepo

    @Test
    fun findingAll() {
        repo.findAll("en") shouldHaveSize 3
    }

    @Test
    fun findingKeywordDefinitions_withUnspecifiedFilter_findsAllDefinitions() {
        val kds = repo.findPageOfKeywordDefinitions(
            KeywordFilter(),
            PaginationRequest(Sort.Direction.ASC, "name")
        )

        kds shouldHaveSize 3

        var kd = kds[0]
        kd.id shouldBeEqualTo 1
        kd.name shouldBeEqualTo "Aerosol"
        kd.searchOverride.shouldBeNull()
        kd.getNameInLanguage("de") shouldBeEqualTo "Aerosol"
        kd.getNameInLanguage("en") shouldBeEqualTo "Aerosol"
        kd.getNameInLanguage("fr") shouldBeEqualTo "Aérosol"

        kd = kds[1]
        kd.id shouldBeEqualTo 2
        kd.name shouldBeEqualTo "Aktivität, eingeschränkte"
        kd.searchOverride shouldBeEqualTo "Aktivität"
        kd.getNameInLanguage("de") shouldBeEqualTo "Aktivität, eingeschränkte"
        kd.getNameInLanguage("en") shouldBeEqualTo "Restricted activity"
        kd.getNameInLanguage("fr") shouldBeEqualTo "Activités réduites"

        kd = kds[2]
        kd.id shouldBeEqualTo 3
        kd.name shouldBeEqualTo "Allergie (not Atopie)"
        kd.searchOverride.shouldBeNull()
        kd.getNameInLanguage("de") shouldBeEqualTo "Allergie (not Atopie)"
        kd.getNameInLanguage("en") shouldBeEqualTo "Allergies"
        kd.getNameInLanguage("fr") shouldBeEqualTo "Allergie"
    }

    @Test
    fun findingKeywordDefinitions_withFilterMatchingSingleGermanTitle_findsOne() {
        val filter = KeywordFilter()
        filter.nameMask = "Allergie (not Atopie)"
        val kds = repo.findPageOfKeywordDefinitions(
            filter,
            PaginationRequest(Sort.Direction.ASC, "name")
        )

        kds shouldHaveSize 1

        val kd = kds[0]
        kd.id shouldBeEqualTo 3
        kd.name shouldBeEqualTo "Allergie (not Atopie)"
        kd.searchOverride.shouldBeNull()
        kd.getNameInLanguage("de") shouldBeEqualTo "Allergie (not Atopie)"
        kd.getNameInLanguage("en") shouldBeEqualTo "Allergies"
        kd.getNameInLanguage("fr") shouldBeEqualTo "Allergie"
    }

    @Test
    fun findingKeywordDefinitions_haveVersionFieldsPopulated() {
        val filter = KeywordFilter()
        filter.nameMask = "Allergie"
        val kds = repo.findPageOfKeywordDefinitions(
            filter,
            PaginationRequest(Sort.Direction.ASC, "name")
        )

        kds shouldHaveSize 1

        val ntd = kds[0]

        ntd.version shouldBeEqualTo 1
        ntd.created.shouldBeNull()
        ntd.lastModified.shouldBeNull()

        val tr = ntd.getTranslations().first()
        tr.version shouldBeEqualTo 1
        tr.created.shouldBeNull()
        tr.lastModified.shouldBeNull()
    }

    @Test
    fun findingKeywordDefinitions_withFilterMatchingSeveral() {
        val filter = KeywordFilter()
        filter.nameMask = "er"
        val kds = repo.findPageOfKeywordDefinitions(
            filter,
            PaginationRequest(Sort.Direction.ASC, "name")
        )

        kds shouldHaveSize 2

        var ntd = kds[0]
        ntd.id shouldBeEqualTo 1
        ntd.name shouldBeEqualTo "Aerosol"
        ntd.getNameInLanguage("de") shouldBeEqualTo "Aerosol"
        ntd.getNameInLanguage("en") shouldBeEqualTo "Aerosol"
        ntd.getNameInLanguage("fr") shouldBeEqualTo "Aérosol"

        ntd = kds[1]
        ntd.id shouldBeEqualTo 3
        ntd.name shouldBeEqualTo "Allergie (not Atopie)"
        ntd.getNameInLanguage("de") shouldBeEqualTo "Allergie (not Atopie)"
        ntd.getNameInLanguage("en") shouldBeEqualTo "Allergies"
        ntd.getNameInLanguage("fr") shouldBeEqualTo "Allergie"
    }

    @Test
    fun countingKeywords_witNullFilter_findsAllDefinitions() {
        repo.countByFilter(null) shouldBeEqualTo 3
    }

    @Test
    fun countingKeywords_withUnspecifiedFilter_findsAllDefinitions() {
        repo.countByFilter(KeywordFilter()) shouldBeEqualTo 3
    }

    @Test
    fun countingKeywords_withFilter_findsAllMatchingDefinitions() {
        val filter = KeywordFilter()
        filter.nameMask = "es"
        repo.countByFilter(filter) shouldBeEqualTo 2
    }

    @Test
    fun countingKeywords_withNaFilter_findsThem() {
        val filter = KeywordFilter()
        filter.nameMask = "n.a."
        repo.countByFilter(filter) shouldBeEqualTo 0
    }

    @Test
    fun countingKeywords_withNonMatchingFilter_findsNone() {
        val filter = KeywordFilter()
        filter.nameMask = "foobar"
        repo.countByFilter(filter) shouldBeEqualTo 0
    }

    @Test
    fun gettingMainLanguage() {
        repo.mainLanguage shouldBeEqualTo "de"
    }

    @Test
    fun findingMainLanguage() {
        val ntd = repo.newUnpersistedKeywordDefinition()

        ntd.id.shouldBeNull()
        ntd.mainLanguageCode shouldBeEqualTo "de"
        ntd.name shouldBeEqualTo "n.a."
        ntd.getNameInLanguage("de").shouldBeNull()
        ntd.getTranslations() shouldHaveSize 3

        val translations = ntd.getTranslations()
        translations.map { it.langCode } shouldContainSame listOf("de", "en", "fr")
        translations.map { it.id } shouldContainAll listOf(null, null, null)
        translations.map { it.name } shouldContainAll listOf(null, null, null)
    }

    @Test
    fun findingKeywordDefinition_withNonExistingId_returnsNull() {
        repo.findKeywordDefinitionById(-1).shouldBeNull()
    }

    @Test
    fun findingKeywordDefinition_withExistingId_loadsWithAllLanguages() {
        val existing = repo.findKeywordDefinitionById(1)
            ?: fail { "Unable to find keyword definition with id 1" }

        existing.id shouldBeEqualTo 1
        existing.name shouldBeEqualTo "Aerosol"
        existing.getTranslations() shouldHaveSize 3
        existing.getNameInLanguage("de") shouldBeEqualTo "Aerosol"
        existing.getNameInLanguage("en") shouldBeEqualTo "Aerosol"
        existing.getNameInLanguage("fr") shouldBeEqualTo "Aérosol"
    }

    @Test
    @Suppress("LocalVariableName", "VariableNaming")
    fun insertingRecord_savesRecordAndRefreshesId() {
        val kt_de = KeywordTranslation(null, "de", "foo_de", 0)
        val kt_en = KeywordTranslation(null, "en", "foo1_en", 0)
        val kt_fr = KeywordTranslation(null, "fr", "foo1_fr", 0)
        val kd = KeywordDefinition(null, "de", 0, kt_de, kt_en, kt_fr)

        kd.id.shouldBeNull()
        kd.getTranslations().map { it.id } shouldContainAll listOf(null, null, null)

        val saved = repo.insert(kd) ?: fail { "Unable to insert keyword definition" }

        saved.id?.shouldBeGreaterThan(0)
        saved.name shouldBeEqualTo "foo_de"
        saved.getTranslations().map { it.version } shouldContainAll listOf(1, 1, 1)
    }

    @Test
    fun updatingRecord() {
        val kd = repo.findKeywordDefinitionById(2)
            ?: fail { "Unable to find keyword definition with id 2" }

        kd.id shouldBeEqualTo 2
        kd.searchOverride shouldBeEqualTo "Aktivität"
        kd.getTranslations() shouldHaveSize 3
        kd.getNameInLanguage("de") shouldBeEqualTo "Aktivität, eingeschränkte"
        kd.getNameInLanguage("en") shouldBeEqualTo "Restricted activity"
        kd.getNameInLanguage("fr") shouldBeEqualTo "Activités réduites"
        kd.getTranslations("de").first().version shouldBeEqualTo 1
        kd.getTranslations("en").first().version shouldBeEqualTo 1

        kd.searchOverride = "a"
        kd.setNameInLanguage("de", "ae")
        kd.setNameInLanguage("fr", "ar")

        val updated = repo.update(kd)

        updated.id shouldBeEqualTo 2
        updated.searchOverride shouldBeEqualTo "a"
        updated.getTranslations() shouldHaveSize 3
        updated.getNameInLanguage("de") shouldBeEqualTo "ae"
        updated.getNameInLanguage("en") shouldBeEqualTo "Restricted activity"
        updated.getNameInLanguage("fr") shouldBeEqualTo "ar"

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
            "Record in table 'keyword' has been modified prior to the delete attempt. Aborting...."
    }

    @Test
    fun deleting_withExistingIdAndVersion_deletes() {
        // insert new record to the database and verify it's there
        val kd = KeywordDefinition(null, "de", null)
        val persisted = repo.insert(kd) ?: fail { "Unable to insert keyword definition" }
        val id = persisted.id ?: fail { "Id should not be null now" }
        val version = persisted.version
        repo.findKeywordDefinitionById(id).shouldNotBeNull()

        // delete the record
        val deleted = repo.delete(id, version) ?: fail { "Unable to delete keyword definition" }
        deleted.id shouldBeEqualTo id

        // verify the record is not there anymore
        repo.findKeywordDefinitionById(id).shouldBeNull()
    }

    @Test
    fun canLoadKeywordWithMultipleTranslationsInOneLanguage() {
        val kd = repo.findKeywordDefinitionById(3) ?: fail { "Unable to find keyword definition with id 3" }
        kd.translationsAsString shouldBeEqualTo
            "DE: 'Allergie (not Atopie)','Allergie'; EN: 'Allergies'; FR: 'Allergie'"
    }

    @Test
    fun findingKeywordDefinitions_sortedByName() {
        assertSortedList("name", 3)
    }

    @Test
    fun findingKeywordDefinitions_sortedByUndefinedProperty() {
        assertSortedList("whatever", 1)
    }

    private fun assertSortedList(sortProperty: String, id: Int?) {
        val cds = repo.findPageOfKeywordDefinitions(
            KeywordFilter(),
            PaginationRequest(0, 10, Sort.Direction.DESC, sortProperty)
        )

        cds shouldHaveSize 3

        val cd = cds[0]
        cd.id shouldBeEqualTo id
    }
}
