package ch.difty.scipamato.core.persistence.paper.searchorder

import ch.difty.scipamato.core.entity.search.SearchTerm

abstract class SearchTermEvaluatorIntegrationTest<T : SearchTerm> {

    protected abstract val searchTermType: Int

    protected abstract fun makeSearchTerm(rawSearchTerm: String): T

    companion object {
        const val ID: Long = 1
        const val SC_ID: Long = 10
        const val FN = "fn"
    }

}
