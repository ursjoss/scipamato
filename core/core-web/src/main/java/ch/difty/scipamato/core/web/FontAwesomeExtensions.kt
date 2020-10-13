package ch.difty.scipamato.core.web

import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconTypeBuilder

fun FontAwesome5IconTypeBuilder.FontAwesome5Graphic.fixed(): FontAwesome5IconType =
    FontAwesome5IconTypeBuilder.on(this).fixedWidth().build()
