package ch.difty.scipamato.core.web.paper.search;

import org.apache.wicket.ajax.AjaxRequestTarget;

import ch.difty.scipamato.common.web.event.WicketEvent;

/**
 * Announces a changed exclusion setting
 *
 * @author u.joss
 */
class ToggleExclusionsEvent extends WicketEvent {

    ToggleExclusionsEvent(final AjaxRequestTarget target) {
        super(target);
    }

}
