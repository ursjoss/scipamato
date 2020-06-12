package ch.difty.scipamato.core.pubmed

import ch.difty.scipamato.common.ClearAllMocksExtension
import ch.difty.scipamato.core.config.ApplicationCoreProperties
import ch.difty.scipamato.core.persistence.PaperService
import ch.difty.scipamato.core.persistence.ServiceResult
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.ArrayList

@ExtendWith(MockKExtension::class, ClearAllMocksExtension::class)
internal class PubmedImportServiceTest {

    private lateinit var pubmedImporter: PubmedImporter

    @MockK
    private lateinit var pubmedArticleServiceMock: PubmedArticleService

    @MockK
    private lateinit var paperServiceMock: PaperService

    @MockK
    private lateinit var applicationPropertiesMock: ApplicationCoreProperties

    @MockK
    private lateinit var pubmedArticleMock: PubmedArticleFacade

    @MockK
    private lateinit var serviceResultMock: ServiceResult

    private val pubmedArticles: MutableList<PubmedArticleFacade?> = ArrayList()

    @BeforeEach
    fun setUp() {
        pubmedArticles.add(pubmedArticleMock)
        every { applicationPropertiesMock.minimumPaperNumberToBeRecycled } returns 7L
        pubmedImporter = PubmedImportService(pubmedArticleServiceMock, paperServiceMock, applicationPropertiesMock)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(
            pubmedArticleServiceMock, paperServiceMock, pubmedArticleMock, serviceResultMock, applicationPropertiesMock
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
        every { paperServiceMock.dumpPubmedArticlesToDb(pubmedArticles, minimumNumber) } returns serviceResultMock
        pubmedImporter.persistPubmedArticlesFromXml("content") shouldBeEqualTo serviceResultMock
        verify { applicationPropertiesMock.minimumPaperNumberToBeRecycled }
        verify { pubmedArticleServiceMock.extractArticlesFrom("content") }
        verify { paperServiceMock.dumpPubmedArticlesToDb(pubmedArticles, minimumNumber) }
        verify { serviceResultMock == serviceResultMock }
    }
}
