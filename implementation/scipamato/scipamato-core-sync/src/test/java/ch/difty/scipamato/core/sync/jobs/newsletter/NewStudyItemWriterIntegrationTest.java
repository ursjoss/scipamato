package ch.difty.scipamato.core.sync.jobs.newsletter;

import static ch.difty.scipamato.publ.db.public_.tables.NewStudy.NEW_STUDY;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Test;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest;
import ch.difty.scipamato.publ.db.public_.tables.records.NewStudyRecord;

public class NewStudyItemWriterIntegrationTest
    extends AbstractItemWriterIntegrationTest<PublicNewStudy, NewStudyItemWriter> {

    private static final int  NL_ID_EXISTING        = 1;
    private static final long PAPER_NUMBER_EXISTING = 8924;
    private static final int  NL_ID_NEW             = 1;
    private static final long PAPER_NUMBER_NEW      = -10;

    public static final String ORIGINAL_AUTHORS = "Di et al.";

    private PublicNewStudy newNewStudy, existingNewStudy;

    @Override
    protected NewStudyItemWriter newWriter() {
        return new NewStudyItemWriter(dsl);
    }

    @Override
    public void setUpEntities() {
        newNewStudy = newNewStudy(NL_ID_NEW, PAPER_NUMBER_NEW);

        existingNewStudy = getExistingNewStudyFromDb(NL_ID_EXISTING, PAPER_NUMBER_EXISTING);
        assertThat(existingNewStudy.getAuthors()).isEqualTo("Di et al.");
        existingNewStudy.setAuthors("foo");
    }

    private PublicNewStudy newNewStudy(final int newsletterId, final long paperNumber) {
        return PublicNewStudy
            .builder()
            .newsletterId(newsletterId)
            .paperNumber(paperNumber)
            .newsletterTopicId(1)
            .sort(1)
            .year(2018)
            .authors("authors")
            .headline("headline")
            .description("description")
            .lastSynched(Timestamp.valueOf(LocalDateTime.now()))
            .build();
    }

    private PublicNewStudy getExistingNewStudyFromDb(final int newsletterId, final long paperNumber) {
        final NewStudyRecord nr = dsl
            .selectFrom(NEW_STUDY)
            .where(NEW_STUDY.NEWSLETTER_ID
                .eq(newsletterId)
                .and(NEW_STUDY.PAPER_NUMBER.eq(paperNumber)))
            .fetchOne();

        return PublicNewStudy
            .builder()
            .newsletterId(nr.getNewsletterId())
            .paperNumber(nr.getPaperNumber())
            .newsletterTopicId(nr.getNewsletterTopicId())
            .sort(nr.getSort())
            .year(nr.getYear())
            .authors(nr.getAuthors())
            .headline(nr.getHeadline())
            .description(nr.getDescription())
            .version(nr.getVersion())
            .created(nr.getCreated())
            .lastModified(nr.getLastModified())
            .lastSynched(nr.getLastSynched())
            .build();
    }

    @After
    public void tearDown() {
        dsl
            .deleteFrom(NEW_STUDY)
            .where(NEW_STUDY.NEWSLETTER_ID
                .eq(NL_ID_NEW)
                .and(NEW_STUDY.PAPER_NUMBER.eq(PAPER_NUMBER_NEW)))
            .execute();
        dsl
            .update(NEW_STUDY)
            .set(NEW_STUDY.AUTHORS, ORIGINAL_AUTHORS)
            .where(NEW_STUDY.NEWSLETTER_ID
                .eq(NL_ID_EXISTING)
                .and(NEW_STUDY.PAPER_NUMBER.eq(PAPER_NUMBER_EXISTING)))
            .execute();
    }

    @Test
    public void insertingNewNewStudy_succeeds() {
        final int newsletterId = newNewStudy.getNewsletterId();
        final long paperNumber = newNewStudy.getPaperNumber();
        assertNewStudyDoesNotExistWith(newsletterId, paperNumber);
        assertThat(getWriter().executeUpdate(newNewStudy)).isEqualTo(1);
        assertNewStudyExistsWith(newsletterId, paperNumber);
    }

    private void assertNewStudyExistsWith(final int newsletterId, final long paperNumber) {
        assertRecordCountForId(newsletterId, paperNumber, 1);
    }

    private void assertNewStudyDoesNotExistWith(final int newsletterId, final long paperNumber) {
        assertRecordCountForId(newsletterId, paperNumber, 0);
    }

    private void assertRecordCountForId(int newsletterId, long paperNumber, int size) {
        assertThat(dsl
            .selectOne()
            .from(NEW_STUDY)
            .where(NEW_STUDY.NEWSLETTER_ID
                .eq(newsletterId)
                .and(NEW_STUDY.PAPER_NUMBER.eq(paperNumber)))
            .fetch()).hasSize(size);
    }

    @Test
    public void updatingExistingNewStudy_succeeds() {
        assertThat(getWriter().executeUpdate(existingNewStudy)).isEqualTo(1);
    }

}
