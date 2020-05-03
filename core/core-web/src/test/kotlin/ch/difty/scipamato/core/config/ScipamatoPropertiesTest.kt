package ch.difty.scipamato.core.config

import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.junit.jupiter.api.Test

internal class ScipamatoPropertiesTest {

    private val sp = ScipamatoProperties()

    @Test
    fun brand_hasDefaultValue() {
        sp.brand shouldBeEqualTo "SciPaMaTo-Core"
    }

    @Test
    fun defaultLocalization_hasDefaultEnglish() {
        sp.defaultLocalization shouldBeEqualTo "en"
    }

    @Test
    fun pubmedBaseUrl_hasDefaultValue() {
        sp.pubmedBaseUrl shouldBeEqualTo "https://www.ncbi.nlm.nih.gov/pubmed/"
    }

    @Test
    fun cmsUrlSearchPage_hasNoDefaultValue() {
        sp.cmsUrlSearchPage.shouldBeNull()
    }

    @Test
    fun authorParser_isDefault() {
        sp.authorParser shouldBeEqualTo "DEFAULT"
    }

    @Test
    fun authorParserStrategy_isDefault() {
        sp.authorParserStrategy shouldBeEqualTo AuthorParserStrategy.PUBMED
    }

    @Test
    fun risExporterStrategy_isDefault() {
        sp.risExporterStrategy shouldBeEqualTo RisExporterStrategy.DEFAULT
    }

    @Test
    fun paperMinimumToBeRecycled_hasDefaultValue() {
        sp.paperNumberMinimumToBeRecycled shouldBeEqualTo 0
    }

    @Test
    fun dbSchema_hasDefaultValuePublic() {
        sp.dbSchema shouldBeEqualTo "public"
    }

    @Test
    fun gettingRedirectPort_hasNoDefaultValue() {
        sp.redirectFromPort.shouldBeNull()
    }

    @Test
    fun gettingMultiSelectBoxActionBoxWithMoreEntriesThan_hasDefaultValue() {
        sp.multiSelectBoxActionBoxWithMoreEntriesThan shouldBeEqualTo 4
    }

    @Test
    fun gettingPubmedApiKey_hasNoDefaultValue() {
        sp.pubmedApiKey.shouldBeNull()
    }
}
