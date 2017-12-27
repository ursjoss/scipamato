package ch.difty.scipamato.core.web.panel.paper;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextArea;

import ch.difty.scipamato.common.web.event.WicketEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * The event indicates that one of the new fields has changed that are present
 * multiple times on the PaperPanel and therefore need synchronization.
 *
 * @author u.joss
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class NewFieldChangeEvent extends WicketEvent {

    private String id;
    private String markupId;

    public NewFieldChangeEvent(AjaxRequestTarget target) {
        super(target);
    }

    public NewFieldChangeEvent withId(String id) {
        this.id = id;
        return this;
    }

    public NewFieldChangeEvent withMarkupId(String markupId) {
        this.markupId = markupId;
        return this;
    }

    /**
     * A component is added to the target if it
     * <ul>
     * <li>has the same id but a different markupId than the event source</li>
     * <li>has the same id as the event source but the latter did not specify a
     * markupId</li>
     * <li>the event source has not specified an id</li>
     * </ul>
     *
     * @param component
     *            the candidate to be added to the target
     */
    public void considerAddingToTarget(final TextArea<?> component) {
        if (isValidTarget(component.getId(), component.getMarkupId())) {
            getTarget().add(component);
        }
    }

    private boolean isValidTarget(final String id, final String markupId) {
        return this.id == null || (this.id.equals(id) && (this.markupId == null || !this.markupId.equals(markupId)));
    }

}
