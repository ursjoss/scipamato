package ch.difty.scipamato.core.logic.parsing

import ch.difty.scipamato.core.config.ApplicationCoreProperties
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class AuthorParserConfigurationTest {

    @Test
    fun canRetrieveAuthorParserFactory() {
        val conf = AuthorParserConfiguration()
        val appProperties = mockk<ApplicationCoreProperties> {
            every { authorParserStrategy } returns AuthorParserStrategy.PUBMED
        }
        val factory = conf.authorParserFactory(appProperties)
        factory shouldBeInstanceOf AuthorParserFactory::class
        verify { appProperties.authorParserStrategy }
    }
}
