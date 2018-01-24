package ch.difty.scipamato.core.web.paper.search;

import org.apache.wicket.ajax.AjaxRequestTarget;

import ch.difty.scipamato.common.web.event.WicketEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * The event indicates that there were changes in the search order deriving from
 * the {@link SearchOrderPanel} that will or may affect other child components
 * of the parent page container holding the {@link SearchOrderPanel}.
 *
 * There are a couple of special options that can be achieved with the event:
 *
 * <ul>
 * <li>if the excluded id is set, the sink will be requested to handle that id
 * an treat it as an excluded id.</li>
 * <li>if the newSearchOrderRequestd flag is set, the sink will need to take
 * care of creating a new SearchOrder and set it as the model.</li>
 * </ul>
 *
 * @author u.joss
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class SearchOrderChangeEvent extends WicketEvent {

    private Long    excludedId;
    private Long    droppedConditionId;
    private boolean newSearchOrderRequested;

    public SearchOrderChangeEvent(final AjaxRequestTarget target) {
        super(target);
    }

    public SearchOrderChangeEvent withExcludedPaperId(Long excludedId) {
        this.excludedId = excludedId;
        this.droppedConditionId = null;
        this.newSearchOrderRequested = false;
        return this;
    }

    public SearchOrderChangeEvent withDroppedConditionId(Long id) {
        this.droppedConditionId = id;
        this.excludedId = null;
        this.newSearchOrderRequested = false;
        return this;
    }

    public SearchOrderChangeEvent requestingNewSearchOrder() {
        newSearchOrderRequested = true;
        droppedConditionId = null;
        excludedId = null;
        return this;
    }

}
