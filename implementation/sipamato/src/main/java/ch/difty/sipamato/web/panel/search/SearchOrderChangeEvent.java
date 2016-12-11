package ch.difty.sipamato.web.panel.search;

import org.apache.wicket.ajax.AjaxRequestTarget;

import ch.difty.sipamato.web.event.WicketEvent;

/**
 * The event indicates that there were changes in the search order deriving from the {@link SearchOrderPanel}
 * that will or may affect other child components of the parent page container holding the {@link SearchOrderPanel}.
 *
 * There are a couple of special options that can be achieved with the event:
 *
 * <ul>
 * <li> if the excluded id is set, the sink will be requested to handle that id an treat it as an excluded id.</li>
 * <li>if the newSearchOrderRequestd flag is set, the sink will need to take care of creating a new SearchOrder and set it as the model.</li>
 * </ul>
 *
 * @author u.joss
 */
public class SearchOrderChangeEvent extends WicketEvent {

    private Long excludedId;
    private boolean newSearchOrderRequested;

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

    public SearchOrderChangeEvent requestNewSearchOrder() {
        newSearchOrderRequested = true;
        excludedId = null;
        return this;
    }

    public boolean isNewSearchOrderRequested() {
        return newSearchOrderRequested;
    }

}
