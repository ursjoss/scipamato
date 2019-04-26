package ch.difty.scipamato.core.sync.jobs.newsletter;

import static ch.difty.scipamato.publ.db.public_.tables.Newsletter.NEWSLETTER;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest;
import ch.difty.scipamato.publ.db.public_.tables.records.NewsletterRecord;

@SuppressWarnings("SameParameterValue")
class NewsletterItemWriterIntegrationTest
    extends AbstractItemWriterIntegrationTest<PublicNewsletter, NewsletterItemWriter> {

    private static final int ID_EXISTING = 1;
    private static final int ID_NEW      = -1;

    private PublicNewsletter newNewsletter, existingNewsletter;

    @Override
    protected NewsletterItemWriter newWriter() {
        return new NewsletterItemWriter(dsl);
    }

    @Override
    public void setUpEntities() {
        newNewsletter = newNewsletter(ID_NEW);

        existingNewsletter = getExistingNewsletterFromDb(ID_EXISTING);
        assertThat(existingNewsletter.getIssue()).isEqualTo("2018/04");
        existingNewsletter.setIssue("foo");
    }

    private PublicNewsletter newNewsletter(int id) {
        return PublicNewsletter
            .builder()
            .id(id)
            .issue("issue")
            .issueDate(Date.valueOf("2018-06-14"))
            .lastSynched(Timestamp.valueOf(LocalDateTime.now()))
            .build();
    }

    private PublicNewsletter getExistingNewsletterFromDb(int id) {
        final NewsletterRecord nr = dsl
            .selectFrom(NEWSLETTER)
            .where(NEWSLETTER.ID.eq(id))
            .fetchOne();
        return PublicNewsletter
            .builder()
            .id(nr.getId())
            .issue(nr.getIssue())
            .issueDate(nr.getIssueDate())
            .version(nr.getVersion())
            .created(nr.getCreated())
            .lastModified(nr.getLastModified())
            .lastSynched(nr.getLastSynched())
            .build();
    }

    @AfterEach
    void tearDown() {
        dsl
            .deleteFrom(NEWSLETTER)
            .where(NEWSLETTER.ID.eq(ID_NEW))
            .execute();
        dsl
            .update(NEWSLETTER)
            .set(NEWSLETTER.ISSUE, "2018/04")
            .where(NEWSLETTER.ID.eq(ID_EXISTING))
            .execute();
    }

    @Test
    void insertingNewNewsletter_succeeds() {
        int id = newNewsletter.getId();
        assertNewsletterDoesNotExistWith(id);
        assertThat(getWriter().executeUpdate(newNewsletter)).isEqualTo(1);
        assertNewsletterExistsWith(id);
    }

    private void assertNewsletterExistsWith(int id) {
        assertRecordCountForId(id, 1);
    }

    private void assertNewsletterDoesNotExistWith(int id) {
        assertRecordCountForId(id, 0);
    }

    private void assertRecordCountForId(int id, int size) {
        assertThat(dsl
            .select(NEWSLETTER.ID)
            .from(NEWSLETTER)
            .where(NEWSLETTER.ID.eq(id))
            .fetch()).hasSize(size);
    }

    @Test
    void updatingExistingNewsletter_succeeds() {
        assertThat(getWriter().executeUpdate(existingNewsletter)).isEqualTo(1);
    }

}
