package ch.difty.scipamato.common;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfiguration {

    @NotNull
    @Bean
    public DateTimeService dateTimeService() {
        return new CurrentDateTimeService();
    }
}
