package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition
import ch.difty.scipamato.core.entity.keyword.KeywordFilter
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation
import ch.difty.scipamato.core.persistence.keyword.JooqKeywordRepo
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Fail.fail
import org.junit.jupiter.api.Test
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
        assertThat(repo.findAll("en")).hasSize(3)
    }

    @Test
    fun findingKeywordDefinitions_withUnspecifiedFilter_findsAllDefinitions() {
        val kds = repo.findPageOfKeywordDefinitions(
            KeywordFilter(),
            PaginationRequest(Sort.Direction.ASC, "name")
        )

        assertThat(kds).hasSize(3)

        var kd = kds[0]
        assertThat(kd.id).isEqualTo(1)
        assertThat(kd.name).isEqualTo("Aerosol")
        assertThat(kd.searchOverride == null).isTrue()
        assertThat(kd.getNameInLanguage("de")).isEqualTo("Aerosol")
        assertThat(kd.getNameInLanguage("en")).isEqualTo("Aerosol")
        assertThat(kd.getNameInLanguage("fr")).isEqualTo("Aérosol")

        kd = kds[1]
        assertThat(kd.id).isEqualTo(2)
        assertThat(kd.name).isEqualTo("Aktivität, eingeschränkte")
        assertThat(kd.searchOverride).isEqualTo("Aktivität")
        assertThat(kd.getNameInLanguage("de")).isEqualTo("Aktivität, eingeschränkte")
        assertThat(kd.getNameInLanguage("en")).isEqualTo("Restricted activity")
        assertThat(kd.getNameInLanguage("fr")).isEqualTo("Activités réduites")

        kd = kds[2]
        assertThat(kd.id).isEqualTo(3)
        assertThat(kd.name).isEqualTo("Allergie (not Atopie)")
        assertThat(kd.searchOverride).isNull()
        assertThat(kd.getNameInLanguage("de")).isEqualTo("Allergie (not Atopie)")
        assertThat(kd.getNameInLanguage("en")).isEqualTo("Allergies")
        assertThat(kd.getNameInLanguage("fr")).isEqualTo("Allergie")
    }

    @Test
    fun findingKeywordDefinitions_withFilterMatchingSingleGermanTitle_findsOne() {
        val filter = KeywordFilter()
        filter.nameMask = "Allergie (not Atopie)"
        val kds = repo.findPageOfKeywordDefinitions(
            filter,
            PaginationRequest(Sort.Direction.ASC, "name")
        )

        assertThat(kds).hasSize(1)

        val kd = kds[0]
        assertThat(kd.id).isEqualTo(3)
        assertThat(kd.name).isEqualTo("Allergie (not Atopie)")
        assertThat(kd.searchOverride == null).isTrue()
        assertThat(kd.getNameInLanguage("de")).isEqualTo("Allergie (not Atopie)")
        assertThat(kd.getNameInLanguage("en")).isEqualTo("Allergies")
        assertThat(kd.getNameInLanguage("fr")).isEqualTo("Allergie")
    }

    @Test
    fun findingKeywordDefinitions_haveVersionFieldsPopulated() {
        val filter = KeywordFilter()
        filter.nameMask = "Allergie"
        val kds = repo.findPageOfKeywordDefinitions(
            filter,
            PaginationRequest(Sort.Direction.ASC, "name")
        )

        assertThat(kds).hasSize(1)

        val ntd = kds[0]

        assertThat(ntd.version).isEqualTo(1)
        assertThat(ntd.created).isNull()
        assertThat(ntd.lastModified).isNull()

        val tr = ntd.getTranslations().first()
        assertThat(tr.version).isEqualTo(1)
        assertThat(tr.created).isNull()
        assertThat(tr.lastModified).isNull()
    }

    @Test
    fun findingKeywordDefinitions_withFilterMatchingSeveral() {
        val filter = KeywordFilter()
        filter.nameMask = "er"
        val kds = repo.findPageOfKeywordDefinitions(
            filter,
            PaginationRequest(Sort.Direction.ASC, "name")
        )

        assertThat(kds).hasSize(2)

        var ntd = kds[0]
        assertThat(ntd.id).isEqualTo(1)
        assertThat(ntd.name).isEqualTo("Aerosol")
        assertThat(ntd.getNameInLanguage("de")).isEqualTo("Aerosol")
        assertThat(ntd.getNameInLanguage("en")).isEqualTo("Aerosol")
        assertThat(ntd.getNameInLanguage("fr")).isEqualTo("Aérosol")

        ntd = kds[1]
        assertThat(ntd.id).isEqualTo(3)
        assertThat(ntd.name).isEqualTo("Allergie (not Atopie)")
        assertThat(ntd.getNameInLanguage("de")).isEqualTo("Allergie (not Atopie)")
        assertThat(ntd.getNameInLanguage("en")).isEqualTo("Allergies")
        assertThat(ntd.getNameInLanguage("fr")).isEqualTo("Allergie")
    }

    @Test
    fun countingKeywords_witNullFilter_findsAllDefinitions() {
        assertThat(repo.countByFilter(null)).isEqualTo(3)
    }

    @Test
    fun countingKeywords_withUnspecifiedFilter_findsAllDefinitions() {
        assertThat(repo.countByFilter(KeywordFilter())).isEqualTo(3)
    }

    @Test
    fun countingKeywords_withFilter_findsAllMatchingDefinitions() {
        val filter = KeywordFilter()
        filter.nameMask = "es"
        assertThat(repo.countByFilter(filter)).isEqualTo(2)
    }

    @Test
    fun countingKeywords_withNaFilter_findsThem() {
        val filter = KeywordFilter()
        filter.nameMask = "n.a."
        assertThat(repo.countByFilter(filter)).isEqualTo(0)
    }

    @Test
    fun countingKeywords_withNonMatchingFilter_findsNone() {
        val filter = KeywordFilter()
        filter.nameMask = "foobar"
        assertThat(repo.countByFilter(filter)).isEqualTo(0)
    }

    @Test
    fun gettingMainLanguage() {
        assertThat(repo.mainLanguage).isEqualTo("de")
    }

    @Test
    fun findingMainLanguage() {
        val ntd = repo.newUnpersistedKeywordDefinition()

        assertThat(ntd.id).isNull()
        assertThat(ntd.mainLanguageCode).isEqualTo("de")
        assertThat(ntd.name).isEqualTo("n.a.")
        assertThat(ntd.getNameInLanguage("de")).isNull()
        assertThat(ntd.getTranslations()).hasSize(3)

        val translations = ntd.getTranslations()
        assertThat(translations.map { it.langCode }).containsOnly("de", "en", "fr")
        assertThat(translations.map { it.id }).containsExactly(null, null, null)
        assertThat(translations.map { it.name }).containsExactly(null, null, null)
    }

    @Test
    fun findingKeywordDefinition_withNonExistingId_returnsNull() {
        assertThat(repo.findKeywordDefinitionById(-1)).isNull()
    }

    @Test
    fun findingKeywordDefinition_withExistingId_loadsWithAllLanguages() {
        val existing = repo.findKeywordDefinitionById(1)
            ?: fail("Unable to find keyword definition with id 1")

        assertThat(existing.id).isEqualTo(1)
        assertThat(existing.name).isEqualTo("Aerosol")
        assertThat(existing.getTranslations()).hasSize(3)
        assertThat(existing.getNameInLanguage("de")).isEqualTo("Aerosol")
        assertThat(existing.getNameInLanguage("en")).isEqualTo("Aerosol")
        assertThat(existing.getNameInLanguage("fr")).isEqualTo("Aérosol")
    }

    @Test
    @Suppress("LocalVariableName", "VariableNaming")
    fun insertingRecord_savesRecordAndRefreshesId() {
        val kt_de = KeywordTranslation(null, "de", "foo_de", 0)
        val kt_en = KeywordTranslation(null, "en", "foo1_en", 0)
        val kt_fr = KeywordTranslation(null, "fr", "foo1_fr", 0)
        val kd = KeywordDefinition(null, "de", 0, kt_de, kt_en, kt_fr)

        assertThat(kd.id).isNull()
        assertThat(kd.getTranslations().map { it.id }).containsExactly(null, null, null)

        val saved = repo.insert(kd) ?: fail("Unable to insert keyword definition")

        assertThat(saved.id).isGreaterThan(0)
        assertThat(saved.name).isEqualTo("foo_de")
        assertThat(saved.getTranslations().map { it.version }).containsExactly(1, 1, 1)
    }

    @Test
    fun updatingRecord() {
        val kd = repo.findKeywordDefinitionById(2)
            ?: fail("Unable to find keyword definition with id 2")

        assertThat(kd.id).isEqualTo(2)
        assertThat(kd.searchOverride).isEqualTo("Aktivität")
        assertThat(kd.getTranslations()).hasSize(3)
        assertThat(kd.getNameInLanguage("de")).isEqualTo("Aktivität, eingeschränkte")
        assertThat(kd.getNameInLanguage("en")).isEqualTo("Restricted activity")
        assertThat(kd.getNameInLanguage("fr")).isEqualTo("Activités réduites")
        assertThat(kd.getTranslations("de").first().version).isEqualTo(1)
        assertThat(kd.getTranslations("en").first().version).isEqualTo(1)

        kd.searchOverride = "a"
        kd.setNameInLanguage("de", "ae")
        kd.setNameInLanguage("fr", "ar")

        val updated = repo.update(kd)

        assertThat(updated.id).isEqualTo(2)
        assertThat(updated.searchOverride).isEqualTo("a")
        assertThat(updated.getTranslations()).hasSize(3)
        assertThat(updated.getNameInLanguage("de")).isEqualTo("ae")
        assertThat(updated.getNameInLanguage("en")).isEqualTo("Restricted activity")
        assertThat(updated.getNameInLanguage("fr")).isEqualTo("ar")

        assertThat(updated.version).isEqualTo(2)
        assertThat(updated.getTranslations("de").first().version).isEqualTo(2)
        assertThat(updated.getTranslations("en").first().version).isEqualTo(2)
        assertThat(updated.getTranslations("fr").first().version).isEqualTo(2)
    }

    @Test
    fun deleting_withNonExistingId_returnsNull() {
        assertThat(repo.delete(-1, 1)).isNull()
    }

    @Suppress("TooGenericExceptionCaught")
    @Test
    fun deleting_withExistingId_butWrongVersion_throwsOptimisticLockingException() {
        try {
            repo.delete(1, -1)
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException::class.java)
                .hasMessage("Record in table 'keyword' has been modified prior to the delete attempt. Aborting....")
        }
    }

    @Test
    fun deleting_withExistingIdAndVersion_deletes() {
        // insert new record to the database and verify it's there
        val kd = KeywordDefinition(null, "de", null)
        val persisted = repo.insert(kd) ?: fail("Unable to insert keyword definition")
        val id = persisted.id ?: fail("Id should not be null now")
        val version = persisted.version
        assertThat(repo.findKeywordDefinitionById(id)).isNotNull()

        // delete the record
        val deleted = repo.delete(id, version) ?: fail("Unable to delete keyword definition")
        assertThat(deleted.id).isEqualTo(id)

        // verify the record is not there anymore
        assertThat(repo.findKeywordDefinitionById(id)).isNull()
    }

    @Test
    fun canLoadKeywordWithMultipleTranslationsInOneLanguage() {
        val kd = repo.findKeywordDefinitionById(3) ?: fail("Unable to find keyword definition with id 3")
        assertThat(kd.translationsAsString).isEqualTo(
            "DE: 'Allergie (not Atopie)','Allergie'; EN: 'Allergies'; FR: 'Allergie'"
        )
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

        assertThat(cds).hasSize(3)

        val cd = cds[0]
        assertThat(cd.id).isEqualTo(id)
    }
}
