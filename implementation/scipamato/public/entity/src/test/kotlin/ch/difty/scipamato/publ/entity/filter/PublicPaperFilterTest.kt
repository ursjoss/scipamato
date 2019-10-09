package ch.difty.scipamato.publ.entity.filter

import ch.difty.scipamato.publ.entity.Code
import ch.difty.scipamato.publ.entity.Keyword
import ch.difty.scipamato.publ.entity.PopulationCode
import ch.difty.scipamato.publ.entity.StudyDesignCode
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PublicPaperFilterTest {

    @Suppress("MaximumLineLength", "MaxLineLength")
    @Test
    fun construct() {
        val filter = PublicPaperFilter()
        filter.number = 1L
        filter.authorMask = "am"
        filter.titleMask = "tm"
        filter.methodsMask = "mm"
        filter.publicationYearFrom = 2000
        filter.publicationYearUntil = 3000
        filter.populationCodes = listOf(PopulationCode.CHILDREN, PopulationCode.ADULTS)
        filter.studyDesignCodes = listOf(StudyDesignCode.EXPERIMENTAL)
        filter.codesOfClass1 = listOf(newCode("1A"), newCode("1B"))
        filter.codesOfClass2 = listOf(newCode("2A"), newCode("2B"))
        filter.codesOfClass3 = listOf(newCode("3A"), newCode("3B"))
        filter.codesOfClass4 = listOf(newCode("4A"), newCode("4B"))
        filter.codesOfClass5 = listOf(newCode("5A"), newCode("5B"))
        filter.codesOfClass6 = listOf(newCode("6A"), newCode("6B"))
        filter.codesOfClass7 = listOf(newCode("7A"), newCode("7B"))
        filter.codesOfClass8 = listOf(newCode("8A"), newCode("8B"))
        filter.keywords = listOf(Keyword(1, 1, "de", "k1", null))

        assertThat(filter.number).isEqualTo(1L)
        assertThat(filter.authorMask).isEqualTo("am")
        assertThat(filter.titleMask).isEqualTo("tm")
        assertThat(filter.methodsMask).isEqualTo("mm")
        assertThat(filter.publicationYearFrom).isEqualTo(2000)
        assertThat(filter.publicationYearUntil).isEqualTo(3000)

        assertThat(filter.populationCodes).contains(PopulationCode.CHILDREN, PopulationCode.ADULTS)
        assertThat(filter.studyDesignCodes).contains(StudyDesignCode.EXPERIMENTAL)

        assertThat(filter.codesOfClass1).hasSize(2)
        assertThat(filter.codesOfClass2).hasSize(2)
        assertThat(filter.codesOfClass3).hasSize(2)
        assertThat(filter.codesOfClass4).hasSize(2)
        assertThat(filter.codesOfClass5).hasSize(2)
        assertThat(filter.codesOfClass6).hasSize(2)
        assertThat(filter.codesOfClass7).hasSize(2)
        assertThat(filter.codesOfClass8).hasSize(2)

        assertThat(filter.keywords.map { it.keywordId }).containsExactly(1)

        assertThat(filter.toString()).isEqualTo(
            "PublicPaperFilter(number=1, authorMask=am, titleMask=tm, methodsMask=mm, publicationYearFrom=2000, publicationYearUntil=3000, populationCodes=[CHILDREN, ADULTS], studyDesignCodes=[EXPERIMENTAL], " +
                "codesOfClass1=[Code(codeClassId=1, code=1A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=1, code=1B, langCode=en, name=null, comment=null, sort=0)], " +
                "codesOfClass2=[Code(codeClassId=2, code=2A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=2, code=2B, langCode=en, name=null, comment=null, sort=0)], " +
                "codesOfClass3=[Code(codeClassId=3, code=3A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=3, code=3B, langCode=en, name=null, comment=null, sort=0)], " +
                "codesOfClass4=[Code(codeClassId=4, code=4A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=4, code=4B, langCode=en, name=null, comment=null, sort=0)], " +
                "codesOfClass5=[Code(codeClassId=5, code=5A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=5, code=5B, langCode=en, name=null, comment=null, sort=0)], " +
                "codesOfClass6=[Code(codeClassId=6, code=6A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=6, code=6B, langCode=en, name=null, comment=null, sort=0)], " +
                "codesOfClass7=[Code(codeClassId=7, code=7A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=7, code=7B, langCode=en, name=null, comment=null, sort=0)], " +
                "codesOfClass8=[Code(codeClassId=8, code=8A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=8, code=8B, langCode=en, name=null, comment=null, sort=0)], " +
                "keywords=[Keyword(id=1, keywordId=1, langCode=de, name=k1, searchOverride=null)])")
    }

    private fun newCode(code: String): Code {
        return Code.builder()
            .code(code)
            .codeClassId(Integer.parseInt(code.substring(0, 1)))
            .langCode("en")
            .build()
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(PublicPaperFilter::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        assertThat(PublicPaperFilter.PublicPaperFilterFields.values().map { it.fieldName })
            .containsExactly("number", "authorMask", "titleMask", "methodsMask", "publicationYearFrom",
                "publicationYearUntil", "populationCodes", "studyDesignCodes", "codesOfClass1", "codesOfClass2",
                "codesOfClass3", "codesOfClass4", "codesOfClass5", "codesOfClass6", "codesOfClass7", "codesOfClass8",
                "keywords")
    }
}
