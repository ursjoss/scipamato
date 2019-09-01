package ch.difty.scipamato.core.pubmed;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.Object;
import java.util.List;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.pubmed.api.*;

class ScipamatoPubmedArticleTest {

    private final PubmedArticle pubmedArticle = makeMinimalValidPubmedArticle();

    static PubmedArticle makeMinimalValidPubmedArticle() {
        PubmedArticle pa = new PubmedArticle();
        MedlineCitation medlineCitation = new MedlineCitation();
        Article article = new Article();
        Journal journal = new Journal();
        JournalIssue journalIssue = new JournalIssue();
        journalIssue.setPubDate(new PubDate());
        journal.setJournalIssue(journalIssue);
        article.setJournal(journal);
        article.setArticleTitle(new ArticleTitle());
        medlineCitation.setArticle(article);
        medlineCitation.setPMID(new PMID());
        medlineCitation.setMedlineJournalInfo(new MedlineJournalInfo());
        pa.setMedlineCitation(medlineCitation);
        return pa;
    }

    @Test
    void degenerateConstruction_withNullPubmedArticle() {
        assertDegenerateSupplierParameter(() -> new ScipamatoPubmedArticle(null), "pubmedArticle");
    }

    @Test
    void degenerateConstruction_withNullMedlineCitation() {
        pubmedArticle.setMedlineCitation(null);
        assertDegenerateSupplierParameter(() -> new ScipamatoPubmedArticle(pubmedArticle),
            "pubmedArticle.medlineCitation");
    }

    @Test
    void degenerateConstruction_withNullArticle() {
        pubmedArticle
            .getMedlineCitation()
            .setArticle(null);
        assertDegenerateSupplierParameter(() -> new ScipamatoPubmedArticle(pubmedArticle),
            "pubmedArticle.medlineCitation.article");
    }

    @Test
    void degenerateConstruction_withNullJournal() {
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .setJournal(null);
        assertDegenerateSupplierParameter(() -> new ScipamatoPubmedArticle(pubmedArticle),
            "pubmedArticle.medlineCitation.article.journal");
    }

    @Test
    void degenerateConstruction_withNullPmid() {
        pubmedArticle
            .getMedlineCitation()
            .setPMID(null);
        assertDegenerateSupplierParameter(() -> new ScipamatoPubmedArticle(pubmedArticle),
            "pubmedArticle.medlineCitation.pmid");
    }

    @Test
    void degenerateConstruction_withNullJournalIssue() {
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .setJournalIssue(null);
        assertDegenerateSupplierParameter(() -> new ScipamatoPubmedArticle(pubmedArticle),
            "pubmedArticle.medlineCitation.article.journal.journalIssue");
    }

    @Test
    void degenerateConstruction_withNullPubDate() {
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .getJournalIssue()
            .setPubDate(null);
        assertDegenerateSupplierParameter(() -> new ScipamatoPubmedArticle(pubmedArticle),
            "pubmedArticle.medlineCitation.article.journal.journalIssue.pubDate");
    }

    @Test
    void degenerateConstruction_withNullMedlineJournalInfo() {
        pubmedArticle
            .getMedlineCitation()
            .setMedlineJournalInfo(null);
        assertDegenerateSupplierParameter(() -> new ScipamatoPubmedArticle(pubmedArticle),
            "pubmedArticle.medlineCitation.medlineJournalInfo");
    }

    @Test
    void validConstruction() {
        assertThat(new ScipamatoPubmedArticle(pubmedArticle)).isNotNull();
    }

    @Test
    void validConstructionUsingOf() {
        assertThat(PubmedArticleFacade.newPubmedArticleFrom(pubmedArticle)).isNotNull();
    }

    @Test
    void extractingYearFromNeitherYearObjectNorMedlineDate_returnsYear0() {
        assertThat(pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .getJournalIssue()
            .getPubDate()
            .getYearOrMonthOrDayOrSeasonOrMedlineDate()).isEmpty();
        Month month = new Month();
        month.setvalue("2016");
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .getJournalIssue()
            .getPubDate()
            .getYearOrMonthOrDayOrSeasonOrMedlineDate()
            .add(month);
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getPublicationYear()).isEqualTo("0");
    }

    @Test
    void extractYearFromYearObject() {
        Year year = new Year();
        year.setvalue("2016");
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .getJournalIssue()
            .getPubDate()
            .getYearOrMonthOrDayOrSeasonOrMedlineDate()
            .add(year);
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getPublicationYear()).isEqualTo("2016");
    }

    @Test
    void extractYearFromMedlineDate() {
        MedlineDate md = new MedlineDate();
        md.setvalue("2016 Nov-Dec");
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .getJournalIssue()
            .getPubDate()
            .getYearOrMonthOrDayOrSeasonOrMedlineDate()
            .add(md);

        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getPublicationYear()).isEqualTo("2016");
    }

    @Test
    void withNoFurtherAttributes() {
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getAuthors()).isNull();
        assertThat(spa.getFirstAuthor()).isNull();
        assertThat(spa.getLocation()).isEqualTo("null. 0;");
        assertThat(spa.getTitle()).isNull();
        assertThat(spa.getDoi()).isNull();
    }

    @Test
    void authors_withoutCollectiveAuthor() {
        AuthorList authorList = new AuthorList();
        authorList
            .getAuthor()
            .add(makeAuthor("Bond", "James", "J", null, null));
        authorList
            .getAuthor()
            .add(makeAuthor("Kid", "Billy", "B", "Jr", null));
        authorList
            .getAuthor()
            .add(makeAuthor("Joice", "James", "J", null, null));
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .setAuthorList(authorList);
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getAuthors()).isEqualTo("Bond J, Kid B Jr, Joice J.");
        assertThat(spa.getFirstAuthor()).isEqualTo("Bond");
    }

    @Test
    void authors_withCollectiveAuthor() {
        AuthorList authorList = new AuthorList();
        authorList
            .getAuthor()
            .add(makeAuthor("Bond", "James", "J", null, null));
        authorList
            .getAuthor()
            .add(makeAuthor("Kid", "Billy", "B", "Jr", null));
        authorList
            .getAuthor()
            .add(makeAuthor(null, null, null, null, "Joice J"));
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .setAuthorList(authorList);
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getAuthors()).isEqualTo("Bond J, Kid B Jr; Joice J.");
        assertThat(spa.getFirstAuthor()).isEqualTo("Bond");
    }

    @Test
    void noAuthors_withCollectiveAuthor() {
        AuthorList authorList = new AuthorList();
        authorList
            .getAuthor()
            .add(makeAuthor(null, null, null, null, "Joice J"));
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .setAuthorList(authorList);
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getAuthors()).isEqualTo("Joice J.");
        assertThat(spa.getFirstAuthor()).isEqualTo("Joice J");
    }

    @Test
    void noAuthors_noCollectiveAuthor() {
        AuthorList authorList = new AuthorList();
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .setAuthorList(authorList);
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getAuthors()).isEqualTo("");
        assertThat(spa.getFirstAuthor()).isEqualTo("");
    }

    @Test
    void location_withMedlinePagination() {
        pubmedArticle
            .getMedlineCitation()
            .getMedlineJournalInfo()
            .setMedlineTA("Medline TA");
        Year year = new Year();
        year.setvalue("2016");
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .getJournalIssue()
            .getPubDate()
            .getYearOrMonthOrDayOrSeasonOrMedlineDate()
            .add(year);
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .getJournalIssue()
            .setVolume("6");
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .getJournalIssue()
            .setIssue("10");
        Pagination pagination = new Pagination();
        MedlinePgn medlinePgn = new MedlinePgn();
        medlinePgn.setvalue("1145-9");
        pagination
            .getStartPageOrEndPageOrMedlinePgn()
            .add(medlinePgn);
        MedlinePgn medlinePgn2 = new MedlinePgn();
        medlinePgn2.setvalue("3456-3458");
        pagination
            .getStartPageOrEndPageOrMedlinePgn()
            .add(medlinePgn2);
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getPaginationOrELocationID()
            .add(pagination);
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getLocation()).isEqualTo("Medline TA. 2016; 6 (10): 1145-1149.");
    }

    @Test
    void location_withMedlinePaginationWithoutPageRange() {
        pubmedArticle
            .getMedlineCitation()
            .getMedlineJournalInfo()
            .setMedlineTA("Medline TA");
        Year year = new Year();
        year.setvalue("2016");
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .getJournalIssue()
            .getPubDate()
            .getYearOrMonthOrDayOrSeasonOrMedlineDate()
            .add(year);
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .getJournalIssue()
            .setVolume("6");
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .getJournalIssue()
            .setIssue("10");
        Pagination pagination = new Pagination();
        MedlinePgn medlinePgn = new MedlinePgn();
        medlinePgn.setvalue("1145");
        pagination
            .getStartPageOrEndPageOrMedlinePgn()
            .add(medlinePgn);
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getPaginationOrELocationID()
            .add(pagination);
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getLocation()).isEqualTo("Medline TA. 2016; 6 (10): 1145.");
    }

    @Test
    void location_withMedlineElocation() {
        pubmedArticle
            .getMedlineCitation()
            .getMedlineJournalInfo()
            .setMedlineTA("Medline TA");
        Year year = new Year();
        year.setvalue("2016");
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .getJournalIssue()
            .getPubDate()
            .getYearOrMonthOrDayOrSeasonOrMedlineDate()
            .add(year);
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .getJournalIssue()
            .setVolume("6");
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getJournal()
            .getJournalIssue()
            .setIssue("10");
        ELocationID elocationId1 = new ELocationID();
        elocationId1.setEIdType("foo");
        elocationId1.setvalue("bar");
        ELocationID elocationId2 = new ELocationID();
        elocationId2.setEIdType("pii");
        elocationId2.setvalue("baz");
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getPaginationOrELocationID()
            .add(elocationId1);
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getPaginationOrELocationID()
            .add(elocationId2);
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getLocation()).isEqualTo("Medline TA. 2016; 6 (10). pii: baz.");
    }

    private Author makeAuthor(String lastname, String forename, String initials, String suffix, String collectiveName) {
        Author author = new Author();
        if (lastname != null) {
            LastName ln = new LastName();
            ln.setvalue(lastname);
            author
                .getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName()
                .add(ln);
        }
        if (forename != null) {
            ForeName fn = new ForeName();
            fn.setvalue(forename);
            author
                .getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName()
                .add(fn);
        }
        if (initials != null) {
            Initials i = new Initials();
            i.setvalue(initials);
            author
                .getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName()
                .add(i);
        }
        if (suffix != null) {
            Suffix s = new Suffix();
            s.setvalue(suffix);
            author
                .getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName()
                .add(s);
        }
        if (collectiveName != null) {
            CollectiveName cn = new CollectiveName();
            cn.setvalue(collectiveName);
            author
                .getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName()
                .add(cn);
        }
        return author;
    }

    @Test
    void title() {
        ArticleTitle articleTitle = new ArticleTitle();
        articleTitle.setMixedContent(List.of("article title"));
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .setArticleTitle(articleTitle);
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getTitle()).isEqualTo("article title");
    }

    @Test
    void doi_fromArticleIdList() {
        ArticleId articleId1 = new ArticleId();
        articleId1.setIdType("foo");
        articleId1.setvalue("bar");
        ArticleId articleId2 = new ArticleId();
        articleId2.setIdType("doi");
        articleId2.setvalue("10.0000012345");
        ArticleIdList articleIdList = new ArticleIdList();
        articleIdList
            .getArticleId()
            .add(articleId1);
        articleIdList
            .getArticleId()
            .add(articleId2);
        PubmedData pubmedData = new PubmedData();
        pubmedData.setArticleIdList(articleIdList);
        pubmedArticle.setPubmedData(pubmedData);
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getDoi()).isEqualTo("10.0000012345");
    }

    @Test
    void doi_withoutArticleIdList_usesElocIfthere() {
        ELocationID elocationId = new ELocationID();
        elocationId.setvalue("eloc");
        pubmedArticle
            .getMedlineCitation()
            .getArticle()
            .getPaginationOrELocationID()
            .add(elocationId);
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getDoi()).isEqualTo("eloc");
    }

    @Test
    void equals() {
        EqualsVerifier
            .forClass(ScipamatoPubmedArticle.class)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void testingToString() {
        ScipamatoPubmedArticle pa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(pa.toString()).isEqualTo(
            "AbstractPubmedArticleFacade(pmId=null, authors=null, firstAuthor=null, publicationYear=0, location=null. 0;, title=null, doi=null, originalAbstract=null)");
    }

    @Test
    void handleDatishObject_withYear() {
        Year o = new Year();
        o.setvalue("2018");
        assertDatish("2018 ", o);
    }

    @Test
    void handleDatishObject_withMonth() {
        Month o = new Month();
        o.setvalue("12");
        assertDatish("12 ", o);
    }

    @Test
    void handleDatishObject_withDay() {
        Day o = new Day();
        o.setvalue("13");
        assertDatish("13", o);
    }

    @Test
    void handleDatishObject_withUndefinedObject() {
        assertDatish("", "foo");
    }

    private void assertDatish(final String expected, final Object o) {
        final ScipamatoPubmedArticle pa = new ScipamatoPubmedArticle(pubmedArticle);
        final StringBuilder sb = new StringBuilder();
        pa.handleDatishObject(sb, o);
        assertThat(sb.toString()).isEqualTo(expected);
    }
}
