package ch.difty.scipamato.core.web.paper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.web.event.WicketEvent;

/**
 * The event indicates that there were changes in the association between
 * newsletters and papers.
 *
 * @author u.joss
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class NewsletterChangeEvent extends WicketEvent {
    public NewsletterChangeEvent(@NotNull final AjaxRequestTarget target) {
        super(target);
    }
}
