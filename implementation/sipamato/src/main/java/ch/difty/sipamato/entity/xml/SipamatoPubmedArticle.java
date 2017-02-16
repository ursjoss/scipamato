package ch.difty.sipamato.entity.xml;

import java.util.List;

import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.pubmed.Article;
import ch.difty.sipamato.pubmed.AuthorList;
import ch.difty.sipamato.pubmed.ELocationID;
import ch.difty.sipamato.pubmed.Journal;
import ch.difty.sipamato.pubmed.JournalIssue;
import ch.difty.sipamato.pubmed.MedlineCitation;
import ch.difty.sipamato.pubmed.MedlineJournalInfo;
import ch.difty.sipamato.pubmed.MedlinePgn;
import ch.difty.sipamato.pubmed.Pagination;
import ch.difty.sipamato.pubmed.PubDate;
import ch.difty.sipamato.pubmed.PubmedArticle;
import ch.difty.sipamato.pubmed.Year;

/**
 * Derives from {@link PubmedArticleFacade} wrapping an instance of {@link PubmedArticle}.
 * 
 * @author u.joss
 */
public class SipamatoPubmedArticle extends PubmedArticleFacade {

    protected SipamatoPubmedArticle(final PubmedArticle pubmedArticle) {
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
        setDoi(getDoiFrom(article));
        setOriginalAbstract(getAbstractFrom(article.getAbstract()));
    }

    private String getPublicationYearFrom(final Journal journal) {
        JournalIssue journalIssue = AssertAs.notNull(journal.getJournalIssue(), "pubmedArticle.medlineCitation.article.journal.journalIssue");
        PubDate pubDate = AssertAs.notNull(journalIssue.getPubDate(), "pubmedArticle.medlineCitation.article.journal.journalIssue.pubDate");
        return pubDate.getYearOrMonthOrDayOrSeasonOrMedlineDate().stream().filter(o -> o instanceof Year).map(o -> (Year) o).map(Year::getvalue).findFirst().orElse(null);
    }

    private String makeLocationFrom(final MedlineJournalInfo medlineJournalInfo, final JournalIssue journalIssue, final List<Object> paginationElongation) {
        final StringBuilder sb = new StringBuilder();
        AssertAs.notNull(medlineJournalInfo, "pubmedArticle.medlineCitation.medlineJournalInfo");
        sb.append(medlineJournalInfo.getMedlineTA()).append(". ");
        sb.append(getPublicationYear()).append(";");
        final String volume = journalIssue.getVolume();
        if (volume != null && !volume.isEmpty())
            sb.append(" ").append(volume);
        final String issue = journalIssue.getIssue();
        if (issue != null && !issue.isEmpty())
            sb.append(" (").append(issue).append("):");
        if (paginationElongation != null && !paginationElongation.isEmpty()) {
            final String pages = paginationElongation.stream()
                    .filter(pe -> pe instanceof Pagination)
                    .flatMap(p -> ((Pagination) p).getStartPageOrEndPageOrMedlinePgn().stream())
                    .filter(mlp -> mlp instanceof MedlinePgn)
                    .map(mlp -> ((MedlinePgn) mlp).getvalue())
                    .findFirst()
                    .orElse(null);
            if (pages != null) {
                sb.append(" ").append(complementPageRange(pages)).append(".");
            }
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

    private String getDoiFrom(final Article article) {
        return article.getPaginationOrELocationID().stream().filter(pel -> pel instanceof ELocationID).map(l -> ((ELocationID) l).getvalue()).findFirst().orElse(null);
    }

}
