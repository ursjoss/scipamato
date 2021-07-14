package ch.difty.scipamato.core.pubmed

import ch.difty.scipamato.core.config.ApplicationCoreProperties
import ch.difty.scipamato.core.persistence.DefaultServiceResult
import ch.difty.scipamato.core.persistence.PaperService
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class PubmedImportServiceTest {

    private lateinit var pubmedImporter: PubmedImporter

    @MockK
    private lateinit var pubmedArticleServiceMock: PubmedArticleService

    @MockK
    private lateinit var paperServiceMock: PaperService

    @MockK
    private lateinit var applicationPropertiesMock: ApplicationCoreProperties

    private val serviceResult = DefaultServiceResult()

    private val pubmedArticles = listOf(mockk<PubmedArticleFacade>())

    @BeforeEach
    fun setUp() {
        every { applicationPropertiesMock.minimumPaperNumberToBeRecycled } returns 7L
        pubmedImporter = PubmedImportService(pubmedArticleServiceMock, paperServiceMock, applicationPropertiesMock)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(
            pubmedArticleServiceMock, paperServiceMock, applicationPropertiesMock
        )
    }

    @Test
    fun persistingPubmedArticlesFromXml_withNullXml_fails() {
        val sr = pubmedImporter.persistPubmedArticlesFromXml(null)
        sr.errorMessages shouldHaveSize 1
        sr.errorMessages shouldContain "xml must not be null."
        verify { applicationPropertiesMock.minimumPaperNumberToBeRecycled }
    }

    @Test
    fun persistingPubmedArticlesFromXml_delegatesExtractionAndPersistingToNestedServices() {
        val minimumNumber = 7L
        every { pubmedArticleServiceMock.extractArticlesFrom("content") } returns pubmedArticles
        every { paperServiceMock.dumpPubmedArticlesToDb(pubmedArticles, minimumNumber) } returns serviceResult
        pubmedImporter.persistPubmedArticlesFromXml("content") shouldBeEqualTo serviceResult
        verify { applicationPropertiesMock.minimumPaperNumberToBeRecycled }
        verify { pubmedArticleServiceMock.extractArticlesFrom("content") }
        verify { paperServiceMock.dumpPubmedArticlesToDb(pubmedArticles, minimumNumber) }
    }
}
