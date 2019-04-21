package ch.difty.scipamato.core.sync.jobs.paper;

import static ch.difty.scipamato.publ.db.public_.tables.Paper.PAPER;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest;
import ch.difty.scipamato.publ.db.public_.tables.records.PaperRecord;

@SuppressWarnings("SameParameterValue")
public class PaperItemWriterIntegrationTest extends AbstractItemWriterIntegrationTest<PublicPaper, PaperItemWriter> {

    private static final long NUMBER_EXISTING = 1L;
    private static final long NUMBER_NEW      = -10L;

    private PublicPaper newPaper, existingPaper;

    @Override
    protected PaperItemWriter newWriter() {
        return new PaperItemWriter(dsl);
    }

    @Override
    public void setUpEntities() {
        newPaper = newPaperWithNumber(NUMBER_NEW);

        existingPaper = getExistingPaperFromDb(NUMBER_EXISTING);
        assertThat(existingPaper.getNumber()).isEqualTo(existingPaper.getId());
        existingPaper.setId(-2L);
        assertThat(existingPaper.getNumber()).isNotEqualTo(existingPaper.getId());
    }

    private PublicPaper newPaperWithNumber(long number) {
        return PublicPaper
            .builder()
            .id(number)
            .number(number)
            .codesPopulation(new Short[] { (short) 1, (short) 2 })
            .codesStudyDesign(new Short[] {})
            .codes(new String[] { "F1" })
            .lastSynched(Timestamp.valueOf(LocalDateTime.now()))
            .build();
    }

    private PublicPaper getExistingPaperFromDb(long number) {
        final PaperRecord pr = dsl
            .selectFrom(PAPER)
            .where(PAPER.NUMBER.eq(number))
            .fetchOne();
        return PublicPaper
            .builder()
            .id(pr.getId())
            .number(pr.getNumber())
            .pmId(pr.getPmId())
            .authors(pr.getAuthors())
            .title(pr.getTitle())
            .location(pr.getLocation())
            .publicationYear(pr.getPublicationYear())
            .goals(pr.getGoals())
            .methods(pr.getMethods())
            .population(pr.getPopulation())
            .result(pr.getResult())
            .comment(pr.getComment())
            .codesPopulation(pr.getCodesPopulation())
            .codesStudyDesign(pr.getCodesStudyDesign())
            .codes(pr.getCodes())
            .version(pr.getVersion())
            .created(pr.getCreated())
            .lastModified(pr.getLastModified())
            .lastSynched(pr.getLastSynched())
            .build();
    }

    @AfterEach
    public void tearDown() {
        dsl
            .deleteFrom(PAPER)
            .where(PAPER.NUMBER.lt(0L))
            .execute();
        dsl
            .update(PAPER)
            .set(PAPER.ID, PAPER.NUMBER)
            .where(PAPER.NUMBER.eq(NUMBER_EXISTING))
            .execute();
    }

    @Test
    public void insertingNewPaper_succeeds() {
        long number = newPaper.getNumber();
        assertPaperDoesNotExistWith(number);
        assertThat(getWriter().executeUpdate(newPaper)).isEqualTo(1);
        assertPaperExistsWith(number);
    }

    private void assertPaperExistsWith(long id) {
        assertRecordCountForNumber(id, 1);
    }

    private void assertPaperDoesNotExistWith(long number) {
        assertRecordCountForNumber(number, 0);
    }

    private void assertRecordCountForNumber(long number, int size) {
        assertThat(dsl
            .select(PAPER.NUMBER)
            .from(PAPER)
            .where(PAPER.NUMBER.eq(number))
            .fetch()).hasSize(size);
    }

    @Test
    public void updatingExistingPaper_succeeds() {
        assertThat(getWriter().executeUpdate(existingPaper)).isEqualTo(1);
    }

}
