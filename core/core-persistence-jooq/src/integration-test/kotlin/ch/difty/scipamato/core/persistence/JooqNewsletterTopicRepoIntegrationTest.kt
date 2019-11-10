package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation
import ch.difty.scipamato.core.persistence.newsletter.JooqNewsletterTopicRepo
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Fail.fail
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@JooqTest
@Testcontainers
@Suppress("TooManyFunctions", "SpellCheckingInspection", "FunctionName", "MagicNumber", "DuplicatedCode")
internal open class JooqNewsletterTopicRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqNewsletterTopicRepo

    @Test
    fun findingAll() {
        assertThat(repo.findAll("en")).hasSize(3)
    }

    @Test
    fun findingNewsletterTopicDefinitions_withUnspecifiedFilter_findsAllDefinitions() {
        val ntds = repo.findPageOfNewsletterTopicDefinitions(
            NewsletterTopicFilter(), PaginationRequest(Sort.Direction.ASC, "title"))

        assertThat(ntds).hasSize(3)

        var ntd = ntds[0]
        assertThat(ntd.id).isEqualTo(3)
        assertThat(ntd.title).isEqualTo("Gesundheitsfolgenabschätzung")
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Gesundheitsfolgenabschätzung")
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Health Impact Assessment")
        assertThat(ntd.getTitleInLanguage("fr") == null).isTrue()

        ntd = ntds[1]
        assertThat(ntd.id).isEqualTo(2)
        assertThat(ntd.title).isEqualTo("Sterblichkeit")
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Sterblichkeit")
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Mortality")
        assertThat(ntd.getTitleInLanguage("fr") == null).isTrue()

        ntd = ntds[2]
        assertThat(ntd.id).isEqualTo(1)
        assertThat(ntd.title).isEqualTo("Ultrafeine Partikel")
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Ultrafeine Partikel")
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Ultrafine Particles")
        assertThat(ntd.getTitleInLanguage("fr") == null).isTrue()
    }

    @Test
    fun findingNewsletterTopicDefinitions_withUnspecifiedFilter_sortingDescendingly_findsAllDefinitions() {
        val ntds = repo.findPageOfNewsletterTopicDefinitions(
            NewsletterTopicFilter(), PaginationRequest(Sort.Direction.DESC, "title"))

        assertThat(ntds).hasSize(3)

        val ntd = ntds[0]
        assertThat(ntd.id).isEqualTo(1)
    }

    @Test
    fun findingNewsletterTopicDefinitions_withFilterMatchingSingleGermanTitle_findsOne() {
        val filter = NewsletterTopicFilter().apply { titleMask = "Partikel" }
        val ntds = repo.findPageOfNewsletterTopicDefinitions(filter,
            PaginationRequest(Sort.Direction.ASC, "title"))

        assertThat(ntds).hasSize(1)

        val ntd = ntds[0]
        assertThat(ntd.id).isEqualTo(1)
        assertThat(ntd.title).isEqualTo("Ultrafeine Partikel")
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Ultrafeine Partikel")
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Ultrafine Particles")
        assertThat(ntd.getTitleInLanguage("fr") == null).isTrue()
    }

    @Test
    fun findingNewsletterTopicDefinitions_withFilterMatchingNa_findsSomeWithMissingTranslations() {
        val filter = NewsletterTopicFilter().apply { titleMask = "n.a." }
        val ntds = repo.findPageOfNewsletterTopicDefinitions(filter,
            PaginationRequest(Sort.Direction.ASC, "title"))

        assertThat(ntds).hasSize(3)

        val ntd = ntds[0]
        assertThat(ntd.id).isEqualTo(3)
        assertThat(ntd.title).isEqualTo("Gesundheitsfolgenabschätzung")
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Gesundheitsfolgenabschätzung")
        assertThat(ntd.getTitleInLanguage("fr") == null).isTrue()
    }

    @Test
    fun findingNewsletterTopicDefinitions_haveVersionFieldsPopulated() {
        val filter = NewsletterTopicFilter().apply { titleMask = "Partikel" }
        val ntds = repo.findPageOfNewsletterTopicDefinitions(filter,
            PaginationRequest(Sort.Direction.ASC, "title"))

        assertThat(ntds).hasSize(1)

        val ntd = ntds[0]

        assertThat(ntd.version).isEqualTo(1)
        assertThat(ntd.created == null).isTrue()
        assertThat(ntd.lastModified == null).isTrue()

        val translations = ntd.translations.values()
        assertThat(translations).isNotEmpty
        val tr = translations.first()
        assertThat(tr.version).isEqualTo(1)
        assertThat(tr.created == null).isTrue()
        assertThat(tr.lastModified == null).isTrue()
    }

    @Test
    fun findingNewsletterTopicDefinitions_withFilterMatchingSeveral() {
        val filter = NewsletterTopicFilter().apply { titleMask = "es" }
        val ntds = repo.findPageOfNewsletterTopicDefinitions(filter, PaginationRequest(Sort.Direction.ASC, "title"))

        assertThat(ntds).hasSize(2)

        var ntd = ntds[0]
        assertThat(ntd.id).isEqualTo(3)
        assertThat(ntd.title).isEqualTo("Gesundheitsfolgenabschätzung")
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Gesundheitsfolgenabschätzung")
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Health Impact Assessment")
        assertThat(ntd.getTitleInLanguage("fr") == null).isTrue()

        ntd = ntds[1]
        assertThat(ntd.id).isEqualTo(1)
        assertThat(ntd.title).isEqualTo("Ultrafeine Partikel")
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Ultrafeine Partikel")
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Ultrafine Particles")
        assertThat(ntd.getTitleInLanguage("fr") == null).isTrue()
    }

    @Test
    fun countingNewsletterTopics_withUnspecifiedFilter_findsAllDefinitions() {
        assertThat(repo.countByFilter(NewsletterTopicFilter())).isEqualTo(3)
    }

    @Test
    fun countingNewsletterTopics_withFilter_findsAllMatchingDefinitions() {
        val filter = NewsletterTopicFilter().apply { titleMask = "es" }
        assertThat(repo.countByFilter(filter)).isEqualTo(2)
    }

    @Test
    fun countingNewsletterTopics_withNonMatchingFilter_findsNone() {
        val filter = NewsletterTopicFilter().apply { titleMask = "foobar" }
        assertThat(repo.countByFilter(filter)).isEqualTo(0)
    }

    @Test
    fun gettingMainLanguage() {
        assertThat(repo.mainLanguage).isEqualTo("de")
    }

    @Test
    fun findingMainLanguage() {
        val ntd = repo.newUnpersistedNewsletterTopicDefinition()

        assertThat(ntd.id == null).isTrue()
        assertThat(ntd.mainLanguageCode).isEqualTo("de")
        assertThat(ntd.title).isEqualTo("n.a.")
        assertThat(ntd.getTitleInLanguage("de") == null).isTrue()
        assertThat(ntd.translations.asMap()).hasSize(3)

        val translations = ntd.translations.values()
        assertThat(translations.map { it.langCode }).containsOnly("de", "en", "fr")
        assertThat(translations.map { it.id }).containsExactly(null, null, null)
        assertThat(translations.map { it.title }).containsExactly(null, null, null)
    }

    @Test
    fun findingNewsletterTopicDefinition_withNonExistingId_returnsNull() {
        assertThat(repo.findNewsletterTopicDefinitionById(-1) == null).isTrue()
    }

    @Test
    fun findingNewsletterTopicDefinition_withExistingId_loadsWithAllLanguages() {
        val existing = repo.findNewsletterTopicDefinitionById(1)
            ?: Assertions.fail("Unable to find newsletter topic definition")

        assertThat(existing.id).isEqualTo(1)
        assertThat(existing.title).isEqualTo("Ultrafeine Partikel")
        assertThat(existing.translations.asMap()).hasSize(3)
        assertThat(existing.getTitleInLanguage("de")).isEqualTo("Ultrafeine Partikel")
        assertThat(existing.getTitleInLanguage("en")).isEqualTo("Ultrafine Particles")
        assertThat(existing.getTitleInLanguage("fr") == null).isTrue()
    }

    @Suppress("LocalVariableName", "VariableNaming")
    @Test
    fun insertingRecord_savesRecordAndRefreshesId() {
        val ntt_de = NewsletterTopicTranslation(null, "de", "foo_de", 0)
        val ntt_en = NewsletterTopicTranslation(null, "en", "foo1_en", 0)
        val ntt_fr = NewsletterTopicTranslation(null, "fr", "foo1_fr", 0)
        val ntd = NewsletterTopicDefinition(null, "de", 0, ntt_de, ntt_en, ntt_fr)

        assertThat(ntd.id == null).isTrue()
        assertThat(ntd.translations.values().map { it.id }).containsExactly(null, null, null)

        val saved = repo.insert(ntd) ?: Assertions.fail("Unable to insert newsletter topic definition")

        assertThat(saved.id).isGreaterThan(0)
        assertThat(saved.title).isEqualTo("foo_de")
        assertThat(saved.translations.size()).isEqualTo(3)
        assertThat(saved.translations.values().map { it.version }).containsExactly(1, 1, 1)
    }

    @Test
    fun updatingRecord() {
        val ntd = repo.findNewsletterTopicDefinitionById(1)
            ?: Assertions.fail("Unable to find newsletter topic definition")

        assertThat(ntd.id).isEqualTo(1)
        assertThat(ntd.translations.asMap()).hasSize(3)
        assertThat(ntd.getTitleInLanguage("de")).isEqualTo("Ultrafeine Partikel")
        assertThat(ntd.getTitleInLanguage("en")).isEqualTo("Ultrafine Particles")
        assertThat(ntd.getTitleInLanguage("fr") == null).isTrue()
        assertThat(ntd.translations.get("de").first().version).isEqualTo(1)
        assertThat(ntd.translations.get("en").first().version).isEqualTo(1)

        ntd.setTitleInLanguage("de", "ufp")
        ntd.setTitleInLanguage("fr", "foo")

        val updated = repo.update(ntd) ?: Assertions.fail("Unable to update newsletter topic definition")

        assertThat(updated.id).isEqualTo(1)
        assertThat(updated.translations.asMap()).hasSize(3)
        assertThat(updated.getTitleInLanguage("de")).isEqualTo("ufp")
        assertThat(updated.getTitleInLanguage("en")).isEqualTo("Ultrafine Particles")
        assertThat(updated.getTitleInLanguage("fr")).isEqualTo("foo")

        assertThat(updated.version).isEqualTo(2)
        assertThat(updated.translations.get("de").first().version).isEqualTo(2)
        assertThat(updated.translations.get("en").first().version).isEqualTo(2)
        assertThat(updated.translations.get("fr").first().version).isEqualTo(1)
    }

    @Test
    fun deleting_withNonExistingId_returnsNull() {
        assertThat(repo.delete(-1, 1) == null).isTrue()
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
                .hasMessage(
                    "Record in table 'newsletter_topic' has been modified prior to the delete attempt. Aborting...."
                )
        }
    }

    @Test
    fun deleting_withExistingIdAndVersion_deletes() {
        // insert new record to the database and verify it's there
        val ntd = NewsletterTopicDefinition(null, "de", null)
        val persisted = repo.insert(ntd) ?: Assertions.fail("Unable to insert newsletter topic definition")
        val id = persisted.id
        val version = persisted.version
        assertThat(repo.findNewsletterTopicDefinitionById(id) == null).isFalse()

        // delete the record
        val deleted = repo.delete(id, version) ?: Assertions.fail("Unable to delete newsletter topic definition")
        assertThat(deleted.id).isEqualTo(id)

        // verify the record is not there anymore
        assertThat(repo.findNewsletterTopicDefinitionById(id) == null).isTrue()
    }

    @Test
    fun findingPersistedSortedNewsletterTopicsForNewsletterWithId() {
        assertThat(repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(1)).isEmpty()
    }

    @Test
    fun findingAllSortedNewsletterTopicsForNewsletterWithId() {
        assertThat(repo.findAllSortedNewsletterTopicsForNewsletterWithId(1)).hasSize(3)
    }

    @Test
    fun removingObsoleteNewsletterTopicsFromSort() {
        repo.removeObsoleteNewsletterTopicsFromSort(1)
        // TODO currently only asserting that the method runs without failure. Need test data and actually assert the behavior
    }

    @Test
    fun savingSortedNewsletterTopics() {
        val newsletterId = 1

        val initialRecords = repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)
        assertThat(initialRecords).isEmpty()

        val topics = ArrayList<NewsletterNewsletterTopic>()
        topics.add(NewsletterNewsletterTopic(newsletterId, 1, 1, "foo"))
        topics.add(NewsletterNewsletterTopic(newsletterId, 2, 2, "bar"))
        topics.add(NewsletterNewsletterTopic(newsletterId + 1, 3, 3, "baz"))

        repo.saveSortedNewsletterTopics(newsletterId, topics)

        val newRecords = repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)
        assertThat(newRecords.map { it.sort }).containsExactly(1, 2)

        repo.removeObsoleteNewsletterTopicsFromSort(newsletterId)

        assertThat(repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)).isEmpty()
    }

    @Test
    fun findingNewsletterTopicDefinitions_sortedByName() {
        assertSortedList("name", 1)
    }

    @Test
    fun findingNewsletterTopicDefinitions_sortedByUndefinedProperty() {
        assertSortedList("whatever", 1)
    }

    @Suppress("SameParameterValue")
    private fun assertSortedList(sortProperty: String, id: Int?) {
        val cds = repo.findPageOfNewsletterTopicDefinitions(
            NewsletterTopicFilter(), PaginationRequest(0, 10, Sort.Direction.DESC, sortProperty))

        assertThat(cds).hasSize(3)

        val ntd = cds[0]
        assertThat(ntd.id).isEqualTo(id)
    }
}
