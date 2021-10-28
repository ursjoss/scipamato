package ch.difty.scipamato.core.sync.launcher

import ch.difty.scipamato.core.db.tables.Newsletter
import ch.difty.scipamato.core.db.tables.Paper
import ch.difty.scipamato.core.db.tables.PaperCode
import ch.difty.scipamato.core.db.tables.PaperNewsletter
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Qualifier

class UnsynchronizedEntitiesWarner @JvmOverloads constructor(
    @Qualifier("dslContext") jooqCore: DSLContext,
    val paperProvider: () -> List<Long> = { retrievePaperRecords(jooqCore) },
    val newsletterPaperProvider: () -> List<String> = { retrieveNewsletterPaperRecords(jooqCore) },
) : Warner {

    override fun findUnsynchronizedPapers(): String? {
        val numbers = paperProvider()
        return if (numbers.isEmpty()) null
        else "Papers not synchronized due to missing codes: Number " +
            numbers.joinToString(", ", postfix = ".")
    }

    override fun findNewsletterswithUnsynchronizedPapers(): String? {
        val issues = newsletterPaperProvider()
        return if (issues.isEmpty()) null
        else "Incomplete Newsletters due to papers with missing codes - Papers: " +
            issues.joinToString(", ", postfix = ".")
    }
}

internal fun retrievePaperRecords(jooqCore: DSLContext): List<Long> = jooqCore
    .select(Paper.PAPER.NUMBER)
    .from(Paper.PAPER)
    .where(Paper.PAPER.ID.notIn(DSL
        .select(PaperCode.PAPER_CODE.PAPER_ID)
        .from(PaperCode.PAPER_CODE)))
    .fetchInto(Long::class.java)

internal fun retrieveNewsletterPaperRecords(jooqCore: DSLContext): List<String> = jooqCore
    .select(Paper.PAPER.NUMBER.concat(" (").concat(Newsletter.NEWSLETTER.ISSUE).concat(")"))
    .from(Newsletter.NEWSLETTER)
    .innerJoin(PaperNewsletter.PAPER_NEWSLETTER)
    .on(Newsletter.NEWSLETTER.ID.eq(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_ID))
    .innerJoin(Paper.PAPER)
    .on(PaperNewsletter.PAPER_NEWSLETTER.PAPER_ID.eq(Paper.PAPER.ID))
    .where(Paper.PAPER.ID.notIn(DSL
        .select(PaperCode.PAPER_CODE.PAPER_ID)
        .from(PaperCode.PAPER_CODE)))
    .fetchInto(String::class.java)
