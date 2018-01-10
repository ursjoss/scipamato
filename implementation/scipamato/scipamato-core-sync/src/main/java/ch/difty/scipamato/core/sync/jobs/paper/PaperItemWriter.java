package ch.difty.scipamato.core.sync.jobs.paper;

import static ch.difty.scipamato.public_.db.public_.tables.Paper.PAPER;

import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;

import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter;

/**
 * {@link ItemWriter} implementation to synchronize papers from scipamato-core
 * to public.
 *
 * <ul>
 * <li>Takes care of inserts and updates.</li>
 * <li>Every record will be updated at least with the current timestamp in
 * last_synched.</li>
 * <li>id columns and audit fields are those of the scipamato-core tables</li>
 * </ul>
 *
 * @author u.joss
 */
public class PaperItemWriter extends ScipamatoItemWriter<PublicPaper> {

    public PaperItemWriter(final DSLContext jooqDslContextPublic) {
        super(jooqDslContextPublic, "paper");
    }

    @Override
    protected int executeUpdate(final PublicPaper p) {
        return getDslContext().insertInto(PAPER)
            .columns(PAPER.ID, PAPER.NUMBER, PAPER.PM_ID, PAPER.AUTHORS, PAPER.TITLE, PAPER.LOCATION,
                PAPER.PUBLICATION_YEAR, PAPER.GOALS, PAPER.METHODS, PAPER.POPULATION, PAPER.RESULT, PAPER.COMMENT,
                PAPER.CODES_POPULATION, PAPER.CODES_STUDY_DESIGN, PAPER.CODES, PAPER.VERSION, PAPER.CREATED,
                PAPER.LAST_MODIFIED, PAPER.LAST_SYNCHED)
            .values(p.getId(), p.getNumber(), p.getPmId(), p.getAuthors(), p.getTitle(), p.getLocation(),
                p.getPublicationYear(), p.getGoals(), p.getMethods(), p.getPopulation(), p.getResult(), p.getComment(),
                p.getCodesPopulation(), p.getCodesStudyDesign(), p.getCodes(), p.getVersion(), p.getCreated(),
                p.getLastModified(), p.getLastSynched())
            .onConflict(PAPER.ID)
            .doUpdate()
            .set(PAPER.NUMBER, p.getNumber())
            .set(PAPER.PM_ID, p.getPmId())
            .set(PAPER.AUTHORS, p.getAuthors())
            .set(PAPER.TITLE, p.getTitle())
            .set(PAPER.LOCATION, p.getLocation())
            .set(PAPER.PUBLICATION_YEAR, p.getPublicationYear())
            .set(PAPER.GOALS, p.getGoals())
            .set(PAPER.METHODS, p.getMethods())
            .set(PAPER.POPULATION, p.getPopulation())
            .set(PAPER.RESULT, p.getResult())
            .set(PAPER.COMMENT, p.getComment())
            .set(PAPER.CODES_POPULATION, p.getCodesPopulation())
            .set(PAPER.CODES_STUDY_DESIGN, p.getCodesStudyDesign())
            .set(PAPER.CODES, p.getCodes())
            .set(PAPER.VERSION, p.getVersion())
            .set(PAPER.CREATED, p.getCreated())
            .set(PAPER.LAST_MODIFIED, p.getLastModified())
            .set(PAPER.LAST_SYNCHED, p.getLastSynched())
            .where(PAPER.ID.eq(p.getId()))
            .execute();
    }

}
