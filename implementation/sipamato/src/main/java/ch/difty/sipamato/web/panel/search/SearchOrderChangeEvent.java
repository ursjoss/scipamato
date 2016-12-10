package ch.difty.sipamato.web.panel.search;

import org.apache.wicket.ajax.AjaxRequestTarget;

import ch.difty.sipamato.web.event.WicketEvent;

/**
 * The event indicates that there were changes in the search order deriving from the {@link SearchOrderPanel}
 * that will or may affect other child components of the parent page container holding the {@link SearchOrderPanel}.
 *
 * @author u.joss
 */
public class SearchOrderChangeEvent extends WicketEvent {

    public SearchOrderChangeEvent(final AjaxRequestTarget target) {
        super(target);
    }

}
