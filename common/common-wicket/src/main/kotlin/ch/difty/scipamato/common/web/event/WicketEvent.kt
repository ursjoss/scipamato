package ch.difty.scipamato.common.web.event

import org.apache.wicket.ajax.AjaxRequestTarget

data class WicketEvent(val target: AjaxRequestTarget? = null)
