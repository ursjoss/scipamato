package ch.difty.scipamato.core.web.common

import ch.difty.scipamato.common.navigator.ItemNavigator
import ch.difty.scipamato.common.web.AbstractPage
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade
import ch.difty.scipamato.common.web.pages.MenuBuilder
import ch.difty.scipamato.core.config.ApplicationCoreProperties
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.web.resources.MainCssResourceReference
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome7CdnCssReference
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.model.IModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

abstract class BasePage<T> : AbstractPage<T> {

    @SpringBean
    private lateinit var applicationProperties: ApplicationCoreProperties

    @SpringBean
    private lateinit var menuBuilder: MenuBuilder

    @SpringBean
    private lateinit var sessionFacade: ScipamatoWebSessionFacade

    override val properties: ApplicationCoreProperties
        get() = applicationProperties

    private val authentication: Authentication
        get() = SecurityContextHolder.getContext().authentication

    protected val activeUser: User
        get() = authentication.principal as User

    protected val paperIdManager: ItemNavigator<Long>
        get() = sessionFacade.paperIdManager

    protected val languageCode: String
        get() = sessionFacade.languageCode

    protected constructor(parameters: PageParameters?) : super(parameters)
    protected constructor(model: IModel<T>?) : super(model)

    override fun renderHead(response: IHeaderResponse) {
        super.renderHead(response)
        response.render(CssHeaderItem.forReference(MainCssResourceReference.get()))
        response.render(CssHeaderItem.forReference(FontAwesome7CdnCssReference.instance()))
    }

    override fun addLinksTo(nb: Navbar) {
        super.addLinksTo(nb)
        menuBuilder.addMenuLinksTo(nb, this)
    }

    @Suppress("SameParameterValue")
    protected fun hasOneOfRoles(vararg roles: String): Boolean = sessionFacade.hasAtLeastOneRoleOutOf(*roles)

    companion object {
        private const val serialVersionUID = 1L
    }
}
