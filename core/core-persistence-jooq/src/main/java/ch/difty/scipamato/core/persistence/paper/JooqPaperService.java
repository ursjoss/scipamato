package ch.difty.scipamato.core.persistence.paper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperAttachment;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.*;
import ch.difty.scipamato.core.persistence.newsletter.NewsletterRepository;
import ch.difty.scipamato.core.pubmed.PubmedArticleFacade;

/**
 * jOOQ specific implementation of the {@link PaperService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqPaperService extends JooqEntityService<Long, Paper, PaperFilter, PaperRepository> implements PaperService {

    private final NewsletterRepository newsletterRepo;

    protected JooqPaperService(@NotNull final PaperRepository repo, @NotNull final NewsletterRepository newsletterRepo,
        @NotNull final UserRepository userRepo) {
        super(repo, userRepo);
        this.newsletterRepo = newsletterRepo;
    }

    @NotNull
    @Override
    public List<Paper> findBySearchOrder(@NotNull final SearchOrder searchOrder, @NotNull final String languageCode) {
        return getRepository().findBySearchOrder(searchOrder, languageCode);
    }

    @NotNull
    @Override
    public List<Paper> findPageBySearchOrder(@NotNull final SearchOrder searchOrder, @NotNull final PaginationContext paginationContext,
        @NotNull final String languageCode) {
        return getRepository().findPageBySearchOrder(searchOrder, paginationContext, languageCode);
    }

    @Override
    public int countBySearchOrder(@NotNull final SearchOrder searchOrder) {
        return getRepository().countBySearchOrder(searchOrder);
    }

    @NotNull
    @Override
    @Transactional
    public ServiceResult dumpPubmedArticlesToDb(@NotNull final List<PubmedArticleFacade> articles, final long minimumNumber) {
        final ServiceResult sr = new DefaultServiceResult();
        final List<Integer> pmIdCandidates = articles
            .stream()
            .map(PubmedArticleFacade::getPmId)
            .filter(Objects::nonNull)
            .map(Integer::valueOf)
            .collect(Collectors.toList());
        if (!pmIdCandidates.isEmpty()) {
            final List<String> existingPmIds = getRepository()
                .findExistingPmIdsOutOf(pmIdCandidates)
                .stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
            final List<Paper> savedPapers = articles
                .stream()
                .filter(a -> a.getPmId() != null && !existingPmIds.contains(a.getPmId()))
                .map((final PubmedArticleFacade a) -> this.savePubmedArticle(a, minimumNumber))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            fillServiceResultFrom(savedPapers, existingPmIds, sr);
        }
        return sr;
    }

    private Paper savePubmedArticle(@NotNull final PubmedArticleFacade article, final long minimumNumber) {
        final Paper p = new Paper();
        p.setPmId(Integer.valueOf(Objects.requireNonNull(article.getPmId())));
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

    private void fillServiceResultFrom(@NotNull final List<Paper> newPapers, @NotNull final List<String> existingPmIds,
        @NotNull final ServiceResult sr) {
        existingPmIds
            .stream()
            .map(pmId -> "PMID " + pmId)
            .forEach(sr::addWarnMessage);
        newPapers
            .stream()
            .map(p -> "PMID " + p.getPmId() + " (id " + p.getId() + ")")
            .forEach(sr::addInfoMessage);
    }

    @NotNull
    @Override
    public Optional<Paper> findByNumber(@NotNull final Long number, @NotNull final String languageCode) {
        final List<Paper> papers = getRepository().findByNumbers(Collections.singletonList(number), languageCode);
        if (!papers.isEmpty()) {
            final Paper paper = papers.getFirst();

            enrichAuditNamesOf(paper);
            return Optional.ofNullable(paper);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public long findLowestFreeNumberStartingFrom(final long minimumPaperNumberToBeRecycled) {
        return getRepository().findLowestFreeNumberStartingFrom(minimumPaperNumberToBeRecycled);
    }

    @NotNull
    @Override
    public List<Long> findPageOfIdsBySearchOrder(@NotNull final SearchOrder searchOrder, @NotNull final PaginationContext paginationContext) {
        return getRepository().findPageOfIdsBySearchOrder(searchOrder, paginationContext);
    }

    @Transactional
    @Override
    public void excludeFromSearchOrder(final long searchOrderId, final long paperId) {
        getRepository().excludePaperFromSearchOrderResults(searchOrderId, paperId);
    }

    @Transactional
    @Override
    public void reincludeIntoSearchOrder(final long searchOrderId, final long paperId) {
        getRepository().reincludePaperIntoSearchOrderResults(searchOrderId, paperId);
    }

    @Nullable
    @Transactional
    @Override
    public Paper saveAttachment(@NotNull final PaperAttachment paperAttachment) {
        return getRepository().saveAttachment(paperAttachment);
    }

    @Nullable
    @Override
    public PaperAttachment loadAttachmentWithContentBy(@NotNull final Integer id) {
        return getRepository().loadAttachmentWithContentBy(id);
    }

    @Nullable
    @Transactional
    @Override
    public Paper deleteAttachment(@NotNull final Integer id) {
        return getRepository().deleteAttachment(id);
    }

    @Transactional
    @Override
    public void deletePapersWithIds(@NotNull final List<Long> ids) {
        getRepository().delete(ids);
    }

    @NotNull
    @Transactional
    @Override
    public Optional<Paper.NewsletterLink> mergePaperIntoWipNewsletter(final long paperId, @Nullable final Integer newsletterTopicId,
        @NotNull final String languageCode) {
        final Optional<Newsletter> nlo = newsletterRepo.getNewsletterInStatusWorkInProgress();
        return nlo.flatMap(
            newsletter -> newsletterRepo.mergePaperIntoNewsletter(Objects.requireNonNull(newsletter.getId()), paperId, newsletterTopicId,
                languageCode));
    }

    @Transactional
    @Override
    public int removePaperFromNewsletter(final int newsletterId, final long paperId) {
        return newsletterRepo.removePaperFromNewsletter(newsletterId, paperId);
    }

    @NotNull
    @Override
    public Optional<String> hasDuplicateFieldNextToCurrent(@NotNull final String fieldName, @Nullable final Object fieldValue,
        @NotNull final Long idOfCurrentPaper) {
        if (fieldValue == null)
            return Optional.empty();
        return switch (fieldName) {
            case "doi" -> getRepository().isDoiAlreadyAssigned((String) fieldValue, idOfCurrentPaper);
            case "pmId" -> getRepository().isPmIdAlreadyAssigned((Integer) fieldValue, idOfCurrentPaper);
            default -> throw new IllegalArgumentException("Field '" + fieldName + "' is not supported by this validator.");
        };
    }
}
