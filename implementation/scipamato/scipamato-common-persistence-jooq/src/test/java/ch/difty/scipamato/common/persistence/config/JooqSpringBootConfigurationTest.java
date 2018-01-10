package ch.difty.scipamato.common.persistence.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.jooq.conf.Settings;
import org.junit.Test;

public class JooqSpringBootConfigurationTest {

    private final JooqSpringBootConfiguration conf = new JooqSpringBootConfiguration();

    @Test
    public void settings_haveOptimisticLockingConfigured() {
        final Settings settings = conf.settings();
        assertThat(settings.isExecuteWithOptimisticLocking()).isTrue();
    }
}
