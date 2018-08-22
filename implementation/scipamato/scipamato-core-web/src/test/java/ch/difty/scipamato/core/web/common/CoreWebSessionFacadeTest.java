package ch.difty.scipamato.core.web.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.Test;

import ch.difty.scipamato.common.navigator.LongNavigator;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.core.web.WicketTest;

public class CoreWebSessionFacadeTest extends WicketTest {

    private final ScipamatoWebSessionFacade sessionFacade = new CoreWebSessionFacade();

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
    public void roleCheck() {
        assertThat(sessionFacade.hasAtLeastOneRoleOutOf("admin", "user")).isFalse();
        assertThat(sessionFacade.hasAtLeastOneRoleOutOf("viewer")).isTrue();
    }
}
