package ch.difty.scipamato.core.web

import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome7IconType
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome7IconTypeBuilder

fun FontAwesome7IconTypeBuilder.FontAwesome7Graphic.fixed(): FontAwesome7IconType =
    FontAwesome7IconTypeBuilder.on(this).fixedWidth().build()
