package ch.difty.scipamato.core.logic.export;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.core.config.ApplicationCoreProperties;
import ch.difty.scipamato.core.logic.exporting.RisAdapterFactory;
import ch.difty.scipamato.core.logic.exporting.RisExporterConfiguration;
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy;

@ExtendWith(MockitoExtension.class)
class RisExporterConfigurationTest {

    private RisExporterConfiguration conf;

    @Mock
    private ApplicationCoreProperties appProperties;

    @BeforeEach
    void setUp() {
        conf = new RisExporterConfiguration();
        when(appProperties.getRisExporterStrategy()).thenReturn(RisExporterStrategy.DISTILLERSR);
    }

    @Test
    void canRetrieveRisAdapterFactory() {
        RisAdapterFactory factory = conf.risAdapterFactory(appProperties);

        assertThat(factory)
            .isNotNull()
            .isInstanceOf(RisAdapterFactory.class);

        verify(appProperties).getRisExporterStrategy();
    }
}
