package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.logger
import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition
import ch.difty.scipamato.core.entity.codeclass.CodeClassFilter
import ch.difty.scipamato.core.entity.codeclass.CodeClassTranslation
import ch.difty.scipamato.core.persistence.codeclass.JooqCodeClassRepo
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

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
        assertThat(ccs).hasSize(CODE_CLASS_COUNT)
        ccs.forEach { cc -> log.debug(cc.toString()) }
    }

    @Test
    fun findingAllCodesClassesInEnglish() {
        val ccs = repo.find("en")
        assertThat(ccs).hasSize(CODE_CLASS_COUNT)
        ccs.forEach { cc -> log.debug(cc.toString()) }
    }

    @Test
    fun findingAllCodesClassesInFrench() {
        val ccs = repo.find("fr")
        assertThat(ccs).hasSize(CODE_CLASS_COUNT)
        ccs.forEach { cc -> log.debug(cc.toString()) }
    }

    @Test
    fun findingCodeClassDefinitions_withUnspecifiedFilter_findsAllDefinitions() {
        val ccds = repo.findPageOfCodeClassDefinitions(
            CodeClassFilter(),
            PaginationRequest(0, 8, Sort.Direction.ASC, "name")
        )

        assertThat(ccds).hasSize(8)

        var ccd = ccds[0]
        assertThat(ccd.name).isEqualTo("Kollektiv")
        assertThat(ccd.getNameInLanguage("de")).startsWith("Kollektiv")
        assertThat(ccd.getNameInLanguage("en")).startsWith("Study Population")
        assertThat(ccd.getNameInLanguage("fr")).startsWith("Population")

        ccd = ccds[1]
        assertThat(ccd.name).isEqualTo("Region")
        assertThat(ccd.getNameInLanguage("de")).startsWith("Region")
        assertThat(ccd.getNameInLanguage("en")).startsWith("Region")
        assertThat(ccd.getNameInLanguage("fr")).startsWith("Région")
    }

    @Test
    fun findingCodeClassDefinitions_sortingByUndefinedField_doesNotSort() {
        val ccds = repo.findPageOfCodeClassDefinitions(
            CodeClassFilter(),
            PaginationRequest(0, 8, Sort.Direction.ASC, "foobar")
        )

        assertThat(ccds).hasSize(8)
        assertThat(ccds[0].name).isEqualTo("Schadstoffe")
        assertThat(ccds[1].name).isEqualTo("Region")
    }

    @Test
    fun findingCodeClassDefinitions_withUnspecifiedFilter_withReverseSortByTranslations() {
        val filter = CodeClassFilter()
        filter.descriptionMask = "en"
        val ccds = repo.findPageOfCodeClassDefinitions(
            filter,
            PaginationRequest(0, 8, Sort.Direction.DESC, "translationsAsString")
        )

        assertThat(ccds).hasSize(3)

        assertThat(ccds.first().name).isEqualTo("Zielgrössen")
    }

    @Test
    fun findingCodeClassDefinitions_withFilterMatchingSingleGermanName_findsOne() {
        val filter = CodeClassFilter()
        filter.nameMask = "Zeitdauer"
        val ccds = repo.findPageOfCodeClassDefinitions(
            filter,
            PaginationRequest(Sort.Direction.ASC, "name")
        )

        assertThat(ccds).hasSize(1)

        val ntd = ccds.first()
        assertThat(ntd.name).isEqualTo("Zeitdauer")
        assertThat(ntd.getNameInLanguage("de")).isEqualTo("Zeitdauer")
        assertThat(ntd.getNameInLanguage("en")).isEqualTo("Duration of Exposure")
        assertThat(ntd.getNameInLanguage("fr")).isEqualTo("Durée de l'exposition")
    }

    @Test
    fun findingCodeClassDefinitions_haveVersionFieldsPopulated() {
        val filter = CodeClassFilter()
        filter.nameMask = "Zeitdauer"
        val ccds = repo.findPageOfCodeClassDefinitions(
            filter,
            PaginationRequest(Sort.Direction.DESC, "name")
        )

        assertThat(ccds).hasSize(1)

        val ccd = ccds.first()

        assertThat(ccd.version).isEqualTo(1)
        assertThat(ccd.created).isNull()
        assertThat(ccd.lastModified).isNull()

        val tr = ccd.getTranslations().first()
        assertThat(tr.version).isEqualTo(1)
        assertThat(tr.created).isNull()
        assertThat(tr.lastModified).isNull()
    }

    @Test
    fun countingCodeClasses_witNullFilter_findsAllDefinitions() {
        assertThat(repo.countByFilter(null)).isEqualTo(8)
    }

    @Test
    fun countingCodeClasses_withUnspecifiedFilter_findsAllDefinitions() {
        assertThat(repo.countByFilter(CodeClassFilter())).isEqualTo(8)
    }

    @Test
    fun countingCodeClasses_withFilter_findsAllMatchingDefinitions() {
        val filter = CodeClassFilter()
        filter.nameMask = "en"
        assertThat(repo.countByFilter(filter)).isEqualTo(3)
    }

    @Test
    fun countingCodeClasses_withNonMatchingFilter_findsNone() {
        val filter = CodeClassFilter()
        filter.nameMask = "foobar"
        assertThat(repo.countByFilter(filter)).isEqualTo(0)
    }

    @Test
    fun gettingMainLanguage() {
        assertThat(repo.mainLanguage).isEqualTo("de")
    }

    @Test
    fun findingMainLanguage() {
        val ccd = repo.newUnpersistedCodeClassDefinition()

        assertThat(ccd.mainLanguageCode).isEqualTo("de")
        assertThat(ccd.name).isEqualTo("n.a.")
        assertThat(ccd.getNameInLanguage("de")).isNull()

        val translations = ccd.getTranslations()
        assertThat(translations.map { it.langCode }).containsOnly("de", "en", "fr")
        assertThat(translations.map { it.id }).containsExactly(null, null, null)
        assertThat(translations.map { it.name }).containsExactly(null, null, null)
    }

    @Test
    fun findingCodeClassDefinition_withNonExistingId_returnsNull() {
        assertThat(repo.findCodeClassDefinition(800)).isNull()
    }

    @Test
    fun findingCodeClassDefinition_withExistingId_loadsWithAllLanguages() {
        val existing = repo.findCodeClassDefinition(1)
            ?: fail("could not retrievee code class definition with id 1")

        assertThat(existing.name).startsWith("Schadstoffe")
        assertThat(existing.getTranslations()).hasSize(3)
        assertThat(existing.getNameInLanguage("de")).startsWith("Schadstoffe")
        assertThat(existing.getNameInLanguage("en")).startsWith("Exposure Agent")
        assertThat(existing.getNameInLanguage("fr")).startsWith("Polluant nocif")
    }

    @Test
    @Suppress("LocalVariableName", "VariableNaming")
    fun savingNewRecord_savesRecordAndRefreshesId() {
        val ct_de = CodeClassTranslation(null, "de", "foo_de", "Kommentar", 0)
        val ct_en = CodeClassTranslation(null, "en", "foo1_en", null, 0)
        val ct_fr = CodeClassTranslation(null, "fr", "foo1_fr", null, 0)
        val ccd = CodeClassDefinition(10, "de", 1, ct_de, ct_en, ct_fr)

        assertThat(ccd.getTranslations().map { it.id }).containsExactly(null, null, null)

        val saved = repo.saveOrUpdate(ccd)

        assertThat(saved.name).isEqualTo("foo_de")
        assertThat(saved.getTranslations().map { it.version }).containsExactly(1, 1, 1)
    }

    @Test
    fun updatingRecord() {
        val ccd = repo.findCodeClassDefinition(1)
            ?: fail("unable to retrieve code class definition with id 1")

        assertThat(ccd.name).isEqualTo("Schadstoffe")
        assertThat(ccd.getTranslations()).hasSize(3)
        assertThat(ccd.getNameInLanguage("de")).isEqualTo("Schadstoffe")
        assertThat(ccd.getNameInLanguage("en")).isEqualTo("Exposure Agent")
        assertThat(ccd.getNameInLanguage("fr")).isEqualTo("Polluant nocif")
        assertThat(ccd.getTranslations("de").first().version).isEqualTo(1)
        assertThat(ccd.getTranslations("en").first().version).isEqualTo(1)

        ccd.setNameInLanguage("de", "ss")
        ccd.setNameInLanguage("fr", "pn")

        val updated = repo.saveOrUpdate(ccd)

        assertThat(updated.getTranslations()).hasSize(3)
        assertThat(updated.getNameInLanguage("de")).isEqualTo("ss")
        assertThat(updated.getNameInLanguage("en")).isEqualTo("Exposure Agent")
        assertThat(updated.getNameInLanguage("fr")).isEqualTo("pn")

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
                .hasMessage("Record in table 'code_class' has been modified prior to the delete attempt. Aborting....")
        }
    }

    @Test
    fun deleting_withExistingIdAndVersion_deletes() {
        // insert new record to the database and verify it's there
        val ccd = CodeClassDefinition(10, "de", 1)
        val persisted = repo.saveOrUpdate(ccd)
        val id = persisted.id ?: fail("id should not be null")
        val version = persisted.version
        assertThat(repo.findCodeClassDefinition(id)).isNotNull()

        // delete the record
        val deleted = repo.delete(id, version) ?: fail("Unable to delete the record")
        assertThat(deleted.id).isEqualTo(id)

        // verify the record is not there anymore
        assertThat(repo.findCodeClassDefinition(id)).isNull()
    }

    companion object {
        private const val CODE_CLASS_COUNT = 8
    }
}
