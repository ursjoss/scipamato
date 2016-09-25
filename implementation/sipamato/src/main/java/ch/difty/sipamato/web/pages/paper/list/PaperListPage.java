package ch.difty.sipamato.web.pages.paper.list;

import java.util.Optional;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.web.pages.BasePage;

@MountPath("list")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperListPage extends BasePage {

    private static final long serialVersionUID = 1L;

    private IModel<PaperFilter> filterModel;

    private Form<?> form;

    public PaperListPage(PageParameters parameters) {
        super(parameters);
    }

    protected void onInitialize() {
        super.onInitialize();

        initFilterModel();

        makeAndAddSearchForm("searchForm");
        makeAndAddPaperList("paperlist");
    }

    private void initFilterModel() {
        PaperFilter pf = new PaperFilter();
        filterModel = Model.of(pf);
    }

    private void makeAndAddSearchForm(String id) {
        form = new Form<PaperFilter>(id, CompoundPropertyModel.of(filterModel)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                info("submit called with searchMask: " + getModelObject().getSearchMask());
            }
        };
        add(form);

        addFieldAndLabel(new TextField<String>("searchField", PropertyModel.of(filterModel, PaperFilter.SEARCH_MASK)));
    }

    private void addFieldAndLabel(FormComponent<?> field) {
        addFieldAndLabel(form, field, Optional.empty());
    }

    private void makeAndAddPaperList(String id) {

    }
}
