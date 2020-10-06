package ch.difty.scipamato.core.web.common

import org.apache.wicket.ajax.AjaxRequestTarget

/**
 * The event indicates that the page has been updated based on the self updating behavior.
 */
class SelfUpdateEvent internal constructor(val target: AjaxRequestTarget)
