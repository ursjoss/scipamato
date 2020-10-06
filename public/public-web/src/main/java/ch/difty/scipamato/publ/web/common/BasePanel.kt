package ch.difty.scipamato.publ.web.common

import ch.difty.scipamato.common.web.AbstractPanel
import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade
import org.apache.wicket.model.IModel
import org.apache.wicket.spring.injection.annot.SpringBean

abstract class BasePanel<T> @JvmOverloads constructor(
    id: String,
    model: IModel<T>? = null,
    mode: Mode = Mode.VIEW,
) : AbstractPanel<T>(id, model, mode) {

    @SpringBean
    private lateinit var webSessionFacade: ScipamatoWebSessionFacade

    open val localization: String
        get() = webSessionFacade.languageCode

    companion object {
        private const val serialVersionUID = 1L
    }
}
