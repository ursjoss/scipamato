package ch.difty.scipamato.web.security;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

import ch.difty.scipamato.web.WicketTest;

public class ScipamatoAuthenticatedWebSessionConfigTest extends WicketTest {

    @Autowired
    private ScipamatoAuthenticatedWebSessionConfig config;

    @Test
    public void canWire() {
        assertThat(config).isNotNull();
    }

    @Test
    public void providesSecureWebSession() {
        assertThat(config.getAuthenticatedWebSessionClass()).isNotNull();
        assertThat(config.getAuthenticatedWebSessionClass().getName()).isEqualTo(SecureWebSession.class.getName());
    }
}
