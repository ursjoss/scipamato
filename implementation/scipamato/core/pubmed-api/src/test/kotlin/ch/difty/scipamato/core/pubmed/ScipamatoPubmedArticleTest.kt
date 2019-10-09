package ch.difty.scipamato.core.pubmed

import ch.difty.scipamato.core.pubmed.api.*
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class ScipamatoPubmedArticleTest {

    private val pubmedArticle = makeMinimalValidPubmedArticle()

    @Test
    fun extractingYearFromNeitherYearObjectNorMedlineDate_returnsYear0() {
        assertThat(pubmedArticle
            .medlineCitation
            .article
            .journal
            .journalIssue
            .pubDate
            .yearOrMonthOrDayOrSeasonOrMedlineDate).isEmpty()
        val month = Month()
        month.setvalue("2016")
        pubmedArticle
            .medlineCitation
            .article
            .journal
            .journalIssue
            .pubDate
            .yearOrMonthOrDayOrSeasonOrMedlineDate
            .add(month)
        val spa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(spa.publicationYear).isEqualTo("0")
    }

    @Test
    fun extractYearFromYearObject() {
        val year = Year()
        year.setvalue("2016")
        pubmedArticle
            .medlineCitation
            .article
            .journal
            .journalIssue
            .pubDate
            .yearOrMonthOrDayOrSeasonOrMedlineDate
            .add(year)
        val spa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(spa.publicationYear).isEqualTo("2016")
    }

    @Test
    fun extractYearFromMedlineDate() {
        val md = MedlineDate()
        md.setvalue("2016 Nov-Dec")
        pubmedArticle
            .medlineCitation
            .article
            .journal
            .journalIssue
            .pubDate
            .yearOrMonthOrDayOrSeasonOrMedlineDate
            .add(md)

        val spa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(spa.publicationYear).isEqualTo("2016")
    }

    @Test
    fun withNoFurtherAttributes() {
        val spa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(spa.authors).isNull()
        assertThat(spa.firstAuthor).isNull()
        assertThat(spa.location).isEqualTo("null. 0;")
        assertThat(spa.title).isNull()
        assertThat(spa.doi).isNull()
    }

    @Test
    fun authors_withoutCollectiveAuthor() {
        val authorList = AuthorList()
        authorList.author.add(makeAuthor("Bond", "James", "J", null, null))
        authorList.author.add(makeAuthor("Kid", "Billy", "B", "Jr", null))
        authorList.author.add(makeAuthor("Joice", "James", "J", null, null))
        pubmedArticle.medlineCitation.article.authorList = authorList
        val spa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(spa.authors).isEqualTo("Bond J, Kid B Jr, Joice J.")
        assertThat(spa.firstAuthor).isEqualTo("Bond")
    }

    @Test
    fun authors_withCollectiveAuthor() {
        val authorList = AuthorList()
        authorList.author.add(makeAuthor("Bond", "James", "J", null, null))
        authorList.author.add(makeAuthor("Kid", "Billy", "B", "Jr", null))
        authorList.author.add(makeAuthor(null, null, null, null, "Joice J"))
        pubmedArticle.medlineCitation.article.authorList = authorList
        val spa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(spa.authors).isEqualTo("Bond J, Kid B Jr; Joice J.")
        assertThat(spa.firstAuthor).isEqualTo("Bond")
    }

    @Test
    fun noAuthors_withCollectiveAuthor() {
        val authorList = AuthorList()
        authorList.author.add(makeAuthor(null, null, null, null, "Joice J"))
        pubmedArticle.medlineCitation.article.authorList = authorList
        val spa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(spa.authors).isEqualTo("Joice J.")
        assertThat(spa.firstAuthor).isEqualTo("Joice J")
    }

    @Test
    fun noAuthors_noCollectiveAuthor() {
        val authorList = AuthorList()
        pubmedArticle.medlineCitation.article.authorList = authorList
        val spa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(spa.authors).isEqualTo("")
        assertThat(spa.firstAuthor).isEqualTo("")
    }

    @Test
    fun location_withMedlinePagination() {
        pubmedArticle
            .medlineCitation
            .medlineJournalInfo.medlineTA = "Medline TA"
        val year = Year()
        year.setvalue("2016")
        pubmedArticle
            .medlineCitation
            .article
            .journal
            .journalIssue
            .pubDate
            .yearOrMonthOrDayOrSeasonOrMedlineDate
            .add(year)
        pubmedArticle
            .medlineCitation
            .article
            .journal
            .journalIssue.volume = "6"
        pubmedArticle
            .medlineCitation
            .article
            .journal
            .journalIssue.issue = "10"
        val pagination = Pagination()
        val medlinePgn = MedlinePgn()
        medlinePgn.setvalue("1145-9")
        pagination
            .startPageOrEndPageOrMedlinePgn
            .add(medlinePgn)
        val medlinePgn2 = MedlinePgn()
        medlinePgn2.setvalue("3456-3458")
        pagination
            .startPageOrEndPageOrMedlinePgn
            .add(medlinePgn2)
        pubmedArticle
            .medlineCitation
            .article
            .paginationOrELocationID
            .add(pagination)
        val spa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(spa.location).isEqualTo("Medline TA. 2016; 6 (10): 1145-1149.")
    }

    @Test
    fun location_withMedlinePaginationWithoutPageRange() {
        pubmedArticle
            .medlineCitation
            .medlineJournalInfo.medlineTA = "Medline TA"
        val year = Year()
        year.setvalue("2016")
        pubmedArticle
            .medlineCitation
            .article
            .journal
            .journalIssue
            .pubDate
            .yearOrMonthOrDayOrSeasonOrMedlineDate
            .add(year)
        pubmedArticle
            .medlineCitation
            .article
            .journal
            .journalIssue.volume = "6"
        pubmedArticle
            .medlineCitation
            .article
            .journal
            .journalIssue.issue = "10"
        val pagination = Pagination()
        val medlinePgn = MedlinePgn()
        medlinePgn.setvalue("1145")
        pagination
            .startPageOrEndPageOrMedlinePgn
            .add(medlinePgn)
        pubmedArticle
            .medlineCitation
            .article
            .paginationOrELocationID
            .add(pagination)
        val spa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(spa.location).isEqualTo("Medline TA. 2016; 6 (10): 1145.")
    }

    @Test
    fun location_withMedlineElocation() {
        pubmedArticle.medlineCitation.medlineJournalInfo.medlineTA = "Medline TA"
        val year = Year()
        year.setvalue("2016")
        pubmedArticle
            .medlineCitation
            .article
            .journal
            .journalIssue
            .pubDate
            .yearOrMonthOrDayOrSeasonOrMedlineDate
            .add(year)
        pubmedArticle
            .medlineCitation
            .article
            .journal
            .journalIssue.volume = "6"
        pubmedArticle
            .medlineCitation
            .article
            .journal
            .journalIssue.issue = "10"
        val elocationId1 = ELocationID()
        elocationId1.eIdType = "foo"
        elocationId1.setvalue("bar")
        val elocationId2 = ELocationID()
        elocationId2.eIdType = "pii"
        elocationId2.setvalue("baz")
        pubmedArticle
            .medlineCitation
            .article
            .paginationOrELocationID
            .add(elocationId1)
        pubmedArticle
            .medlineCitation
            .article
            .paginationOrELocationID
            .add(elocationId2)
        val spa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(spa.location).isEqualTo("Medline TA. 2016; 6 (10). pii: baz.")
    }

    private fun makeAuthor(
        lastname: String?,
        forename: String?,
        initials: String?,
        suffix: String?,
        collectiveName: String?
    ): Author {
        val author = Author()
        if (lastname != null) {
            val ln = LastName()
            ln.setvalue(lastname)
            author.lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName.add(ln)
        }
        if (forename != null) {
            val fn = ForeName()
            fn.setvalue(forename)
            author.lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName.add(fn)
        }
        if (initials != null) {
            val i = Initials()
            i.setvalue(initials)
            author.lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName.add(i)
        }
        if (suffix != null) {
            val s = Suffix()
            s.setvalue(suffix)
            author.lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName.add(s)
        }
        if (collectiveName != null) {
            val cn = CollectiveName()
            cn.setvalue(collectiveName)
            author.lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName.add(cn)
        }
        return author
    }

    @Test
    fun title() {
        val articleTitle = ArticleTitle()
        articleTitle.mixedContent = listOf("article title")
        pubmedArticle.medlineCitation.article.articleTitle = articleTitle
        val spa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(spa.title).isEqualTo("article title")
    }

    @Test
    fun doi_fromArticleIdList() {
        val articleId1 = ArticleId()
        articleId1.idType = "foo"
        articleId1.setvalue("bar")
        val articleId2 = ArticleId()
        articleId2.idType = "doi"
        articleId2.setvalue("10.0000012345")
        val articleIdList = ArticleIdList()
        articleIdList.articleId.add(articleId1)
        articleIdList.articleId.add(articleId2)
        val pubmedData = PubmedData()
        pubmedData.articleIdList = articleIdList
        pubmedArticle.pubmedData = pubmedData
        val spa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(spa.doi).isEqualTo("10.0000012345")
    }

    @Test
    fun doi_withoutArticleIdList_usesElocIfthere() {
        val elocationId = ELocationID()
        elocationId.setvalue("eloc")
        pubmedArticle
            .medlineCitation
            .article
            .paginationOrELocationID
            .add(elocationId)
        val spa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(spa.doi).isEqualTo("eloc")
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(ScipamatoPubmedArticle::class.java)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun testingToString() {
        val pa = ScipamatoPubmedArticle(pubmedArticle)
        assertThat(pa.toString()).isEqualTo(
            "AbstractPubmedArticleFacade(pmId=null, authors=null, firstAuthor=null, publicationYear=0, " +
                "location=null. 0;, title=null, doi=null, originalAbstract=null)"
        )
    }

    @Test
    fun handleDatishObject_withYear() {
        val o = Year()
        o.setvalue("2018")
        assertDatish("2018 ", o)
    }

    @Test
    fun handleDatishObject_withMonth() {
        val o = Month()
        o.setvalue("12")
        assertDatish("12 ", o)
    }

    @Test
    fun handleDatishObject_withDay() {
        val o = Day()
        o.setvalue("13")
        assertDatish("13", o)
    }

    @Test
    fun handleDatishObject_withUndefinedObject() {
        assertDatish("", "foo")
    }

    private fun assertDatish(expected: String, o: Any) {
        val pa = ScipamatoPubmedArticle(pubmedArticle)
        val sb = StringBuilder()
        pa.handleDatishObject(sb, o)
        assertThat(sb.toString()).isEqualTo(expected)
    }

    companion object {

        fun makeMinimalValidPubmedArticle(): PubmedArticle {
            val pa = PubmedArticle()
            val medlineCitation = MedlineCitation()
            val article = Article()
            val journal = Journal()
            val journalIssue = JournalIssue()
            journalIssue.pubDate = PubDate()
            journal.journalIssue = journalIssue
            article.journal = journal
            article.articleTitle = ArticleTitle()
            medlineCitation.article = article
            medlineCitation.pmid = PMID()
            medlineCitation.medlineJournalInfo = MedlineJournalInfo()
            pa.medlineCitation = medlineCitation
            return pa
        }
    }
}
