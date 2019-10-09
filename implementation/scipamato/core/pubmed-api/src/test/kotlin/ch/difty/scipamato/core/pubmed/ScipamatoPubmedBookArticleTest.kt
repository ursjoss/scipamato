package ch.difty.scipamato.core.pubmed

import ch.difty.scipamato.core.pubmed.api.*
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class ScipamatoPubmedBookArticleTest {

    private val pubmedBookArticle = PubmedBookArticle()

    @BeforeEach
    fun setUp() {
        val bookDocument = BookDocument()
        pubmedBookArticle.bookDocument = bookDocument

        val pmId = PMID()
        pmId.setvalue("pmid")
        bookDocument.pmid = pmId

        val authorLists = ArrayList<AuthorList>()
        val authorList = AuthorList()
        authorList.author.add(makeAuthor("ln1", "i1"))
        authorList.author.add(makeAuthor("ln2", "i2"))
        authorList.author.add(makeAuthor("ln3", "i3"))
        authorLists.add(authorList)
        bookDocument.authorList.addAll(authorLists)

        val contributionDate = ContributionDate()
        val year = Year()
        year.setvalue("2017")
        contributionDate.year = year
        bookDocument.contributionDate = contributionDate

        bookDocument.locationLabel.add(makeLocationLabel("ll1"))
        bookDocument.locationLabel.add(makeLocationLabel("ll2"))

        val articleTitle = ArticleTitle()
        articleTitle.mixedContent = listOf("title")
        bookDocument.articleTitle = articleTitle

        val articleIdList = ArticleIdList()
        val pmIdArticleId = ArticleId()
        pmIdArticleId.setvalue("pmid")
        articleIdList.articleId.add(pmIdArticleId)
        val doiId = ArticleId()
        doiId.idType = "doi"
        doiId.setvalue("DOI")
        articleIdList.articleId.add(doiId)
        bookDocument.articleIdList = articleIdList

        val abstr = Abstract()
        val abstrText = AbstractText()
        abstrText.label = "ABSTRACT"
        abstrText.mixedContent = listOf("abstract")
        abstr.abstractText.add(abstrText)
        bookDocument.abstract = abstr
    }

    private fun makeAuthor(lastName: String, initials: String): Author {
        val author = Author()
        val ln = LastName()
        ln.setvalue(lastName)
        author.lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName.add(ln)
        val i = Initials()
        i.setvalue(initials)
        author.lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName.add(i)
        return author
    }

    private fun makeLocationLabel(label: String): LocationLabel {
        val locationLabel = LocationLabel()
        locationLabel.setvalue(label)
        return locationLabel
    }

    @Test
    fun canConstructWithMinimalAttributes() {
        val pubmedBookArticle = PubmedBookArticle()
        pubmedBookArticle.bookDocument = BookDocument()
        val pa = ScipamatoPubmedBookArticle(pubmedBookArticle)

        assertThat(pa.pmId).isNull()
        assertThat(pa.authors).isNull()
        assertThat(pa.firstAuthor).isNull()
        assertThat(pa.publicationYear).isNull()
        assertThat(pa.location).isEmpty()
        assertThat(pa.title).isNull()
        assertThat(pa.doi).isNull()
        assertThat(pa.originalAbstract).isNull()
    }

    @Test
    fun canParseAllFieldsOfFullyEquippedPubmedBookArticle() {
        val pa = ScipamatoPubmedBookArticle(pubmedBookArticle)

        assertThat(pa.pmId).isEqualTo("pmid")
        assertThat(pa.authors).isEqualTo("ln1 i1, ln2 i2, ln3 i3.")
        assertThat(pa.firstAuthor).isEqualTo("ln1")
        assertThat(pa.publicationYear).isEqualTo("2017")
        assertThat(pa.location).isEqualTo("ll1 - ll2")
        assertThat(pa.title).isEqualTo("title")
        assertThat(pa.doi).isEqualTo("DOI")
        assertThat(pa.originalAbstract).startsWith("ABSTRACT: abstract")

        assertThat(pa.toString()).isEqualTo(
            "AbstractPubmedArticleFacade(pmId=pmid, authors=ln1 i1, ln2 i2, ln3 i3., firstAuthor=ln1," +
                " publicationYear=2017, location=ll1 - ll2, title=title, doi=DOI, originalAbstract=ABSTRACT: abstract)"
        )
    }

    @Test
    fun canParseAbstractWithoutAbstractLabel() {
        pubmedBookArticle
            .bookDocument
            .abstract
            .abstractText[0].label = null
        val pa = ScipamatoPubmedBookArticle(pubmedBookArticle)
        assertThat(pa.originalAbstract).startsWith("abstract")
    }

    @Test
    fun withArticleIdListNull_leavesDoiNull() {
        pubmedBookArticle
            .bookDocument.articleIdList = null

        val pa = ScipamatoPubmedBookArticle(pubmedBookArticle)

        assertThat(pa.doi).isNull()
    }

    @Test
    fun withAuthorsNull_leavesAuthorAndFirstAuthorNull() {
        pubmedBookArticle
            .bookDocument
            .authorList
            .clear()

        val pa = ScipamatoPubmedBookArticle(pubmedBookArticle)

        assertThat(pa.authors).isNull()
        assertThat(pa.firstAuthor).isNull()
    }

    @Test
    fun invalidConstructionUsingOfWithForeignObject() {
        try {
            PubmedArticleFacade.newPubmedArticleFrom(1)
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Cannot instantiate ScipamatoArticle from provided object 1")
        }
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(ScipamatoPubmedBookArticle::class.java)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }
}
