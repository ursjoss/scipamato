package ch.difty.sipamato.entity.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.pubmed.Abstract;
import ch.difty.sipamato.pubmed.Article;
import ch.difty.sipamato.pubmed.Author;
import ch.difty.sipamato.pubmed.AuthorList;
import ch.difty.sipamato.pubmed.CollectiveName;
import ch.difty.sipamato.pubmed.ELocationID;
import ch.difty.sipamato.pubmed.ForeName;
import ch.difty.sipamato.pubmed.Initials;
import ch.difty.sipamato.pubmed.Journal;
import ch.difty.sipamato.pubmed.JournalIssue;
import ch.difty.sipamato.pubmed.LastName;
import ch.difty.sipamato.pubmed.MedlineCitation;
import ch.difty.sipamato.pubmed.MedlineJournalInfo;
import ch.difty.sipamato.pubmed.MedlinePgn;
import ch.difty.sipamato.pubmed.Pagination;
import ch.difty.sipamato.pubmed.PubmedArticle;
import ch.difty.sipamato.pubmed.Suffix;
import ch.difty.sipamato.pubmed.Year;

/**
 * Derives from {@link PubmedArticleFacade} wrapping an instance of {@link PubmedArticle}.
 * 
 * @author u.joss
 */
public class SipamatoPubmedArticle extends PubmedArticleFacade {

    protected SipamatoPubmedArticle(final PubmedArticle pubmedArticle) {
        AssertAs.notNull(pubmedArticle, "pubmedArticle");
        final MedlineCitation medlineCitation = pubmedArticle.getMedlineCitation();
        final Article article = medlineCitation.getArticle();
        final Journal journal = article.getJournal();
        final AuthorList authorList = article.getAuthorList();

        setPmId(medlineCitation.getPMID().getvalue());
        setAuthors(getAuthorsFrom(authorList));
        setFirstAuthor(getFirstAuthorFrom(authorList));
        setPublicationYear(getPublicationYearFrom(journal));
        setLocation(makeLocationFrom(medlineCitation.getMedlineJournalInfo(), journal.getJournalIssue(), article.getPaginationOrELocationID()));
        setTitle(article.getArticleTitle().getvalue());
        setDoi(getDoiFrom(article));
        setOriginalAbstract(getAbstractFrom(article.getAbstract()));
    }

    private String getAuthorsFrom(final AuthorList authorList) {
        final List<String> names = new ArrayList<>();
        for (final Author author : authorList.getAuthor()) {
            final StringBuilder asb = new StringBuilder();
            for (final Object o : author.getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName()) {
                if (o instanceof ForeName || o instanceof CollectiveName)
                    continue;
                if (asb.length() > 0)
                    asb.append(" ");
                if (o instanceof LastName)
                    asb.append(((LastName) o).getvalue());
                else if (o instanceof Initials)
                    asb.append(((Initials) o).getvalue());
                else if (o instanceof Suffix)
                    asb.append(((Suffix) o).getvalue());
            }
            names.add(asb.toString());
        }
        return names.stream().collect(Collectors.joining(", ", "", "."));
    }

    private String getFirstAuthorFrom(final AuthorList authorList) {
        return authorList.getAuthor()
                .stream()
                .map(Author::getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName)
                .flatMap(n -> n.stream())
                .filter(o -> o instanceof LastName)
                .map(lm -> ((LastName) lm).getvalue())
                .limit(1)
                .findFirst()
                .orElseGet(null);
    }

    private String getPublicationYearFrom(final Journal journal) {
        final JournalIssue journalIssue = journal.getJournalIssue();
        return journalIssue.getPubDate().getYearOrMonthOrDayOrSeasonOrMedlineDate().stream().filter(o -> o instanceof Year).map(o -> (Year) o).map(Year::getvalue).findFirst().orElse(null);
    }

    private String makeLocationFrom(final MedlineJournalInfo medlineJournalInfo, final JournalIssue journalIssue, final List<Object> paginationElongation) {
        final StringBuilder sb = new StringBuilder();
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

    private String getAbstractFrom(final Abstract abstr) {
        if (abstr == null)
            return null;
        return abstr.getAbstractText().stream().map(a -> a.getvalue()).collect(Collectors.joining("\n"));
    }

    private String getDoiFrom(final Article article) {
        return article.getPaginationOrELocationID().stream().filter(pel -> pel instanceof ELocationID).map(l -> ((ELocationID) l).getvalue()).findFirst().orElse(null);
    }

}
