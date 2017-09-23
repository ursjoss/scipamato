package ch.difty.scipamato.pubmed;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.difty.scipamato.NullArgumentException;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class ScipamatoPubmedBookArticleTest {

    private final PubmedBookArticle pubmedBookArticle = new PubmedBookArticle();

    @Before
    public void setUp() {
        BookDocument bookDocument = new BookDocument();
        pubmedBookArticle.setBookDocument(bookDocument);

        PMID pmId = new PMID();
        pmId.setvalue("pmid");
        bookDocument.setPMID(pmId);

        List<AuthorList> authorLists = new ArrayList<>();
        AuthorList authorList = new AuthorList();
        authorList.getAuthor().add(makeAuthor("ln1", "i1"));
        authorList.getAuthor().add(makeAuthor("ln2", "i2"));
        authorList.getAuthor().add(makeAuthor("ln3", "i3"));
        authorLists.add(authorList);
        bookDocument.getAuthorList().addAll(authorLists);

        ContributionDate contributionDate = new ContributionDate();
        Year year = new Year();
        year.setvalue("2017");
        contributionDate.setYear(year);
        bookDocument.setContributionDate(contributionDate);

        bookDocument.getLocationLabel().add(makeLocationLabel("ll1"));
        bookDocument.getLocationLabel().add(makeLocationLabel("ll2"));

        ArticleTitle articleTitle = new ArticleTitle();
        articleTitle.setvalue("title");
        bookDocument.setArticleTitle(articleTitle);

        ArticleIdList articleIdList = new ArticleIdList();
        ArticleId pmIdArticleId = new ArticleId();
        pmIdArticleId.setvalue("pmid");
        articleIdList.getArticleId().add(pmIdArticleId);
        ArticleId doiId = new ArticleId();
        doiId.setIdType("doi");
        doiId.setvalue("DOI");
        articleIdList.getArticleId().add(doiId);
        bookDocument.setArticleIdList(articleIdList);

        Abstract abstr = new Abstract();
        AbstractText abstrText = new AbstractText();
        abstrText.setvalue("abstract");
        abstr.getAbstractText().add(abstrText);
        bookDocument.setAbstract(abstr);

    }

    private Author makeAuthor(String lastName, String initials) {
        Author author = new Author();
        LastName ln = new LastName();
        ln.setvalue(lastName);
        author.getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName().add(ln);
        Initials i = new Initials();
        i.setvalue(initials);
        author.getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName().add(i);
        return author;
    }

    private LocationLabel makeLocationLabel(String label) {
        LocationLabel locationLabel = new LocationLabel();
        locationLabel.setvalue(label);
        return locationLabel;
    }

    @Test
    public void degenerateConstruction_withNullPubmedBookArticle_throws() {
        try {
            new ScipamatoPubmedBookArticle(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedBookArticle must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullBookDocument_throws() {
        PubmedBookArticle pubmedBookArticle = new PubmedBookArticle();
        assertThat(pubmedBookArticle.getBookDocument()).isNull();
        try {
            new ScipamatoPubmedBookArticle(pubmedBookArticle);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedBookArticle.bookDocument must not be null.");
        }
    }

    @Test
    public void canConstructWithMinimalAttributes() {
        PubmedBookArticle pubmedBookArticle = new PubmedBookArticle();
        pubmedBookArticle.setBookDocument(new BookDocument());
        PubmedArticleFacade pa = new ScipamatoPubmedBookArticle(pubmedBookArticle);

        assertThat(pa.getPmId()).isNull();
        assertThat(pa.getAuthors()).isNull();
        assertThat(pa.getFirstAuthor()).isNull();
        assertThat(pa.getPublicationYear()).isNull();
        assertThat(pa.getLocation()).isEmpty();
        assertThat(pa.getTitle()).isNull();
        assertThat(pa.getDoi()).isNull();
        assertThat(pa.getOriginalAbstract()).isNull();
    }

    @Test
    public void canParseAllFieldsOfFullyEquippedPubmedBookArticle() {
        PubmedArticleFacade pa = new ScipamatoPubmedBookArticle(pubmedBookArticle);

        assertThat(pa.getPmId()).isEqualTo("pmid");
        assertThat(pa.getAuthors()).isEqualTo("ln1 i1, ln2 i2, ln3 i3.");
        assertThat(pa.getFirstAuthor()).isEqualTo("ln1");
        assertThat(pa.getPublicationYear()).isEqualTo("2017");
        assertThat(pa.getLocation()).isEqualTo("ll1 - ll2");
        assertThat(pa.getTitle()).isEqualTo("title");
        assertThat(pa.getDoi()).isEqualTo("DOI");
        assertThat(pa.getOriginalAbstract()).startsWith("abstract");

        assertThat(pa.toString())
            .isEqualTo("PubmedArticleFacade(pmId=pmid, authors=ln1 i1, ln2 i2, ln3 i3., firstAuthor=ln1, publicationYear=2017, location=ll1 - ll2, title=title, doi=DOI, originalAbstract=abstract)");
    }

    @Test
    public void withArticleIdListNull_leavesDoiNull() {
        pubmedBookArticle.getBookDocument().setArticleIdList(null);

        PubmedArticleFacade pa = new ScipamatoPubmedBookArticle(pubmedBookArticle);

        assertThat(pa.getDoi()).isNull();
    }

    @Test
    public void withAuthorsNull_leavesAuthorAndFirstAuthorNull() {
        pubmedBookArticle.getBookDocument().getAuthorList().clear();

        PubmedArticleFacade pa = new ScipamatoPubmedBookArticle(pubmedBookArticle);

        assertThat(pa.getAuthors()).isNull();
        assertThat(pa.getFirstAuthor()).isNull();
    }

    @Test
    public void validConstructionUsingOf() {
        assertThat(ScipamatoPubmedArticle.of(pubmedBookArticle)).isNotNull();
    }

    @Test
    public void invalidConstructionUsingOfWithForiegnObject() {
        try {
            ScipamatoPubmedArticle.of(Integer.valueOf(1));
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage("Cannot instantiate ScipamatoArticle from provided object 1");
        }
    }

    @Test
    public void equals() {
        EqualsVerifier.forClass(ScipamatoPubmedBookArticle.class).suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS).verify();
    }

}
