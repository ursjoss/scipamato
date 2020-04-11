package ch.difty.scipamato.core.persistence.paper.searchorder

import ch.difty.scipamato.common.TranslationUtils.deCamelCase
import ch.difty.scipamato.core.entity.search.IntegerSearchTerm
import org.jooq.Condition
import org.jooq.impl.DSL

/**
 * [SearchTermEvaluator] implementation evaluating integer searchTerms.
 *
 * @author u.joss
 */
class IntegerSearchTermEvaluator : SearchTermEvaluator<IntegerSearchTerm?> {

    override fun evaluate(searchTerm: IntegerSearchTerm): Condition {
        val field = DSL.field(searchTerm.fieldName.sanitize())
        val value = DSL.`val`(searchTerm.value)
        return when (searchTerm.type) {
            IntegerSearchTerm.MatchType.LESS_OR_EQUAL -> field.le(value)
            IntegerSearchTerm.MatchType.LESS_THAN -> field.lt(value)
            IntegerSearchTerm.MatchType.GREATER_OR_EQUAL -> field.ge(value)
            IntegerSearchTerm.MatchType.GREATER_THAN -> field.gt(value)
            IntegerSearchTerm.MatchType.RANGE -> field.between(value, DSL.`val`(searchTerm.value2))
            IntegerSearchTerm.MatchType.MISSING -> field.isNull
            IntegerSearchTerm.MatchType.PRESENT -> field.isNotNull
            IntegerSearchTerm.MatchType.INCOMPLETE -> DSL.falseCondition()
            else -> field.equal(value)
        }
    }

    private fun String.sanitize(): String? = when {
        "id" == this -> "paper.id"
        else -> deCamelCase(this)
    }
}