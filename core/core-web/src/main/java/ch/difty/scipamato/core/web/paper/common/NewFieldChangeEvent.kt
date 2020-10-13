package ch.difty.scipamato.core.web.paper.common

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.form.TextArea

/**
 * The event indicates that one of the new fields has changed that are present
 * multiple times on the PaperPanel and therefore need synchronization.
 */
data class NewFieldChangeEvent @JvmOverloads constructor(
    val target: AjaxRequestTarget,
    val id: String? = null,
    val markupId: String? = null,
) {

    /**
     * A component is added to the target, if it
     *
     *  * has the same id but a different markupId than the event source
     *  * has the same id as the event source but the latter did not specify a markupId
     *  * the event source has not specified an id
     */
    fun considerAddingToTarget(component: TextArea<*>) {
        if (isValidTarget(component)) target.add(component)
    }

    private fun isValidTarget(component: TextArea<*>): Boolean {
        return this.id == null || this.id == component.id && (this.markupId == null || this.markupId != component.markupId)
    }
}
