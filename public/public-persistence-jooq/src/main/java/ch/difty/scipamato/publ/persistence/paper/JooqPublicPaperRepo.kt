package ch.difty.scipamato.publ.persistence.paper

import ch.difty.scipamato.common.persistence.JooqSortMapper
import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.publ.db.tables.Language
import ch.difty.scipamato.publ.db.tables.Paper
import ch.difty.scipamato.publ.db.tables.records.PaperRecord
import ch.difty.scipamato.publ.entity.Keyword
import ch.difty.scipamato.publ.entity.PublicPaper
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.util.ArrayList

/**
 * The repository to read [PublicPaper]s.
 *
 * @author u.joss
 */
@Repository
open class JooqPublicPaperRepo(
    private val dsl: DSLContext,
    private val sortMapper: JooqSortMapper<PaperRecord, PublicPaper, Paper>,
    private val filterConditionMapper: PublicPaperFilterConditionMapper,
    private val authorsAbbreviator: AuthorsAbbreviator,
    private val journalExtractor: JournalExtractor
) : PublicPaperRepository {

    private val table: Paper
        get() = Paper.PAPER

    private val recordClass: Class<out PaperRecord>
        get() = PaperRecord::class.java

    override fun findByNumber(number: Long): PublicPaper? =
        dsl.selectFrom(table)
            .where(Paper.PAPER.NUMBER.equal(number))
            .fetchOneInto(recordClass)?.let { map(it) }

    override fun findPageByFilter(filter: PublicPaperFilter, pc: PaginationContext): List<PublicPaper> =
        dsl.selectFrom(table)
            .where(getConditions(filter))
            .orderBy(sortMapper.map(pc.sort, table))
            .limit(pc.pageSize)
            .offset(pc.offset)
            .fetchInto(recordClass)
            .map { r: PaperRecord -> this.map(r) }.toList()

    fun map(r: PaperRecord): PublicPaper =
        PublicPaper.builder()
            .id(r.id)
            .number(r.number)
            .pmId(r.pmId)
            .authors(r.authors)
            .authorsAbbreviated(authorsAbbreviator.abbreviate(r.authors))
            .title(r.title)
            .location(r.location)
            .journal(journalExtractor.extractJournal(r.location))
            .publicationYear(r.publicationYear)
            .goals(r.goals)
            .methods(r.methods)
            .population(r.population)
            .result(r.result)
            .comment(r.comment)
            .build().apply {
                r.created?.let { created = it.toLocalDateTime() }
                r.lastModified?.let { lastModified = it.toLocalDateTime() }
                version = r.version
            }

    override fun countByFilter(filter: PublicPaperFilter): Int =
        dsl.fetchCount(
            dsl.selectOne()
                .from(Paper.PAPER)
                .where(getConditions(filter))
        )

    override fun findPageOfNumbersByFilter(
        filter: PublicPaperFilter, pc: PaginationContext
    ): List<Long> = dsl
        .select()
        .from(table)
        .where(getConditions(filter))
        .orderBy(sortMapper.map(pc.sort, table))
        .limit(pc.pageSize)
        .offset(pc.offset)
        .fetch(Paper.PAPER.NUMBER)

    private fun getConditions(filter: PublicPaperFilter): Condition {
        val conditions = filterConditionMapper.map(filter)
        return if (filter.keywords?.isEmpty() != false) conditions
        else DSL.and(conditions, evaluateKeywords(filter.keywords))
    }

    private fun evaluateKeywords(keywords: List<Keyword>): Condition {
        val keywordConditions: List<Condition> =
            keywords
                .map { obj: Keyword -> obj.keywordId }.asSequence()
                .map {
                    dsl.select(
                        DSL.coalesce(
                            ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.SEARCH_OVERRIDE,
                            ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.NAME
                        )
                    ).from(ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD)
                        .where(
                            ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.KEYWORD_ID.eq(it).and(
                                ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.LANG_CODE.eq(this.mainLanguage))
                        )
                        .orderBy(ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.NAME)
                        .limit(1)
                        .fetchOne()
                        .value1()
                }.mapTo(ArrayList()) { Paper.PAPER.METHODS.containsIgnoreCase(it) }
        return if (keywordConditions.size == 1) keywordConditions[0] else DSL.and(keywordConditions)
    }

    protected open val mainLanguage: String
        get() = dsl.select(Language.LANGUAGE.CODE)
            .from(Language.LANGUAGE)
            .where(Language.LANGUAGE.MAIN_LANGUAGE.eq(true))
            .limit(1)
            .fetchOne()
            .value1()
}
