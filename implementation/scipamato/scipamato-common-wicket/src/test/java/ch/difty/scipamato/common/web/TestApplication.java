package ch.difty.scipamato.common.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder().sources(TestApplication.class).run(args);
    }
}
