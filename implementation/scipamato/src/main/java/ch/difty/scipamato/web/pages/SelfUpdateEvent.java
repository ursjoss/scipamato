package ch.difty.scipamato.web.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;

import ch.difty.scipamato.web.event.WicketEvent;

/**
 * The event indicates that the page has been updated based on the self updating behavior.
 *
 * @author u.joss
 */
public class SelfUpdateEvent extends WicketEvent {

    public SelfUpdateEvent(AjaxRequestTarget target) {
        super(target);
    }

}
