package ch.difty.scipamato.core.sync;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = DefaultBatchConfigurer.class)
public class BatchConfiguration {

}
