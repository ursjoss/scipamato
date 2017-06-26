package ch.difty.scipamato.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.scipamato.config.ApplicationProperties;
import ch.difty.scipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class ScipamatoLocalizationTest {

    private static final String DEFAULT_LOCALIZATION = "xy";

    private Localization localization;

    @Mock
    private ApplicationProperties appPropertiesMock;

    @Before
    public void setUp() {
        when(appPropertiesMock.getDefaultLocalization()).thenReturn(DEFAULT_LOCALIZATION);
        localization = new ScipamatoLocalization(appPropertiesMock);
    }

    @Test
    public void constructing_withNullApplicationProperties_throws() {
        try {
            new ScipamatoLocalization(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("appProperties must not be null.");
        }
    }

    @Test
    public void gettingDefaultLocalization_returnsLocalizationFromSettings() {
        assertThat(localization.getDefaultLocalization()).isEqualTo(DEFAULT_LOCALIZATION);
        verify(appPropertiesMock).getDefaultLocalization();
    }

    @Test
    public void gettingLocalization_withoutSettingIt_returnsLocalizationFromSettings() {
        assertThat(localization.getDefaultLocalization()).isEqualTo(DEFAULT_LOCALIZATION);
        verify(appPropertiesMock).getDefaultLocalization();
    }

    @Test
    public void gettingLocalization_afterSettingIt_returnsNewValue() {
        final String newLocalization = "ab";
        localization.setLocalization(newLocalization);
        assertThat(localization.getLocalization()).isEqualTo(newLocalization);
        assertThat(localization.getDefaultLocalization()).isEqualTo(DEFAULT_LOCALIZATION);
        verify(appPropertiesMock).getDefaultLocalization();
    }

    @Test
    public void gettingDefaultLocalization_afterSettingLocalization_stillReturnsLocalizationFromSettings() {
        final String newLocalization = "ab";
        localization.setLocalization(newLocalization);
        assertThat(localization.getDefaultLocalization()).isEqualTo(DEFAULT_LOCALIZATION);
        verify(appPropertiesMock).getDefaultLocalization();
    }
}
