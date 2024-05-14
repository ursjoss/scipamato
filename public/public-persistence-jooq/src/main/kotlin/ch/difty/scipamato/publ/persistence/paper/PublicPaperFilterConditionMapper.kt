package ch.difty.scipamato.publ.persistence.paper

import ch.difty.scipamato.common.persistence.AbstractFilterConditionMapper
import ch.difty.scipamato.common.persistence.FilterConditionMapper
import ch.difty.scipamato.publ.db.tables.Paper
import ch.difty.scipamato.publ.db.tables.records.PaperRecord
import ch.difty.scipamato.publ.entity.Code
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter
import org.jooq.Condition
import org.jooq.TableField
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
import java.util.regex.Matcher
import java.util.regex.Pattern

private const val RE_QUOTE = "\""
private val QUOTED = Pattern.compile("$RE_QUOTE([^$RE_QUOTE]+)$RE_QUOTE")
private const val QUOTED_GROUP_INDEX = 1

@FilterConditionMapper
class PublicPaperFilterConditionMapper : AbstractFilterConditionMapper<PublicPaperFilter>() {

    @Suppress("ComplexMethod")
    override fun internalMap(filter: PublicPaperFilter): List<Condition> {
        val conditions = mutableListOf<Condition>()
        filter.number?.let { conditions.add(Paper.PAPER.NUMBER.eq(it)) }
        filter.authorMask?.let { conditions.addAll(Paper.PAPER.AUTHORS.tokenize(it)) }
        filter.titleMask?.let { conditions.addAll(Paper.PAPER.TITLE.tokenize(it)) }
        filter.methodsMask?.let { conditions.addAll(Paper.PAPER.METHODS.tokenize(it)) }
        if (filter.publicationYearFrom != null) {
            if (hasNoOrIdenticalPubYearUntil(filter)) {
                conditions.add(Paper.PAPER.PUBLICATION_YEAR.eq(filter.publicationYearFrom))
            } else {
                conditions.add(
                    Paper.PAPER.PUBLICATION_YEAR.between(filter.publicationYearFrom, filter.publicationYearUntil))
            }
        } else if (filter.publicationYearUntil != null) {
            conditions.add(Paper.PAPER.PUBLICATION_YEAR.le(filter.publicationYearUntil))
        }
        filter.populationCodes?.let { codes ->
            val ids = codes.map { it.id }.toTypedArray()
            conditions.add(Paper.PAPER.CODES_POPULATION.contains(ids))
        }
        filter.studyDesignCodes?.let { codes ->
            val ids = codes.map { it.id }.toTypedArray()
            conditions.add(Paper.PAPER.CODES_STUDY_DESIGN.contains(ids))
        }
        @Suppress("ComplexCondition")
        if (
            filter.codesOfClass1 != null ||
            filter.codesOfClass2 != null ||
            filter.codesOfClass3 != null ||
            filter.codesOfClass4 != null ||
            filter.codesOfClass5 != null ||
            filter.codesOfClass6 != null ||
            filter.codesOfClass7 != null ||
            filter.codesOfClass8 != null
        ) {
            val allCodes = filter.allCodes()
            if (allCodes.isNotEmpty()) conditions.add(allCodes.toCondition())
        }
        return conditions
    }

    private fun hasNoOrIdenticalPubYearUntil(filter: PublicPaperFilter) =
        filter.publicationYearUntil == null ||
            filter.publicationYearFrom == filter.publicationYearUntil

    /*
     * Currently does not allow to mix quoted and unquoted terms. If this
     * becomes necessary, we might have to implement a proper tokenization of the
     * search term, as was done in core with the SearchTerm hierarchy.
     */
    private fun TableField<PaperRecord, String>.tokenize(mask: String): List<Condition> {
        val m = QUOTED.matcher(mask)
        val (conditions, done) = tokenizeQuoted(m)
        return conditions + if (!done) tokenizeUnquoted(mask) else emptyList()
    }

    private fun TableField<PaperRecord, String>.tokenizeQuoted(m: Matcher): Pair<List<Condition>, Boolean> {
        val conditions = mutableListOf<Condition>()
        var term: String? = null
        while (m.find()) {
            term = m.group(QUOTED_GROUP_INDEX)
            conditions.add(likeIgnoreCase("%$term%"))
        }
        return Pair(conditions, term != null)
    }

    private fun TableField<PaperRecord, String>.tokenizeUnquoted(mask: String): List<Condition> {
        val conditions = mutableListOf<Condition>()
        if (!mask.contains(" "))
            conditions.add(likeIgnoreCase("%$mask%"))
        else
            for (t in mask.split(" ").toTypedArray()) {
                val token = t.trim { it <= ' ' }
                if (token.isNotEmpty()) conditions.add(likeIgnoreCase("%$token%"))
            }
        return conditions
    }

    private fun PublicPaperFilter.allCodes() = with(this) {
        codesOfClass1.codesNullSafe() +
            codesOfClass2.codesNullSafe() +
            codesOfClass3.codesNullSafe() +
            codesOfClass4.codesNullSafe() +
            codesOfClass5.codesNullSafe() +
            codesOfClass6.codesNullSafe() +
            codesOfClass7.codesNullSafe() +
            codesOfClass8.codesNullSafe()
    }

    private fun List<Code>?.codesNullSafe() = this?.mapNotNull { it.code } ?: emptyList()

    /**
     * Due to bug https://github.com/jOOQ/jOOQ/issues/4754, the straightforward way
     * of mapping the codes to the array of type text does not work:
     *
     * <pre>
     * return PAPER.CODES.contains(codeCollection.toArray(new String[0]));
     * </pre>
     *
     * While I originally cast to `PostgresDataType.TEXT`, I now need to cast to
     * `SQLDataType.CLOB` due to https://github.com/jOOQ/jOOQ/issues/7375
     */
    private fun List<String>.toCondition(): Condition =
        Paper.PAPER.CODES.contains(
            DSL.array(
                mapNotNull { DSL.`val`(it).cast(SQLDataType.CLOB) }
            )
        )
}
