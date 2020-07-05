package ch.difty.scipamato.core.config

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class JaxbConfigurationTest {

    @Test
    fun assertLink() {
        JaxbConfiguration.PUBMED_URL shouldBeEqualTo "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/"
    }
}
