package ch.difty.sipamato.web.pages.paper.provider;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.persistance.repository.PaperRepository;

public class SortablePaperProvider extends SortableDataProvider<Paper, String> implements IFilterStateLocator<PaperFilter> {

    private static final long serialVersionUID = 1L;

    private PaperFilter filter = new PaperFilter();

    @SpringBean
    private PaperRepository repo;

    public SortablePaperProvider() {
        Injector.get().inject(this);
        setSort(Paper.AUTHORS, SortOrder.ASCENDING);
    }

    // TODO filter in DB.
    // TODO paging in DB
    @Override
    public Iterator<? extends Paper> iterator(long first, long count) {
        List<Paper> papers = repo.findAll();
        return filterPapers(papers).subList((int) first, (int) (first + count)).iterator();
    }

    private List<Paper> filterPapers(List<Paper> papers) {
        String searchMask = filter.getSearchMask();
        if (StringUtils.isEmpty(searchMask)) {
            return papers;
        } else {
            String sm = searchMask.toLowerCase();
            return papers.stream()
                    .filter(p -> p.getAuthors().toLowerCase().contains(sm) || p.getTitle().toLowerCase().contains(sm) || p.getResult().toLowerCase().contains(sm) || p.getMethods().toLowerCase().contains(sm))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public long size() {
        return filterPapers(repo.findAll()).size();
    }

    @Override
    public IModel<Paper> model(Paper object) {
        return new Model<Paper>(object);
    }

    @Override
    public PaperFilter getFilterState() {
        return filter;
    }

    @Override
    public void setFilterState(PaperFilter state) {
        filter = state;
    }

}
