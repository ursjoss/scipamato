package ch.difty.scipamato.publ.config;

import lombok.Getter;
import lombok.Setter;
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
    private String schema = "public";
}
