package ch.difty.scipamato.core.sync;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.FrozenDateTimeService;

@SpringBootApplication
public class TestApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .sources(TestApplication.class)
            .run(args);
    }

    @Bean
    @Primary
    public DateTimeService dateTimeService() {
        return new FrozenDateTimeService();
    }
}
