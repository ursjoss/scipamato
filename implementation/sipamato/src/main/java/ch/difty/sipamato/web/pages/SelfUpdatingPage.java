package ch.difty.sipamato.web.pages;

import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.sipamato.config.ApplicationProperties;
import ch.difty.sipamato.service.Localization;

/**
 * Abstract page that enables the implementing concrete pages to auto-update the model.
 *
 * @author u.joss
 *
 * @param <T> the type of the entity the implementing pages use as the model.
 */
public abstract class SelfUpdatingPage<T> extends BasePage<T> {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private ApplicationProperties applicationProperties;

    @SpringBean
    private Localization localization;

    public SelfUpdatingPage(PageParameters parameters) {
        super(parameters);
    }

    public SelfUpdatingPage(IModel<T> model) {
        super(model);
    }

    protected Localization getLocalization() {
        return localization;
    }

    /**
     * Made this final in order to enforce #addFieldBehavior to come after form component initialization.
     * Override #implSpecificOnInitialize in the pages inheriting from {@link SelfUpdatingPage}
     */
    @Override
    protected final void onInitialize() {
        super.onInitialize();

        implSpecificOnInitialize();

        // this must come after having initialized the form components
        addFieldBehavior();
    }

    /**
     * pages extending {@link SelfUpdatingPage} can override this method to do what they usually did in onInitialize. 
     */
    protected void implSpecificOnInitialize() {
    }

    /**
     * This method must be called in onInitialize() after all fields have been added to the page (e.g. last)
     * 
     * @param form
     */
    protected void addFieldBehavior() {
        getForm().visitFormComponents((fc, visit) -> {
            fc.setOutputMarkupId(true);
            fc.add(new AjaxFormValidatingBehavior("change"));
        });
    }

    /**
     * Override to provide the form
     */
    protected abstract Form<T> getForm();

}
