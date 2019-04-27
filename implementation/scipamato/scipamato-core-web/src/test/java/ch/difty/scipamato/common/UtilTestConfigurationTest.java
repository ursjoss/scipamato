package ch.difty.scipamato.common;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

class UtilTestConfigurationTest {

    @Test
    void dateTimeService() {
        UtilTestConfiguration uc = new UtilTestConfiguration();
        assertThat(uc.dateTimeService()).isInstanceOf(FrozenDateTimeService.class);
    }
}