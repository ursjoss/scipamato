package ch.difty.scipamato.publ.web.font;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.publ.config.ApplicationPublicProperties;
import ch.difty.scipamato.publ.web.resources.MetaOTCssResourceReference;

@ExtendWith(MockitoExtension.class)
class MetaOTFontResourceProviderTest {

    private MetaOTFontResourceProvider provider;

    @Mock
    private ApplicationPublicProperties applicationProperties;

    @Test
    void withNoCommercialFontPresentSetting_getsNull() {
        when(applicationProperties.isCommercialFontPresent()).thenReturn(false);

        provider = new MetaOTFontResourceProvider(applicationProperties);
        assertThat(provider.getCssResourceReference()).isNull();
        assertThat(provider.isCommercialFontPresent()).isFalse();

        verify(applicationProperties).isCommercialFontPresent();
    }

    @Test
    void withCommercialFontPresentSetting_getsReference() {
        when(applicationProperties.isCommercialFontPresent()).thenReturn(true);

        provider = new MetaOTFontResourceProvider(applicationProperties);
        assertThat(provider.getCssResourceReference())
            .isNotNull()
            .isInstanceOf(MetaOTCssResourceReference.class);
        assertThat(provider.isCommercialFontPresent()).isTrue();

        verify(applicationProperties).isCommercialFontPresent();
    }
}
