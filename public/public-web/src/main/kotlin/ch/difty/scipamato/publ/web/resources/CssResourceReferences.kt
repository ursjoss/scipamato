package ch.difty.scipamato.publ.web.resources

import org.apache.wicket.request.resource.CssResourceReference

object MainCssResourceReference : CssResourceReference(MainCssResourceReference::class.java, "css/main.css") {
    fun readResolve(): Any = MainCssResourceReference
    private const val serialVersionUID: Long = 1L
}

/**
 * This resource reference relies on commercial fonts that are not present in the
 * open-source distribution of SciPaMaTo. Do not use this object unless those fonts
 * are actually present in the resource folder (sub-folder `fonts/IcoMoon`).
 */
object IcoMoonCssResourceReference : CssResourceReference(IcoMoonCssResourceReference::class.java, "css/IcoMoon.css") {
    fun readResolve(): Any = IcoMoonCssResourceReference
    private const val serialVersionUID: Long = 1L
}

/**
 * This resource reference relies on commercial fonts that are not present in the
 * open-source distribution of SciPaMaTo. Do not use this object unless those fonts
 * are actually present in the resource folder (sub-folder `fonts/MetaOT`).
 */
object MetaOTCssResourceReference : CssResourceReference(IcoMoonCssResourceReference::class.java, "css/MetaOT.css") {
    fun readResolve(): Any = MetaOTCssResourceReference
    private const val serialVersionUID: Long = 1L
}

/**
 * This resource reference relies on commercial fonts that are not present in the
 * open-source distribution of SciPaMaTo. Do not use this object unless those fonts
 * are actually present in the resource folder (sub-folder `fonts/Simplon`).
 */
object SimplonCssResourceReference : CssResourceReference(IcoMoonCssResourceReference::class.java, "css/Simplon.css") {
    fun readResolve(): Any = SimplonCssResourceReference
    private const val serialVersionUID: Long = 1L
}
