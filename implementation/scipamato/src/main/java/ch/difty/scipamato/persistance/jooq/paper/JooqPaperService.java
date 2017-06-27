package ch.difty.scipamato.persistance.jooq.paper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.entity.PaperAttachment;
import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.paging.PaginationContext;
import ch.difty.scipamato.persistance.jooq.JooqEntityService;
import ch.difty.scipamato.pubmed.entity.PubmedArticleFacade;
import ch.difty.scipamato.service.DefaultServiceResult;
import ch.difty.scipamato.service.PaperService;
import ch.difty.scipamato.service.ServiceResult;

/**
 * jOOQ specific implementation of the {@link PaperService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqPaperService extends JooqEntityService<Long, Paper, PaperFilter, PaperRepository> implements PaperService {

    private static final long serialVersionUID = 1L;

    /** {@inheritDoc} */
    @Override
    public List<Paper> findBySearchOrder(final SearchOrder searchOrder) {
        return getRepository().findBySearchOrder(searchOrder);
    }

    /** {@inheritDoc} */
    @Override
    public List<Paper> findPageBySearchOrder(final SearchOrder searchOrder, final PaginationContext paginationContext) {
        return getRepository().findPageBySearchOrder(searchOrder, paginationContext);
    }

    /** {@inheritDoc} */
    @Override
    public int countBySearchOrder(final SearchOrder searchOrder) {
        return getRepository().countBySearchOrder(searchOrder);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public ServiceResult dumpPubmedArticlesToDb(final List<PubmedArticleFacade> articles, final long minimumNumber) {
        final ServiceResult sr = new DefaultServiceResult();
        final List<Integer> pmIdCandidates = articles.stream().map(PubmedArticleFacade::getPmId).filter(Objects::nonNull).map(Integer::valueOf).collect(Collectors.toList());
        if (!pmIdCandidates.isEmpty()) {
            final List<String> existingPmIds = getRepository().findByPmIds(pmIdCandidates).stream().map(Paper::getPmId).map(String::valueOf).collect(Collectors.toList());
            final List<Paper> savedPapers = articles.stream()
                    .filter(a -> a.getPmId() != null && !existingPmIds.contains(a.getPmId()))
                    .map((PubmedArticleFacade a) -> this.savePubmedArticle(a, minimumNumber))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            fillServiceResultFrom(savedPapers, existingPmIds, sr);
        }
        return sr;
    }

    private Paper savePubmedArticle(final PubmedArticleFacade article, final long minimumNumber) {
        final Paper p = new Paper();
        p.setPmId(Integer.valueOf(article.getPmId()));
        p.setAuthors(article.getAuthors());
        p.setFirstAuthor(article.getFirstAuthor());
        p.setTitle(article.getTitle());
        p.setPublicationYear(Integer.valueOf(article.getPublicationYear()));
        p.setLocation(article.getLocation());
        p.setDoi(article.getDoi());
        p.setOriginalAbstract(article.getOriginalAbstract());
        p.setNumber(findLowestFreeNumberStartingFrom(minimumNumber));
        return getRepository().add(p);
    }

    private void fillServiceResultFrom(final List<Paper> newPapers, final List<String> existingPmIds, final ServiceResult sr) {
        existingPmIds.stream().map(pmId -> "PMID " + pmId).forEach(sr::addWarnMessage);
        newPapers.stream().map(p -> "PMID " + p.getPmId() + " (id " + p.getId() + ")").forEach(sr::addInfoMessage);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Paper> findByNumber(Long number) {
        List<Paper> papers = getRepository().findByNumbers(Arrays.asList(number));
        if (!papers.isEmpty()) {
            Paper paper = papers.get(0);

            enrichAuditNamesOf(paper);
            return Optional.ofNullable(paper);
        } else {
            return Optional.empty();
        }
    }

    /** {@inheritDoc} */
    @Override
    public long findLowestFreeNumberStartingFrom(long minimumPaperNumberToBeRecycled) {
        return getRepository().findLowestFreeNumberStartingFrom(minimumPaperNumberToBeRecycled);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public void deleteByIds(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            ids.forEach(id -> getRepository().delete(id));
        }
    }

    @Override
    public List<Long> findPageOfIdsBySearchOrder(SearchOrder searchOrder, PaginationContext paginationContext) {
        return getRepository().findPageOfIdsBySearchOrder(searchOrder, paginationContext);
    }

    @Transactional(readOnly = false)
    @Override
    public void excludeFromSearchOrder(long searchOrderId, long paperId) {
        getRepository().excludePaperFromSearchOrderResults(searchOrderId, paperId);
    }

    @Transactional(readOnly = false)
    @Override
    public void reincludeIntoSearchOrder(long searchOrderId, long paperId) {
        getRepository().reincludePaperIntoSearchOrderResults(searchOrderId, paperId);
    }

    @Transactional(readOnly = false)
    @Override
    public Paper saveAttachment(PaperAttachment paperAttachment) {
        return getRepository().saveAttachment(paperAttachment);
    }

    @Override
    public PaperAttachment loadAttachmentWithContentBy(Integer id) {
        return getRepository().loadAttachmentWithContentBy(id);
    }

    @Transactional(readOnly = false)
    @Override
    public Paper deleteAttachment(Integer id) {
        return getRepository().deleteAttachment(id);
    }

}
