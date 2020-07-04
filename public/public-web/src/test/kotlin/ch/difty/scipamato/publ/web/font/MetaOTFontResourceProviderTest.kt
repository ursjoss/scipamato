package ch.difty.scipamato.publ.web.font

import ch.difty.scipamato.publ.config.ApplicationPublicProperties
import ch.difty.scipamato.publ.web.resources.MetaOTCssResourceReference
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test

internal class MetaOTFontResourceProviderTest {

    @Test
    fun withNoCommercialFontPresentSetting_getsNull() {
        val applicationProperties = mockk<ApplicationPublicProperties> {
            every { isCommercialFontPresent } returns false
        }
        with(MetaOTFontResourceProvider(applicationProperties)) {
            cssResourceReference.shouldBeNull()
            isCommercialFontPresent.shouldBeFalse()
        }
        verify { applicationProperties.isCommercialFontPresent }
    }

    @Test
    fun withCommercialFontPresentSetting_getsReference() {
        val applicationProperties = mockk<ApplicationPublicProperties> {
            every { isCommercialFontPresent } returns true
        }
        with(MetaOTFontResourceProvider(applicationProperties)) {
            cssResourceReference shouldBeInstanceOf MetaOTCssResourceReference::class
            isCommercialFontPresent.shouldBeTrue()
        }
        verify { applicationProperties.isCommercialFontPresent }
    }
}
