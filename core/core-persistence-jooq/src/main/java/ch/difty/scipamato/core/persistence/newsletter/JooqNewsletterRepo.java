package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;
import static ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC;
import static ch.difty.scipamato.core.db.tables.NewsletterTopicTr.NEWSLETTER_TOPIC_TR;
import static ch.difty.scipamato.core.db.tables.Paper.PAPER;
import static ch.difty.scipamato.core.db.tables.PaperNewsletter.PAPER_NEWSLETTER;

import java.sql.Timestamp;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Record;
import org.jooq.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.persistence.InsertSetStepSetter;
import ch.difty.scipamato.core.persistence.JooqEntityRepo;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter;

@Repository
@Slf4j
@Profile("!wickettest")
public class JooqNewsletterRepo extends
    JooqEntityRepo<NewsletterRecord, Newsletter, Integer, ch.difty.scipamato.core.db.tables.Newsletter, NewsletterRecordMapper, NewsletterFilter>
    implements NewsletterRepository {

    protected JooqNewsletterRepo(@Qualifier("dslContext") @NotNull final DSLContext dsl, @NotNull final NewsletterRecordMapper mapper,
        @NotNull final JooqSortMapper<NewsletterRecord, Newsletter, ch.difty.scipamato.core.db.tables.Newsletter> sortMapper,
        @NotNull final GenericFilterConditionMapper<NewsletterFilter> filterConditionMapper, @NotNull final DateTimeService dateTimeService,
        @NotNull final InsertSetStepSetter<NewsletterRecord, Newsletter> insertSetStepSetter,
        @NotNull final UpdateSetStepSetter<NewsletterRecord, Newsletter> updateSetStepSetter,
        @NotNull final ApplicationProperties applicationProperties) {
        super(dsl, mapper, sortMapper, filterConditionMapper, dateTimeService, insertSetStepSetter, updateSetStepSetter, applicationProperties);
    }

    @NotNull
    @Override
    protected Logger getLogger() {
        return log;
    }

    @NotNull
    @Override
    protected Integer getIdFrom(@NotNull final NewsletterRecord record) {
        return record.getId();
    }

    @NotNull
    @Override
    protected Integer getIdFrom(@NotNull final Newsletter entity) {
        return entity.getId();
    }

    @NotNull
    @Override
    protected ch.difty.scipamato.core.db.tables.Newsletter getTable() {
        return NEWSLETTER;
    }

    @NotNull
    @Override
    protected TableField<NewsletterRecord, Integer> getTableId() {
        return NEWSLETTER.ID;
    }

    @NotNull
    @Override
    protected TableField<NewsletterRecord, Integer> getRecordVersion() {
        return NEWSLETTER.VERSION;
    }

    @Override
    protected void enrichAssociatedEntitiesOf(@Nullable final Newsletter entity, @Nullable final String languageCode) {
        if (entity != null)
            enrichPaperAssociationsOf(entity, languageCode);
    }

    private void enrichPaperAssociationsOf(final Newsletter entity, final String languageCode) {
        final Result<Record7<Long, Long, String, Integer, String, Integer, String>> records = loadNewsletterPaperTopicRecords(entity.getId(),
            languageCode);
        for (final Record record : records)
            entity.addPaper(extractPaperFrom(record), extractTopicFrom(record));
    }

    /**
     * Load the raw data for the association between newsletter and paper (with optional topics)
     *
     * @param newsletterId
     *     the id of the newsletter
     * @param languageCode
     *     the language code, e.g. 'de' or 'en'
     * @return record containing paper, paper_newsletter and (left joined) newsletter_topic
     */
    private Result<Record7<Long, Long, String, Integer, String, Integer, String>> loadNewsletterPaperTopicRecords(final int newsletterId,
        final String languageCode) {
        return getDsl()
            .select(PAPER.ID, PAPER.NUMBER, PAPER.FIRST_AUTHOR, PAPER.PUBLICATION_YEAR, PAPER.TITLE, NEWSLETTER_TOPIC.ID, NEWSLETTER_TOPIC_TR.TITLE)
            .from(PAPER)
            .join(PAPER_NEWSLETTER)
            .on(PAPER.ID.equal(PAPER_NEWSLETTER.PAPER_ID))
            .leftOuterJoin(NEWSLETTER_TOPIC)
            .on(PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID.eq(NEWSLETTER_TOPIC.ID))
            .leftOuterJoin(NEWSLETTER_TOPIC_TR)
            .on(NEWSLETTER_TOPIC.ID.eq(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
            .where(PAPER_NEWSLETTER.NEWSLETTER_ID
                .eq(newsletterId)
                .and(PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID
                    .isNull()
                    .or(NEWSLETTER_TOPIC_TR.LANG_CODE.eq(languageCode))))
            .fetch();
    }

    /**
     * Extracts the {@link PaperSlim}s from the record.
     *
     * @param record
     *     the newsletter paper topic record
     * @return PaperSlim associated with the newsletter
     */
    private PaperSlim extractPaperFrom(final Record record) {
        return new PaperSlim(record.get(PAPER.ID), record.get(PAPER.NUMBER), record.get(PAPER.FIRST_AUTHOR), record.get(PAPER.PUBLICATION_YEAR),
            record.get(PAPER.TITLE));
    }

    /**
     * Extracts the {@link NewsletterTopic} from the record.
     *
     * @param record
     *     the newsletter paper topic record
     * @return NewsletterTopic of the paper to newsletter association, if defined - null otherwise
     */
    private NewsletterTopic extractTopicFrom(final Record record) {
        final Integer newsletterTopicId = record.get(NEWSLETTER_TOPIC.ID);
        if (newsletterTopicId != null) {
            return new NewsletterTopic(newsletterTopicId, record.get(NEWSLETTER_TOPIC_TR.TITLE));
        } else {
            return null;
        }
    }

    @NotNull
    @Override
    public Optional<Newsletter> getNewsletterInStatusWorkInProgress() {
        return Optional.ofNullable(getDsl()
            .selectFrom(NEWSLETTER)
            .where(NEWSLETTER.PUBLICATION_STATUS.eq(PublicationStatus.WIP.getId()))
            .orderBy(NEWSLETTER.ISSUE_DATE.desc())
            .limit(1)
            .fetchOne(getMapper()));
    }

    @NotNull
    @Override
    public Optional<Paper.NewsletterLink> mergePaperIntoNewsletter(final int newsletterId, final long paperId,
        @Nullable final Integer newsletterTopicId, @NotNull String languageCode) {
        final Timestamp ts = getDateTimeService().getCurrentTimestamp();
        final int count = tryInserting(newsletterId, paperId, newsletterTopicId, ts);
        return handleInsertedNewsletter(count, newsletterId, paperId, languageCode);

    }

    // package-private for testing purposes
    @NotNull Optional<Paper.NewsletterLink> handleInsertedNewsletter(final int count, final int newsletterId, final long paperId,
        @NotNull final String languageCode) {
        if (count > 0) {
            final Record6<Integer, String, Integer, Integer, String, String> r = fetchMergedNewsletter(newsletterId, paperId, languageCode);
            if (r != null)
                return Optional.of(new Paper.NewsletterLink(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6()));
        }
        return Optional.empty();
    }

    // package-private for stubbing purposes
    @Nullable Record6<Integer, String, Integer, Integer, String, String> fetchMergedNewsletter(final int newsletterId, final long paperId,
        @NotNull final String languageCode) {
        return getDsl()
            .select(NEWSLETTER.ID, NEWSLETTER.ISSUE, NEWSLETTER.PUBLICATION_STATUS, NEWSLETTER_TOPIC.ID, NEWSLETTER_TOPIC_TR.TITLE,
                PAPER_NEWSLETTER.HEADLINE)
            .from(PAPER_NEWSLETTER)
            .innerJoin(NEWSLETTER)
            .on(PAPER_NEWSLETTER.NEWSLETTER_ID.eq(NEWSLETTER.ID))
            .leftOuterJoin(NEWSLETTER_TOPIC)
            .on(PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID.eq(NEWSLETTER_TOPIC.ID))
            .leftOuterJoin(NEWSLETTER_TOPIC_TR)
            .on(NEWSLETTER_TOPIC.ID.eq(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
            .where(PAPER_NEWSLETTER.PAPER_ID.eq(paperId))
            .and(PAPER_NEWSLETTER.NEWSLETTER_ID.eq(newsletterId))
            .and(PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID
                .isNull()
                .or(NEWSLETTER_TOPIC_TR.LANG_CODE.eq(languageCode)))
            .fetchOne();
    }

    // package-private for stubbing
    int tryInserting(final int newsletterId, final long paperId, @Nullable final Integer newsletterTopicId, @Nullable final Timestamp ts) {
        final int inserted = getDsl()
            .insertInto(PAPER_NEWSLETTER)
            .columns(PAPER_NEWSLETTER.PAPER_ID, PAPER_NEWSLETTER.NEWSLETTER_ID, PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID, PAPER_NEWSLETTER.VERSION,
                PAPER_NEWSLETTER.CREATED, PAPER_NEWSLETTER.CREATED_BY)
            .values(paperId, newsletterId, newsletterTopicId, 1, ts, getUserId())
            .onDuplicateKeyUpdate()
            .set(PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID, newsletterTopicId)
            .set(PAPER_NEWSLETTER.VERSION, PAPER_NEWSLETTER.VERSION.add(1))
            .set(PAPER_NEWSLETTER.LAST_MODIFIED, ts)
            .set(PAPER_NEWSLETTER.LAST_MODIFIED_BY, getUserId())
            .execute();
        getLogger().info("{} merged paper with id {} to newsletter with id {}.", getActiveUser().getUserName(), paperId, newsletterId);
        return inserted;
    }

    @Override
    public int removePaperFromNewsletter(final int newsletterId, final long paperId) {
        final int deleted = getDsl()
            .deleteFrom(PAPER_NEWSLETTER)
            .where(PAPER_NEWSLETTER.NEWSLETTER_ID.eq(newsletterId))
            .and(PAPER_NEWSLETTER.PAPER_ID.eq(paperId))
            .execute();
        getLogger().info("{} removed paper with id {} from newsletter with id {}.", getActiveUser().getUserName(), paperId, newsletterId);
        return deleted;
    }
}
