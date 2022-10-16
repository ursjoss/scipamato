@file:Suppress("MaxLineLength")

package ch.difty.scipamato.core.config

import ch.difty.scipamato.common.config.AbstractScipamatoProperties
import ch.difty.scipamato.common.config.MavenProperties
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy
import org.springframework.stereotype.Component

/**
 * This bean is used to evaluate all environment properties used in the
 * application in one place and serve those as bean to wherever they are used.
 *
 * @see [https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-injecting-property-values-into-configuration-beans/](https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-injecting-property-values-into-configuration-beans/)
 */
@Component
class ScipamatoCoreProperties(
    scipamatoProperties: ScipamatoProperties,
    mavenProperties: MavenProperties,
) : AbstractScipamatoProperties<ScipamatoProperties>(
    scipamatoProperties,
    mavenProperties
), ApplicationCoreProperties {

    override val authorParserStrategy: AuthorParserStrategy
        get() = scipamatoProperties.authorParserStrategy
    override val risExporterStrategy: RisExporterStrategy
        get() = scipamatoProperties.risExporterStrategy
    override val minimumPaperNumberToBeRecycled: Long
        get() = scipamatoProperties.paperNumberMinimumToBeRecycled.toLong()
    override val pubmedApiKey: String?
        get() = scipamatoProperties.pubmedApiKey

    companion object {
        private const val serialVersionUID = 1L
    }
}
