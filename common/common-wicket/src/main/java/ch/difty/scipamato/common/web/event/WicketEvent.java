package ch.difty.scipamato.common.web.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.wicket.ajax.AjaxRequestTarget;

@Data
@AllArgsConstructor
public abstract class WicketEvent {
    private AjaxRequestTarget target;
}
