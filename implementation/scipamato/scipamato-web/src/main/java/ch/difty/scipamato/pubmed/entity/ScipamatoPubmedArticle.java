package ch.difty.scipamato.pubmed.entity;

import java.util.List;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.pubmed.Article;
import ch.difty.scipamato.pubmed.AuthorList;
import ch.difty.scipamato.pubmed.ELocationID;
import ch.difty.scipamato.pubmed.Journal;
import ch.difty.scipamato.pubmed.JournalIssue;
import ch.difty.scipamato.pubmed.MedlineCitation;
import ch.difty.scipamato.pubmed.MedlineDate;
import ch.difty.scipamato.pubmed.MedlineJournalInfo;
import ch.difty.scipamato.pubmed.MedlinePgn;
import ch.difty.scipamato.pubmed.Pagination;
import ch.difty.scipamato.pubmed.PubDate;
import ch.difty.scipamato.pubmed.PubmedArticle;
import ch.difty.scipamato.pubmed.Year;

/**
 * Derives from {@link PubmedArticleFacade} wrapping an instance of {@link PubmedArticle}.
 * 
 * @author u.joss
 */
public class ScipamatoPubmedArticle extends PubmedArticleFacade {

    private static final String PII = "pii";

    protected ScipamatoPubmedArticle(final PubmedArticle pubmedArticle) {
        AssertAs.notNull(pubmedArticle, "pubmedArticle");
        final MedlineCitation medlineCitation = AssertAs.notNull(pubmedArticle.getMedlineCitation(), "pubmedArticle.medlineCitation");
        final Article article = AssertAs.notNull(medlineCitation.getArticle(), "pubmedArticle.medlineCitation.article");
        final Journal journal = AssertAs.notNull(article.getJournal(), "pubmedArticle.medlineCitation.article.journal");
        final AuthorList authorList = article.getAuthorList();

        setPmId(AssertAs.notNull(medlineCitation.getPMID(), "pubmedArticle.medlineCitation.pmid").getvalue());
        if (authorList != null) {
            setAuthors(getAuthorsFrom(authorList));
            setFirstAuthor(getFirstAuthorFrom(authorList));
        }
        setPublicationYear(getPublicationYearFrom(journal));
        setLocation(makeLocationFrom(medlineCitation.getMedlineJournalInfo(), journal.getJournalIssue(), article.getPaginationOrELocationID()));
        setTitle(AssertAs.notNull(article.getArticleTitle(), "pubmedArticle.medlineCitation.article.articleTitle").getvalue());
        setDoi(getDoiFrom(pubmedArticle));
        setOriginalAbstract(getAbstractFrom(article.getAbstract()));
    }

    /**
     * Get the year from {@link Year} - otherwise from {@link MedlineDate}
     */
    private String getPublicationYearFrom(final Journal journal) {
        final JournalIssue journalIssue = AssertAs.notNull(journal.getJournalIssue(), "pubmedArticle.medlineCitation.article.journal.journalIssue");
        final PubDate pubDate = AssertAs.notNull(journalIssue.getPubDate(), "pubmedArticle.medlineCitation.article.journal.journalIssue.pubDate");
        final List<Object> datishObjects = pubDate.getYearOrMonthOrDayOrSeasonOrMedlineDate();
        return datishObjects.stream().filter(o -> o instanceof Year).map(o -> (Year) o).map(Year::getvalue).findFirst().orElseGet(
                () -> datishObjects.stream().filter(o -> o instanceof MedlineDate).map(o -> (MedlineDate) o).map(MedlineDate::getvalue).map(mld -> mld.substring(0, 4)).findFirst().orElse("0"));
    }

    private String makeLocationFrom(final MedlineJournalInfo medlineJournalInfo, final JournalIssue journalIssue, final List<Object> paginationElocation) {
        final StringBuilder sb = new StringBuilder();
        AssertAs.notNull(medlineJournalInfo, "pubmedArticle.medlineCitation.medlineJournalInfo");
        sb.append(medlineJournalInfo.getMedlineTA()).append(". ");
        sb.append(getPublicationYear()).append(";");
        final String volume = journalIssue.getVolume();
        if (volume != null && !volume.isEmpty()) {
            sb.append(" ").append(volume);
        }
        final String issue = journalIssue.getIssue();
        if (issue != null && !issue.isEmpty()) {
            sb.append(" (").append(issue).append(")");
        }
        if (paginationElocation != null && !paginationElocation.isEmpty()) {
            final String pages = paginationElocation.stream()
                    .filter(pe -> pe instanceof Pagination)
                    .flatMap(p -> ((Pagination) p).getStartPageOrEndPageOrMedlinePgn().stream())
                    .filter(mlp -> mlp instanceof MedlinePgn)
                    .map(mlp -> complementPageRange(((MedlinePgn) mlp).getvalue()))
                    .map(range -> ": " + range)
                    .findFirst()
                    .orElseGet(() -> paginationElocation.stream()
                            .filter(pe -> pe instanceof ELocationID)
                            .map(eli -> (ELocationID) eli)
                            .filter(eli -> PII.equals(eli.getEIdType()))
                            .map(eli -> ". " + eli.getEIdType() + ": " + eli.getvalue())
                            .findFirst()
                            .orElse(null));
            if (pages != null)
                sb.append(pages).append(".");
        }
        return sb.toString();
    }

    /**
     * Completes abbreviated forms of page ranges, where the end of the range only lists the differing page numbers.
     * 
     * E.g. from "1145-9" to "1145-1149"
     */
    private String complementPageRange(final String pages) {
        final int psPos = pages.indexOf(PAGE_SEPARATOR);
        if (psPos > -1 && psPos < pages.length()) {
            final String first = pages.substring(0, psPos);
            final String last = pages.substring(psPos + 1, pages.length());
            final StringBuilder sb = new StringBuilder();
            sb.append(first).append(PAGE_SEPARATOR);
            if (first.length() > last.length())
                sb.append(first.substring(0, first.length() - last.length())).append(last);
            else
                sb.append(last);
            return sb.toString();
        } else {
            return pages;
        }
    }

    private String getDoiFrom(final PubmedArticle pubmedArticle) {
        String doi = null;
        if (pubmedArticle.getPubmedData() != null) {
            doi = getDoiFromArticleIdList(pubmedArticle.getPubmedData().getArticleIdList());
        }
        if (doi == null) {
            Article article = pubmedArticle.getMedlineCitation().getArticle();
            doi = article.getPaginationOrELocationID().stream().filter(pel -> pel instanceof ELocationID).map(l -> ((ELocationID) l).getvalue()).findFirst().orElse(null);
        }
        return doi;
    }

}
