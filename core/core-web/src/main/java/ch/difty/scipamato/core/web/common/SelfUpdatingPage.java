package ch.difty.scipamato.core.web.common;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.config.ApplicationCoreProperties;

/**
 * Abstract page that enables the implementing concrete pages to auto-update the
 * model.
 * <p>
 * Offers the implementing pages to limit feedback messages to one message only
 * or set it back to showing all feedback messages without filtering. This can
 * be helpful if validations prevent saving a new entity {@code T} which does
 * not yet have all required fields or does not pass validation until more data
 * has been entered. See
 * {@link #tuneDownFeedbackMessages()}/{@link #resetFeedbackMessages()}.
 *
 * @param <T>
 *     the type of the entity the implementing pages use as the model.
 * @author u.joss
 */
@SuppressWarnings("unused")
public abstract class SelfUpdatingPage<T> extends BasePage<T> {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @SpringBean
    private ApplicationCoreProperties applicationProperties;

    protected SelfUpdatingPage(@Nullable final PageParameters parameters) {
        super(parameters);
    }

    protected SelfUpdatingPage(@Nullable final IModel<T> model) {
        super(model);
    }

    @NotNull
    protected ApplicationCoreProperties getApplicationProperties() {
        return applicationProperties;
    }

    /**
     * Made this final in order to enforce #addFieldBehavior to come after form
     * component initialization. Override #implSpecificOnInitialize in the pages
     * inheriting from {@link SelfUpdatingPage}
     */
    @Override
    protected final void onInitialize() {
        super.onInitialize();

        implSpecificOnInitialize();

        activateSelfUpdatingBehavior();
    }

    private void activateSelfUpdatingBehavior() {
        getForm().add(new SelfUpdateBroadcastingBehavior(getPage()));
    }

    /**
     * pages extending {@link SelfUpdatingPage} can override this method to do what
     * they usually did in onInitialize.
     */
    protected void implSpecificOnInitialize() {
    }

    /**
     * Override to provide the form
     *
     * @return Form of type {@code T}
     */
    @NotNull
    protected abstract Form<T> getForm();

    /**
     * Indicates the entity {@code T} has not been persisted. Turns down the number
     * of feedback messages (one message only) in order not to flood the user with
     * too much information.
     */
    protected void tuneDownFeedbackMessages() {
        final NotificationPanel panel = getFeedbackPanel();
        panel.setMaxMessages(1);
    }

    /**
     * Indicates the entity {@code T} has successfully been persisted. Turns back on
     * feedback messages to the max value, so further validation issues can be
     * indicated to the user in full detail.
     */
    protected void resetFeedbackMessages() {
        final NotificationPanel panel = getFeedbackPanel();
        panel.setMaxMessages(Integer.MAX_VALUE);
    }
}
