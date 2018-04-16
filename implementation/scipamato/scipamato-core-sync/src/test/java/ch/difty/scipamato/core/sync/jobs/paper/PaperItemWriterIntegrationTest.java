package ch.difty.scipamato.core.sync.jobs.paper;

import static ch.difty.scipamato.publ.db.public_.tables.Paper.PAPER;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Test;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest;
import ch.difty.scipamato.publ.db.public_.tables.records.PaperRecord;

public class PaperItemWriterIntegrationTest extends AbstractItemWriterIntegrationTest<PublicPaper, PaperItemWriter> {

    private static final long ID_EXISTING = 1L;
    private static final long ID_NEW      = -1L;

    private PublicPaper newPaper, existingPaper;

    @Override
    protected PaperItemWriter newWriter() {
        return new PaperItemWriter(dsl);
    }

    @Override
    public void setUpEntities() {
        newPaper = newPaper(ID_NEW);

        existingPaper = getExistingPaperFromDb(ID_EXISTING);
        assertThat(existingPaper.getNumber()).isEqualTo(existingPaper.getId());
        existingPaper.setNumber(-2L);
        assertThat(existingPaper.getNumber()).isNotEqualTo(existingPaper.getId());
    }

    private PublicPaper newPaper(long id) {
        return PublicPaper.builder()
            .id(id)
            .number(id)
            .codesPopulation(new Short[] { (short) 1, (short) 2 })
            .codesStudyDesign(new Short[] {})
            .codes(new String[] { "F1" })
            .lastSynched(Timestamp.valueOf(LocalDateTime.now()))
            .build();
    }

    private PublicPaper getExistingPaperFromDb(long id) {
        final PaperRecord pr = dsl.selectFrom(PAPER)
            .where(PAPER.ID.eq(id))
            .fetchOne();
        return PublicPaper.builder()
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

    @After
    public void tearDown() {
        dsl.deleteFrom(PAPER)
            .where(PAPER.ID.lt(0L))
            .execute();
        dsl.update(PAPER)
            .set(PAPER.NUMBER, PAPER.ID)
            .where(PAPER.ID.eq(ID_EXISTING))
            .execute();
    }

    @Test
    public void insertingNewPaper_succeeds() {
        long id = newPaper.getId();
        assertPaperDoesNotExistWith(id);
        assertThat(getWriter().executeUpdate(newPaper)).isEqualTo(1);
        assertPaperExistsWith(id);
    }

    private void assertPaperExistsWith(long id) {
        assertRecordCountForId(id, 1);
    }

    private void assertPaperDoesNotExistWith(long id) {
        assertRecordCountForId(id, 0);
    }

    private void assertRecordCountForId(long id, int size) {
        assertThat(dsl.select(PAPER.NUMBER)
            .from(PAPER)
            .where(PAPER.ID.eq(id))
            .fetch()).hasSize(size);
    }

    @Test
    public void updatingExistingPaper_succeeds() {
        assertThat(getWriter().executeUpdate(existingPaper)).isEqualTo(1);
    }

}
