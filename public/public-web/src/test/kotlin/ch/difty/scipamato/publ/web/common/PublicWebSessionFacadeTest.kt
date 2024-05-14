package ch.difty.scipamato.publ.web.common

import ch.difty.scipamato.common.navigator.LongNavigator
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade
import ch.difty.scipamato.publ.web.WicketTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeInstanceOf
import org.apache.wicket.authroles.authorization.strategies.role.Roles
import org.junit.jupiter.api.Test
import java.util.Locale

class PublicWebSessionFacadeTest : WicketTest() {

    private val sessionFacade: ScipamatoWebSessionFacade = PublicWebSessionFacade()

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
    fun hasAtLeastOneRoleOutOf_staticallyReturnsFalse() {
        sessionFacade.hasAtLeastOneRoleOutOf().shouldBeFalse()
        sessionFacade.hasAtLeastOneRoleOutOf(Roles.ADMIN, Roles.USER).shouldBeFalse()
    }
}
