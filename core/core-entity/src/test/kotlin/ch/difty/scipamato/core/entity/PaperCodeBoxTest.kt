package ch.difty.scipamato.core.entity

import ch.difty.scipamato.common.entity.CodeClassId
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class PaperCodeBoxTest {

    private val codeBox = PaperCodeBox()

    @Test
    fun newCodeBox_withNoCodes() {
        codeBox.isEmpty.shouldBeTrue()
        codeBox.size() shouldBeEqualTo 0
        codeBox.codes.shouldBeEmpty()
    }

    @Test
    fun addingCodes_increasesSize() {
        codeBox.addCode(CODE_1F)
        codeBox.isEmpty.shouldBeFalse()
        codeBox.size() shouldBeEqualTo 1
        codeBox.codes shouldContainAll listOf(CODE_1F)

        codeBox.addCode(CODE_5H)
        codeBox.size() shouldBeEqualTo 2
        codeBox.codes shouldContainAll listOf(CODE_1F, CODE_5H)
    }

    @Test
    fun addingNull_isIgnored() {
        codeBox.addCode(null)
        codeBox.size() shouldBeEqualTo 0
        codeBox.codes.shouldBeEmpty()
    }

    @Test
    fun addingSameCodeTwice_onlyAddsItOnce() {
        codeBox.addCode(CODE_1F)
        codeBox.addCode(CODE_1F)
        codeBox.size() shouldBeEqualTo 1
        codeBox.codes shouldContainAll listOf(CODE_1F)
    }

    @Test
    fun gettingCodes_andThenAlteringList_doesNotModifyOriginal() {
        val codes = codeBox.codes
        codes.contains(CODE_5H).shouldBeFalse()
        codeBox.codes.add(CODE_5H)
        codeBox.codes shouldBeEqualTo codes
    }

    @Test
    fun addingMultipleCodesWithEmptyList_leavesCodesAsIs() {
        codeBox.addCode(CODE_1F)
        codeBox.addCodes(emptyList())
        codeBox.codes shouldContainSame listOf(CODE_1F)
    }

    @Test
    fun addingMultipleCodes_addsEachExactlyOnceExceptNull() {
        codeBox.addCodes(listOf(CODE_1F, CODE_5F, CODE_5H, CODE_1F))
        codeBox.codes shouldContainAll listOf(CODE_1F, CODE_5F, CODE_5H)
    }

    @Test
    fun clearingCodes() {
        codeBox.addCodes(listOf(CODE_1F, CODE_5F))
        codeBox.size() shouldBeEqualTo 2

        codeBox.clear()

        codeBox.isEmpty.shouldBeTrue()
    }

    @Test
    fun gettingCodesByCodeClass() {
        codeBox.addCodes(listOf(CODE_1F, CODE_5H, CODE_5F))

        codeBox.getCodesBy(CodeClassId.CC1) shouldContainAll listOf(CODE_1F)
        codeBox.getCodesBy(CodeClassId.CC2).shouldBeEmpty()
        codeBox.getCodesBy(CodeClassId.CC5) shouldContainAll listOf(CODE_5H, CODE_5F)
    }

    @Test
    fun clearingByCodeClassId_leavesOtherCategoriesUntouched() {
        codeBox.addCodes(listOf(CODE_1F, CODE_5H, CODE_5F))
        codeBox.clearBy(CodeClassId.CC1)
        codeBox.codes shouldContainAll listOf(CODE_5H, CODE_5F)
        codeBox.clearBy(CodeClassId.CC2)
        codeBox.codes shouldContainAll listOf(CODE_5H, CODE_5F)
        codeBox.clearBy(CodeClassId.CC5)
        codeBox.isEmpty.shouldBeTrue()
    }

    @Test
    fun sizePerCodeClass() {
        codeBox.addCodes(listOf(CODE_1F, CODE_5H, CODE_5F))
        codeBox.sizeOf(CodeClassId.CC1) shouldBeEqualTo 1
        codeBox.sizeOf(CodeClassId.CC2) shouldBeEqualTo 0
        codeBox.sizeOf(CodeClassId.CC5) shouldBeEqualTo 2
    }

    @Test
    fun assertingToString_withNoCodes() {
        codeBox.isEmpty.shouldBeTrue()
        codeBox.toString() shouldBeEqualTo "[]"
    }

    @Test
    @Disabled("TODO")
    fun assertingToString_withMembers() {
        codeBox.addCodes(listOf(CODE_1F, CODE_5H, CODE_5F))
        codeBox.toString() shouldBeEqualTo
            // @formatter:off
              "[" +
                "codesOfClass1=[" +
                    "Code[code=1F,name=Code 1F,comment=<null>,internal=false,codeClass=CodeClass[id=1],sort=1," +
                         "createdBy=1,lastModifiedBy=2,created=2017-01-01T08:00:00.123," +
                         "lastModified=2017-01-02T09:00:00.456,version=3]" +
                "]" +
                ",codesOfClass5=[" +
                    "Code[code=5H,name=Code 5H,comment=<null>,internal=false,codeClass=CodeClass[id=5],sort=7," +
                        "createdBy=1,lastModifiedBy=2,created=2017-01-01T08:00:00.123," +
                        "lastModified=2017-01-02T09:00:00.456,version=3]" +
                    ", Code[code=5F,name=Code 5F,comment=<null>,internal=false,codeClass=CodeClass[id=5],sort=5," +
                         "createdBy=1,lastModifiedBy=2,created=2017-01-01T08:00:00.123," +
                         "lastModified=2017-01-02T09:00:00.456,version=3]" +
                    "]" +
                "]"
            // @formatter:on
    }

    @Test
    fun equality_ofEmptyCodeBoxes() {
        val cb1 = PaperCodeBox()
        val cb2 = PaperCodeBox()
        assertEqualityOf(cb1, cb2)
    }

    @Test
    fun equality_ofCodeHoldingCodeBoxes() {
        val cb1 = PaperCodeBox()
        cb1.addCode(CODE_1F)
        cb1.addCode(CODE_5H)
        val cb2 = PaperCodeBox()
        cb2.addCode(CODE_1F)
        cb2.addCode(CODE_5H)
        assertEqualityOf(cb1, cb2)
    }

    @Test
    fun equality_ofCodeHoldingCodeBoxes_despiteDifferentOrder() {
        val cb1 = PaperCodeBox()
        cb1.addCode(CODE_1F)
        cb1.addCode(CODE_5H)
        val cb2 = PaperCodeBox()
        cb2.addCode(CODE_5H)
        cb2.addCode(CODE_1F)
        assertEqualityOf(cb1, cb2)
    }

    private fun assertEqualityOf(cb1: CodeBox, cb2: CodeBox) {
        (cb1 == cb1).shouldBeTrue()
        (cb2 == cb2).shouldBeTrue()
        (cb1 == cb2).shouldBeTrue()
        cb1.hashCode() shouldBeEqualTo cb2.hashCode()
    }

    @Test
    fun inequality_ofCodeBoxes() {
        val cb1 = PaperCodeBox()
        cb1.addCode(CODE_1F)
        val cb2 = PaperCodeBox()
        cb2.addCode(CODE_1F)
        cb2.addCode(CODE_5H)
        assertInequalityOf(cb1, cb2)
    }

    @Test
    fun inequality_ofCodeBoxes2() {
        val cb1 = PaperCodeBox()
        val cb2 = PaperCodeBox()
        cb2.addCode(CODE_5H)
        assertInequalityOf(cb1, cb2)
    }

    private fun assertInequalityOf(cb1: CodeBox, cb2: CodeBox) {
        (cb1 == cb2).shouldBeFalse()
        cb1.hashCode() shouldNotBeEqualTo cb2.hashCode()
    }

    companion object {
        private val CREAT = LocalDateTime.parse("2017-01-01T08:00:00.123")
        private val MOD = LocalDateTime.parse("2017-01-02T09:00:00.456")

        private val CODE_1F = makeCode(CodeClassId.CC1, "F", 1)
        private val CODE_5H = makeCode(CodeClassId.CC5, "H", 7)
        private val CODE_5F = makeCode(CodeClassId.CC5, "F", 5)

        private fun makeCode(codeClassId: CodeClassId, codePart2: String, sort: Int): Code {
            val ccId = codeClassId.id
            val code = ccId.toString() + codePart2

            return Code(code, "Code $code", null, false, ccId, codeClassId.name, "", sort, CREAT, 1, MOD, 2, 3)
        }
    }
}
