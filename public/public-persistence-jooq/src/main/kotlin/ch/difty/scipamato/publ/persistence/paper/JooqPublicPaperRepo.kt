package ch.difty.scipamato.publ.persistence.paper

import ch.difty.scipamato.common.persistence.JooqDbSortMapper
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
 */
@Repository
@Suppress("SpellCheckingInspection")
open class JooqPublicPaperRepo(
    private val dsl: DSLContext,
    private val sortMapper: JooqDbSortMapper<PaperRecord, PublicPaper, Paper>,
    private val filterConditionMapper: PublicPaperFilterConditionMapper,
    private val authorsAbbreviator: AuthorsAbbreviator,
    private val journalExtractor: JournalExtractor,
) : PublicPaperRepository {

    private val table: Paper
        get() = Paper.PAPER

    private val recordClass: Class<out PaperRecord>
        get() = PaperRecord::class.java

    override fun findByNumber(number: Long): PublicPaper? =
        dsl.selectFrom(table)
            .where(Paper.PAPER.NUMBER.equal(number))
            .fetchOneInto(recordClass)?.let { map(it) }

    override fun findPageByFilter(filter: PublicPaperFilter, paginationContext: PaginationContext): List<PublicPaper> =
        dsl.selectFrom(table)
            .where(filter.asCondition())
            .orderBy(sortMapper.map(paginationContext.sort, table))
            .limit(paginationContext.pageSize)
            .offset(paginationContext.offset)
            .fetchInto(recordClass)
            .map { r: PaperRecord -> this.map(r) }.toList()

    fun map(r: PaperRecord) = PublicPaper(
        id = r.id,
        number = r.number,
        pmId = r.pmId,
        authors = r.authors,
        authorsAbbreviated = authorsAbbreviator.abbreviate(r.authors),
        title = r.title,
        location = r.location,
        journal = journalExtractor.extractJournal(r.location),
        publicationYear = r.publicationYear,
        goals = r.goals,
        methods = r.methods,
        population = r.population,
        result = r.result,
        comment = r.comment,
    )

    override fun countByFilter(filter: PublicPaperFilter): Int =
        dsl.fetchCount(
            dsl.selectOne().from(Paper.PAPER).where(filter.asCondition())
        )

    override fun findPageOfNumbersByFilter(
        filter: PublicPaperFilter,
        paginationContext: PaginationContext,
    ): List<Long> = dsl
        .select()
        .from(table)
        .where(filter.asCondition())
        .orderBy(sortMapper.map(paginationContext.sort, table))
        .limit(paginationContext.pageSize)
        .offset(paginationContext.offset)
        .fetch(Paper.PAPER.NUMBER)

    private fun PublicPaperFilter.asCondition(): Condition {
        val conditions = filterConditionMapper.map(this)
        return if (keywords?.isEmpty() != false) conditions
        else DSL.and(conditions, keywords?.asCondition())
    }

    private fun List<Keyword>.asCondition(): Condition {
        val conditions: List<Condition> =
            map { obj: Keyword -> obj.keywordId }.asSequence()
                .map {
                    dsl.select(
                        DSL.coalesce(
                            ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.SEARCH_OVERRIDE,
                            ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.NAME
                        )
                    ).from(ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD)
                        .where(
                            ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.KEYWORD_ID.eq(it).and(
                                ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.LANG_CODE.eq(
                                    this@JooqPublicPaperRepo.mainLanguage
                                )
                            )
                        )
                        .orderBy(ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.NAME)
                        .limit(1)
                        .fetchOne()
                        ?.value1()
                }.mapTo(ArrayList()) { Paper.PAPER.METHODS.containsIgnoreCase(it) }
        return if (conditions.size == 1) conditions.first() else DSL.and(conditions)
    }

    protected open val mainLanguage: String
        get() = dsl.select(Language.LANGUAGE.CODE)
            .from(Language.LANGUAGE)
            .where(Language.LANGUAGE.MAIN_LANGUAGE.eq(true))
            .limit(1)
            .fetchOne()
            ?.value1() ?: "en_US"
}
