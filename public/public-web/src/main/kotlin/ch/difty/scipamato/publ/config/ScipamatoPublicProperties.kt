package ch.difty.scipamato.publ.config

import ch.difty.scipamato.common.config.AbstractScipamatoProperties
import ch.difty.scipamato.common.config.MavenProperties
import org.springframework.stereotype.Component

/**
 * This bean is used to evaluate all environment properties used in the application
 * in one place and serve those as bean to wherever they are used.
 *
 * @see [https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-injecting-property-values-into-configuration-beans/](https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-injecting-property-values-into-configuration-beans/)
 */
@Component
class ScipamatoPublicProperties(
    scipamatoProperties: ScipamatoProperties,
    mavenProperties: MavenProperties,
) : AbstractScipamatoProperties<ScipamatoProperties>(
    scipamatoProperties,
    mavenProperties
), ApplicationPublicProperties {

    override val isCommercialFontPresent: Boolean
        get() = scipamatoProperties.commercialFontPresent

    override val isLessUsedOverCss: Boolean
        get() = scipamatoProperties.lessUsedOverCss

    override val isNavbarVisibleByDefault: Boolean
        get() = scipamatoProperties.navbarVisibleByDefault

    override val cmsUrlNewStudyPage: String?
        get() = scipamatoProperties.cmsUrlNewStudyPage

    override val authorsAbbreviatedMaxLength: Int
        get() = scipamatoProperties.authorsAbbreviatedMaxLength

    override val isResponsiveIframeSupportEnabled: Boolean
        get() = scipamatoProperties.responsiveIframeSupportEnabled

    override val managementUserName: String
        get() = scipamatoProperties.managementUserName

    override val managementUserPassword: String?
        get() = scipamatoProperties.managementUserPassword

    override val numberOfPreviousNewslettersInArchive: Int
        get() = scipamatoProperties.numberOfPreviousNewslettersInArchive

    companion object {
        private const val serialVersionUID = 1L
    }
}
