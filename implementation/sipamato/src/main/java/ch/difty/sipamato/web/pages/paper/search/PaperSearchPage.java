package ch.difty.sipamato.web.pages.paper.search;

import java.util.Map;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.web.pages.BasePage;

@MountPath("search")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperSearchPage extends BasePage<Map<Long, PaperSlim>> {

    private static final long serialVersionUID = 1L;

    public PaperSearchPage(PageParameters parameters) {
        super(parameters);
    }

    public PaperSearchPage(IModel<Map<Long, PaperSlim>> paperModel) {
        super(paperModel);
    }

    protected void onInitialize() {
        super.onInitialize();

        queue(new Form<>("form", getModel()));

        queue(new Label("paperCount", (getModelObject() != null ? getModelObject().size() : 0)));
        queueResponsePageButton("addSearch", new PaperSearchCriteriaPage(getModelObject()));
    }

}
