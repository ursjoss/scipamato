package ch.difty.scipamato.core.web.common

import ch.difty.scipamato.common.navigator.LongNavigator
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade
import ch.difty.scipamato.core.auth.Roles
import ch.difty.scipamato.core.web.WicketTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test
import java.util.*

internal class CoreWebSessionFacadeTest : WicketTest() {

    private val sessionFacade: ScipamatoWebSessionFacade = CoreWebSessionFacade()

    @Test
    fun gettingLanguageCode_withBritishLocale_returnsBritishCode() {
        tester.session.locale = Locale.of("en_GB")
        sessionFacade.languageCode shouldBeEqualTo "en_gb"
    }

    @Test
    fun gettingLanguageCode_withFrenchLocale_returnsFrenchCode() {
        tester.session.locale = Locale.of("fr")
        sessionFacade.languageCode shouldBeEqualTo "fr"
    }

    @Test
    fun gettingPaperIdManager_returnsMock() {
        sessionFacade.paperIdManager shouldBeInstanceOf LongNavigator::class
    }

    @Test
    fun roleCheck() {
        sessionFacade.hasAtLeastOneRoleOutOf(Roles.VIEWER, Roles.ADMIN).shouldBeFalse()
        sessionFacade.hasAtLeastOneRoleOutOf(Roles.USER).shouldBeTrue()
    }
}
