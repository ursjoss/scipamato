package ch.difty.scipamato.publ.web.common

import ch.difty.scipamato.common.logger
import ch.difty.scipamato.common.navigator.ItemNavigator
import ch.difty.scipamato.common.web.AbstractPage
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade
import ch.difty.scipamato.publ.config.ApplicationPublicProperties
import ch.difty.scipamato.publ.misc.LocaleExtractor
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider
import ch.difty.scipamato.publ.web.PublicPageParameters
import ch.difty.scipamato.publ.web.pym.PymScripts
import ch.difty.scipamato.publ.web.resources.MainCssResourceReference
import ch.difty.scipamato.publ.web.resources.PymJavaScriptResourceReference
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome7CdnCssReference
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem
import org.apache.wicket.markup.head.JavaScriptHeaderItem
import org.apache.wicket.markup.head.OnLoadHeaderItem
import org.apache.wicket.model.IModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean

private val log = logger()

abstract class BasePage<T> : AbstractPage<T> {

    @SpringBean
    private lateinit var sessionFacade: ScipamatoWebSessionFacade

    @SpringBean
    private lateinit var applicationProperties: ApplicationPublicProperties

    @SpringBean(name = "parentUrlLocaleExtractor")
    private lateinit var localeExtractor: LocaleExtractor

    @SpringBean(name = "metaOTFontResourceProvider")
    private lateinit var metaOtFontResourceProvider: CommercialFontResourceProvider

    final override val isNavbarVisible: Boolean

    protected val languageCode: String
        get() = sessionFacade.languageCode
    protected val paperIdManager: ItemNavigator<Long>
        get() = sessionFacade.paperIdManager
    override val properties: ApplicationPublicProperties
        get() = applicationProperties

    protected constructor(parameters: PageParameters) : super(parameters) {
        val showNavbarValue = parameters[PublicPageParameters.SHOW_NAVBAR.parameterName]
        isNavbarVisible = showNavbarValue.toBoolean(properties.isNavbarVisibleByDefault)
        considerSettingLocaleFromParentUrl(parameters)
    }

    protected constructor(model: IModel<T>?) : super(model) {
        isNavbarVisible = properties.isNavbarVisibleByDefault
    }

    private fun considerSettingLocaleFromParentUrl(parameters: PageParameters) {
        val puValue = parameters[PublicPageParameters.PARENT_URL.parameterName]
        if (!puValue.isNull) {
            val locale = localeExtractor.extractLocaleFrom(puValue.toString())
            log.info("Switching Locale to {}", locale.language)
            session.locale = locale
        }
    }

    override fun renderHead(response: IHeaderResponse) {
        super.renderHead(response)
        response.render(CssHeaderItem.forReference(MainCssResourceReference))
        response.render(CssHeaderItem.forReference(FontAwesome7CdnCssReference.instance()))
        if (properties.isCommercialFontPresent) renderCommercialFonts(response)
        if (properties.isResponsiveIframeSupportEnabled) renderPymForResponsiveIframe(response)
    }

    private fun renderCommercialFonts(response: IHeaderResponse) = with(response) {
        render(CssHeaderItem.forReference(metaOtFontResourceProvider.cssResourceReference))
        renderAdditionalCommercialFonts(this)
    }

    /**
     * Override to render page specific additional commercial fonts. Note: This code
     * is only called if the property `commercial-font-present` is set to true.
     *
     * @param response the response to render the css header item references on
     */
    open fun renderAdditionalCommercialFonts(response: IHeaderResponse) {
        // no default implementation
    }

    /**
     * Adds pym.js to the page and instantiates the `pymChild`.
     *
     * @param response the response to render the javascript for
     */
    private fun renderPymForResponsiveIframe(response: IHeaderResponse) = with(response) {
        render(JavaScriptHeaderItem.forReference(PymJavaScriptResourceReference))
        render(JavaScriptContentHeaderItem(PymScripts.INSTANTIATE.script, PymScripts.INSTANTIATE.id))
        render(OnLoadHeaderItem(PymScripts.RESIZE.script))
    }

    companion object {
        private const val serialVersionUID = 1L
        const val SELECT_ALL_RESOURCE_TAG = "multiselect.selectAll"
        const val DESELECT_ALL_RESOURCE_TAG = "multiselect.deselectAll"
    }
}
