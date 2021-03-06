package ch.difty.scipamato.core.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "db")
data class DbProperties(

    /** Database schema */
    var schema: String = "public",
)
