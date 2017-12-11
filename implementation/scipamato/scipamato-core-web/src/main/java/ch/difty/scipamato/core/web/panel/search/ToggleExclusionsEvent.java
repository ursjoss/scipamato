package ch.difty.scipamato.core.web.panel.search;

import org.apache.wicket.ajax.AjaxRequestTarget;

import ch.difty.scipamato.common.web.event.WicketEvent;

/**
 * Announces a changed exclusion setting
 *
 * @author u.joss
 */
public class ToggleExclusionsEvent extends WicketEvent {

    public ToggleExclusionsEvent(final AjaxRequestTarget target) {
        super(target);
    }

}
