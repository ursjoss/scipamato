package ch.difty.scipamato.publ.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

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
