package ch.difty.scipamato.core.pubmed

import ch.difty.scipamato.common.readFileAsString
import ch.difty.scipamato.core.pubmed.api.PubmedArticleSet
import org.amshove.kluent.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Suppress("SpellCheckingInspection")
@SpringBootTest
internal abstract class PubmedIntegrationTest {

    @Autowired
    private lateinit var service: PubmedXmlService

    fun getPubmedArticleSet(fileName: String): PubmedArticleSet? {
        val xml = readFileAsString(fileName)
        xml.shouldNotBeNull()
        return service.unmarshal(xml)
    }

    fun getPubmedArticles(fileName: String): List<PubmedArticleFacade> {
        val xml = readFileAsString(fileName)
        xml.shouldNotBeNull()
        return service.extractArticlesFrom(xml)
    }
}
