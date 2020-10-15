package ch.difty.scipamato.publ.web.paper.browse

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.form.FormComponent

/**
 * The event indicates that one of the filter fields has changed that are present
 * multiple times on the SimpleFilterPanel and therefore need synchronization.
 */
data class SimpleFilterPanelChangeEvent(
    val target: AjaxRequestTarget,
    val id: String? = null,
    val markupId: String? = null,
) {

    /**
     * A [component] is added to the target, if it
     *  * has the same id but a different markupId than the event source
     *  * has the same id as the event source but the latter did not specify a  markupId
     *  * the event source has not specified an id
     */
    fun considerAddingToTarget(component: FormComponent<*>) {
        if (isValidTarget(component)) target.add(component)
    }

    private fun isValidTarget(comp: FormComponent<*>): Boolean =
        id == null || id == comp.id &&
            (markupId == null || markupId != comp.markupId)
}
