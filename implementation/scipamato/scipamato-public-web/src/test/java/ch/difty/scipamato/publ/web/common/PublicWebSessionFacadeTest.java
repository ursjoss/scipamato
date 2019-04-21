package ch.difty.scipamato.publ.web.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.navigator.LongNavigator;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.publ.web.WicketTest;

public class PublicWebSessionFacadeTest extends WicketTest {

    private final ScipamatoWebSessionFacade sessionFacade = new PublicWebSessionFacade();

    @Test
    public void gettingLanguageCode_withBritishLocale_returnsBritishCode() {
        getTester()
            .getSession()
            .setLocale(new Locale("en_GB"));
        assertThat(sessionFacade.getLanguageCode()).isEqualTo("en_gb");
    }

    @Test
    public void gettingLanguageCode_withFrenchLocale_returnsFrenchCode() {
        getTester()
            .getSession()
            .setLocale(new Locale("fr"));
        assertThat(sessionFacade.getLanguageCode()).isEqualTo("fr");
    }

    @Test
    public void gettingPaperIdManager_returnsMock() {
        assertThat(sessionFacade.getPaperIdManager()).isInstanceOf(LongNavigator.class);
    }

    @Test
    public void hasAtLeastOneRoleOutOf_staticallyReturnsFalse() {
        assertThat(sessionFacade.hasAtLeastOneRoleOutOf()).isFalse();
        assertThat(sessionFacade.hasAtLeastOneRoleOutOf(null)).isFalse();
        assertThat(sessionFacade.hasAtLeastOneRoleOutOf(Roles.ADMIN, Roles.USER)).isFalse();
    }
}
