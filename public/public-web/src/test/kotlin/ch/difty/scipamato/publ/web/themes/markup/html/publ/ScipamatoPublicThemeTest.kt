package ch.difty.scipamato.publ.web.themes.markup.html.publ

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldHaveSize
import org.apache.wicket.markup.head.CssReferenceHeaderItem
import org.junit.jupiter.api.Test

class ScipamatoPublicThemeTest {
    @Test
    fun newTheme_withNoSpecificName_hasDefaultName() {
        val theme = ScipamatoPublicTheme(false)
        theme.name() shouldBeEqualTo "scipamato-public"
    }

    @Test
    fun newTheme_withSomeName_hasSomeName() {
        val theme = ScipamatoPublicTheme(false, "somename")
        theme.name() shouldBeEqualTo "somename"
    }

    @Test
    fun newTheme_notUsingSassOverCss_hasCssReference() {
        val theme = ScipamatoPublicTheme(useSassOverCss = false)
        theme.dependencies shouldHaveSize 1
        val resourceReference = (theme.dependencies[0] as CssReferenceHeaderItem).reference
        resourceReference shouldBeInstanceOf ScipamatoPublicCssReference::class
    }

    @Test
    fun newTheme_usingSassOverCss_hasSassReference() {
        val theme = ScipamatoPublicTheme(useSassOverCss = true)
        theme.dependencies shouldHaveSize 1
        val resourceReference = (theme.dependencies[0] as CssReferenceHeaderItem).reference
        resourceReference shouldBeInstanceOf ScipamatoPublicSassReference::class
    }
}
