package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.Tables.PAPER_NEWSLETTER;
import static ch.difty.scipamato.core.db.tables.Language.LANGUAGE;
import static ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC;
import static ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC;
import static ch.difty.scipamato.core.db.tables.NewsletterTopicTr.NEWSLETTER_TOPIC_TR;
import static java.util.stream.Collectors.toList;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.db.tables.records.NewsletterTopicRecord;
import ch.difty.scipamato.core.db.tables.records.NewsletterTopicTrRecord;
import ch.difty.scipamato.core.entity.newsletter.*;
import ch.difty.scipamato.core.persistence.AbstractRepo;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

@Repository
@Slf4j
public class JooqNewsletterTopicRepo extends AbstractRepo implements NewsletterTopicRepository {

    public JooqNewsletterTopicRepo(@Qualifier("dslContext") final DSLContext dslContext,
        DateTimeService dateTimeService) {
        super(dslContext, dateTimeService);
    }

    @Override
    public List<NewsletterTopic> findAll(final String languageCode) {
        final String lang = TranslationUtils.trimLanguageCode(languageCode);
        // skipping the audit fields
        return getDsl()
            .select(NEWSLETTER_TOPIC.ID.as("NT_ID"), DSL
                .coalesce(NEWSLETTER_TOPIC_TR.TITLE, TranslationUtils.NOT_TRANSL)
                .as("NT_TITLE"))
            .from(NEWSLETTER_TOPIC)
            .leftOuterJoin(NEWSLETTER_TOPIC_TR)
            .on(NEWSLETTER_TOPIC.ID
                .equal(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID)
                .and(NEWSLETTER_TOPIC_TR.LANG_CODE.equal(lang)))
            .orderBy(NEWSLETTER_TOPIC_TR.TITLE)
            .fetchInto(NewsletterTopic.class);
    }

    @Override
    public List<NewsletterTopicDefinition> findPageOfNewsletterTopicDefinitions(final NewsletterTopicFilter filter,
        final PaginationContext pc) {
        final SelectOnConditionStep<Record> selectStep = getDsl()
            .select(NEWSLETTER_TOPIC.fields())
            .select(LANGUAGE.CODE)
            .select(NEWSLETTER_TOPIC_TR.fields())
            .from(NEWSLETTER_TOPIC)
            .crossJoin(LANGUAGE)
            .leftOuterJoin(NEWSLETTER_TOPIC_TR)
            .on(NEWSLETTER_TOPIC.ID.eq(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
            .and(LANGUAGE.CODE.eq(NEWSLETTER_TOPIC_TR.LANG_CODE));
        final SelectConditionStep<Record> selectConditionStep = applyWhereCondition(filter, selectStep);
        // the subsequent grouping requires ordering by id then language_code
        final Map<Integer, Result<Record>> rawRecords = selectConditionStep
            .orderBy(NEWSLETTER_TOPIC.ID, LANGUAGE.CODE, NEWSLETTER_TOPIC_TR.TITLE)
            .fetchGroups(NEWSLETTER_TOPIC.ID);
        final List<NewsletterTopicDefinition> results = mapRawRecordsIntoNewsletterTopicDefinitions(rawRecords);
        considerSorting(pc, results);
        // need to page after sorting due to grouping, not profiting from DB filtering :-(
        return results
            .stream()
            .skip((long) pc.getOffset())
            .limit((long) pc.getPageSize())
            .collect(toList());
    }

    private <R extends Record> SelectConditionStep<R> applyWhereCondition(final NewsletterTopicFilter filter,
        final SelectJoinStep<R> selectStep) {
        if (filter != null && filter.getTitleMask() != null) {
            final String titleMask = filter.getTitleMask();
            if ("n.a.".equals(titleMask))
                return selectStep.where(NEWSLETTER_TOPIC.ID.in(DSL
                    .selectDistinct(NEWSLETTER_TOPIC.ID)
                    .from(NEWSLETTER_TOPIC)
                    .crossJoin(LANGUAGE)
                    .leftOuterJoin(NEWSLETTER_TOPIC_TR)
                    .on(NEWSLETTER_TOPIC.ID.eq(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
                    .and(LANGUAGE.CODE.eq(NEWSLETTER_TOPIC_TR.LANG_CODE))
                    .where(NEWSLETTER_TOPIC_TR.TITLE.isNull())));
            else
                return selectStep.where(NEWSLETTER_TOPIC.ID.in(DSL
                    .selectDistinct(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID)
                    .from(NEWSLETTER_TOPIC_TR)
                    .where(NEWSLETTER_TOPIC_TR.TITLE.likeIgnoreCase('%' + titleMask + '%'))));
        } else {
            return selectStep.where(DSL.trueCondition());
        }
    }

    private List<NewsletterTopicDefinition> mapRawRecordsIntoNewsletterTopicDefinitions(
        final Map<Integer, Result<Record>> rawRecords) {
        final List<NewsletterTopicDefinition> definitions = new ArrayList<>();
        for (final Map.Entry<Integer, Result<Record>> entry : rawRecords.entrySet()) {
            final List<NewsletterTopicTranslation> translations = entry
                .getValue()
                .stream()
                .map(r -> new NewsletterTopicTranslation(r.getValue(NEWSLETTER_TOPIC_TR.ID), r.getValue(LANGUAGE.CODE),
                    r.getValue(NEWSLETTER_TOPIC_TR.TITLE), r.getValue(NEWSLETTER_TOPIC_TR.VERSION)))
                .collect(toList());
            final Record r = entry
                .getValue()
                .stream()
                .findFirst()
                .orElseThrow();
            definitions.add(toTopicDefinition(entry.getKey(), r.getValue(NEWSLETTER_TOPIC.VERSION), translations));
        }
        return definitions;
    }

    @Override
    public String getMainLanguage() {
        return getDsl()
            .select(LANGUAGE.CODE)
            .from(LANGUAGE)
            .where(LANGUAGE.MAIN_LANGUAGE.eq(true))
            .fetchOneInto(String.class);
    }

    @Override
    public NewsletterTopicDefinition newUnpersistedNewsletterTopicDefinition() {
        final List<NewsletterTopicTranslation> translations = getDsl()
            .select(LANGUAGE.CODE)
            .from(LANGUAGE)
            .fetchInto(String.class)
            .stream()
            .map(lc -> new NewsletterTopicTranslation(null, lc, null, 0))
            .collect(toList());
        return toTopicDefinition(null, 0, translations);
    }

    /**
     * Currently only accepting title as sort. It's a bit hacky...
     */
    private void considerSorting(final PaginationContext pc, final List<NewsletterTopicDefinition> results) {
        for (final Sort.SortProperty sortProperty : pc.getSort()) {
            final String propName = sortProperty.getName();
            if (propName.equals(NEWSLETTER_TOPIC_TR.TITLE.getName())) {
                if (sortProperty.getDirection() == Sort.Direction.DESC)
                    results.sort(Comparator
                        .comparing(NewsletterTopicDefinition::getTranslationsAsString)
                        .reversed());
                else
                    results.sort(Comparator.comparing(NewsletterTopicDefinition::getTranslationsAsString));
            }
        }
    }

    @Override
    public int countByFilter(final NewsletterTopicFilter filter) {
        final SelectJoinStep<Record1<Integer>> selectStep = getDsl()
            .selectCount()
            .from(NEWSLETTER_TOPIC);
        return applyWhereCondition(filter, selectStep).fetchOneInto(Integer.class);
    }

    @Override
    public NewsletterTopicDefinition findNewsletterTopicDefinitionById(final int id) {
        final Map<Integer, Result<Record>> records = getDsl()
            .select(NEWSLETTER_TOPIC.fields())
            .select(LANGUAGE.CODE)
            .select(NEWSLETTER_TOPIC_TR.fields())
            .from(NEWSLETTER_TOPIC)
            .crossJoin(LANGUAGE)
            .leftOuterJoin(NEWSLETTER_TOPIC_TR)
            .on(NEWSLETTER_TOPIC.ID.eq(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
            .and(LANGUAGE.CODE.eq(NEWSLETTER_TOPIC_TR.LANG_CODE))
            .where(NEWSLETTER_TOPIC.ID.eq(id))
            .fetchGroups(NEWSLETTER_TOPIC.ID);
        if (!records.isEmpty()) {
            final List<NewsletterTopicDefinition> results = mapRawRecordsIntoNewsletterTopicDefinitions(records);
            if (!results.isEmpty())
                return results.get(0);
        }
        return null;
    }

    @Override
    public NewsletterTopicDefinition insert(final NewsletterTopicDefinition entity) {
        AssertAs.notNull(entity, "entity");
        if (entity.getId() != null) {
            throw new IllegalArgumentException("id must be null.");
        }

        final int userId = getUserId();

        final NewsletterTopicRecord ntRecord = getDsl()
            .insertInto(NEWSLETTER_TOPIC)
            .set(NEWSLETTER_TOPIC.CREATED_BY, userId)
            .set(NEWSLETTER_TOPIC.LAST_MODIFIED_BY, userId)
            .returning()
            .fetchOne();
        final int ntId = ntRecord.get(NEWSLETTER_TOPIC.ID);

        final List<NewsletterTopicTranslation> persistedTranslations = persistTranslations(entity, userId, ntId);
        final NewsletterTopicDefinition persistedEntity = toTopicDefinition(ntId,
            ntRecord.get(NEWSLETTER_TOPIC.VERSION), persistedTranslations);
        log.info("{} inserted 1 record: {} with id {}.", getActiveUser().getUserName(), NEWSLETTER_TOPIC.getName(),
            ntId);
        return persistedEntity;
    }

    private List<NewsletterTopicTranslation> persistTranslations(final NewsletterTopicDefinition entity,
        final int userId, final int ntId) {
        final List<NewsletterTopicTranslation> nttPersisted = new ArrayList<>();
        for (final NewsletterTopicTranslation ntt : entity
            .getTranslations()
            .values()) {
            final NewsletterTopicTrRecord nttRecord = insertAndGetNewsletterTopicTr(ntId, userId, ntt);
            nttPersisted.add(toTopicTranslation(nttRecord));
        }
        return nttPersisted;
    }

    @Override
    public NewsletterTopicDefinition update(final NewsletterTopicDefinition entity) {
        AssertAs.notNull(entity, "entity");
        AssertAs.notNull(entity.getId(), "entity.id");

        final int userId = getUserId();
        final int currentVersion = entity.getVersion();

        final NewsletterTopicRecord record = updateAndLoadNewsletterTopicDefinition(entity, userId, currentVersion);
        if (record != null) {
            final List<NewsletterTopicTranslation> persistedTranslations = updateOrInsertAndLoadNewsletterTopicTranslations(
                entity, userId);
            final NewsletterTopicDefinition updatedEntity = toTopicDefinition(entity.getId(),
                record.get(NEWSLETTER_TOPIC.VERSION), persistedTranslations);
            log.info("{} updated 1 record: {} with id {}.", getActiveUser().getUserName(), NEWSLETTER_TOPIC.getName(),
                updatedEntity.getId());
            return updatedEntity;
        } else {
            throw new OptimisticLockingException(NEWSLETTER_TOPIC.getName(), entity.toString(),
                OptimisticLockingException.Type.UPDATE);
        }
    }

    private NewsletterTopicDefinition toTopicDefinition(final Integer id, final int version,
        final List<NewsletterTopicTranslation> persistedTranslations) {
        return new NewsletterTopicDefinition(id, getMainLanguage(), version,
            persistedTranslations.toArray(new NewsletterTopicTranslation[0]));
    }

    private NewsletterTopicRecord updateAndLoadNewsletterTopicDefinition(final NewsletterTopicDefinition entity,
        final int userId, final int currentVersion) {
        return getDsl()
            .update(NEWSLETTER_TOPIC)
            .set(NEWSLETTER_TOPIC.VERSION, currentVersion + 1)
            .set(NEWSLETTER_TOPIC.LAST_MODIFIED_BY, userId)
            .set(NEWSLETTER_TOPIC.LAST_MODIFIED, getTs())
            .where(NEWSLETTER_TOPIC.ID.eq(entity.getId()))
            .and(NEWSLETTER_TOPIC.VERSION.eq(currentVersion))
            .returning()
            .fetchOne();
    }

    private List<NewsletterTopicTranslation> updateOrInsertAndLoadNewsletterTopicTranslations(
        final NewsletterTopicDefinition entity, final int userId) {
        final List<NewsletterTopicTranslation> nttPersisted = new ArrayList<>();
        for (final NewsletterTopicTranslation ntt : entity
            .getTranslations()
            .values()) {
            if (ntt.getId() != null) {
                final int currentVersion = ntt.getVersion();
                final NewsletterTopicTrRecord nttRecord = updateNewsletterTopicTr(entity, ntt, userId, currentVersion);
                if (nttRecord != null) {
                    nttPersisted.add(toTopicTranslation(nttRecord));
                } else {
                    throw new OptimisticLockingException(NEWSLETTER_TOPIC_TR.getName(), ntt.toString(),
                        OptimisticLockingException.Type.UPDATE);
                }
            } else {
                final NewsletterTopicTrRecord nttRecord = insertAndGetNewsletterTopicTr(entity.getId(), userId, ntt);
                nttPersisted.add(toTopicTranslation(nttRecord));
            }
        }
        return nttPersisted;
    }

    private NewsletterTopicTrRecord insertAndGetNewsletterTopicTr(final int topicId, final int userId,
        final NewsletterTopicTranslation ntt) {
        return getDsl()
            .insertInto(NEWSLETTER_TOPIC_TR)
            .set(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID, topicId)
            .set(NEWSLETTER_TOPIC_TR.LANG_CODE, ntt.getLangCode())
            .set(NEWSLETTER_TOPIC_TR.TITLE, ntt.getTitle())
            .set(NEWSLETTER_TOPIC_TR.CREATED_BY, userId)
            .set(NEWSLETTER_TOPIC_TR.LAST_MODIFIED_BY, userId)
            .returning()
            .fetchOne();
    }

    private NewsletterTopicTrRecord updateNewsletterTopicTr(final NewsletterTopicDefinition entity,
        final NewsletterTopicTranslation ntt, final int userId, final int currentNttVersion) {
        final int topicId = entity.getId();
        return getDsl()
            .update(NEWSLETTER_TOPIC_TR)
            .set(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID, topicId)
            .set(NEWSLETTER_TOPIC_TR.LANG_CODE, ntt.getLangCode())
            .set(NEWSLETTER_TOPIC_TR.TITLE, ntt.getTitle())
            .set(NEWSLETTER_TOPIC_TR.LAST_MODIFIED_BY, userId)
            .set(NEWSLETTER_TOPIC_TR.LAST_MODIFIED, getTs())
            .set(NEWSLETTER_TOPIC_TR.VERSION, currentNttVersion + 1)
            .where(NEWSLETTER_TOPIC_TR.ID.eq(ntt.getId()))
            .and(NEWSLETTER_TOPIC_TR.VERSION.eq(currentNttVersion))
            .returning()
            .fetchOne();
    }

    private NewsletterTopicTranslation toTopicTranslation(final NewsletterTopicTrRecord record) {
        return new NewsletterTopicTranslation(record.get(NEWSLETTER_TOPIC_TR.ID),
            record.get(NEWSLETTER_TOPIC_TR.LANG_CODE), record.get(NEWSLETTER_TOPIC_TR.TITLE),
            record.get(NEWSLETTER_TOPIC_TR.VERSION));
    }

    @Override
    public NewsletterTopicDefinition delete(final Integer id, final int version) {
        AssertAs.notNull(id, "id");

        final NewsletterTopicDefinition toBeDeleted = findNewsletterTopicDefinitionById(id);
        if (toBeDeleted != null) {
            if (toBeDeleted.getVersion() == version) {
                final int deleteCount = getDsl()
                    .delete(NEWSLETTER_TOPIC)
                    .where(NEWSLETTER_TOPIC.ID.equal(id))
                    .and(NEWSLETTER_TOPIC.VERSION.eq(version))
                    .execute();
                if (deleteCount > 0) {
                    log.info("{} deleted {} record{}: {} with id {}.", getActiveUser().getUserName(), deleteCount,
                        (deleteCount != 1 ? "s" : ""), NEWSLETTER_TOPIC.getName(), id);
                } else {
                    throw new OptimisticLockingException(NEWSLETTER_TOPIC.getName(), toBeDeleted.toString(),
                        OptimisticLockingException.Type.DELETE);
                }
            } else {
                throw new OptimisticLockingException(NEWSLETTER_TOPIC.getName(),
                    OptimisticLockingException.Type.DELETE);
            }
        }
        return toBeDeleted;
    }

    @Override
    public List<NewsletterNewsletterTopic> findPersistedSortedNewsletterTopicsForNewsletterWithId(
        final int newsletterId) {
        return getDsl()
            .select(NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_ID, NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_TOPIC_ID,
                NEWSLETTER_NEWSLETTER_TOPIC.SORT, NEWSLETTER_TOPIC_TR.TITLE)
            .from(NEWSLETTER_NEWSLETTER_TOPIC)
            .innerJoin(NEWSLETTER_TOPIC_TR)
            .on(NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_TOPIC_ID.eq(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
            .where(NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_ID.eq(newsletterId))
            .and(NEWSLETTER_TOPIC_TR.LANG_CODE.eq(getMainLanguage()))
            .orderBy(NEWSLETTER_NEWSLETTER_TOPIC.SORT, NEWSLETTER_TOPIC_TR.TITLE)
            .fetchInto(NewsletterNewsletterTopic.class);
    }

    @Override
    public List<NewsletterNewsletterTopic> findAllSortedNewsletterTopicsForNewsletterWithId(final int newsletterId) {
        return getDsl()
            .selectDistinct(PAPER_NEWSLETTER.NEWSLETTER_ID, PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID,
                DSL.value(Integer.MAX_VALUE), NEWSLETTER_TOPIC_TR.TITLE)
            .from(PAPER_NEWSLETTER)
            .innerJoin(NEWSLETTER_TOPIC_TR)
            .on(PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID.eq(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
            .where(PAPER_NEWSLETTER.NEWSLETTER_ID.eq(newsletterId))
            .and(NEWSLETTER_TOPIC_TR.LANG_CODE.eq(getMainLanguage()))
            .orderBy(NEWSLETTER_TOPIC_TR.TITLE)
            .fetchInto(NewsletterNewsletterTopic.class);
    }

    @Override
    public void removeObsoleteNewsletterTopicsFromSort(final int newsletterId) {
        final List<NewsletterNewsletterTopic> persistedTopics = findPersistedSortedNewsletterTopicsForNewsletterWithId(
            (newsletterId));
        final List<NewsletterNewsletterTopic> usedTopics = findAllSortedNewsletterTopicsForNewsletterWithId(
            (newsletterId));
        persistedTopics.removeAll(usedTopics);

        persistedTopics.forEach(t -> getDsl()
            .deleteFrom(NEWSLETTER_NEWSLETTER_TOPIC)
            .where(NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_ID
                .eq(t.getNewsletterId())
                .and(NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_TOPIC_ID.eq(t.getNewsletterTopicId())))
            .execute());
    }

    @Override
    public void saveSortedNewsletterTopics(final int newsletterId, final List<NewsletterNewsletterTopic> topics) {
        final Timestamp ts = getDateTimeService().getCurrentTimestamp();
        topics
            .stream()
            .filter(t -> newsletterId == t.getNewsletterId())
            .forEach(t -> getDsl()
                .insertInto(NEWSLETTER_NEWSLETTER_TOPIC)
                .columns(NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_ID, NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_TOPIC_ID,
                    NEWSLETTER_NEWSLETTER_TOPIC.SORT, NEWSLETTER_NEWSLETTER_TOPIC.VERSION,
                    NEWSLETTER_NEWSLETTER_TOPIC.CREATED, NEWSLETTER_NEWSLETTER_TOPIC.CREATED_BY)
                .values(t.getNewsletterId(), t.getNewsletterTopicId(), t.getSort(), 1, ts, getUserId())
                .onDuplicateKeyUpdate()
                .set(NEWSLETTER_NEWSLETTER_TOPIC.SORT, t.getSort())
                .set(NEWSLETTER_NEWSLETTER_TOPIC.VERSION, t.getVersion() + 1)
                .set(NEWSLETTER_NEWSLETTER_TOPIC.LAST_MODIFIED, ts)
                .set(NEWSLETTER_NEWSLETTER_TOPIC.LAST_MODIFIED_BY, getUserId())
                .execute());
    }

}
