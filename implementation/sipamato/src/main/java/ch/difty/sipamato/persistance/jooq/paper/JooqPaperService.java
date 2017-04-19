package ch.difty.sipamato.persistance.jooq.paper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.paging.Pageable;
import ch.difty.sipamato.persistance.jooq.JooqEntityService;
import ch.difty.sipamato.pubmed.entity.PubmedArticleFacade;
import ch.difty.sipamato.service.DefaultServiceResult;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.service.ServiceResult;

/**
 * jOOQ specific implementation of the {@link PaperService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqPaperService extends JooqEntityService<Long, Paper, PaperFilter, PaperRepository> implements PaperService {

    private static final long serialVersionUID = 1L;

    /** {@inheritDocs} */
    @Override
    public List<Paper> findBySearchOrder(final SearchOrder searchOrder) {
        return getRepository().findBySearchOrder(searchOrder);
    }

    /** {@inheritDocs} */
    @Override
    public List<Paper> findPageBySearchOrder(final SearchOrder searchOrder, final Pageable pageable) {
        return getRepository().findPageBySearchOrder(searchOrder, pageable);
    }

    /** {@inheritDocs} */
    @Override
    public int countBySearchOrder(final SearchOrder searchOrder) {
        return getRepository().countBySearchOrder(searchOrder);
    }

    /** {@inheritDocs} */
    @Override
    public ServiceResult dumpPubmedArticlesToDb(final List<PubmedArticleFacade> articles) {
        final ServiceResult sr = new DefaultServiceResult();
        final List<Integer> pmIds = articles.stream().map(PubmedArticleFacade::getPmId).filter(Objects::nonNull).map(Integer::valueOf).collect(Collectors.toList());
        final List<String> existingPmIds = getRepository().findByPmIds(pmIds).stream().map(Paper::getPmId).map(String::valueOf).collect(Collectors.toList());
        final List<Paper> newPapers = articles.stream().filter(a -> a.getPmId() != null && !existingPmIds.contains(a.getPmId())).map(this::savePubmedArticle).collect(Collectors.toList());
        fillServiceResultFrom(existingPmIds, newPapers, sr);
        return sr;
    }

    private Paper savePubmedArticle(final PubmedArticleFacade article) {
        final Paper p = new Paper();
        p.setPmId(Integer.valueOf(article.getPmId()));
        p.setAuthors(article.getAuthors());
        p.setFirstAuthor(article.getFirstAuthor());
        p.setTitle(article.getTitle());
        p.setPublicationYear(Integer.valueOf(article.getPublicationYear()));
        p.setLocation(article.getLocation());
        p.setDoi(article.getDoi());
        p.setOriginalAbstract(article.getOriginalAbstract());
        return getRepository().add(p);
    }

    private void fillServiceResultFrom(final List<String> existingPmIds, final List<Paper> newPapers, final ServiceResult sr) {
        existingPmIds.stream().map(pmId -> "PMID " + pmId).forEach(sr::addWarnMessage);
        newPapers.stream().map(p -> "PMID " + p.getPmId() + " (id " + p.getId() + ")").forEach(sr::addInfoMessage);
    }

}
