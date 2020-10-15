package ch.difty.scipamato.publ.web.resources

import org.apache.wicket.request.resource.CssResourceReference

object MainCssResourceReference : CssResourceReference(MainCssResourceReference::class.java, "css/main.css")

/**
 * This resource reference relies on commercial fonts that are not present in the open-source distribution of SciPaMaTo.
 * Do not use this object unless those fonts are actually present in the resource folder (sub-folder fonts/IcoMoon).
 */
object IcoMoonCssResourceReference : CssResourceReference(IcoMoonCssResourceReference::class.java, "css/IcoMoon.css")

/**
 * This resource reference relies on commercial fonts that are not present in the open-source distribution of SciPaMaTo.
 * Do not call get() on this class unless those fonts are actually present in the resource folder (sub-folder fonts/MetaOT).
 */
object MetaOTCssResourceReference : CssResourceReference(IcoMoonCssResourceReference::class.java, "css/MetaOT.css")

/**
 * This resource reference relies on commercial fonts that are not present in the open-source distribution of SciPaMaTo.
 * Do not call get() on this class unless those fonts are actually present in the resource folder (sub-folder fonts/Simplon).
 */
object SimplonCssResourceReference : CssResourceReference(IcoMoonCssResourceReference::class.java, "css/Simplon.css")
