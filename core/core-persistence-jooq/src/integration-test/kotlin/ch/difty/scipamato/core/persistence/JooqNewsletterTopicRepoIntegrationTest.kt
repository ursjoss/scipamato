package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation
import ch.difty.scipamato.core.persistence.newsletter.JooqNewsletterTopicRepo
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEmpty
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
@Suppress("TooManyFunctions", "SpellCheckingInspection", "FunctionName", "MagicNumber", "DuplicatedCode")
internal open class JooqNewsletterTopicRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqNewsletterTopicRepo

    @Test
    fun findingAll() {
        repo.findAll("en") shouldHaveSize 4
    }

    @Test
    fun findingNewsletterTopicDefinitions_withUnspecifiedFilter_findsAllDefinitions() {
        val ntds = repo.findPageOfNewsletterTopicDefinitions(
            NewsletterTopicFilter(), PaginationRequest(Sort.Direction.ASC, "title")
        )

        ntds shouldHaveSize 4

        var ntd = ntds[0]
        ntd.id shouldBeEqualTo 4
        ntd.title shouldBeEqualTo "Andere"
        ntd.getTitleInLanguage("de") shouldBeEqualTo "Andere"
        ntd.getTitleInLanguage("en") shouldBeEqualTo "Others"
        ntd.getTitleInLanguage("fr").shouldBeNull()

        ntd = ntds[1]
        ntd.id shouldBeEqualTo 3
        ntd.title shouldBeEqualTo "Gesundheitsfolgenabschätzung"
        ntd.getTitleInLanguage("de") shouldBeEqualTo "Gesundheitsfolgenabschätzung"
        ntd.getTitleInLanguage("en") shouldBeEqualTo "Health Impact Assessment"
        ntd.getTitleInLanguage("fr").shouldBeNull()

        ntd = ntds[2]
        ntd.id shouldBeEqualTo 2
        ntd.title shouldBeEqualTo "Sterblichkeit"
        ntd.getTitleInLanguage("de") shouldBeEqualTo "Sterblichkeit"
        ntd.getTitleInLanguage("en") shouldBeEqualTo "Mortality"
        ntd.getTitleInLanguage("fr").shouldBeNull()

        ntd = ntds[3]
        ntd.id shouldBeEqualTo 1
        ntd.title shouldBeEqualTo "Ultrafeine Partikel"
        ntd.getTitleInLanguage("de") shouldBeEqualTo "Ultrafeine Partikel"
        ntd.getTitleInLanguage("en") shouldBeEqualTo "Ultrafine Particles"
        ntd.getTitleInLanguage("fr").shouldBeNull()
    }

    @Test
    fun findingNewsletterTopicDefinitions_withUnspecifiedFilter_sortingDescendingly_findsAllDefinitions() {
        val ntds = repo.findPageOfNewsletterTopicDefinitions(
            NewsletterTopicFilter(), PaginationRequest(Sort.Direction.DESC, "title")
        )

        ntds shouldHaveSize 4

        val ntd = ntds[0]
        ntd.id shouldBeEqualTo 1
    }

    @Test
    fun findingNewsletterTopicDefinitions_withFilterMatchingSingleGermanTitle_findsOne() {
        val filter = NewsletterTopicFilter().apply { titleMask = "Partikel" }
        val ntds = repo.findPageOfNewsletterTopicDefinitions(
            filter,
            PaginationRequest(Sort.Direction.ASC, "title")
        )

        ntds shouldHaveSize 1

        val ntd = ntds[0]
        ntd.id shouldBeEqualTo 1
        ntd.title shouldBeEqualTo "Ultrafeine Partikel"
        ntd.getTitleInLanguage("de") shouldBeEqualTo "Ultrafeine Partikel"
        ntd.getTitleInLanguage("en") shouldBeEqualTo "Ultrafine Particles"
        ntd.getTitleInLanguage("fr").shouldBeNull()
    }

    @Test
    fun findingNewsletterTopicDefinitions_withFilterMatchingNa_findsSomeWithMissingTranslations() {
        val filter = NewsletterTopicFilter().apply { titleMask = "n.a." }
        val ntds = repo.findPageOfNewsletterTopicDefinitions(
            filter,
            PaginationRequest(Sort.Direction.ASC, "title")
        )

        ntds shouldHaveSize 4

        val ntd = ntds[1]
        ntd.id shouldBeEqualTo 3
        ntd.title shouldBeEqualTo "Gesundheitsfolgenabschätzung"
        ntd.getTitleInLanguage("de") shouldBeEqualTo "Gesundheitsfolgenabschätzung"
        ntd.getTitleInLanguage("fr").shouldBeNull()
    }

    @Test
    fun findingNewsletterTopicDefinitions_haveVersionFieldsPopulated() {
        val filter = NewsletterTopicFilter().apply { titleMask = "Partikel" }
        val ntds = repo.findPageOfNewsletterTopicDefinitions(
            filter,
            PaginationRequest(Sort.Direction.ASC, "title")
        )

        ntds shouldHaveSize 1

        val ntd = ntds[0]

        ntd.version shouldBeEqualTo 1
        ntd.created.shouldBeNull()
        ntd.lastModified.shouldBeNull()

        val translations = ntd.getTranslations()
        translations.isNotEmpty()
        val tr = translations.first()
        tr.version shouldBeEqualTo 1
        tr.created.shouldBeNull()
        tr.lastModified.shouldBeNull()
    }

    @Test
    fun findingNewsletterTopicDefinitions_withFilterMatchingSeveral() {
        val filter = NewsletterTopicFilter().apply { titleMask = "es" }
        val ntds = repo.findPageOfNewsletterTopicDefinitions(filter, PaginationRequest(Sort.Direction.ASC, "title"))

        ntds shouldHaveSize 2

        var ntd = ntds[0]
        ntd.id shouldBeEqualTo 3
        ntd.title shouldBeEqualTo "Gesundheitsfolgenabschätzung"
        ntd.getTitleInLanguage("de") shouldBeEqualTo "Gesundheitsfolgenabschätzung"
        ntd.getTitleInLanguage("en") shouldBeEqualTo "Health Impact Assessment"
        ntd.getTitleInLanguage("fr").shouldBeNull()

        ntd = ntds[1]
        ntd.id shouldBeEqualTo 1
        ntd.title shouldBeEqualTo "Ultrafeine Partikel"
        ntd.getTitleInLanguage("de") shouldBeEqualTo "Ultrafeine Partikel"
        ntd.getTitleInLanguage("en") shouldBeEqualTo "Ultrafine Particles"
        ntd.getTitleInLanguage("fr").shouldBeNull()
    }

    @Test
    fun countingNewsletterTopics_withUnspecifiedFilter_findsAllDefinitions() {
        repo.countByFilter(NewsletterTopicFilter()) shouldBeEqualTo 4
    }

    @Test
    fun countingNewsletterTopics_withFilter_findsAllMatchingDefinitions() {
        val filter = NewsletterTopicFilter().apply { titleMask = "es" }
        repo.countByFilter(filter) shouldBeEqualTo 2
    }

    @Test
    fun countingNewsletterTopics_withNonMatchingFilter_findsNone() {
        val filter = NewsletterTopicFilter().apply { titleMask = "foobar" }
        repo.countByFilter(filter) shouldBeEqualTo 0
    }

    @Test
    fun gettingMainLanguage() {
        repo.mainLanguage shouldBeEqualTo "de"
    }

    @Test
    fun findingMainLanguage() {
        val ntd = repo.newUnpersistedNewsletterTopicDefinition()

        ntd.id.shouldBeNull()
        ntd.mainLanguageCode shouldBeEqualTo "de"
        ntd.title shouldBeEqualTo "n.a."
        ntd.getTitleInLanguage("de").shouldBeNull()
        ntd.getTranslations() shouldHaveSize 3

        val translations = ntd.getTranslations()
        translations.map { it.langCode } shouldContainSame listOf("de", "en", "fr")
        translations.map { it.id } shouldContainAll listOf(null, null, null)
        translations.map { it.title } shouldContainAll listOf(null, null, null)
    }

    @Test
    fun findingNewsletterTopicDefinition_withNonExistingId_returnsNull() {
        repo.findNewsletterTopicDefinitionById(-1).shouldBeNull()
    }

    @Test
    fun findingNewsletterTopicDefinition_withExistingId_loadsWithAllLanguages() {
        val existing = repo.findNewsletterTopicDefinitionById(1)
            ?: fail { "Unable to find newsletter topic definition" }

        existing.id shouldBeEqualTo 1
        existing.title shouldBeEqualTo "Ultrafeine Partikel"
        existing.getTranslations() shouldHaveSize 3
        existing.getTitleInLanguage("de") shouldBeEqualTo "Ultrafeine Partikel"
        existing.getTitleInLanguage("en") shouldBeEqualTo "Ultrafine Particles"
        existing.getTitleInLanguage("fr").shouldBeNull()
    }

    @Suppress("LocalVariableName", "VariableNaming")
    @Test
    fun insertingRecord_savesRecordAndRefreshesId() {
        val ntt_de = NewsletterTopicTranslation(null, "de", "foo_de", 0)
        val ntt_en = NewsletterTopicTranslation(null, "en", "foo1_en", 0)
        val ntt_fr = NewsletterTopicTranslation(null, "fr", "foo1_fr", 0)
        val ntd = NewsletterTopicDefinition(null, "de", 0, ntt_de, ntt_en, ntt_fr)

        ntd.id.shouldBeNull()
        ntd.getTranslations().map { it.id } shouldContainAll listOf(null, null, null)

        val saved = repo.insert(ntd) ?: fail { "Unable to insert newsletter topic definition" }

        saved.id?.shouldBeGreaterThan(0)
        saved.title shouldBeEqualTo "foo_de"
        saved.getTranslations() shouldHaveSize 3
        saved.getTranslations().map { it.version } shouldContainAll listOf(1, 1, 1)
    }

    @Test
    fun updatingRecord() {
        val ntd = repo.findNewsletterTopicDefinitionById(1)
            ?: fail { "Unable to find newsletter topic definition" }

        ntd.id shouldBeEqualTo 1
        ntd.getTranslations() shouldHaveSize 3
        ntd.getTitleInLanguage("de") shouldBeEqualTo "Ultrafeine Partikel"
        ntd.getTitleInLanguage("en") shouldBeEqualTo "Ultrafine Particles"
        ntd.getTitleInLanguage("fr").shouldBeNull()
        ntd.getTranslations("de").first().version shouldBeEqualTo 1
        ntd.getTranslations("en").first().version shouldBeEqualTo 1

        ntd.setTitleInLanguage("de", "ufp")
        ntd.setTitleInLanguage("fr", "foo")

        val updated = repo.update(ntd) ?: fail { "Unable to update newsletter topic definition" }

        updated.id shouldBeEqualTo 1
        updated.getTranslations() shouldHaveSize 3
        updated.getTitleInLanguage("de") shouldBeEqualTo "ufp"
        updated.getTitleInLanguage("en") shouldBeEqualTo "Ultrafine Particles"
        updated.getTitleInLanguage("fr") shouldBeEqualTo "foo"

        updated.version shouldBeEqualTo 2
        updated.getTranslations("de").first().version shouldBeEqualTo 2
        updated.getTranslations("en").first().version shouldBeEqualTo 2
        updated.getTranslations("fr").first().version shouldBeEqualTo 1
    }

    @Test
    fun deleting_withNonExistingId_returnsNull() {
        repo.delete(-1, 1).shouldBeNull()
    }

    @Suppress("TooGenericExceptionCaught")
    @Test
    fun deleting_withExistingId_butWrongVersion_throwsOptimisticLockingException() {
        invoking { repo.delete(1, -1) } shouldThrow OptimisticLockingException::class withMessage
            "Record in table 'newsletter_topic' has been modified prior to the delete attempt. Aborting...."
    }

    @Test
    fun deleting_withExistingIdAndVersion_deletes() {
        // insert new record to the database and verify it's there
        val ntd = NewsletterTopicDefinition(null, "de", null)
        val persisted = repo.insert(ntd) ?: fail { "Unable to insert newsletter topic definition" }
        val id = persisted.id ?: fail { "id should not be null" }
        val version = persisted.version
        repo.findNewsletterTopicDefinitionById(id).shouldNotBeNull()

        // delete the record
        val deleted = repo.delete(id, version) ?: fail { "Unable to delete newsletter topic definition" }
        deleted.id shouldBeEqualTo id

        // verify the record is not there anymore
        repo.findNewsletterTopicDefinitionById(id).shouldBeNull()
    }

    @Test
    fun findingPersistedSortedNewsletterTopicsForNewsletterWithId() {
        repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(1).shouldBeEmpty()
    }

    @Test
    fun findingAllSortedNewsletterTopicsForNewsletterWithId() {
        repo.findAllSortedNewsletterTopicsForNewsletterWithId(1) shouldHaveSize 3
    }

    @Test
    fun removingObsoleteNewsletterTopicsFromSort() {
        repo.removeObsoleteNewsletterTopicsFromSort(1)
        // TODO currently only asserting that the method runs without failure. Need test data and actually assert the behavior
    }

    @Test
    fun savingSortedNewsletterTopics_ignoresAnyTopicsNotAsssignedToCurrentNewsletter() {
        val newsletterId = 1
        val initialRecords = repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)
        initialRecords.shouldBeEmpty()

        val topics = ArrayList<NewsletterNewsletterTopic>()
        topics.add(NewsletterNewsletterTopic(newsletterId, 1, 1, "foo"))
        topics.add(NewsletterNewsletterTopic(newsletterId, 2, 2, "bar"))
        topics.add(NewsletterNewsletterTopic(newsletterId + 1, 3, 3, "baz"))

        repo.saveSortedNewsletterTopics(newsletterId, topics)

        val newRecords = repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)
        newRecords.size shouldBeEqualTo 2
        newRecords.map { it.newsletterId }.toSet() shouldContainSame setOf(newsletterId)
        newRecords.map { it.sort } shouldContainAll listOf(1, 2)
    }

    @Test
    fun removingObsoleteNewsletterTopicsFromSort_doesNotRemoveActiveRecords() {
        val newsletterId = 1

        val initialRecords = repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)
        initialRecords.shouldBeEmpty()

        val topics = ArrayList<NewsletterNewsletterTopic>()
        // topics actually assigned to the newsletter in papers
        topics.add(NewsletterNewsletterTopic(newsletterId, 1, 1, "foo"))
        topics.add(NewsletterNewsletterTopic(newsletterId, 2, 2, "bar"))
        topics.add(NewsletterNewsletterTopic(newsletterId, 3, 3, "baz"))
        // topic not assigned to the newsletter in papers - obsolete
        topics.add(NewsletterNewsletterTopic(newsletterId, 4, 4, "bam"))

        repo.saveSortedNewsletterTopics(newsletterId, topics)

        val newRecords = repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)
        newRecords.map { it.sort } shouldContainAll listOf(1, 2, 3, 4)

        repo.removeObsoleteNewsletterTopicsFromSort(newsletterId)

        val cleansedRecords = repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)
        cleansedRecords.map { it.sort } shouldContainAll listOf(1, 2, 3)
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
            NewsletterTopicFilter(), PaginationRequest(0, 10, Sort.Direction.DESC, sortProperty)
        )

        cds shouldHaveSize 4

        val ntd = cds[0]
        ntd.id shouldBeEqualTo id
    }
}
