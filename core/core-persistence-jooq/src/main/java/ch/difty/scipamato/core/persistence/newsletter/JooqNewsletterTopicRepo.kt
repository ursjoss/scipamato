package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.TranslationUtils
import ch.difty.scipamato.common.TranslationUtils.trimLanguageCode
import ch.difty.scipamato.common.logger
import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.core.db.Tables
import ch.difty.scipamato.core.db.tables.Language
import ch.difty.scipamato.core.db.tables.NewsletterTopicTr
import ch.difty.scipamato.core.db.tables.records.NewsletterTopicRecord
import ch.difty.scipamato.core.db.tables.records.NewsletterTopicTrRecord
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation
import ch.difty.scipamato.core.persistence.AbstractRepo
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.SelectConditionStep
import org.jooq.SelectJoinStep
import org.jooq.SelectOnConditionStep
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.util.ArrayList
import java.util.Comparator
import java.util.function.Consumer

private val log = logger()
private val name = ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.name

@Suppress("SpellCheckingInspection")
@Repository
@Profile("!wickettest")
open class JooqNewsletterTopicRepo(@Qualifier("dslContext") dslContext: DSLContext, dateTimeService: DateTimeService) : AbstractRepo(dslContext, dateTimeService), NewsletterTopicRepository {

    override fun findAll(languageCode: String): List<NewsletterTopic> {
        val lang = trimLanguageCode(languageCode)
        // skipping the audit fields
        return dsl
            .select(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID.`as`("NT_ID"), DSL
                .coalesce(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE, TranslationUtils.NOT_TRANSL)
                .`as`("NT_TITLE"))
            .from(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC)
            .leftOuterJoin(NewsletterTopicTr.NEWSLETTER_TOPIC_TR)
            .on(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID
                .equal(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID)
                .and(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LANG_CODE.equal(lang)))
            .orderBy(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE)
            .fetchInto(NewsletterTopic::class.java)
    }

    override fun findPageOfNewsletterTopicDefinitions(
        filter: NewsletterTopicFilter?,
        pc: PaginationContext
    ): List<NewsletterTopicDefinition> {
        val selectConditionStep = applyWhereCondition(filter, selectBaseStep())
        // the subsequent grouping requires ordering by id then language_code
        val rawRecords = selectConditionStep
            .orderBy(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID, Language.LANGUAGE.CODE, NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE)
            .fetchGroups(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID)
        val results = mapRawRecordsIntoNewsletterTopicDefinitions(rawRecords)
        // need to page after sorting due to grouping, not profiting from DB filtering :-(
        return results
            .bestEffortSortUsing(pc)
            .drop(pc.offset)
            .take(pc.pageSize)
    }

    private fun selectBaseStep(): SelectOnConditionStep<Record> = dsl
        .select(*ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.fields())
        .select(Language.LANGUAGE.CODE)
        .select(*NewsletterTopicTr.NEWSLETTER_TOPIC_TR.fields())
        .from(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC)
        .crossJoin(Language.LANGUAGE)
        .leftOuterJoin(NewsletterTopicTr.NEWSLETTER_TOPIC_TR)
        .on(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID.eq(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
        .and(Language.LANGUAGE.CODE.eq(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LANG_CODE))

    // package-private for test purposes
    fun <R : Record?> applyWhereCondition(
        filter: NewsletterTopicFilter?,
        selectStep: SelectJoinStep<R>
    ): SelectConditionStep<R> =
        if (filter != null && filter.titleMask != null) {
            val titleMask = filter.titleMask
            if ("n.a." == titleMask) selectStep.where(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID.`in`(DSL
                .selectDistinct(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID)
                .from(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC)
                .crossJoin(Language.LANGUAGE)
                .leftOuterJoin(NewsletterTopicTr.NEWSLETTER_TOPIC_TR)
                .on(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID.eq(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
                .and(Language.LANGUAGE.CODE.eq(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LANG_CODE))
                .where(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE.isNull))) else selectStep.where(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID.`in`(DSL
                .selectDistinct(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID)
                .from(NewsletterTopicTr.NEWSLETTER_TOPIC_TR)
                .where(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE.likeIgnoreCase("%$titleMask%"))))
        } else {
            selectStep.where(DSL.noCondition())
        }

    private fun mapRawRecordsIntoNewsletterTopicDefinitions(rawRecords: Map<Int, Result<Record>>): List<NewsletterTopicDefinition> {
        val definitions: MutableList<NewsletterTopicDefinition> = ArrayList()
        for ((key, value) in rawRecords) {
            val translations = value
                .map { r: Record ->
                    NewsletterTopicTranslation(r.getValue(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.ID), r.getValue(Language.LANGUAGE.CODE),
                        r.getValue(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE), r.getValue(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.VERSION))
                }
            val r = value.firstOrNull() ?: error("unable to find translation.")
            definitions.add(toTopicDefinition(key, r.getValue(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.VERSION), translations))
        }
        return definitions
    }

    override fun getMainLanguage(): String = dsl
        .select(Language.LANGUAGE.CODE)
        .from(Language.LANGUAGE)
        .where(Language.LANGUAGE.MAIN_LANGUAGE.eq(true))
        .fetchOneInto(String::class.java)

    override fun newUnpersistedNewsletterTopicDefinition(): NewsletterTopicDefinition {
        val translations = dsl
            .select(Language.LANGUAGE.CODE)
            .from(Language.LANGUAGE)
            .fetchInto(String::class.java)
            .map { lc: String? -> NewsletterTopicTranslation(null, lc!!, null, 0) }
        return toTopicDefinition(null, 0, translations)
    }

    /**
     * Currently only accepting title as sort. It's a bit hacky...
     */
    private fun List<NewsletterTopicDefinition>.bestEffortSortUsing(pc: PaginationContext): List<NewsletterTopicDefinition> {
        for ((propName, direction) in pc.sort) {
            if (propName == NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE.name) {
                val comparator: Comparator<NewsletterTopicDefinition> = if (direction === Sort.Direction.DESC)
                    compareByDescending(String.CASE_INSENSITIVE_ORDER) { it.translationsAsString ?: "n.a." }
                else
                    compareBy(String.CASE_INSENSITIVE_ORDER) { it.translationsAsString ?: "n.a." }
                return sortedWith(comparator)
            }
        }
        return this
    }

    override fun countByFilter(filter: NewsletterTopicFilter?): Int {
        val selectStep = dsl
            .selectCount()
            .from(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC)
        return applyWhereCondition(filter, selectStep).fetchOneInto(Int::class.java)
    }

    override fun findNewsletterTopicDefinitionById(id: Int): NewsletterTopicDefinition? {
        val records = selectBaseStep()
            .where(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID.eq(id))
            .fetchGroups(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID)
        return mapRawRecordsIntoNewsletterTopicDefinitions(records).firstOrNull()
    }


    override fun insert(entity: NewsletterTopicDefinition): NewsletterTopicDefinition? {
        require(entity.id == null) { "id must be null." }
        val ntRecord = dsl
            .insertInto(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC)
            .set(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.CREATED_BY, userId)
            .set(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.LAST_MODIFIED_BY, userId)
            .returning()
            .fetchOne() ?: error("unable to find newsletter topic record")
        val ntId = ntRecord.get(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID)
        val persistedTranslations = persistTranslations(entity, userId, ntId)
        val persistedEntity = toTopicDefinition(ntId, ntRecord.get(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.VERSION), persistedTranslations)
        log.info { "${activeUser.userName} inserted 1 record: $name with id $ntId." }
        return persistedEntity
    }

    private fun persistTranslations(entity: NewsletterTopicDefinition, userId: Int, ntId: Int): List<NewsletterTopicTranslation> {
        val nttPersisted: MutableList<NewsletterTopicTranslation> = ArrayList()
        entity.getTranslations(null).forEach { ntt ->
            val nttRecord = insertAndGetNewsletterTopicTr(ntId, userId, ntt)
            nttPersisted.add(toTopicTranslation(nttRecord))
        }
        return nttPersisted
    }

    override fun update(entity: NewsletterTopicDefinition): NewsletterTopicDefinition? {
        require(entity.id != null) { "entity.id must not be null" }
        val currentVersion = entity.version
        val record = updateAndLoadNewsletterTopicDefinition(entity, userId, currentVersion)
        return handleUpdatedRecord(record, entity, userId)
    }

    // package-private for testing purposes
    // entity.id must not be null
    fun handleUpdatedRecord(
        record: NewsletterTopicRecord?,
        entity: NewsletterTopicDefinition,
        userId: Int
    ): NewsletterTopicDefinition {
        if (record != null) {
            val persistedTranslations = updateOrInsertAndLoadNewsletterTopicTranslations(entity, userId)
            val updatedEntity = toTopicDefinition(entity.id, record.get(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.VERSION),
                persistedTranslations)
            log.info { "${activeUser.userName} updated 1 record: $name with id ${updatedEntity.id}." }
            return updatedEntity
        } else {
            throw OptimisticLockingException(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.name, entity.toString(), OptimisticLockingException.Type.UPDATE)
        }
    }

    private fun toTopicDefinition(
        id: Int?, version: Int,
        persistedTranslations: List<NewsletterTopicTranslation>
    ): NewsletterTopicDefinition = NewsletterTopicDefinition(id, mainLanguage, version, *persistedTranslations.toTypedArray())

    private fun updateAndLoadNewsletterTopicDefinition(
        entity: NewsletterTopicDefinition, userId: Int,
        currentVersion: Int
    ): NewsletterTopicRecord? = dsl
        .update(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC)
        .set(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.VERSION, currentVersion + 1)
        .set(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.LAST_MODIFIED_BY, userId)
        .set(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.LAST_MODIFIED, ts)
        .where(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID.eq(entity.id))
        .and(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.VERSION.eq(currentVersion))
        .returning()
        .fetchOne()

    // entity.id must not be null
    private fun updateOrInsertAndLoadNewsletterTopicTranslations(
        entity: NewsletterTopicDefinition,
        userId: Int
    ): List<NewsletterTopicTranslation> {
        val nttPersisted: MutableList<NewsletterTopicTranslation> = ArrayList()
        for (ntt in entity.getTranslations(null)) {
            if (ntt.id != null) {
                val nttRecord = updateNewsletterTopicTr(entity, ntt, userId, ntt.version)
                addOrThrow(nttRecord, nttPersisted, ntt.toString())
            } else {
                val nttRecord = insertAndGetNewsletterTopicTr(entity.id!!, userId, ntt)
                nttPersisted.add(toTopicTranslation(nttRecord))
            }
        }
        return nttPersisted
    }

    private fun insertAndGetNewsletterTopicTr(
        topicId: Int, userId: Int,
        ntt: NewsletterTopicTranslation
    ): NewsletterTopicTrRecord = dsl
        .insertInto(NewsletterTopicTr.NEWSLETTER_TOPIC_TR)
        .set(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID, topicId)
        .set(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LANG_CODE, ntt.langCode)
        .set(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE, ntt.title)
        .set(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.CREATED_BY, userId)
        .set(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LAST_MODIFIED_BY, userId)
        .returning()
        .fetchOne() ?: error("Unable to store and retrieve newsletter topic translation with topic id $topicId")

    // entity.id must not be null
    private fun updateNewsletterTopicTr(
        entity: NewsletterTopicDefinition, ntt: NewsletterTopicTranslation,
        userId: Int, currentNttVersion: Int
    ): NewsletterTopicTrRecord? = dsl
        .update(NewsletterTopicTr.NEWSLETTER_TOPIC_TR)
        .set(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID, entity.id!!)
        .set(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LANG_CODE, ntt.langCode)
        .set(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE, ntt.title)
        .set(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LAST_MODIFIED_BY, userId)
        .set(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LAST_MODIFIED, ts)
        .set(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.VERSION, currentNttVersion + 1)
        .where(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.ID.eq(ntt.id))
        .and(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.VERSION.eq(currentNttVersion))
        .returning()
        .fetchOne()

    // package-private for test purposes
    fun addOrThrow(
        nttRecord: NewsletterTopicTrRecord?,
        nttPersisted: MutableList<NewsletterTopicTranslation>,
        nttAsString: String?
    ) {
        if (nttRecord != null) {
            nttPersisted.add(toTopicTranslation(nttRecord))
        } else {
            throw OptimisticLockingException(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.name, nttAsString, OptimisticLockingException.Type.UPDATE)
        }
    }

    private fun toTopicTranslation(record: NewsletterTopicTrRecord): NewsletterTopicTranslation =
        NewsletterTopicTranslation(
            record.get(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.ID),
            record.get(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LANG_CODE),
            record.get(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE),
            record.get(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.VERSION)
        )

    override fun delete(id: Int, version: Int): NewsletterTopicDefinition? {
        val toBeDeleted = findNewsletterTopicDefinitionById(id)
        toBeDeleted?.let {
            if (it.version == version) {
                val deleteCount = doDelete(id, version)
                logOrThrow(deleteCount, id, it.toString())
            } else {
                throw OptimisticLockingException(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.name, OptimisticLockingException.Type.DELETE)
            }
        }
        return toBeDeleted
    }

    private fun doDelete(id: Int, version: Int): Int = dsl
        .delete(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC)
        .where(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.ID.equal(id))
        .and(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.VERSION.eq(version))
        .execute()

    // package-private for test purposes
    fun logOrThrow(deleteCount: Int, id: Int, deletedAsString: String) {
        if (deleteCount > 0) {
            log.info { "${activeUser.userName} deleted $deleteCount record: $name with id $id." }
        } else {
            throw OptimisticLockingException(ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC.name, deletedAsString, OptimisticLockingException.Type.DELETE)
        }
    }

    override fun findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId: Int): MutableList<NewsletterNewsletterTopic> = dsl
        .select(
            ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_ID,
            ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_TOPIC_ID,
            ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.SORT,
            NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE
        )
        .from(ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC)
        .innerJoin(NewsletterTopicTr.NEWSLETTER_TOPIC_TR)
        .on(ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_TOPIC_ID.eq(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
        .where(ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_ID.eq(newsletterId))
        .and(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LANG_CODE.eq(mainLanguage))
        .orderBy(ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.SORT, NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE)
        .fetchInto(NewsletterNewsletterTopic::class.java)

    override fun findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId: Int): List<NewsletterNewsletterTopic> = dsl
        .selectDistinct(
            Tables.PAPER_NEWSLETTER.NEWSLETTER_ID,
            Tables.PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID,
            DSL.value(Int.MAX_VALUE),
            NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE
        )
        .from(Tables.PAPER_NEWSLETTER)
        .innerJoin(NewsletterTopicTr.NEWSLETTER_TOPIC_TR)
        .on(Tables.PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID.eq(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
        .where(Tables.PAPER_NEWSLETTER.NEWSLETTER_ID.eq(newsletterId))
        .and(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LANG_CODE.eq(mainLanguage))
        .orderBy(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE)
        .fetchInto(NewsletterNewsletterTopic::class.java)

    override fun removeObsoleteNewsletterTopicsFromSort(newsletterId: Int) {
        val persistedTopics = findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)
        val usedTopics = findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)
        persistedTopics.removeAll(usedTopics)
        persistedTopics.forEach(Consumer { t: NewsletterNewsletterTopic ->
            dsl
                .deleteFrom(ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC)
                .where(ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_ID
                    .eq(t.newsletterId)
                    .and(ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_TOPIC_ID.eq(t.newsletterTopicId)))
                .execute()
        })
    }

    override fun saveSortedNewsletterTopics(newsletterId: Int, topics: List<NewsletterNewsletterTopic>) {
        val ts = dateTimeService.currentTimestamp
        topics
            .filter { t: NewsletterNewsletterTopic -> newsletterId == t.newsletterId }
            .forEach { t: NewsletterNewsletterTopic ->
                dsl
                    .insertInto(ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC)
                    .columns(
                        ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_ID,
                        ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_TOPIC_ID,
                        ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.SORT,
                        ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.VERSION,
                        ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.CREATED,
                        ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.CREATED_BY
                    )
                    .values(t.newsletterId, t.newsletterTopicId, t.sort, 1, ts, userId)
                    .onDuplicateKeyUpdate()
                    .set(ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.SORT, t.sort)
                    .set(ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.VERSION, t.version + 1)
                    .set(ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.LAST_MODIFIED, ts)
                    .set(ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC.LAST_MODIFIED_BY, userId)
                    .execute()
            }
    }
}
