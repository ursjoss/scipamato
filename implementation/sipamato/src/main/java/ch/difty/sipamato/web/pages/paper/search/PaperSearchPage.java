package ch.difty.sipamato.web.pages.paper.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperSlimService;
import ch.difty.sipamato.web.pages.BasePage;

@MountPath("search")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperSearchPage extends BasePage<List<PaperSlim>> {

    private static final long serialVersionUID = 1L;

    private final List<Paper> accumulatedSearchCriteria = new ArrayList<>();

    @SpringBean
    private PaperSlimService service;

    public PaperSearchPage(PageParameters parameters) {
        super(parameters);
    }

    public PaperSearchPage(List<Paper> searchCriteria) {
        super(new PageParameters());
        if (searchCriteria != null) {
            accumulatedSearchCriteria.addAll(searchCriteria);
        }
        applySearch();
    }

    private void applySearch() {
        final Map<Long, PaperSlim> results = new HashMap<>();
        for (final Paper p : accumulatedSearchCriteria) {
            for (final PaperSlim ps : service.findByExample(p)) {
                results.put(ps.getId(), ps);
            }
        }
        setDefaultModel(Model.ofList(new ArrayList<PaperSlim>(results.values())));
    }

    protected void onInitialize() {
        super.onInitialize();

        queue(new Form<>("form", getModel()));

        queue(new Label("paperCount", (getModelObject() != null ? getModelObject().size() : 0)));
        queueResponsePageButton("addSearch", new PaperSearchCriteriaPage(accumulatedSearchCriteria));
    }

}
