package ch.difty.scipamato.core.logic.parsing

import ch.difty.scipamato.core.config.ApplicationCoreProperties
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class AuthorParserConfigurationTest {

    private lateinit var conf: AuthorParserConfiguration

    @MockK
    private lateinit var appProperties: ApplicationCoreProperties

    @BeforeEach
    fun setUp() {
        conf = AuthorParserConfiguration()
        every { appProperties.authorParserStrategy } returns AuthorParserStrategy.PUBMED
    }

    @Test
    fun canRetrieveAuthorParserFactory() {
        val factory = conf.authorParserFactory(appProperties)
        factory shouldBeInstanceOf AuthorParserFactory::class
        verify { appProperties.authorParserStrategy }
    }
}
