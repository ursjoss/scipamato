package ch.difty.scipamato.core.sync.jobs.newsletter;

import static ch.difty.scipamato.publ.db.public_.tables.NewStudyTopic.NEW_STUDY_TOPIC;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest;
import ch.difty.scipamato.publ.db.public_.tables.records.NewStudyTopicRecord;

@SuppressWarnings("SameParameterValue")
class NewStudyTopicItemWriterIntegrationTest
    extends AbstractItemWriterIntegrationTest<PublicNewStudyTopic, NewStudyTopicItemWriter> {

    private static final int NEWSLETTER_ID                = 2;
    private static final int NEWSLETTER_TOPIC_ID_EXISTING = 2;
    private static final int NEWSLETTER_TOPIC_ID_NEW      = 4;

    private PublicNewStudyTopic newNewStudyTopic, existingNewStudyTopic;

    @Override
    protected NewStudyTopicItemWriter newWriter() {
        return new NewStudyTopicItemWriter(dsl);
    }

    @Override
    public void setUpEntities() {
        newNewStudyTopic = newNewStudyTopic(NEWSLETTER_ID, NEWSLETTER_TOPIC_ID_NEW);

        existingNewStudyTopic = getExistingNewStudyTopicFromDb(NEWSLETTER_ID, NEWSLETTER_TOPIC_ID_EXISTING);
        assertThat(existingNewStudyTopic.getSort()).isEqualTo(1);
        existingNewStudyTopic.setSort(4);
    }

    private PublicNewStudyTopic newNewStudyTopic(int newsletterId, int newsletterTopicId) {
        return PublicNewStudyTopic
            .builder()
            .newsletterId(newsletterId)
            .newsletterTopicId(newsletterTopicId)
            .sort(1)
            .lastSynched(Timestamp.valueOf(LocalDateTime.now()))
            .build();
    }

    private PublicNewStudyTopic getExistingNewStudyTopicFromDb(int newsletterId, int newsletterTopicId) {
        final NewStudyTopicRecord nstr = dsl
            .selectFrom(NEW_STUDY_TOPIC)
            .where(NEW_STUDY_TOPIC.NEWSLETTER_ID
                .eq(newsletterId)
                .and(NEW_STUDY_TOPIC.NEWSLETTER_TOPIC_ID.eq(newsletterTopicId)))
            .fetchOne();
        return PublicNewStudyTopic
            .builder()
            .newsletterId(nstr.getNewsletterId())
            .newsletterTopicId(nstr.getNewsletterTopicId())
            .sort(nstr.getSort())
            .version(nstr.getVersion())
            .created(nstr.getCreated())
            .lastModified(nstr.getLastModified())
            .lastSynched(nstr.getLastSynched())
            .build();
    }

    @AfterEach
    void tearDown() {
        dsl
            .deleteFrom(NEW_STUDY_TOPIC)
            .where(NEW_STUDY_TOPIC.NEWSLETTER_ID
                .eq(NEWSLETTER_ID)
                .and(NEW_STUDY_TOPIC.NEWSLETTER_TOPIC_ID.eq(NEWSLETTER_TOPIC_ID_NEW)))
            .execute();
        dsl
            .update(NEW_STUDY_TOPIC)
            .set(NEW_STUDY_TOPIC.SORT, 1)
            .where(NEW_STUDY_TOPIC.NEWSLETTER_ID
                .eq(NEWSLETTER_ID)
                .and(NEW_STUDY_TOPIC.NEWSLETTER_TOPIC_ID.eq(NEWSLETTER_TOPIC_ID_EXISTING)))
            .execute();
    }

    @Test
    void insertingNewNewStudyTopic_succeeds() {
        int newsletterId = newNewStudyTopic.getNewsletterId();
        int newsletterTopicId = newNewStudyTopic.getNewsletterTopicId();
        assertNewStudyTopicDoesNotExistWith(newsletterId, newsletterTopicId);
        assertThat(getWriter().executeUpdate(newNewStudyTopic)).isEqualTo(1);
        assertNewStudyTopicExistsWith(newsletterId, newsletterTopicId);
    }

    private void assertNewStudyTopicExistsWith(int newsletterId, int newsletterTopicId) {
        assertRecordCountForId(newsletterId, newsletterTopicId, 1);
    }

    private void assertNewStudyTopicDoesNotExistWith(int newsletterId, int newsletterTopicId) {
        assertRecordCountForId(newsletterId, newsletterTopicId, 0);
    }

    private void assertRecordCountForId(int newsletterId, int newsletterTopicId, int size) {
        assertThat(dsl
            .selectOne()
            .from(NEW_STUDY_TOPIC)
            .where(NEW_STUDY_TOPIC.NEWSLETTER_ID.eq(newsletterId))
            .and(NEW_STUDY_TOPIC.NEWSLETTER_TOPIC_ID.eq(newsletterTopicId))
            .fetch()).hasSize(size);
    }

    @Test
    void updatingExistingNewStudyTopic_succeeds() {
        assertThat(getWriter().executeUpdate(existingNewStudyTopic)).isEqualTo(1);
    }

}
