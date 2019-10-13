package ch.difty.scipamato.core.config;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "db")
@Getter
@Setter
public class DbProperties {

    /**
     * Database schema.
     */
    @NotNull
    private String schema = "public";
}
