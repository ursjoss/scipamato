package ch.difty.scipamato.publ.entity.filter

import ch.difty.scipamato.publ.entity.Code
import ch.difty.scipamato.publ.entity.Keyword
import ch.difty.scipamato.publ.entity.PopulationCode
import ch.difty.scipamato.publ.entity.StudyDesignCode
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

internal class PublicPaperFilterTest {

    @Suppress("MaximumLineLength", "MaxLineLength")
    @Test
    fun construct() {
        val filter = PublicPaperFilter(
            number = 1L,
            authorMask = "am",
            titleMask = "tm",
            methodsMask = "mm",
            publicationYearFrom = 2000,
            publicationYearUntil = 3000,
            populationCodes = listOf(PopulationCode.CHILDREN, PopulationCode.ADULTS),
            studyDesignCodes = listOf(StudyDesignCode.EXPERIMENTAL),
            codesOfClass1 = listOf(newCode("1A"), newCode("1B")),
            codesOfClass2 = listOf(newCode("2A"), newCode("2B")),
            codesOfClass3 = listOf(newCode("3A"), newCode("3B")),
            codesOfClass4 = listOf(newCode("4A"), newCode("4B")),
            codesOfClass5 = listOf(newCode("5A"), newCode("5B")),
            codesOfClass6 = listOf(newCode("6A"), newCode("6B")),
            codesOfClass7 = listOf(newCode("7A"), newCode("7B")),
            codesOfClass8 = listOf(newCode("8A"), newCode("8B")),
            keywords = listOf(Keyword(1, 1, "de", "k1", null)),
        )

        with(filter) {
            number shouldBeEqualTo 1L
            authorMask shouldBeEqualTo "am"
            titleMask shouldBeEqualTo "tm"
            methodsMask shouldBeEqualTo "mm"
            publicationYearFrom shouldBeEqualTo 2000
            publicationYearUntil shouldBeEqualTo 3000

            populationCodes?.shouldContainSame(listOf(PopulationCode.CHILDREN, PopulationCode.ADULTS))
            studyDesignCodes?.shouldContainSame(listOf(StudyDesignCode.EXPERIMENTAL))

            codesOfClass1?.shouldHaveSize(2)
            codesOfClass2?.shouldHaveSize(2)
            codesOfClass3?.shouldHaveSize(2)
            codesOfClass4?.shouldHaveSize(2)
            codesOfClass5?.shouldHaveSize(2)
            codesOfClass6?.shouldHaveSize(2)
            codesOfClass7?.shouldHaveSize(2)
            codesOfClass8?.shouldHaveSize(2)

            keywords?.map { it.keywordId }?.shouldContainAll(listOf(1)) ?: fail("keywords should not be null")
        }

        filter.toString() shouldBeEqualTo
            "PublicPaperFilter(number=1, authorMask=am, titleMask=tm, methodsMask=mm, publicationYearFrom=2000, " +
            "publicationYearUntil=3000, populationCodes=[CHILDREN, ADULTS], studyDesignCodes=[EXPERIMENTAL], " +
            "codesOfClass1=[Code(codeClassId=1, code=1A, langCode=en, name=null, comment=null, sort=0), " +
            "Code(codeClassId=1, code=1B, langCode=en, name=null, comment=null, sort=0)], " +
            "codesOfClass2=[Code(codeClassId=2, code=2A, langCode=en, name=null, comment=null, sort=0), " +
            "Code(codeClassId=2, code=2B, langCode=en, name=null, comment=null, sort=0)], " +
            "codesOfClass3=[Code(codeClassId=3, code=3A, langCode=en, name=null, comment=null, sort=0), " +
            "Code(codeClassId=3, code=3B, langCode=en, name=null, comment=null, sort=0)], " +
            "codesOfClass4=[Code(codeClassId=4, code=4A, langCode=en, name=null, comment=null, sort=0), " +
            "Code(codeClassId=4, code=4B, langCode=en, name=null, comment=null, sort=0)], " +
            "codesOfClass5=[Code(codeClassId=5, code=5A, langCode=en, name=null, comment=null, sort=0), " +
            "Code(codeClassId=5, code=5B, langCode=en, name=null, comment=null, sort=0)], " +
            "codesOfClass6=[Code(codeClassId=6, code=6A, langCode=en, name=null, comment=null, sort=0), " +
            "Code(codeClassId=6, code=6B, langCode=en, name=null, comment=null, sort=0)], " +
            "codesOfClass7=[Code(codeClassId=7, code=7A, langCode=en, name=null, comment=null, sort=0), " +
            "Code(codeClassId=7, code=7B, langCode=en, name=null, comment=null, sort=0)], " +
            "codesOfClass8=[Code(codeClassId=8, code=8A, langCode=en, name=null, comment=null, sort=0), " +
            "Code(codeClassId=8, code=8B, langCode=en, name=null, comment=null, sort=0)], " +
            "keywords=[Keyword(id=1, keywordId=1, langCode=de, name=k1, searchOverride=null)])"
    }

    private fun newCode(code: String): Code = Code.builder()
        .code(code)
        .codeClassId(Integer.parseInt(code.substring(0, 1)))
        .langCode("en")
        .build()

    @Test
    fun equals() {
        EqualsVerifier.simple()
            .forClass(PublicPaperFilter::class.java)
            .withRedefinedSuperclass()
            .verify()
    }
}
