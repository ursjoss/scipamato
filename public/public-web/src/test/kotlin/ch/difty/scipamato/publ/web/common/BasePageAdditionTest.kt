@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.publ.web.common

import ch.difty.scipamato.publ.config.ApplicationPublicProperties
import ch.difty.scipamato.publ.config.ScipamatoPublicProperties
import ch.difty.scipamato.publ.misc.LocaleExtractor
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider
import ch.difty.scipamato.publ.web.PublicPageParameters
import ch.difty.scipamato.publ.web.WicketTest
import ch.difty.scipamato.publ.web.resources.MetaOTCssResourceReference
import com.ninjasquad.springmockk.MockkBean
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotContain
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.Test
import java.util.Locale

internal class BasePageAdditionTest : WicketTest() {

    @MockkBean
    private lateinit var localeExtractor: LocaleExtractor

    @MockkBean(name = "metaOTFontResourceProvider")
    private lateinit var metaOtFontResourceProvider: CommercialFontResourceProvider

    @MockK(relaxed = true)
    private lateinit var applicationProperties: ScipamatoPublicProperties

    private val localeZh = Locale.CHINESE
    private var additionalCommercialFontsRendered = false
    private val pp = PageParameters()

    private fun newPageWithParameters(pp: PageParameters): BasePage<*> {
        return object : BasePage<Any?>(pp) {
            override val properties: ApplicationPublicProperties
                get() = applicationProperties

            override fun renderAdditionalCommercialFonts(response: IHeaderResponse) {
                additionalCommercialFontsRendered = true
            }
        }
    }

    @Test
    fun callingPageWithoutLocaleIndicatingParentUrl_doesNotSetLocale() {
        newPageWithParameters(pp).session.locale shouldNotBeEqualTo localeZh
    }

    @Test
    fun callingPageWithParentUrlIndicatingChinese_setsChineseLocale() {
        pp[PublicPageParameters.PARENT_URL.parameterName] = "someParentUrlIndicatingChineseLocal"
        every { localeExtractor.extractLocaleFrom("someParentUrlIndicatingChineseLocal") } returns localeZh
        newPageWithParameters(pp).session.locale shouldBeEqualTo localeZh
    }

    @Test
    fun page_withCommercialFontsPresent_rendersMetaFontsAndAdditionalCommercialFonts() {
        every { applicationProperties.isCommercialFontPresent } returns true
        every { metaOtFontResourceProvider.cssResourceReference } returns MetaOTCssResourceReference

        tester.startPage(newPageWithParameters(pp))

        tester.lastResponseAsString shouldContain "css/MetaOT-ver"
        additionalCommercialFontsRendered.shouldBeTrue()

        verify { applicationProperties.isCommercialFontPresent }
        verify { metaOtFontResourceProvider.cssResourceReference }
    }

    @Test
    fun page_withoutCommercialFontsPresent_rendersNeitherMetaFontsNorAdditionalCommercialFonts() {
        every { applicationProperties.isCommercialFontPresent } returns false

        tester.startPage(newPageWithParameters(pp))

        tester.lastResponseAsString shouldNotContain "css/MetaOT-ver"
        additionalCommercialFontsRendered.shouldBeFalse()

        verify { applicationProperties.isCommercialFontPresent }
    }

    @Test
    fun page_withResponsiveIframeSupportEnabled_rendersPym() {
        assertPym(true)
    }

    @Test
    fun page_withoutResponsiveIframeSupportEnabled_doesNotRenderPym() {
        assertPym(false)
    }

    private fun assertPym(render: Boolean) {
        every { applicationProperties.isResponsiveIframeSupportEnabled } returns render

        tester.startPage(newPageWithParameters(pp))

        tester.lastResponseAsString.contains("PymJavaScriptResourceReference/js/pym.v1") shouldBeEqualTo render
        tester.lastResponseAsString.contains("var pymChild = new pym.Child({ id: 'scipamato-public' });") shouldBeEqualTo render
        tester.lastResponseAsString.contains("pymChild.sendHeight();") shouldBeEqualTo render

        verify { applicationProperties.isResponsiveIframeSupportEnabled }
    }

    @Test
    fun renderingAdditionalCommercialFonts_isNoop() {
        val response = mockk<IHeaderResponse>()
        // call it for coverage
        var page: BasePage<*> = object : BasePage<Any?>(pp) {
            override val properties: ApplicationPublicProperties
                get() = applicationProperties
        }
        page.renderAdditionalCommercialFonts(response)

        // this time assert call it to assert it
        page = newPageWithParameters(pp)
        page.renderAdditionalCommercialFonts(response)

        confirmVerified(response)
    }
}
