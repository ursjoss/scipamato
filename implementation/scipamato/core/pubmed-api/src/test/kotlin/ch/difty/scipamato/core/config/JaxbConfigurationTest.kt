package ch.difty.scipamato.core.config

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class JaxbConfigurationTest {

    @Test
    fun assertLink() {
        assertThat(JaxbConfiguration.PUBMED_URL).isEqualTo("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/")
    }
}