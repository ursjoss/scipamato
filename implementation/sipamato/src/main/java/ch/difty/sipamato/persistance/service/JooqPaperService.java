package ch.difty.sipamato.persistance.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.persistance.repository.PaperRepository;
import ch.difty.sipamato.service.PaperService;

/**
 * jOOQ specific implementation of the {@link PaperService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqPaperService implements PaperService {

    private static final long serialVersionUID = 1L;

    private PaperRepository repo;

    @Autowired
    public void setRepository(PaperRepository repo) {
        this.repo = repo;
    }

    // TODO implement paging and finding by filter in Repo and replace this hand-rolled paging/filtering -> move them to JooqService
    private List<Paper> filterPapers(List<Paper> papers, PaperFilter filter) {
        String searchMask = filter != null ? filter.getSearchMask() : null;
        if (StringUtils.isEmpty(searchMask)) {
            return papers;
        } else {
            String sm = searchMask.toLowerCase();
            return papers.stream()
                    .filter(p -> p.getAuthors().toLowerCase().contains(sm) || p.getTitle().toLowerCase().contains(sm) || p.getResult().toLowerCase().contains(sm)
                            || p.getMethods().toLowerCase().contains(sm))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Paper> findByFilter(PaperFilter filter, long first, long count) {
        List<Paper> papers = repo.findAll();
        return filterPapers(papers, filter).subList((int) first, (int) (first + count));
    }

    @Override
    public int countByFilter(PaperFilter filter) {
        return filterPapers(repo.findAll(), filter).size();
    }

}
