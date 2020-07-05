package ch.difty.scipamato.core.pubmed

import ch.difty.scipamato.core.pubmed.api.Abstract
import ch.difty.scipamato.core.pubmed.api.AbstractText
import ch.difty.scipamato.core.pubmed.api.ArticleId
import ch.difty.scipamato.core.pubmed.api.ArticleIdList
import ch.difty.scipamato.core.pubmed.api.ArticleTitle
import ch.difty.scipamato.core.pubmed.api.Author
import ch.difty.scipamato.core.pubmed.api.AuthorList
import ch.difty.scipamato.core.pubmed.api.BookDocument
import ch.difty.scipamato.core.pubmed.api.ContributionDate
import ch.difty.scipamato.core.pubmed.api.Initials
import ch.difty.scipamato.core.pubmed.api.LastName
import ch.difty.scipamato.core.pubmed.api.LocationLabel
import ch.difty.scipamato.core.pubmed.api.PMID
import ch.difty.scipamato.core.pubmed.api.PubmedBookArticle
import ch.difty.scipamato.core.pubmed.api.Year
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldStartWith
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
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

        pa.pmId.shouldBeNull()
        pa.authors.shouldBeNull()
        pa.firstAuthor.shouldBeNull()
        pa.publicationYear.shouldBeNull()
        pa.location.shouldBeEmpty()
        pa.title.shouldBeNull()
        pa.doi.shouldBeNull()
        pa.originalAbstract.shouldBeNull()
    }

    @Test
    fun canParseAllFieldsOfFullyEquippedPubmedBookArticle() {
        val pa = ScipamatoPubmedBookArticle(pubmedBookArticle)

        pa.pmId shouldBeEqualTo "pmid"
        pa.authors shouldBeEqualTo "ln1 i1, ln2 i2, ln3 i3."
        pa.firstAuthor shouldBeEqualTo "ln1"
        pa.publicationYear shouldBeEqualTo "2017"
        pa.location shouldBeEqualTo "ll1 - ll2"
        pa.title shouldBeEqualTo "title"
        pa.doi shouldBeEqualTo "DOI"
        pa.originalAbstract shouldStartWith "ABSTRACT: abstract"

        pa.toString() shouldBeEqualTo
            "AbstractPubmedArticleFacade(pmId=pmid, authors=ln1 i1, ln2 i2, ln3 i3., firstAuthor=ln1," +
            " publicationYear=2017, location=ll1 - ll2, title=title, doi=DOI, originalAbstract=ABSTRACT: abstract)"
    }

    @Test
    fun canParseAbstractWithoutAbstractLabel() {
        pubmedBookArticle
            .bookDocument
            .abstract
            .abstractText[0].label = null
        val pa = ScipamatoPubmedBookArticle(pubmedBookArticle)
        pa.originalAbstract shouldStartWith "abstract"
    }

    @Test
    fun withArticleIdListNull_leavesDoiNull() {
        pubmedBookArticle
            .bookDocument.articleIdList = null

        val pa = ScipamatoPubmedBookArticle(pubmedBookArticle)

        pa.doi.shouldBeNull()
    }

    @Test
    fun withAuthorsNull_leavesAuthorAndFirstAuthorNull() {
        pubmedBookArticle
            .bookDocument
            .authorList
            .clear()

        val pa = ScipamatoPubmedBookArticle(pubmedBookArticle)

        pa.authors.shouldBeNull()
        pa.firstAuthor.shouldBeNull()
    }

    @Test
    fun invalidConstructionUsingOfWithForeignObject() {
        invoking { PubmedArticleFacade.newPubmedArticleFrom(1) } shouldThrow
            IllegalArgumentException::class withMessage "Cannot instantiate ScipamatoArticle from provided object 1"
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(ScipamatoPubmedBookArticle::class.java)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }
}
