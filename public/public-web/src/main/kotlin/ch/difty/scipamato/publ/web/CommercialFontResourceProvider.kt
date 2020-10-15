package ch.difty.scipamato.publ.web

import org.apache.wicket.request.resource.CssResourceReference

/**
 * Commercial fonts are not part of the open-source distribution of SciPaMaTo.
 * Hence SciPaMaTo needs to behave differently based on whether the font is configured.
 *
 * If it is configured but not actually present, the application will fail.
 */
interface CommercialFontResourceProvider {

    /**
     * Indicates whether or not we have a configured commercial font available or not
     */
    val isCommercialFontPresent: Boolean

    /**
     * @return the configured [CssResourceReference] or null if not configured
     */
    val cssResourceReference: CssResourceReference?
}
