package ch.difty.scipamato.common;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

class UtilConfigurationTest {

    @Test
    void dateTimeService() {
        UtilConfiguration uc = new UtilConfiguration();
        assertThat(uc.dateTimeService()).isInstanceOf(CurrentDateTimeService.class);
    }
}