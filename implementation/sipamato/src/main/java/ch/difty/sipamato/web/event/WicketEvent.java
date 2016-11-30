package ch.difty.sipamato.web.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

public abstract class WicketEvent {

    private AjaxRequestTarget target;

    public WicketEvent(final AjaxRequestTarget target) {
        this.target = target;
    }

    public AjaxRequestTarget getTarget() {
        return target;
    }

    public void setTarget(final AjaxRequestTarget target) {
        this.target = target;
    }
}
