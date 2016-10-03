package ch.difty.sipamato.config;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@Ignore // TODO get running with values injected
public class SipamatoPropertiesTest {

    @Spy
    private SipamatoProperties propsSpy;

    @Test
    public void assertProperties() {
        propsSpy.getAuthorParserStrategy();
        propsSpy.getAutoSaveIntervalInSeconds();
        propsSpy.isAutoSavingEnabled();
    }

    @Test
    public void isAutoSavingEnabled_withInterval0_returnsFalse() {
        doReturn(0).when(propsSpy).getAutoSaveIntervalInSeconds();
        assertThat(propsSpy.isAutoSavingEnabled()).isFalse();
        verify(propsSpy).getAutoSaveIntervalInSeconds();
    }

    @Test
    public void isAutoSavingEnabled_withIntervalGreaterThan0_returnsTrue() {
        doReturn(1).when(propsSpy).getAutoSaveIntervalInSeconds();
        assertThat(propsSpy.isAutoSavingEnabled()).isTrue();
        verify(propsSpy).getAutoSaveIntervalInSeconds();
    }
}
