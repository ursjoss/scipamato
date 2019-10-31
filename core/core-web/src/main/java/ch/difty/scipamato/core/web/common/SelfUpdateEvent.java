package ch.difty.scipamato.core.web.common;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.web.event.WicketEvent;

/**
 * The event indicates that the page has been updated based on the self updating
 * behavior.
 *
 * @author u.joss
 */
public class SelfUpdateEvent extends WicketEvent {
    SelfUpdateEvent(@NotNull AjaxRequestTarget target) {
        super(target);
    }
}
