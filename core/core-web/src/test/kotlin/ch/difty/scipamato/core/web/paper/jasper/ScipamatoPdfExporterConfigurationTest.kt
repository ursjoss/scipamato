package ch.difty.scipamato.core.web.paper.jasper

import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.User
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test
import java.util.ArrayList

internal class ScipamatoPdfExporterConfigurationTest {

    private lateinit var config: ScipamatoPdfExporterConfiguration
    private val codes: MutableList<Code?> = ArrayList()

    @Test
    fun minimalInstantiation_buildingTitleFromHeaderPartAndNumber_intoMetadataTitle() {
        config = ScipamatoPdfExporterConfiguration.Builder("headerpart:", 5L).build()
        assertingMinimalConfigWithTitle("headerpart: 5")
    }

    @Test
    fun minimalInstantiation_buildingTitleFromHeaderPartAndNullNumber_intoMetadataTitle() {
        config = ScipamatoPdfExporterConfiguration.Builder("headerpart:", null).build()
        assertingMinimalConfigWithTitle("headerpart:")
    }

    @Test
    fun minimalInstantiation_buildingTitleFromNullHeaderPartAndNullNumber_intoMetadataTitle() {
        config = ScipamatoPdfExporterConfiguration.Builder(null, null).build()
        assertingMinimalConfigWithTitle(null)
    }

    @Test
    fun minimalInstantiation_buildingTitleFromNullHeaderPartAndNumber_intoMetadataTitle() {
        config = ScipamatoPdfExporterConfiguration.Builder(null, 3L).build()
        assertingMinimalConfigWithTitle("3")
    }

    private fun assertingMinimalConfigWithTitle(title: String?) {
        config.metadataCreator.shouldBeNull()
        config.metadataAuthor.shouldBeNull()
        config.metadataTitle shouldBeEqualTo title
        config.metadataSubject.shouldBeNull()
        config.metadataKeywords.shouldBeNull()
        config.isCompressed.shouldBeFalse()
    }

    @Test
    fun minimalInstantiation_buildingTitleAsIsIntoMetadataTitle() {
        config = ScipamatoPdfExporterConfiguration.Builder(TITLE).build()
        assertingMinimalConfigWithTitle(TITLE)
    }

    @Test
    fun withAuthor_asString_setsMetadataAuthor() {
        config = ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withAuthor("author")
            .build()
        config.metadataCreator.shouldBeNull()
        config.metadataAuthor shouldBeEqualTo "author"
        config.metadataTitle shouldBeEqualTo TITLE
        config.metadataSubject.shouldBeNull()
        config.metadataKeywords.shouldBeNull()
        config.isCompressed.shouldBeFalse()
    }

    @Test
    fun withAuthor_asUser_setsMetadataAuthorFromFirstAndLastName() {
        val user = User(3, "username", "first", "last", "email", "pw")
        config = ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withAuthor(user)
            .build()
        config.metadataCreator.shouldBeNull()
        config.metadataAuthor shouldBeEqualTo "first last"
        config.metadataTitle shouldBeEqualTo TITLE
        config.metadataSubject.shouldBeNull()
        config.metadataKeywords.shouldBeNull()
        config.isCompressed.shouldBeFalse()
    }

    @Test
    fun withAuthor_asNullUser_doesNotSetMetadataAuthor() {
        config = ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withAuthor(null as User?)
            .build()
        config.metadataAuthor.shouldBeNull()
    }

    @Test
    fun withPaperTitle_concatenatesTitleAndPaperTitleIntoMetadataTitle() {
        config = ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withPaperTitle("papertitle")
            .build()
        config.metadataCreator.shouldBeNull()
        config.metadataAuthor.shouldBeNull()
        config.metadataTitle shouldBeEqualTo "$TITLE - papertitle"
        config.metadataSubject.shouldBeNull()
        config.metadataKeywords.shouldBeNull()
        config.isCompressed.shouldBeFalse()
    }

    @Test
    fun withPaperTitle_withNullTitle_onlyAppliesPaperTitle() {
        config = ScipamatoPdfExporterConfiguration.Builder(null)
            .withPaperTitle("papertitle")
            .build()
        config.metadataTitle shouldBeEqualTo "papertitle"
    }

    @Test
    fun withNullPaperTitle_withTitleAndAuthor_appliesTitleAndAuthor() {
        config = ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withPaperTitle(null)
            .withPaperAuthor("author")
            .build()
        config.metadataTitle shouldBeEqualTo "title - author et al."
    }

    @Test
    fun withPaperTitle_concatenatesTitleAndPaperAuthorAndPaperTitleIntoMetadataTitle() {
        config = ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withPaperTitle("papertitle")
            .withPaperAuthor("paperAuthor")
            .build()
        config.metadataCreator.shouldBeNull()
        config.metadataAuthor.shouldBeNull()
        config.metadataTitle shouldBeEqualTo "$TITLE - paperAuthor et al.: papertitle"
        config.metadataSubject.shouldBeNull()
        config.metadataKeywords.shouldBeNull()
        config.isCompressed.shouldBeFalse()
    }

    @Test
    fun withSubject_setsMetadataSubject() {
        config = ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withSubject("subject")
            .build()
        config.metadataCreator.shouldBeNull()
        config.metadataAuthor.shouldBeNull()
        config.metadataTitle shouldBeEqualTo TITLE
        config.metadataSubject shouldBeEqualTo "subject"
        config.metadataKeywords.shouldBeNull()
        config.isCompressed.shouldBeFalse()
    }

    @Test
    fun withCreator_setsMetadataCreator() {
        config = ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withCreator("creator")
            .build()
        config.metadataCreator shouldBeEqualTo "creator"
        config.metadataAuthor.shouldBeNull()
        config.metadataTitle shouldBeEqualTo TITLE
        config.metadataSubject.shouldBeNull()
        config.metadataKeywords.shouldBeNull()
        config.isCompressed.shouldBeFalse()
    }

    @Test
    fun withSingleCodeSpaceLess_setsMetadataKeywords() {
        codes.add(Code("1A", "code1", null, false, 1, "c1", "", 1))
        config = ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withCodes(codes)
            .build()
        config.metadataCreator.shouldBeNull()
        config.metadataAuthor.shouldBeNull()
        config.metadataTitle shouldBeEqualTo TITLE
        config.metadataSubject.shouldBeNull()
        config.metadataKeywords shouldBeEqualTo "code1"
        config.isCompressed.shouldBeFalse()
    }

    @Test
    fun withDoubleCodesBothSpaceLess_setsMetadataKeywordsCommaSeparated() {
        codes.add(Code("1A", "code1", null, false, 1, "c1", "", 1))
        codes.add(Code("2B", "code2", null, false, 2, "c2", "", 1))
        config = ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withCodes(codes)
            .build()
        config.metadataKeywords shouldBeEqualTo "code1,code2"
    }

    @Test
    fun withTripleCodesOneWithSpaces_setsMetadataKeywordsCommaSeparatedAndPartiallyQuoted() {
        codes.add(Code("1A", "code1", null, false, 1, "c1", "", 1))
        codes.add(Code("2B", "code2 with spaces", null, false, 2, "c2", "", 1))
        codes.add(Code("3C", "code3", null, false, 3, "c3", "", 1))
        config = ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withCodes(codes)
            .build()
        config.metadataKeywords shouldBeEqualTo "code1,\"code2 with spaces\",code3"
    }

    @Test
    fun withCompression() {
        config = ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withCompression()
            .build()
        config.metadataCreator.shouldBeNull()
        config.metadataAuthor.shouldBeNull()
        config.metadataTitle shouldBeEqualTo TITLE
        config.metadataSubject.shouldBeNull()
        config.metadataKeywords.shouldBeNull()
        config.isCompressed.shouldBeTrue()
    }

    @Test
    fun withAllAttributes() {
        codes.add(Code("1A", "c1", null, false, 1, "c1", "", 1))
        codes.add(Code("2B", "c2 with spaces", null, false, 2, "c2", "", 1))
        config = ScipamatoPdfExporterConfiguration.Builder("hp:", 10L)
            .withAuthor("a")
            .withPaperTitle("pt")
            .withPaperAuthor("pa")
            .withSubject("s")
            .withCreator("c")
            .withCodes(codes)
            .withCompression()
            .build()
        config.metadataCreator shouldBeEqualTo "c"
        config.metadataAuthor shouldBeEqualTo "a"
        config.metadataTitle shouldBeEqualTo "hp: 10 - pa et al.: pt"
        config.metadataSubject shouldBeEqualTo "s"
        config.metadataKeywords shouldBeEqualTo "c1,\"c2 with spaces\""
        config.isCompressed.shouldBeTrue()
    }

    @Test
    fun withNullCodes_returnsNullMetadataKeywords() {
        config = ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withCodes(null)
            .build()
        config.metadataCreator.shouldBeNull()
        config.metadataAuthor.shouldBeNull()
        config.metadataTitle shouldBeEqualTo TITLE
        config.metadataSubject.shouldBeNull()
        config.metadataKeywords.shouldBeNull()
        config.isCompressed.shouldBeFalse()
    }

    companion object {
        private const val TITLE = "title"
    }
}
