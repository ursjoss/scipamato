package ch.difty.sipamato.pubmed.entity;

import java.util.List;
import java.util.stream.Collectors;

import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.pubmed.AuthorList;
import ch.difty.sipamato.pubmed.BookDocument;
import ch.difty.sipamato.pubmed.LocationLabel;
import ch.difty.sipamato.pubmed.PubmedBookArticle;

/**
 * Derives from {@link PubmedArticleFacade} wrapping an instance of {@link PubmedBookArticle}.
 * <p>
 * <b>Note:</b> The extraction of the fields implemented so far is purely based upon inspection of the DTD.
 * I'm not sure if the {@linkPubmedBookArticle} is relevant for sipamato at all. If it is, we'll need a real
 * example from pubmed and will need to build an integration test to validate the extraction is correct.
 *
 * @author u.joss
 */
public class SipamatoPubmedBookArticle extends PubmedArticleFacade {

    protected SipamatoPubmedBookArticle(final PubmedBookArticle pubmedBookArticle) {
        AssertAs.notNull(pubmedBookArticle, "pubmedBookArticle");
        final BookDocument bookDocument = AssertAs.notNull(pubmedBookArticle.getBookDocument(), "pubmedBookArticle.bookDocument");
        final List<AuthorList> authorLists = bookDocument.getAuthorList();

        setPmId(bookDocument.getPMID() != null ? bookDocument.getPMID().getvalue() : null);
        if (!authorLists.isEmpty()) {
            AuthorList authorList = authorLists.get(0);
            setAuthors(getAuthorsFrom(authorList));
            setFirstAuthor(getFirstAuthorFrom(authorList));
        }
        setPublicationYear(bookDocument.getContributionDate() != null ? bookDocument.getContributionDate().getYear().getvalue() : null);
        setLocation(getLocationFrom(bookDocument));
        setTitle(bookDocument.getArticleTitle() != null ? bookDocument.getArticleTitle().getvalue() : null);
        setDoi(getDoiFromArticleIdList(bookDocument.getArticleIdList()));
        setOriginalAbstract(getAbstractFrom(bookDocument.getAbstract()));
    }

    private String getLocationFrom(BookDocument bookDocument) {
        return bookDocument.getLocationLabel().stream().map(LocationLabel::getvalue).collect(Collectors.joining(" - "));
    }

}
