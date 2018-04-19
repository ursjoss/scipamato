package ch.difty.scipamato.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class UtilConfiguration {

    @Bean
    @Primary
    public DateTimeService dateTimeService() {
        return new FrozenDateTimeService();
    }
}
