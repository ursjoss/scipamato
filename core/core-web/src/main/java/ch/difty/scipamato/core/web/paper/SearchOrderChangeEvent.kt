package ch.difty.scipamato.core.web.paper

import org.apache.wicket.ajax.AjaxRequestTarget

/**
 * The event indicates that there were changes in the search order deriving from
 * the [SearchOrderPanel] that will or may affect other child components
 * of the parent page container holding the [SearchOrderPanel].
 *
 *
 * There are a couple of special options that can be achieved with the event:
 *
 *
 *  * if the excluded id is set, the sink will be requested to handle that id
 * an treat it as an excluded id.
 *  * if the newSearchOrderRequested flag is set, the sink will need to take
 * care of creating a new SearchOrder and set it as the model.
 *
 *
 * @author u.joss
 */
class SearchOrderChangeEvent(val target: AjaxRequestTarget) {

    var excludedId: Long? = null
        private set
    var droppedConditionId: Long? = null
        private set
    var isNewSearchOrderRequested = false
        private set

    fun withExcludedPaperId(excludedId: Long): SearchOrderChangeEvent {
        this.excludedId = excludedId
        droppedConditionId = null
        isNewSearchOrderRequested = false
        return this
    }

    fun withDroppedConditionId(id: Long?): SearchOrderChangeEvent {
        droppedConditionId = id
        excludedId = null
        isNewSearchOrderRequested = false
        return this
    }

    fun requestingNewSearchOrder(): SearchOrderChangeEvent {
        isNewSearchOrderRequested = true
        droppedConditionId = null
        excludedId = null
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchOrderChangeEvent

        if (target != other.target) return false
        if (excludedId != other.excludedId) return false
        if (droppedConditionId != other.droppedConditionId) return false
        if (isNewSearchOrderRequested != other.isNewSearchOrderRequested) return false

        return true
    }

    override fun hashCode(): Int {
        var result = target.hashCode()
        result = 31 * result + (excludedId?.hashCode() ?: 0)
        result = 31 * result + (droppedConditionId?.hashCode() ?: 0)
        result = 31 * result + isNewSearchOrderRequested.hashCode()
        return result
    }
}
