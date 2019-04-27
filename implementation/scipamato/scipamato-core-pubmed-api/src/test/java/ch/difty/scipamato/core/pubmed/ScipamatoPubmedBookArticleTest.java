package ch.difty.scipamato.core.pubmed;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.pubmed.api.*;

class ScipamatoPubmedBookArticleTest {

    private final PubmedBookArticle pubmedBookArticle = new PubmedBookArticle();

    @BeforeEach
    void setUp() {
        BookDocument bookDocument = new BookDocument();
        pubmedBookArticle.setBookDocument(bookDocument);

        PMID pmId = new PMID();
        pmId.setvalue("pmid");
        bookDocument.setPMID(pmId);

        List<AuthorList> authorLists = new ArrayList<>();
        AuthorList authorList = new AuthorList();
        authorList
            .getAuthor()
            .add(makeAuthor("ln1", "i1"));
        authorList
            .getAuthor()
            .add(makeAuthor("ln2", "i2"));
        authorList
            .getAuthor()
            .add(makeAuthor("ln3", "i3"));
        authorLists.add(authorList);
        bookDocument
            .getAuthorList()
            .addAll(authorLists);

        ContributionDate contributionDate = new ContributionDate();
        Year year = new Year();
        year.setvalue("2017");
        contributionDate.setYear(year);
        bookDocument.setContributionDate(contributionDate);

        bookDocument
            .getLocationLabel()
            .add(makeLocationLabel("ll1"));
        bookDocument
            .getLocationLabel()
            .add(makeLocationLabel("ll2"));

        ArticleTitle articleTitle = new ArticleTitle();
        articleTitle.setMixedContent(List.of("title"));
        bookDocument.setArticleTitle(articleTitle);

        ArticleIdList articleIdList = new ArticleIdList();
        ArticleId pmIdArticleId = new ArticleId();
        pmIdArticleId.setvalue("pmid");
        articleIdList
            .getArticleId()
            .add(pmIdArticleId);
        ArticleId doiId = new ArticleId();
        doiId.setIdType("doi");
        doiId.setvalue("DOI");
        articleIdList
            .getArticleId()
            .add(doiId);
        bookDocument.setArticleIdList(articleIdList);

        Abstract abstr = new Abstract();
        AbstractText abstrText = new AbstractText();
        abstrText.setLabel("ABSTRACT");
        abstrText.setMixedContent(List.of("abstract"));
        abstr
            .getAbstractText()
            .add(abstrText);
        bookDocument.setAbstract(abstr);

    }

    private Author makeAuthor(String lastName, String initials) {
        Author author = new Author();
        LastName ln = new LastName();
        ln.setvalue(lastName);
        author
            .getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName()
            .add(ln);
        Initials i = new Initials();
        i.setvalue(initials);
        author
            .getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName()
            .add(i);
        return author;
    }

    private LocationLabel makeLocationLabel(String label) {
        LocationLabel locationLabel = new LocationLabel();
        locationLabel.setvalue(label);
        return locationLabel;
    }

    @Test
    void degenerateConstruction_withNullPubmedBookArticle_throws() {
        assertDegenerateSupplierParameter(() -> new ScipamatoPubmedBookArticle(null), "pubmedBookArticle");
    }

    @Test
    void degenerateConstruction_withNullBookDocument_throws() {
        PubmedBookArticle pubmedBookArticle = new PubmedBookArticle();
        assertThat(pubmedBookArticle.getBookDocument()).isNull();
        assertDegenerateSupplierParameter(() -> new ScipamatoPubmedBookArticle(pubmedBookArticle),
            "pubmedBookArticle.bookDocument");
    }

    @Test
    void canConstructWithMinimalAttributes() {
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
    void canParseAllFieldsOfFullyEquippedPubmedBookArticle() {
        PubmedArticleFacade pa = new ScipamatoPubmedBookArticle(pubmedBookArticle);

        assertThat(pa.getPmId()).isEqualTo("pmid");
        assertThat(pa.getAuthors()).isEqualTo("ln1 i1, ln2 i2, ln3 i3.");
        assertThat(pa.getFirstAuthor()).isEqualTo("ln1");
        assertThat(pa.getPublicationYear()).isEqualTo("2017");
        assertThat(pa.getLocation()).isEqualTo("ll1 - ll2");
        assertThat(pa.getTitle()).isEqualTo("title");
        assertThat(pa.getDoi()).isEqualTo("DOI");
        assertThat(pa.getOriginalAbstract()).startsWith("ABSTRACT: abstract");

        assertThat(pa.toString()).isEqualTo(
            "AbstractPubmedArticleFacade(pmId=pmid, authors=ln1 i1, ln2 i2, ln3 i3., firstAuthor=ln1, publicationYear=2017, location=ll1 - ll2, title=title, doi=DOI, originalAbstract=ABSTRACT: abstract)");
    }

    @Test
    void canParseAbstractWithoutAbstractLabel() {
        pubmedBookArticle
            .getBookDocument()
            .getAbstract()
            .getAbstractText()
            .get(0)
            .setLabel(null);
        PubmedArticleFacade pa = new ScipamatoPubmedBookArticle(pubmedBookArticle);
        assertThat(pa.getOriginalAbstract()).startsWith("abstract");
    }

    @Test
    void withArticleIdListNull_leavesDoiNull() {
        pubmedBookArticle
            .getBookDocument()
            .setArticleIdList(null);

        PubmedArticleFacade pa = new ScipamatoPubmedBookArticle(pubmedBookArticle);

        assertThat(pa.getDoi()).isNull();
    }

    @Test
    void withAuthorsNull_leavesAuthorAndFirstAuthorNull() {
        pubmedBookArticle
            .getBookDocument()
            .getAuthorList()
            .clear();

        PubmedArticleFacade pa = new ScipamatoPubmedBookArticle(pubmedBookArticle);

        assertThat(pa.getAuthors()).isNull();
        assertThat(pa.getFirstAuthor()).isNull();
    }

    @Test
    void validConstructionUsingOf() {
        assertThat(PubmedArticleFacade.newPubmedArticleFrom(pubmedBookArticle)).isNotNull();
    }

    @Test
    void invalidConstructionUsingOfWithForeignObject() {
        try {
            PubmedArticleFacade.newPubmedArticleFrom(1);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot instantiate ScipamatoArticle from provided object 1");
        }
    }

    @Test
    void equals() {
        EqualsVerifier
            .forClass(ScipamatoPubmedBookArticle.class)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

}
