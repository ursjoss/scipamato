package ch.difty.scipamato.common.web.pages

import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.common.web.AbstractPage
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade
import ch.difty.scipamato.common.web.component.SerializableConsumer
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink
import org.apache.wicket.Component
import org.apache.wicket.Page
import org.apache.wicket.markup.html.link.AbstractLink
import org.apache.wicket.markup.html.link.PopupSettings
import org.apache.wicket.model.Model
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.request.mapper.parameter.PageParameters

abstract class AbstractMenuBuilder protected constructor(
    val applicationProperties: ApplicationProperties,
    private val webSessionFacade: ScipamatoWebSessionFacade,
) : MenuBuilder {

    @Suppress("LongParameterList")
    fun <P : Page> addPageLink(
        navbar: Navbar,
        menuPage: Page,
        referringPageClass: Class<P>,
        labelResource: String,
        iconType: IconType,
        position: Navbar.ComponentPosition,
    ) {
        NavbarButton<Unit, P>(
            referringPageClass,
            StringResourceModel(labelResource, menuPage, null)
        ).setIconType(iconType)
            .also { navbar.addComponents(NavbarComponents.transform(position, it)) }
    }

    fun addExternalLink(
        navbar: Navbar,
        url: String,
        label: String,
        iconType: IconType?,
        position: Navbar.ComponentPosition,
    ) {
        NavbarExternalLink(Model.of(url)).apply {
            setLabel(Model.of(label))
            val popupSettings = PopupSettings(PopupSettings.RESIZABLE or PopupSettings.SCROLLBARS).apply {
                setTarget("_blank")
            }
            setPopupSettings(popupSettings)
            iconType?.let { setIconType(it) }
        }.also { navbar.addComponents(NavbarComponents.transform(position, it)) }
    }

    fun newMenu(
        navbar: Navbar,
        page: Page,
        labelResource: String,
        iconType: IconType,
        action: SerializableConsumer<List<AbstractLink>>,
    ) {
        navbar.addComponents(
            NavbarComponents.transform(
                Navbar.ComponentPosition.LEFT,
                object : NavbarDropDownButton(
                    StringResourceModel("menu.$labelResource", page, null),
                    Model.of(iconType)
                ) {
                    private val serialVersionUID: Long = 1L
                    override fun newSubMenuButtons(buttonMarkupId: String) = ArrayList<AbstractLink>().apply {
                        action.accept(this)
                    }
                }
            ))
    }

    @Suppress("LongParameterList")
    @JvmOverloads
    fun <P : AbstractPage<*>?> addEntryToMenu(
        label: String,
        component: Component,
        pageClass: Class<P>,
        iconType: IconType?,
        links: MutableList<AbstractLink>,
        pageParameters: PageParameters = PageParameters(),
    ) {
        MenuBookmarkablePageLink<Unit, P>(
            pageClass,
            pageParameters,
            StringResourceModel(label, component, null)
        ).apply { iconType?.let { setIconType(it) } }
            .also { links.add(it) }
    }

    val versionAnker: String
        get() {
            val buildVersion = applicationProperties.buildVersion
            return if (buildVersion.isNullOrEmpty()) ""
            else "#" + if (buildVersion.endsWith("SNAPSHOT")) "unreleased" else "v$buildVersion"
        }

    val versionLink: String
        get() = "version ${applicationProperties.buildVersion}"

    fun hasOneOfRoles(vararg roles: String): Boolean = webSessionFacade.hasAtLeastOneRoleOutOf(*roles)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
