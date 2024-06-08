package ch.difty.scipamato.core.pubmed;

import static ch.difty.scipamato.core.pubmed.ExtensionsKt.AHEAD_OF_PRINT_TAG;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import ch.difty.scipamato.core.pubmed.api.*;

/**
 * Derives from {@link AbstractPubmedArticleFacade} wrapping an instance of
 * {@link PubmedArticle}.
 *
 * @author u.joss
 */
@SuppressWarnings("SpellCheckingInspection")
class ScipamatoPubmedArticle extends AbstractPubmedArticleFacade {

    private static final String PII = "pii";

    ScipamatoPubmedArticle(@NotNull final PubmedArticle pubmedArticle) {
        final MedlineCitation medlineCitation = pubmedArticle.getMedlineCitation();
        Objects.requireNonNull(medlineCitation);
        final Article article = medlineCitation.getArticle();
        Objects.requireNonNull(article);
        final Journal journal = article.getJournal();
        Objects.requireNonNull(journal);
        final PMID pmid = medlineCitation.getPMID();
        Objects.requireNonNull(pmid);

        setPmId(pmid.getvalue());
        final AuthorList authorList = article.getAuthorList();
        if (authorList != null) {
            setAuthors(GreekLetterTranslator.INSTANCE.replaceGreekLetters(getAuthorsFrom(authorList)));
            setFirstAuthor(GreekLetterTranslator.INSTANCE.replaceGreekLetters(getFirstAuthorFrom(authorList)));
        }
        final boolean isAheadOfPrint = "aheadofprint".equals(pubmedArticle.getPubmedData() != null ?
            pubmedArticle
                .getPubmedData()
                .getPublicationStatus() :
            "");
        setPublicationYear(getPublicationYearFrom(journal));
        setDoi(getDoiFrom(pubmedArticle));
        setLocation(makeLocationFrom(medlineCitation.getMedlineJournalInfo(), journal.getJournalIssue(), article.getPaginationOrELocationID(),
            isAheadOfPrint));

        final ArticleTitle articleTitle = article.getArticleTitle();
        Objects.requireNonNull(articleTitle);
        setTitle(articleTitle.getvalue());
        setOriginalAbstract(getAbstractFrom(article.getAbstract()));
    }

    /**
     * Get the year from {@link Year} - otherwise from {@link MedlineDate}
     */
    private String getPublicationYearFrom(@NotNull final Journal journal) {
        final JournalIssue journalIssue = journal.getJournalIssue();
        Objects.requireNonNull(journalIssue);
        final PubDate pubDate = journalIssue.getPubDate();
        Objects.requireNonNull(pubDate);
        final List<java.lang.Object> datishObjects = pubDate.getYearOrMonthOrDayOrSeasonOrMedlineDate();
        return datishObjects
            .stream()
            .filter(Year.class::isInstance)
            .map(Year.class::cast)
            .map(Year::getvalue)
            .findFirst()
            .orElseGet(() -> datishObjects
                .stream()
                .filter(MedlineDate.class::isInstance)
                .map(MedlineDate.class::cast)
                .map(MedlineDate::getvalue)
                .map(mld -> mld.substring(0, 4))
                .findFirst()
                .orElse("0"));
    }

    private String makeLocationFrom(@NotNull final MedlineJournalInfo medlineJournalInfo, @NotNull final JournalIssue journalIssue,
        @NotNull final List<java.lang.Object> paginationElocation, final boolean aheadOfPrint) {
        final StringBuilder sb = new StringBuilder();
        appendMedlineTa(medlineJournalInfo, sb);
        appendPublicationYearOrDate(journalIssue, aheadOfPrint, sb);
        appendVolume(journalIssue, sb);
        appendIssue(journalIssue, sb);
        appendPagination(paginationElocation, sb);
        if (aheadOfPrint) {
            appendAdditionalInfoForAheadOfPrint(sb);
        }
        return sb.toString();
    }

    private void appendMedlineTa(final MedlineJournalInfo medlineJournalInfo, final StringBuilder sb) {
        sb.append(medlineJournalInfo.getMedlineTA());
        sb.append(". ");
    }

    private void appendPublicationYearOrDate(@NotNull final JournalIssue journalIssue, final boolean aheadOfPrint, @NotNull final StringBuilder sb) {
        if (!aheadOfPrint) {
            sb.append(getPublicationYear());
            sb.append(";");
        } else {
            sb.append(getAheadOfPrintDateFromArticleDate(journalIssue));
            sb.append(".");
        }
    }

    private void appendVolume(@NotNull final JournalIssue journalIssue, @NotNull final StringBuilder sb) {
        final String volume = journalIssue.getVolume();
        if (StringUtils.hasLength(volume)) {
            sb.append(" ");
            sb.append(volume);
        }
    }

    private void appendIssue(@NotNull final JournalIssue journalIssue, @NotNull final StringBuilder sb) {
        final String issue = journalIssue.getIssue();
        if (StringUtils.hasLength(issue)) {
            sb.append(" (");
            sb.append(issue);
            sb.append(")");
        }
    }

    private void appendPagination(@NotNull final List<java.lang.Object> paginationElocation, @NotNull final StringBuilder sb) {
        if (!CollectionUtils.isEmpty(paginationElocation)) {
            final String pages = paginationElocation
                .stream()
                .filter(Pagination.class::isInstance)
                .flatMap(p -> ((Pagination) p)
                    .getStartPageOrEndPageOrMedlinePgn()
                    .stream())
                .filter(MedlinePgn.class::isInstance)
                .map(mlp -> complementPageRange(((MedlinePgn) mlp).getvalue()))
                .map(range -> ": " + range)
                .findFirst()
                .orElseGet(() -> paginationElocation
                    .stream()
                    .filter(ELocationID.class::isInstance)
                    .map(ELocationID.class::cast)
                    .filter(eli -> PII.equals(eli.getEIdType()))
                    .map(eli -> ". " + eli.getEIdType() + ": " + eli.getvalue())
                    .findFirst()
                    .orElse(null));
            if (pages != null) {
                sb.append(pages);
                sb.append(".");
            }
        }
    }

    private void appendAdditionalInfoForAheadOfPrint(@NotNull final StringBuilder sb) {
        sb.append(" doi: ");
        sb.append(getDoi());
        sb.append(". ");
        sb.append(AHEAD_OF_PRINT_TAG);
    }

    /**
     * For aheadOfPrint papers: Concatenate the Date as {@code Year Month Day} from e.g. the ArticleDate
     */
    private String getAheadOfPrintDateFromArticleDate(@NotNull final JournalIssue journalIssue) {
        final PubDate pubDate = journalIssue.getPubDate();
        Objects.requireNonNull(pubDate);
        final List<java.lang.Object> datishObjects = pubDate.getYearOrMonthOrDayOrSeasonOrMedlineDate();
        final StringBuilder sb = new StringBuilder();
        for (final java.lang.Object o : datishObjects)
            handleDatishObject(sb, o);
        return sb.toString();
    }

    // package-private for testing
    void handleDatishObject(@NotNull final StringBuilder sb, @NotNull final java.lang.Object o) {
        switch (o) {
        case final Year year -> {
            sb.append(year.getvalue());
            sb.append(" ");
        }
        case final Month month -> {
            sb.append(month.getvalue());
            sb.append(" ");
        }
        case final Day day -> sb.append(day.getvalue());
        default -> {
            // no-op
        }
        }
    }

    /**
     * Completes abbreviated forms of page ranges, where the end of the range only
     * lists the differing page numbers.
     * <p>
     * E.g. from "1145-9" to "1145-1149"
     */
    private String complementPageRange(@NotNull final String pages) {
        final int psPos = pages.indexOf(PAGE_SEPARATOR);
        if (psPos > -1) {
            final String first = pages.substring(0, psPos);
            final String last = pages.substring(psPos + 1);
            final StringBuilder sb = new StringBuilder();
            sb.append(first);
            sb.append(PAGE_SEPARATOR);
            if (first.length() > last.length()) {
                sb.append(first, 0, first.length() - last.length());
                sb.append(last);
            } else {
                sb.append(last);
            }
            return sb.toString();
        } else {
            return pages;
        }
    }

    private String getDoiFrom(@NotNull final PubmedArticle pubmedArticle) {
        String doi = null;
        if (pubmedArticle.getPubmedData() != null) {
            doi = getDoiFromArticleIdList(pubmedArticle
                .getPubmedData()
                .getArticleIdList());
        }
        if (doi == null) {
            final Article article = pubmedArticle
                .getMedlineCitation()
                .getArticle();
            doi = article
                .getPaginationOrELocationID()
                .stream()
                .filter(ELocationID.class::isInstance)
                .map(l -> ((ELocationID) l).getvalue())
                .findFirst()
                .orElse(null);
        }
        return doi;
    }
}
