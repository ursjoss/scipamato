package ch.difty.scipamato.common;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.Test;

public class UtilTestConfigurationTest {

    @Test
    public void dateTimeService() {
        UtilTestConfiguration uc = new UtilTestConfiguration();
        assertThat(uc.dateTimeService()).isInstanceOf(FrozenDateTimeService.class);
    }
}