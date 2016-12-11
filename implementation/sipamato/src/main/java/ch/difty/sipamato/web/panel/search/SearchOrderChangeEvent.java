package ch.difty.sipamato.web.panel.search;

import org.apache.wicket.ajax.AjaxRequestTarget;

import ch.difty.sipamato.web.event.WicketEvent;

/**
 * The event indicates that there were changes in the search order deriving from the {@link SearchOrderPanel}
 * that will or may affect other child components of the parent page container holding the {@link SearchOrderPanel}.
 *
 * It may optionally contain the information about an excluded Id, which will need to be handled by the event sink.
 *
 * @author u.joss
 */
public class SearchOrderChangeEvent extends WicketEvent {

    private final Long excludedId;

    public SearchOrderChangeEvent(final AjaxRequestTarget target) {
        this(target, null);
    }

    public SearchOrderChangeEvent(final AjaxRequestTarget target, final Long excludedId) {
        super(target);
        this.excludedId = excludedId;
    }

    public Long getExcludedId() {
        return excludedId;
    }

}
