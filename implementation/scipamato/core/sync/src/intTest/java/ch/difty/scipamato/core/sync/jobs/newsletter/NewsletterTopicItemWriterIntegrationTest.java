package ch.difty.scipamato.core.sync.jobs.newsletter;

import static ch.difty.scipamato.publ.db.tables.NewsletterTopic.NEWSLETTER_TOPIC;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest;
import ch.difty.scipamato.publ.db.tables.records.NewsletterTopicRecord;

@SuppressWarnings("SameParameterValue")
class NewsletterTopicItemWriterIntegrationTest
    extends AbstractItemWriterIntegrationTest<PublicNewsletterTopic, NewsletterTopicItemWriter> {

    private static final int ID_EXISTING = 1;
    private static final int ID_NEW      = -1;

    private static final String LANG_CODE = "de";

    private PublicNewsletterTopic newNewsletterTopic, existingNewsletterTopic;

    @Override
    protected NewsletterTopicItemWriter newWriter() {
        return new NewsletterTopicItemWriter(dsl);
    }

    @Override
    public void setUpEntities() {
        newNewsletterTopic = newNewsletterTopic(ID_NEW);

        existingNewsletterTopic = getExistingNewsletterTopicFromDb(ID_EXISTING, LANG_CODE);
        assertThat(existingNewsletterTopic.getTitle()).isEqualTo("Tiefe Belastungen");
        existingNewsletterTopic.setTitle("foo");
    }

    private PublicNewsletterTopic newNewsletterTopic(int id) {
        return PublicNewsletterTopic
            .builder()
            .id(id)
            .langCode(LANG_CODE)
            .title("title")
            .lastSynched(Timestamp.valueOf(LocalDateTime.now()))
            .build();
    }

    private PublicNewsletterTopic getExistingNewsletterTopicFromDb(int id, String langCode) {
        final NewsletterTopicRecord ntr = dsl
            .selectFrom(NEWSLETTER_TOPIC)
            .where(NEWSLETTER_TOPIC.ID.eq(id))
            .and(NEWSLETTER_TOPIC.LANG_CODE.eq(langCode))
            .fetchOne();
        return PublicNewsletterTopic
            .builder()
            .id(ntr.getId())
            .langCode(ntr.getLangCode())
            .title(ntr.getTitle())
            .version(ntr.getVersion())
            .created(ntr.getCreated())
            .lastModified(ntr.getLastModified())
            .lastSynched(ntr.getLastSynched())
            .build();
    }

    @AfterEach
    void tearDown() {
        dsl
            .deleteFrom(NEWSLETTER_TOPIC)
            .where(NEWSLETTER_TOPIC.ID.eq(ID_NEW))
            .execute();
        dsl
            .update(NEWSLETTER_TOPIC)
            .set(NEWSLETTER_TOPIC.TITLE, "Tiefe Belastungen")
            .where(NEWSLETTER_TOPIC.ID.eq(ID_EXISTING))
            .and(NEWSLETTER_TOPIC.LANG_CODE.eq(LANG_CODE))
            .execute();
    }

    @Test
    void insertingNewNewsletterTopic_succeeds() {
        int id = newNewsletterTopic.getId();
        assertNewsletterTopicDoesNotExistWith(id, LANG_CODE);
        assertThat(getWriter().executeUpdate(newNewsletterTopic)).isEqualTo(1);
        asserNewsletterTopicExistsWith(id, LANG_CODE);
    }

    private void asserNewsletterTopicExistsWith(int id, String langCode) {
        assertRecordCountForId(id, langCode, 1);
    }

    private void assertNewsletterTopicDoesNotExistWith(int id, String langCode) {
        assertRecordCountForId(id, langCode, 0);
    }

    private void assertRecordCountForId(int id, String langCode, int size) {
        assertThat(dsl
            .select(NEWSLETTER_TOPIC.ID)
            .from(NEWSLETTER_TOPIC)
            .where(NEWSLETTER_TOPIC.ID.eq(id))
            .and(NEWSLETTER_TOPIC.LANG_CODE.eq(langCode))
            .fetch()).hasSize(size);
    }

    @Test
    void updatingExistingNewsletterTopic_succeeds() {
        assertThat(getWriter().executeUpdate(existingNewsletterTopic)).isEqualTo(1);
    }

}
