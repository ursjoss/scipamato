package ch.difty.scipamato.core.sync;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TestApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(TestApplication.class)
            .run(args);
    }

}
