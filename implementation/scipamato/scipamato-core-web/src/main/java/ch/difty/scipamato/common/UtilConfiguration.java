package ch.difty.scipamato.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfiguration {

    @Bean
    public DateTimeService dateTimeService() {
        return new CurrentDateTimeService();
    }
}
