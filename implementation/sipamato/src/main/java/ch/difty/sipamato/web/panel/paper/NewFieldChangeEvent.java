package ch.difty.sipamato.web.panel.paper;

import org.apache.wicket.ajax.AjaxRequestTarget;

import ch.difty.sipamato.web.event.WicketEvent;

/**
 * The event indicates that one of the new fields has changed that are present multiple times on the PaperPanel
 * and therefore need synchronization.
 *
 * If an id was passed into the event, the receiver may filter out it's need to add itself to the ajax target request.
 *
 * @author u.joss
 */
public class NewFieldChangeEvent extends WicketEvent {

    private String id;

    public NewFieldChangeEvent(AjaxRequestTarget target) {
        super(target);
    }

    public NewFieldChangeEvent withId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

}
