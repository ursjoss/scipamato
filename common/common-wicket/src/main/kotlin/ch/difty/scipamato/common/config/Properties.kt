package ch.difty.scipamato.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.io.Serializable

interface ScipamatoBaseProperties : Serializable {
    var brand: String
    var pageTitle: String?
    var defaultLocalization: String
    var pubmedBaseUrl: String
    var cmsUrlSearchPage: String?
    var redirectFromPort: Int?
    var multiSelectBoxActionBoxWithMoreEntriesThan: Int
}

/**
 * Common abstract base class for ScipamatoProperties in both the core and public module.
 *
 * @param [SP] the concrete type of the ScipamatoProperties class (extending [ScipamatoBaseProperties]).
 */
abstract class AbstractScipamatoProperties<SP : ScipamatoBaseProperties> protected constructor(
    val scipamatoProperties: SP,
    val mavenProperties: MavenProperties,
) : ApplicationProperties {

    override val buildVersion: String?
        get() = mavenProperties.version

    override val defaultLocalization: String
        get() = scipamatoProperties.defaultLocalization

    override val brand: String
        get() = scipamatoProperties.brand

    override val titleOrBrand: String
        get() = scipamatoProperties.pageTitle ?: brand

    override val pubmedBaseUrl: String
        get() = scipamatoProperties.pubmedBaseUrl

    override val cmsUrlSearchPage: String?
        get() = scipamatoProperties.cmsUrlSearchPage

    override val redirectFromPort: Int?
        get() = scipamatoProperties.redirectFromPort

    override val multiSelectBoxActionBoxWithMoreEntriesThan: Int
        get() = scipamatoProperties.multiSelectBoxActionBoxWithMoreEntriesThan

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

@Component
@ConfigurationProperties(prefix = "build")
data class MavenProperties(var version: String? = null) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
