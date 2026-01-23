package ch.difty.scipamato.common

import org.apache.wicket.Component
import org.apache.wicket.MarkupContainer
import org.apache.wicket.Page
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.core.request.handler.EmptyAjaxRequestHandler
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.request.ILogData
import org.apache.wicket.request.IRequestCycle
import org.apache.wicket.request.component.IRequestablePage
import org.apache.wicket.request.mapper.parameter.PageParameters

@Suppress("TooManyFunctions", "unused")
class AjaxRequestTargetSpy : AjaxRequestTarget {
    private val delegate = EmptyAjaxRequestHandler.getInstance()

    val components = mutableSetOf<Component>()
    val javaScripts = mutableListOf<CharSequence>()

    fun reset() {
        components.clear()
        javaScripts.clear()
    }

    override fun getHeaderResponse(): IHeaderResponse? = null
    override fun addListener(listener: AjaxRequestTarget.IListener?) = Unit
    override fun registerRespondListener(listener: AjaxRequestTarget.ITargetRespondListener?) = Unit
    override fun focusComponent(component: Component?) = Unit
    override fun getPageParameters(): PageParameters? = null
    override fun add(component: Component?, markupId: String?) {
        component?.let { components.add(it) }
    }

    override fun add(vararg components: Component?) {
        components.forEach { comp ->
            comp?.let { c -> this.components.add(c) }
        }
    }

    override fun getLastFocusedElementId(): String? = null
    override fun isPageInstanceCreated(): Boolean = false
    override fun getPageClass(): Class<out IRequestablePage>? = null
    override fun appendJavaScript(javascript: CharSequence?) {
        javascript?.let { javaScripts.add(javascript) }
    }

    override fun getComponents(): MutableCollection<out Component> = components
    override fun getPageId(): Int = 1
    override fun addChildren(parent: MarkupContainer?, childCriteria: Class<*>?) = Unit
    override fun getRenderCount(): Int = 1
    override fun prependJavaScript(javascript: CharSequence?) = Unit
    override fun getPage(): Page? = null
    override fun getLogData(): ILogData? = null
    override fun respond(requestCycle: IRequestCycle?) = delegate.respond(requestCycle)
    override fun equals(other: Any?): Boolean = other is AjaxRequestTargetSpy
    override fun hashCode(): Int = HASH
    override fun toString() = "AjaxRequestTarget"

    companion object {
        @Suppress("MagicNumber")
        /** immutable hashcode. */
        private const val HASH = 17 * 1542323
    }
}
