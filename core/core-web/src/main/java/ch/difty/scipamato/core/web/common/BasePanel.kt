package ch.difty.scipamato.core.web.common

import ch.difty.scipamato.common.navigator.ItemNavigator
import ch.difty.scipamato.common.web.AbstractPanel
import ch.difty.scipamato.common.web.LABEL_RESOURCE_TAG
import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.common.web.SHORT_LABEL_RESOURCE_TAG
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade
import ch.difty.scipamato.core.config.ApplicationCoreProperties
import ch.difty.scipamato.core.entity.User
import org.apache.wicket.model.IModel
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.spring.injection.annot.SpringBean
import org.springframework.security.core.context.SecurityContextHolder

abstract class BasePanel<T> @JvmOverloads constructor(
    id: String,
    model: IModel<T>? = null,
    mode: Mode = Mode.VIEW,
) : AbstractPanel<T>(id, model, mode) {

    @SpringBean
    protected lateinit var properties: ApplicationCoreProperties

    @SpringBean
    private lateinit var webSessionFacade: ScipamatoWebSessionFacade

    protected val paperIdManager: ItemNavigator<Long>
        get() = webSessionFacade.paperIdManager

    protected val activeUser: User
        get() = SecurityContextHolder.getContext().authentication.principal as User

    protected val localization: String
        get() = webSessionFacade.languageCode

    /**
     * Retrieves the label's resource string for the provided [componentId].
     */
    protected fun getLabelResourceFor(componentId: String): String = getResourceFor(componentId, LABEL_RESOURCE_TAG)

    /**
     * Retrieves the label's resource string (short form) for given [componentId].
     */
    protected fun getShortLabelResourceFor(componentId: String): String = getResourceFor(componentId, SHORT_LABEL_RESOURCE_TAG)

    private fun getResourceFor(componentId: String, tag: String): String =
        StringResourceModel("$componentId$tag", this, null).string

    companion object {
        private const val serialVersionUID = 1L
    }
}
