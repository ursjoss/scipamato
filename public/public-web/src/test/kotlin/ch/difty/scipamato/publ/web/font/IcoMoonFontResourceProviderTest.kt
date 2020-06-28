package ch.difty.scipamato.publ.web.font

import ch.difty.scipamato.common.ClearAllMocksExtension
import ch.difty.scipamato.publ.config.ApplicationPublicProperties
import ch.difty.scipamato.publ.web.resources.IcoMoonCssResourceReference
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, ClearAllMocksExtension::class)
internal class IcoMoonFontResourceProviderTest {

    private lateinit var provider: IcoMoonFontResourceProvider

    @MockK
    private lateinit var applicationProperties: ApplicationPublicProperties

    @Test
    fun withNoCommercialFontPresentSetting_getsNull() {
        every { applicationProperties.isCommercialFontPresent } returns false
        provider = IcoMoonFontResourceProvider(applicationProperties)
        with(provider) {
            cssResourceReference.shouldBeNull()
            isCommercialFontPresent.shouldBeFalse()
        }
        verify { applicationProperties.isCommercialFontPresent }
    }

    @Test
    fun withCommercialFontPresentSetting_getsReference() {
        every { applicationProperties.isCommercialFontPresent } returns true
        provider = IcoMoonFontResourceProvider(applicationProperties)
        with(provider) {
            cssResourceReference shouldBeInstanceOf IcoMoonCssResourceReference::class
            isCommercialFontPresent.shouldBeTrue()
        }
        verify { applicationProperties.isCommercialFontPresent }
    }
}
