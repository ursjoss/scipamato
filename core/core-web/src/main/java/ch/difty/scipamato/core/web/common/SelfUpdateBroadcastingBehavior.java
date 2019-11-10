package ch.difty.scipamato.core.web.common;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.jetbrains.annotations.NotNull;

public class SelfUpdateBroadcastingBehavior extends AjaxFormValidatingBehavior {
    private static final long serialVersionUID = 1L;

    private final Page page;

    public SelfUpdateBroadcastingBehavior(@NotNull final Page page) {
        super("change");
        this.page = page;
    }

    @Override
    protected void onAfterSubmit(@NotNull AjaxRequestTarget target) {
        super.onAfterSubmit(target);
        page.send(page, Broadcast.BREADTH, new SelfUpdateEvent(target));
    }
}