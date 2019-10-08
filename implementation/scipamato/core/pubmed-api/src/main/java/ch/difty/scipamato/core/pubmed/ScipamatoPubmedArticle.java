package ch.difty.scipamato.core.pubmed;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import ch.difty.scipamato.common.AssertAs;
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

    ScipamatoPubmedArticle(final PubmedArticle pubmedArticle) {
        AssertAs.INSTANCE.notNull(pubmedArticle, "pubmedArticle");
        final MedlineCitation medlineCitation = AssertAs.INSTANCE.notNull(pubmedArticle.getMedlineCitation(),
            "pubmedArticle.medlineCitation");
        final Article article = AssertAs.INSTANCE.notNull(medlineCitation.getArticle(),
            "pubmedArticle.medlineCitation.article");
        final Journal journal = AssertAs.INSTANCE.notNull(article.getJournal(),
            "pubmedArticle.medlineCitation.article.journal");
        final AuthorList authorList = article.getAuthorList();

        setPmId(AssertAs.INSTANCE
            .notNull(medlineCitation.getPMID(), "pubmedArticle.medlineCitation.pmid")
            .getvalue());
        if (authorList != null) {
            setAuthors(getAuthorsFrom(authorList));
            setFirstAuthor(getFirstAuthorFrom(authorList));
        }
        final boolean isAheadOfPrint = "aheadofprint".equals(pubmedArticle.getPubmedData() != null ?
            pubmedArticle
                .getPubmedData()
                .getPublicationStatus() :
            "");
        setPublicationYear(getPublicationYearFrom(journal));
        setDoi(getDoiFrom(pubmedArticle));
        setLocation(makeLocationFrom(medlineCitation.getMedlineJournalInfo(), journal.getJournalIssue(),
            article.getPaginationOrELocationID(), isAheadOfPrint));
        setTitle(AssertAs.INSTANCE
            .notNull(article.getArticleTitle(), "pubmedArticle.medlineCitation.article.articleTitle")
            .getvalue());
        setOriginalAbstract(getAbstractFrom(article.getAbstract()));
    }

    /**
     * Get the year from {@link Year} - otherwise from {@link MedlineDate}
     */
    private String getPublicationYearFrom(final Journal journal) {
        final JournalIssue journalIssue = AssertAs.INSTANCE.notNull(journal.getJournalIssue(),
            "pubmedArticle.medlineCitation.article.journal.journalIssue");
        final PubDate pubDate = AssertAs.INSTANCE.notNull(journalIssue.getPubDate(),
            "pubmedArticle.medlineCitation.article.journal.journalIssue.pubDate");
        final List<java.lang.Object> datishObjects = pubDate.getYearOrMonthOrDayOrSeasonOrMedlineDate();
        return datishObjects
            .stream()
            .filter(o -> o instanceof Year)
            .map(o -> (Year) o)
            .map(Year::getvalue)
            .findFirst()
            .orElseGet(() -> datishObjects
                .stream()
                .filter(o -> o instanceof MedlineDate)
                .map(o -> (MedlineDate) o)
                .map(MedlineDate::getvalue)
                .map(mld -> mld.substring(0, 4))
                .findFirst()
                .orElse("0"));
    }

    private String makeLocationFrom(final MedlineJournalInfo medlineJournalInfo, final JournalIssue journalIssue,
        final List<java.lang.Object> paginationElocation, boolean aheadOfPrint) {
        AssertAs.INSTANCE.notNull(medlineJournalInfo, "pubmedArticle.medlineCitation.medlineJournalInfo");
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

    private void appendPublicationYearOrDate(final JournalIssue journalIssue, final boolean aheadOfPrint,
        final StringBuilder sb) {
        if (!aheadOfPrint) {
            sb.append(getPublicationYear());
            sb.append(";");
        } else {
            sb.append(getAheadOfPrintDateFromArticleDate(journalIssue));
            sb.append(".");
        }
    }

    private void appendVolume(final JournalIssue journalIssue, final StringBuilder sb) {
        final String volume = journalIssue.getVolume();
        if (!StringUtils.isEmpty(volume)) {
            sb.append(" ");
            sb.append(volume);
        }
    }

    private void appendIssue(final JournalIssue journalIssue, final StringBuilder sb) {
        final String issue = journalIssue.getIssue();
        if (!StringUtils.isEmpty(issue)) {
            sb.append(" (");
            sb.append(issue);
            sb.append(")");
        }
    }

    private void appendPagination(final List<java.lang.Object> paginationElocation, final StringBuilder sb) {
        if (!CollectionUtils.isEmpty(paginationElocation)) {
            final String pages = paginationElocation
                .stream()
                .filter(pe -> pe instanceof Pagination)
                .flatMap(p -> ((Pagination) p)
                    .getStartPageOrEndPageOrMedlinePgn()
                    .stream())
                .filter(mlp -> mlp instanceof MedlinePgn)
                .map(mlp -> complementPageRange(((MedlinePgn) mlp).getvalue()))
                .map(range -> ": " + range)
                .findFirst()
                .orElseGet(() -> paginationElocation
                    .stream()
                    .filter(pe -> pe instanceof ELocationID)
                    .map(eli -> (ELocationID) eli)
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

    private void appendAdditionalInfoForAheadOfPrint(final StringBuilder sb) {
        sb.append(" doi: ");
        sb.append(getDoi());
        sb.append(". [Epub ahead of print]");
    }

    /**
     * For aheadOfPrint papers: Concatenate the Date as {@code Year Month Day} from e.g. the ArticleDate
     */
    private String getAheadOfPrintDateFromArticleDate(final JournalIssue journalIssue) {
        final PubDate pubDate = AssertAs.INSTANCE.notNull(journalIssue.getPubDate(),
            "pubmedArticle.medlineCitation.article.journal.journalIssue.pubDate");
        final List<java.lang.Object> datishObjects = pubDate.getYearOrMonthOrDayOrSeasonOrMedlineDate();
        final StringBuilder sb = new StringBuilder();
        for (final java.lang.Object o : datishObjects)
            handleDatishObject(sb, o);
        return sb.toString();
    }

    // package-private for testing
    void handleDatishObject(final StringBuilder sb, final java.lang.Object o) {
        if (o instanceof Year) {
            sb.append(((Year) o).getvalue());
            sb.append(" ");
        } else if (o instanceof Month) {
            sb.append(((Month) o).getvalue());
            sb.append(" ");
        } else if (o instanceof Day) {
            sb.append(((Day) o).getvalue());
        }
    }

    /**
     * Completes abbreviated forms of page ranges, where the end of the range only
     * lists the differing page numbers.
     * <p>
     * E.g. from "1145-9" to "1145-1149"
     */
    private String complementPageRange(final String pages) {
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

    private String getDoiFrom(final PubmedArticle pubmedArticle) {
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
                .filter(pel -> pel instanceof ELocationID)
                .map(l -> ((ELocationID) l).getvalue())
                .findFirst()
                .orElse(null);
        }
        return doi;
    }

}
