package ch.difty.scipamato.core.web.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.navigator.LongNavigator;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.web.WicketTest;

class CoreWebSessionFacadeTest extends WicketTest {

    private final ScipamatoWebSessionFacade sessionFacade = new CoreWebSessionFacade();

    @Test
    void gettingLanguageCode_withBritishLocale_returnsBritishCode() {
        getTester()
            .getSession()
            .setLocale(new Locale("en_GB"));
        assertThat(sessionFacade.getLanguageCode()).isEqualTo("en_gb");
    }

    @Test
    void gettingLanguageCode_withFrenchLocale_returnsFrenchCode() {
        getTester()
            .getSession()
            .setLocale(new Locale("fr"));
        assertThat(sessionFacade.getLanguageCode()).isEqualTo("fr");
    }

    @Test
    void gettingPaperIdManager_returnsMock() {
        assertThat(sessionFacade.getPaperIdManager()).isInstanceOf(LongNavigator.class);
    }

    @Test
    void roleCheck() {
        assertThat(sessionFacade.hasAtLeastOneRoleOutOf(Roles.VIEWER, Roles.ADMIN)).isFalse();
        assertThat(sessionFacade.hasAtLeastOneRoleOutOf(Roles.USER)).isTrue();
    }
}
