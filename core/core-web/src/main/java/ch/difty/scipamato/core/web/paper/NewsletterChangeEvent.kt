package ch.difty.scipamato.core.web.paper

import org.apache.wicket.ajax.AjaxRequestTarget

/**
 * The event indicates that there were changes in the association between newsletters and papers.
 */
data class NewsletterChangeEvent(val target: AjaxRequestTarget)
