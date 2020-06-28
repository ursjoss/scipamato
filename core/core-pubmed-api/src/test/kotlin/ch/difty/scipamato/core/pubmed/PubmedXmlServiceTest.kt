package ch.difty.scipamato.core.pubmed

import ch.difty.scipamato.core.pubmed.api.PubmedArticle
import ch.difty.scipamato.core.pubmed.api.PubmedArticleSet
import feign.FeignException
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeBlank
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeEmpty
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.oxm.UnmarshallingFailureException
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import javax.xml.transform.stream.StreamSource

@Suppress("SpellCheckingInspection")
internal class PubmedXmlServiceTest {

    private val unmarshallerMock = mockk<Jaxb2Marshaller>()
    private val pubMedMock = mockk<PubMed>()
    private val pubmedArticleSetMock = mockk<PubmedArticleSet>()

    private val pubmedArticleMock = mockk<PubmedArticle> {
        every { pubmedData } returns mockk {
            every { publicationStatus } returns "pubStatus"
            every { articleIdList } returns mockk {
                every { articleId } returns emptyList()
            }
        }
        every { medlineCitation } returns mockk {
            every { article } returns mockk {
                every { abstract } returns mockk {
                    every { abstractText } returns emptyList()
                }
                every { authorList } returns mockk {
                    every { author } returns listOf(mockk {
                        every { lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName } returns emptyList()
                    })
                }
                every { paginationOrELocationID } returns emptyList()
                every { journal } returns mockk {
                    every { journalIssue } returns mockk {
                        every { volume } returns "volume"
                        every { issue } returns "issue"
                        every { pubDate } returns mockk {
                            every { yearOrMonthOrDayOrSeasonOrMedlineDate } returns emptyList()
                        }
                    }
                }
                every { articleTitle } returns mockk {
                    every { getvalue() } returns "articleTitle"
                }
            }
            every { pmid } returns mockk {
                every { getvalue() } returns "PMID"
            }
            every { medlineJournalInfo } returns mockk {
                every { medlineTA } returns "medlineTA"
            }
        }
    }
    private val feignExceptionMock = mockk<FeignException> {
        every { message } returns "msg"
    }

    private var service = PubmedXmlService(unmarshallerMock, pubMedMock)

    @AfterEach
    fun tearDown() {
        confirmVerified(unmarshallerMock, pubMedMock, pubmedArticleSetMock)
    }

    @Test
    fun gettingPubmedArticleWithPmid_withValidId_returnsArticle() {
        val pmId = 25395026
        every { pubMedMock.articleWithId(pmId.toString()) } returns pubmedArticleSetMock
        val objects = ArrayList<Any>()
        objects.add(pubmedArticleMock)
        every { pubmedArticleSetMock.pubmedArticleOrPubmedBookArticle } returns objects

        val pr = service.getPubmedArticleWithPmid(pmId)
        pr.errorMessage.shouldBeBlank()

        verify { pubMedMock.articleWithId(pmId.toString()) }
        verify { pubmedArticleSetMock.pubmedArticleOrPubmedBookArticle }
    }

    @Test
    fun gettingPubmedArticleWithPmid_withInvalidId_returnsNullFacade() {
        val pmId = 999999999
        every { pubMedMock.articleWithId(pmId.toString()) } returns pubmedArticleSetMock
        val objects = ArrayList<Any>()
        every { pubmedArticleSetMock.pubmedArticleOrPubmedBookArticle } returns objects

        val pr = service.getPubmedArticleWithPmid(pmId)
        pr.pubmedArticleFacade.shouldBeNull()
        pr.errorMessage shouldBeEqualTo "PMID $pmId seems to be undefined in PubMed."

        verify { pubMedMock.articleWithId(pmId.toString()) }
        verify { pubmedArticleSetMock.pubmedArticleOrPubmedBookArticle }
    }

    @Test
    fun gettingPubmedArticleWithPmid_withNullObjects_returnsNullFacade() {
        val pmId = 999999999
        every { pubMedMock.articleWithId(pmId.toString()) } returns pubmedArticleSetMock
        every { pubmedArticleSetMock.pubmedArticleOrPubmedBookArticle } returns null

        val pr = service.getPubmedArticleWithPmid(pmId)
        pr.pubmedArticleFacade.shouldBeNull()
        pr.errorMessage.shouldBeBlank()

        verify { pubMedMock.articleWithId(pmId.toString()) }
        verify { pubmedArticleSetMock.pubmedArticleOrPubmedBookArticle }
    }

    @Test
    fun gettingPubmedArticleWithPmidAndApiKey_withValidId_returnsArticle() {
        val pmId = 25395026
        every { pubMedMock.articleWithId(pmId.toString(), "key") } returns pubmedArticleSetMock
        val objects = ArrayList<Any>()
        objects.add(pubmedArticleMock)
        every { pubmedArticleSetMock.pubmedArticleOrPubmedBookArticle } returns objects

        val pr = service.getPubmedArticleWithPmidAndApiKey(pmId, "key")
        pr.pubmedArticleFacade.shouldNotBeNull()
        pr.errorMessage.shouldBeBlank()

        verify { pubMedMock.articleWithId(pmId.toString(), "key") }
        verify { pubmedArticleSetMock.pubmedArticleOrPubmedBookArticle }
    }

    @Test
    fun gettingPubmedArticle_withInvalidId_returnsEmptyArticleAndRawExceptionMessage() {
        val pmId = 25395026
        every { pubMedMock.articleWithId(pmId.toString(), "key") } throws RuntimeException("boom")

        val pr = service.getPubmedArticleWithPmidAndApiKey(pmId, "key")
        pr.pubmedArticleFacade.shouldBeNull()
        pr.errorMessage shouldBeEqualTo "boom"

        verify { pubMedMock.articleWithId(pmId.toString(), "key") }
    }

    @Test
    fun gettingArticles_withUnmarshallerException_returnsEmptyList() {
        every { unmarshallerMock.unmarshal(any<StreamSource>()) } throws UnmarshallingFailureException("boom")
        service.extractArticlesFrom("some invalid xml").shouldBeEmpty()
        verify { unmarshallerMock.unmarshal(any<StreamSource>()) }
    }

    @Test
    fun gettingArticles_withPubmedArticleSetWithoutArticleCollection_returnsEmptyList() {
        val pubmedArticleSet = PubmedArticleSet()
        every { unmarshallerMock.unmarshal(any<StreamSource>()) } returns pubmedArticleSet
        service.extractArticlesFrom("some valid xml").shouldBeEmpty()
        verify { unmarshallerMock.unmarshal(any<StreamSource>()) }
    }

    @Test
    fun gettingArticles_withPubmedArticleSetWithoutArticleCollection2_returnsEmptyList() {
        every { unmarshallerMock.unmarshal(any<StreamSource>()) } returns makeMinimalValidPubmedArticleSet()
        service.extractArticlesFrom("some valid xml").shouldNotBeEmpty()
        verify { unmarshallerMock.unmarshal((any<StreamSource>())) }
    }

    @Test
    fun gettingPubmedArticleWithPmid_withParsableHtmlError502_hasHttpStatusPopulated() {
        val pmId = 25395026
        feignExceptionFixture(502, "status 502 reading PubMed#articleWithId(String,String); content: \nfoo")
        every { pubMedMock.articleWithId(pmId.toString()) } throws feignExceptionMock

        val pr = service.getPubmedArticleWithPmid(pmId)
        pr.pubmedArticleFacade.shouldBeNull()
        pr.errorMessage shouldBeEqualTo
            "Status 502 BAD_GATEWAY: status 502 reading PubMed#articleWithId(String,String); content: \nfoo"

        verify { pubMedMock.articleWithId(pmId.toString()) }
    }

    @Test
    fun gettingPubmedArticleWithPmid_withParsableHtmlError400_hasHttpStatusPopulated() {
        val pmId = 25395026
        feignExceptionFixture(
            400,
            """status 400 reading PubMed#articleWithId(String,String); content:
            |{"error":"API key invalid","api-key":"xxx","type":"invalid","status":"unknown"}""".trimMargin()
        )
        every { pubMedMock.articleWithId(pmId.toString()) } throws feignExceptionMock

        val pr = service.getPubmedArticleWithPmid(pmId)
        pr.pubmedArticleFacade.shouldBeNull()
        pr.errorMessage shouldBeEqualTo "Status 400 BAD_REQUEST: API key invalid"

        verify { pubMedMock.articleWithId(pmId.toString()) }
    }

    private fun feignExceptionFixture(status: Int, msg: String) {
        every { feignExceptionMock.status() } returns status
        every { feignExceptionMock.localizedMessage } returns msg
    }

    @Test
    fun gettingPubmedArticleWithPmid_withParsableHtmlError400_hasHttpStatusPopulated2() {
        val pmId = 25395026
        every { pubMedMock.articleWithId(pmId.toString()) } throws RuntimeException(
            """status 400 reading PubMed#articleWithId(String,String); content:
                |{"error":"API key invalid","api-key":"xxx","type":"invalid","status":"unknown"}""".trimMargin()
        )

        val pr = service.getPubmedArticleWithPmid(pmId)
        pr.pubmedArticleFacade.shouldBeNull()
        pr.errorMessage shouldBeEqualTo
            """status 400 reading PubMed#articleWithId(String,String); content:
            |{"error":"API key invalid","api-key":"xxx","type":"invalid","status":"unknown"}""".trimMargin()

        verify { pubMedMock.articleWithId(pmId.toString()) }
    }

    @Test
    fun gettingPubmedArticleWithPmid_withNoParsableHtmlError_onlyHasMessage() {
        val pmId = 25395026
        every { pubMedMock.articleWithId(pmId.toString()) } throws
            RuntimeException("The network is not reachable")

        val pr = service.getPubmedArticleWithPmid(pmId)
        pr.pubmedArticleFacade.shouldBeNull()
        pr.errorMessage shouldBeEqualTo "The network is not reachable"

        verify { pubMedMock.articleWithId(pmId.toString()) }
    }

    companion object {
        fun makeMinimalValidPubmedArticleSet(): PubmedArticleSet {
            val pubmedArticleSet = PubmedArticleSet()
            pubmedArticleSet
                .pubmedArticleOrPubmedBookArticle
                .add(ScipamatoPubmedArticleTest.makeMinimalValidPubmedArticle())
            return pubmedArticleSet
        }
    }
}
