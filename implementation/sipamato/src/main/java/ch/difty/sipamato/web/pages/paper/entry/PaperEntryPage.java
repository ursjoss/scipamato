package ch.difty.sipamato.web.pages.paper.entry;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.auth.Roles;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.pages.SelfUpdatingPage;
import ch.difty.sipamato.web.panel.paper.EditablePaperPanel;

/**
 * Page to add new papers or modify existing papers. The page implements the {@link SelfUpdatingPage},
 * implying that changes in individual fields will be validated and persisted immediately.
 * <p>
 * There is a number of validations in place (backed by JSR 303 validations on the {@link Paper} entity,
 * that might prevent a save. This is useful if an existing paper is modified in a way that fails validation.
 * The feedback panel on the BasePage immediately indicates the problem that prevents the save.
 * <p>
 * However, if you enter a new paper, you would automatically fail validation on several fields, which is
 * indicated to the user once you have entered the content of the first field. We don't want several
 * validation messages popping up with every field change until you have reached the state where persisting
 * the paper is actually possible.
 * <p>
 * We therefore evaluate if the paper has been persisted already (either previously after loading a saved paper -
 * or once all required fields have been successfully entered in the process of setting up a new paper). The
 * evaluation is based on whether the paper does have an ID (which is assigned by the persistence layer upon
 * saving). If the evaluation indicates the paper has not yet been saved, the feedback messages are restricted
 * to one message at a time. If the paper has been persisted, several feedback messages would be displayed to
 * the user if the come up.
 *
 * @author u.joss
 */
@MountPath("entry")
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
public class PaperEntryPage extends SelfUpdatingPage<Paper> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private PaperService service;

    private EditablePaperPanel contentPanel;

    public PaperEntryPage(PageParameters parameters) {
        super(parameters);
        initDefaultModel();
    }

    private void initDefaultModel() {
        setDefaultModel(Model.of(new Paper()));
    }

    public PaperEntryPage(IModel<Paper> paperModel) {
        super(paperModel);
    }

    @Override
    protected void implSpecificOnInitialize() {
        contentPanel = new EditablePaperPanel("contentPanel", getModel()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onFormSubmit() {
                PaperEntryPage.this.doUpdate();
            }
        };
        queue(contentPanel);

        adaptFeedbackMessageCountBasedOnId();
    }

    protected void doUpdate() {
        doUpdate(getModelObject());
    }

    protected void doUpdate(Paper paper) {
        try {
            Paper persisted = service.saveOrUpdate(paper);
            if (persisted != null) {
                setModelObject(persisted);
                resetFeedbackMessages();
            } else {
                error(new StringResourceModel("save.error.hint", this, null).setParameters(getNullSafeId()).getString());
            }
        } catch (Exception ex) {
            error(new StringResourceModel("save.error.hint", this, null).setParameters(getNullSafeId(), ex.getMessage()).getString());
        }
    }

    private long getNullSafeId() {
        return getModelObject().getId() != null ? getModelObject().getId().longValue() : 0l;
    }

    /**
     * If the id is present, the paper has been persisted. Full feedback messages are enabled.
     * If the paper has not yet been persisted, we restrict the feedback messages to only one item
     * at a time, so the user is not flooded with messages while entering a paper that fails several
     * validations anyhow.
     */
    private void adaptFeedbackMessageCountBasedOnId() {
        if (getNullSafeId() == 0) {
            tuneDownFeedbackMessages();
        } else {
            resetFeedbackMessages();
        }
    }

    @Override
    protected Form<Paper> getForm() {
        return contentPanel.getForm();
    }

}
