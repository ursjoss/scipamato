package ch.difty.scipamato.common.web.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class WicketEvent {
    private AjaxRequestTarget target;
}
