package ch.difty.scipamato.core.web

import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome6IconType
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome6IconTypeBuilder

fun FontAwesome6IconTypeBuilder.FontAwesome6Graphic.fixed(): FontAwesome6IconType =
    FontAwesome6IconTypeBuilder.on(this).fixedWidth().build()
