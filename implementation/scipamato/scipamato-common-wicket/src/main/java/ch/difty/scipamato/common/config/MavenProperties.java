package ch.difty.scipamato.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "build")
@Getter
@Setter
public class MavenProperties {

    /**
     * The project version supplied by maven, typically {@code @project.version@}
     */
    private String version;

    /**
     * The build timestamp suppplied by maven, typically {@code @timestamp@}
     */
    private String timestamp;
}
