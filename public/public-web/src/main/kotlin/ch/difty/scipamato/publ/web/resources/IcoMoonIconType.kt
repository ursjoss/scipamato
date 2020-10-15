package ch.difty.scipamato.publ.web.resources

import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType

class IcoMoonIconType private constructor(cssClassName: String) : IconType(cssClassName) {

    override fun cssClassName(): String = "icon-$cssClassName"

    companion object {
        private const val serialVersionUID = 1L
        val arrow_right = IcoMoonIconType("arrow-right")
        val link = IcoMoonIconType("link")
    }
}
