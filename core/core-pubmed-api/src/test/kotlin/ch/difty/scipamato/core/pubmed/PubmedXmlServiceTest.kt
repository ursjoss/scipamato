package ch.difty.scipamato.core.pubmed

import ch.difty.scipamato.core.pubmed.api.*
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import feign.FeignException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.oxm.UnmarshallingFailureException
import org.springframework.oxm.XmlMappingException
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import java.io.IOException
import javax.xml.transform.stream.StreamSource

@Suppress("SpellCheckingInspection")
internal class PubmedXmlServiceTest {

    private val unmarshallerMock = mock<Jaxb2Marshaller>()
    private val pubMedMock = mock<PubMed>()
    private val pubmedArticleSetMock = mock<PubmedArticleSet>()
    private val pubmedArticleMock = mock<PubmedArticle>()
    private val medLineCitationMock = mock<MedlineCitation>()
    private val articleMock = mock<Article>()
    private val journalMock = mock<Journal>()
    private val pmidMock = mock<PMID>()
    private val journalIssueMock = mock<JournalIssue>()
    private val pubDateMock = mock<PubDate>()
    private val medLineJournalInfoMock = mock<MedlineJournalInfo>()
    private val articleTitleMock = mock<ArticleTitle>()
    private val feignExceptionMock = mock<FeignException>()

    private var service = PubmedXmlService(unmarshallerMock, pubMedMock)

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(unmarshallerMock, pubMedMock, pubmedArticleSetMock)
    }

    @Test
    fun gettingPubmedArticleWithPmid_withValidId_returnsArticle() {
        val pmId = 25395026
        whenever(pubMedMock.articleWithId(pmId.toString())).thenReturn(pubmedArticleSetMock)
        val objects = ArrayList<Any>()
        objects.add(pubmedArticleMock)
        whenever(pubmedArticleSetMock.pubmedArticleOrPubmedBookArticle).thenReturn(objects)

        whenever(pubmedArticleMock.medlineCitation).thenReturn(medLineCitationMock)
        whenever(medLineCitationMock.article).thenReturn(articleMock)
        whenever(articleMock.journal).thenReturn(journalMock)
        whenever(medLineCitationMock.pmid).thenReturn(pmidMock)
        whenever(journalMock.journalIssue).thenReturn(journalIssueMock)
        whenever(journalIssueMock.pubDate).thenReturn(pubDateMock)
        whenever(medLineCitationMock.medlineJournalInfo).thenReturn(medLineJournalInfoMock)
        whenever(articleMock.articleTitle).thenReturn(articleTitleMock)

        val pr = service.getPubmedArticleWithPmid(pmId)
        assertThat(pr.errorMessage).isBlank()

        verify(pubMedMock).articleWithId(pmId.toString())
        verify(pubmedArticleSetMock).pubmedArticleOrPubmedBookArticle
    }

    @Test
    fun gettingPubmedArticleWithPmid_withInvalidId_returnsNullFacade() {
        val pmId = 999999999
        whenever(pubMedMock.articleWithId(pmId.toString())).thenReturn(pubmedArticleSetMock)
        val objects = ArrayList<Any>()
        whenever(pubmedArticleSetMock.pubmedArticleOrPubmedBookArticle).thenReturn(objects)

        val pr = service.getPubmedArticleWithPmid(pmId)
        assertThat(pr.pubmedArticleFacade).isNull()
        assertThat(pr.errorMessage).isEqualTo("PMID $pmId seems to be undefined in PubMed.")

        verify(pubMedMock).articleWithId(pmId.toString())
        verify(pubmedArticleSetMock).pubmedArticleOrPubmedBookArticle
    }

    @Test
    fun gettingPubmedArticleWithPmid_withNullObjects_returnsNullFacade() {
        val pmId = 999999999
        whenever(pubMedMock.articleWithId(pmId.toString())).thenReturn(pubmedArticleSetMock)
        whenever(pubmedArticleSetMock.pubmedArticleOrPubmedBookArticle).thenReturn(null)

        val pr = service.getPubmedArticleWithPmid(pmId)
        assertThat(pr.pubmedArticleFacade).isNull()
        assertThat(pr.errorMessage).isBlank()

        verify(pubMedMock).articleWithId(pmId.toString())
        verify(pubmedArticleSetMock).pubmedArticleOrPubmedBookArticle
    }

    @Test
    fun gettingPubmedArticleWithPmidAndApiKey_withValidId_returnsArticle() {
        val pmId = 25395026
        whenever(pubMedMock.articleWithId(pmId.toString(), "key")).thenReturn(pubmedArticleSetMock)
        val objects = ArrayList<Any>()
        objects.add(pubmedArticleMock)
        whenever(pubmedArticleSetMock.pubmedArticleOrPubmedBookArticle).thenReturn(objects)

        whenever(pubmedArticleMock.medlineCitation).thenReturn(medLineCitationMock)
        whenever(medLineCitationMock.article).thenReturn(articleMock)
        whenever(articleMock.journal).thenReturn(journalMock)
        whenever(medLineCitationMock.pmid).thenReturn(pmidMock)
        whenever(journalMock.journalIssue).thenReturn(journalIssueMock)
        whenever(journalIssueMock.pubDate).thenReturn(pubDateMock)
        whenever(medLineCitationMock.medlineJournalInfo).thenReturn(medLineJournalInfoMock)
        whenever(articleMock.articleTitle).thenReturn(articleTitleMock)

        val pr = service.getPubmedArticleWithPmidAndApiKey(pmId, "key")
        assertThat(pr.pubmedArticleFacade).isNotNull
        assertThat(pr.errorMessage).isBlank()

        verify(pubMedMock).articleWithId(pmId.toString(), "key")
        verify(pubmedArticleSetMock).pubmedArticleOrPubmedBookArticle
    }

    @Test
    fun gettingPubmedArticle_withInvalidId_returnsEmptyArticleAndRawExceptionMessage() {
        val pmId = 25395026
        whenever(pubMedMock.articleWithId(pmId.toString(), "key")).thenThrow(RuntimeException("boom"))

        val pr = service.getPubmedArticleWithPmidAndApiKey(pmId, "key")
        assertThat(pr.pubmedArticleFacade).isNull()
        assertThat(pr.errorMessage).isEqualTo("boom")

        verify(pubMedMock).articleWithId(pmId.toString(), "key")
    }

    @Test
    @Throws(XmlMappingException::class, IOException::class)
    fun nonValidXml_returnsNull() {
        assertThat(service.unmarshal("")).isNull()
        verify(unmarshallerMock).unmarshal(isA<StreamSource>())
    }

    @Test
    fun gettingArticles_withUnmarshallerException_returnsEmptyList() {
        whenever(unmarshallerMock.unmarshal(isA<StreamSource>())).thenThrow(UnmarshallingFailureException("boom"))
        assertThat(service.extractArticlesFrom("some invalid xml")).isEmpty()
        verify(unmarshallerMock).unmarshal(isA<StreamSource>())
    }

    @Test
    fun gettingArticles_withPubmedArticleSetWithoutArticleCollection_returnsEmptyList() {
        val pubmedArticleSet = PubmedArticleSet()
        whenever(unmarshallerMock.unmarshal(isA<StreamSource>())).thenReturn(pubmedArticleSet)
        assertThat(service.extractArticlesFrom("some valid xml")).isEmpty()
        verify(unmarshallerMock).unmarshal(isA<StreamSource>())
    }

    @Test
    fun gettingArticles_withPubmedArticleSetWithoutArticleCollection2_returnsEmptyList() {
        whenever(unmarshallerMock.unmarshal(isA<StreamSource>())).thenReturn(makeMinimalValidPubmedArticleSet())
        assertThat(service.extractArticlesFrom("some valid xml")).isNotEmpty
        verify(unmarshallerMock).unmarshal((isA<StreamSource>()))
    }

    @Test
    fun gettingPubmedArticleWithPmid_withParsableHtmlError502_hasHttpStatusPopulated() {
        val pmId = 25395026
        feignExceptionFixture(502, "status 502 reading PubMed#articleWithId(String,String); content: \nfoo")
        whenever(pubMedMock.articleWithId(pmId.toString())).thenThrow(feignExceptionMock)

        val pr = service.getPubmedArticleWithPmid(pmId)
        assertThat(pr.pubmedArticleFacade).isNull()
        assertThat(pr.errorMessage).isEqualTo(
            "Status 502 BAD_GATEWAY: status 502 reading PubMed#articleWithId(String,String); content: \nfoo")

        verify(pubMedMock).articleWithId(pmId.toString())
    }

    @Test
    fun gettingPubmedArticleWithPmid_withParsableHtmlError400_hasHttpStatusPopulated() {
        val pmId = 25395026
        feignExceptionFixture(400,
            """status 400 reading PubMed#articleWithId(String,String); content:
            |{"error":"API key invalid","api-key":"xxx","type":"invalid","status":"unknown"}""".trimMargin()
        )
        whenever(pubMedMock.articleWithId(pmId.toString())).thenThrow(feignExceptionMock)

        val pr = service.getPubmedArticleWithPmid(pmId)
        assertThat(pr.pubmedArticleFacade).isNull()
        assertThat(pr.errorMessage).isEqualTo("Status 400 BAD_REQUEST: API key invalid")

        verify(pubMedMock).articleWithId(pmId.toString())
    }

    private fun feignExceptionFixture(status: Int, msg: String) {
        whenever(feignExceptionMock.status()).thenReturn(status)
        whenever(feignExceptionMock.localizedMessage).thenReturn(msg)
    }

    @Test
    fun gettingPubmedArticleWithPmid_withParsableHtmlError400_hasHttpStatusPopulated2() {
        val pmId = 25395026
        whenever(pubMedMock.articleWithId(pmId.toString())).thenThrow(
            RuntimeException(
                """status 400 reading PubMed#articleWithId(String,String); content:
                |{"error":"API key invalid","api-key":"xxx","type":"invalid","status":"unknown"}""".trimMargin()
            )
        )

        val pr = service.getPubmedArticleWithPmid(pmId)
        assertThat(pr.pubmedArticleFacade).isNull()
        assertThat(pr.errorMessage).isEqualTo(
            """status 400 reading PubMed#articleWithId(String,String); content:
            |{"error":"API key invalid","api-key":"xxx","type":"invalid","status":"unknown"}""".trimMargin()
        )

        verify(pubMedMock).articleWithId(pmId.toString())
    }

    @Test
    fun gettingPubmedArticleWithPmid_withNoParsableHtmlError_onlyHasMessage() {
        val pmId = 25395026
        whenever(pubMedMock.articleWithId(pmId.toString())).thenThrow(
            RuntimeException("The network is not reachable"))

        val pr = service.getPubmedArticleWithPmid(pmId)
        assertThat(pr.pubmedArticleFacade).isNull()
        assertThat(pr.errorMessage).isEqualTo("The network is not reachable")

        verify(pubMedMock).articleWithId(pmId.toString())
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
