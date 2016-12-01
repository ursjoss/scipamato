package ch.difty.sipamato.web.panel.search;

import org.apache.wicket.ajax.AjaxRequestTarget;

import ch.difty.sipamato.entity.filter.SearchTerm;
import ch.difty.sipamato.web.event.WicketEvent;

/**
 * The event indicates that there were changes in the list of {@link SearchTerm}s deriving from the {@link SearchTermPanel}
 * that will or may affect other child components of the page container holding the {@link SearchTermPanel}.
 *
 * @author u.joss
 */
public class SearchTermModifiedEvent extends WicketEvent {

    public SearchTermModifiedEvent(final AjaxRequestTarget target) {
        super(target);
    }

}
