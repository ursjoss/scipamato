package ch.difty.scipamato.publ;

import static org.mockito.Mockito.when;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.publ.config.ScipamatoPublicProperties;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ScipamatoPublicApplicationTest {

    @Mock
    private ScipamatoPublicProperties properties;

    @Test
    void withCommercialFontEnabled_willOutputLog() {
        when(properties.isCommercialFontPresent()).thenReturn(true);
        when(properties.isResponsiveIframeSupportEnabled()).thenReturn(false);

        ScipamatoPublicApplication app = new ScipamatoPublicApplication(properties);
        log.info("We should see single log about commercial font being enabled");
        app.logSpecialConfiguration();
        // visually assert the respective log is on console (no automatic assertion)
        log.info("----");
    }

    @Test
    void withPymEnabled_willOutputLog() {
        when(properties.isCommercialFontPresent()).thenReturn(false);
        when(properties.isResponsiveIframeSupportEnabled()).thenReturn(true);

        ScipamatoPublicApplication app = new ScipamatoPublicApplication(properties);
        log.info("We should see single log about pym being enabled");
        app.logSpecialConfiguration();
        // visually assert the respective log is on console (no automatic assertion)
        log.info("----");
    }

    @Test
    void withPropertiesDisabled_willNotOutputLogs() {
        when(properties.isCommercialFontPresent()).thenReturn(false);
        when(properties.isResponsiveIframeSupportEnabled()).thenReturn(false);
        ScipamatoPublicApplication app = new ScipamatoPublicApplication(properties);
        log.info("We should see no logs (about commercial fonts or pym)");
        app.logSpecialConfiguration();
        // visually assert no logs are on console
        log.info("----");
    }
}