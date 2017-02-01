package ch.difty.sipamato.web.pages;

import java.time.LocalDateTime;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import ch.difty.sipamato.config.ApplicationProperties;
import ch.difty.sipamato.service.Localization;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarText;

/**
 * Abstract page that enables the implementing concrete pages to auto-save the model (if dirty)
 * in an interval that can be defined as a property.
 *
 * @author u.joss
 *
 * @param <T> the type of the entity the implementing pages use as the model.
 */
public abstract class AutoSaveAwarePage<T> extends BasePage<T> {
    private static final long serialVersionUID = 1L;

    private static final Duration KEEPALIVE_SECONDS = Duration.seconds(60);

    @SpringBean
    private ApplicationProperties applicationProperties;

    @SpringBean
    private Localization localization;

    private boolean dirty = false;
    private LocalDateTime lastSaveTimestamp;

    public AutoSaveAwarePage(PageParameters parameters) {
        super(parameters);
    }

    public AutoSaveAwarePage(IModel<T> model) {
        super(model);
    }

    protected Localization getLocalization() {
        return localization;
    }

    /**
     * Made this final in order to enforce #triggerAutoSave to come after form component initialization.
     * Override #implSpecificOnInitialize in the pages inheriting from {@link AutoSaveAwarePage}
     */
    @Override
    protected final void onInitialize() {
        super.onInitialize();

        lastSaveTimestamp = getDateTimeService().getCurrentDateTime();
        implSpecificOnInitialize();

        getNavBar().setOutputMarkupId(true);
        // this must come after having initialized the form components
        triggerAutoSave();
    }

    /**
     * pages extending {@link AutoSaveAwarePage} can override this method to do what they usually did in onInitialize. 
     */
    protected void implSpecificOnInitialize() {
    }

    @Override
    protected void extendNavBar() {
        NavbarText dirtyHint = new NavbarText(getNavBar().newExtraItemId(), new StringResourceModel("menu.dirty.hint", this, null).getString()) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return isDirty();
            }
        };
        dirtyHint.position(Navbar.ComponentPosition.RIGHT);
        dirtyHint.add(AttributeAppender.prepend("style", "color: red; font-weight: bold;"));
        getNavBar().addComponents(dirtyHint);
    }

    /**
     * @return whether the form is dirty (i.e. contains components with modified values or not.
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Sets the dirty flag
     * @param dirty
     */
    public void setDirty() {
        dirty = true;
    }

    public void setClean() {
        dirty = false;
    }

    /**
     * This method must be called in onInitialize() after all fields have been added to the page (e.g. last)
     * 
     * @param form
     */
    protected void triggerAutoSave() {
        if (applicationProperties.isAutoSavingEnabled()) {

            getForm().visitFormComponents((fc, visit) -> {
                fc.setOutputMarkupId(true);
                fc.add(makeSetDirtyOnChangeBehavior());
            });
            getForm().add(makeAutoSaveAjaxTimerBehavior());
            info(new StringResourceModel("autosave.enabled.hint", this, null).setParameters(getEntityName(), applicationProperties.getAutoSaveIntervalInSeconds()).getString());
        }
    }

    /**
     * Override to provide the form
     */
    protected abstract Form<T> getForm();

    /**
     * Override to provide the entity name (will be used in feedback messages)
     */
    protected abstract String getEntityName();

    /**
     * Make a behavior that is part of the auto-save functionality. A change of a component with this behavior
     * <ol>
     * <li> will push the value to the model on the server
     * <li> will set the page dirty flag, which is picked up by the AbstractAjaxTimerBehavior to actually save the changes
     * </ol>
     * @return
     */
    protected OnChangeAjaxBehavior makeSetDirtyOnChangeBehavior() {
        return new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                setDirty();
                target.add(getNavBar());
            }
        };
    };

    /**
     * scheduled behavior to save the modified text of the paper via ajax in a scheduled interval.
     * It only triggers if the form is dirty. 
     */
    private AbstractAjaxTimerBehavior makeAutoSaveAjaxTimerBehavior() {
        return new AbstractAjaxTimerBehavior(Duration.seconds(applicationProperties.getAutoSaveIntervalInSeconds())) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onTimer(AjaxRequestTarget target) {
                if (isDirty() || keepAliveReached()) {
                    if (!manualValidationFails()) {
                        doUpdate(getForm().getModelObject());
                        getForm().modelChanged();
                        setClean();
                        target.add(getNavBar());
                        target.add(getFeedbackPanel());
                        lastSaveTimestamp = getDateTimeService().getCurrentDateTime();
                    } else {
                        errorValidationMessage();
                        target.add(getNavBar());
                        target.add(getFeedbackPanel());
                    }
                }
            }

            private boolean keepAliveReached() {
                return getDateTimeService().getCurrentDateTime().minusSeconds(Double.valueOf(KEEPALIVE_SECONDS.seconds()).intValue()).isAfter(lastSaveTimestamp);
            }
        };
    }

    /**
     * TODO fix validation really use the default form processing mechanism.
     */
    protected boolean manualValidationFails() {
        return false;
    }

    /**
     * Override to pace the page specific validation error
     */
    protected void errorValidationMessage() {
    }

    /**
     * Implement this method do persist the entity of type <literal>T</literal>
     * @param entity
     */
    protected abstract void doUpdate(T entity);

}
