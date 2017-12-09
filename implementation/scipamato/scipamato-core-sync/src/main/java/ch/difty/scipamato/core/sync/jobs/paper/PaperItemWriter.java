package ch.difty.scipamato.core.sync.jobs.paper;

import static ch.difty.scipamato.db.public_.public_.tables.Paper.*;

import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;

import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter;

/**
 * {@link ItemWriter} implementation to synchronize papers from scipamato-core to public.
 *
 * <ul>
 * <li> Takes care of inserts and updates. </li>
 * <li> Every record will be updated at least with the current timestamp in last_synched. </li>
 * <li> id columns and audit fields are those of the scipamato-core tables </li>
 * </ul>
 *
 * @author u.joss
 */
public class PaperItemWriter extends ScipamatoItemWriter<PublicPaper> {

    public PaperItemWriter(final DSLContext jooqDslContextPublic) {
        super(jooqDslContextPublic, "paper");
    }

    @Override
    protected int executeUpdate(final PublicPaper c) {
        return getDslContext()
            .insertInto(PAPER)
            .columns(PAPER.ID, PAPER.NUMBER, PAPER.PM_ID, PAPER.AUTHORS, PAPER.TITLE, PAPER.LOCATION, PAPER.PUBLICATION_YEAR, PAPER.GOALS, PAPER.METHODS, PAPER.POPULATION, PAPER.RESULT, PAPER.COMMENT,
                    PAPER.CODES_POPULATION, PAPER.CODES_STUDY_DESIGN, PAPER.CODES, PAPER.VERSION, PAPER.CREATED, PAPER.LAST_MODIFIED, PAPER.LAST_SYNCHED)
            .values(c.getId(), c.getNumber(), c.getPmId(), c.getAuthors(), c.getTitle(), c.getLocation(), c.getPublicationYear(), c.getGoals(), c.getMethods(), c.getPopulation(), c.getResult(),
                    c.getComment(), c.getCodesPopulation(), c.getCodesStudyDesign(), c.getCodes(), c.getVersion(), c.getCreated(), c.getLastModified(), c.getLastSynched())
            .onConflict(PAPER.ID)
            .doUpdate()
            .set(PAPER.NUMBER, c.getNumber())
            .set(PAPER.PM_ID, c.getPmId())
            .set(PAPER.AUTHORS, c.getAuthors())
            .set(PAPER.TITLE, c.getTitle())
            .set(PAPER.LOCATION, c.getLocation())
            .set(PAPER.PUBLICATION_YEAR, c.getPublicationYear())
            .set(PAPER.GOALS, c.getGoals())
            .set(PAPER.METHODS, c.getMethods())
            .set(PAPER.POPULATION, c.getPopulation())
            .set(PAPER.RESULT, c.getResult())
            .set(PAPER.COMMENT, c.getComment())
            .set(PAPER.CODES_POPULATION, c.getCodesPopulation())
            .set(PAPER.CODES_STUDY_DESIGN, c.getCodesStudyDesign())
            .set(PAPER.CODES, c.getCodes())
            .set(PAPER.VERSION, c.getVersion())
            .set(PAPER.CREATED, c.getCreated())
            .set(PAPER.LAST_MODIFIED, c.getLastModified())
            .set(PAPER.LAST_SYNCHED, c.getLastSynched())
            .where(PAPER.ID.eq(c.getId()))
            .execute();
    }

}
