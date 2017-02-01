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
    }

    protected void doUpdate() {
        doUpdate(getModelObject());
    }

    private Long getNullSafeId() {
        return getModelObject().getId() != null ? getModelObject().getId().longValue() : 0l;
    }

    @Override
    protected Form<Paper> getForm() {
        return contentPanel.getForm();
    }

    protected void doUpdate(Paper paper) {
        try {
            Paper persisted = service.saveOrUpdate(paper);
            if (persisted != null) {
                setModelObject(persisted);
            } else {
                error(new StringResourceModel("save.error.hint", this, null).setParameters(getNullSafeId()).getString());
            }
        } catch (Exception ex) {
            error(new StringResourceModel("save.error.hint", this, null).setParameters(getNullSafeId()).getString());
        }
    }

}
