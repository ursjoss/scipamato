package ch.difty.scipamato.core.sync.jobs.paper

import ch.difty.scipamato.core.sync.PublicPaper
import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter
import ch.difty.scipamato.publ.db.tables.Paper
import org.jooq.DSLContext

/**
 * [ScipamatoItemWriter] implementation to synchronize papers from scipamato-core to public.
 *
 *  * Takes care of inserts and updates.
 *  * Every record will be updated at least with the current timestamp in last_synched.
 *  * id columns and audit fields are those of the scipamato-core tables
 */
class PaperItemWriter(jooqDslContextPublic: DSLContext) : ScipamatoItemWriter<PublicPaper>(jooqDslContextPublic, "paper") {
    override fun executeUpdate(i: PublicPaper): Int =
        dslContext
            .insertInto(Paper.PAPER)
            .columns(Paper.PAPER.ID, Paper.PAPER.NUMBER, Paper.PAPER.PM_ID, Paper.PAPER.AUTHORS, Paper.PAPER.TITLE, Paper.PAPER.LOCATION,
                Paper.PAPER.PUBLICATION_YEAR, Paper.PAPER.GOALS, Paper.PAPER.METHODS, Paper.PAPER.POPULATION, Paper.PAPER.RESULT, Paper.PAPER.COMMENT,
                Paper.PAPER.CODES_POPULATION, Paper.PAPER.CODES_STUDY_DESIGN, Paper.PAPER.CODES, Paper.PAPER.VERSION, Paper.PAPER.CREATED,
                Paper.PAPER.LAST_MODIFIED, Paper.PAPER.LAST_SYNCHED)
            .values(i.id, i.number, i.pmId, i.authors, i.title, i.location,
                i.publicationYear, i.goals, i.methods, i.population, i.result, i.comment,
                i.codesPopulation, i.codesStudyDesign, i.codes, i.version, i.created,
                i.lastModified, i.lastSynched)
            .onConflict(Paper.PAPER.NUMBER)
            .doUpdate()
            .set(Paper.PAPER.ID, i.id)
            .set(Paper.PAPER.PM_ID, i.pmId)
            .set(Paper.PAPER.AUTHORS, i.authors)
            .set(Paper.PAPER.TITLE, i.title)
            .set(Paper.PAPER.LOCATION, i.location)
            .set(Paper.PAPER.PUBLICATION_YEAR, i.publicationYear)
            .set(Paper.PAPER.GOALS, i.goals)
            .set(Paper.PAPER.METHODS, i.methods)
            .set(Paper.PAPER.POPULATION, i.population)
            .set(Paper.PAPER.RESULT, i.result)
            .set(Paper.PAPER.COMMENT, i.comment)
            .set(Paper.PAPER.CODES_POPULATION, i.codesPopulation)
            .set(Paper.PAPER.CODES_STUDY_DESIGN, i.codesStudyDesign)
            .set(Paper.PAPER.CODES, i.codes)
            .set(Paper.PAPER.VERSION, i.version)
            .set(Paper.PAPER.CREATED, i.created)
            .set(Paper.PAPER.LAST_MODIFIED, i.lastModified)
            .set(Paper.PAPER.LAST_SYNCHED, i.lastSynched)
            .where(Paper.PAPER.NUMBER.eq(i.number))
            .execute()
}
