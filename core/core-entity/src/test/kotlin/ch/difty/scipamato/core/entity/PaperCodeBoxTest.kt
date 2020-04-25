package ch.difty.scipamato.core.entity

import ch.difty.scipamato.common.entity.CodeClassId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class PaperCodeBoxTest {

    private val codeBox = PaperCodeBox()

    @Test
    fun newCodeBox_withNoCodes() {
        assertThat(codeBox.isEmpty).isTrue()
        assertThat(codeBox.size()).isEqualTo(0)
        assertThat(codeBox.codes).isEmpty()
    }

    @Test
    fun addingCodes_increasesSize() {
        codeBox.addCode(CODE_1F)
        assertThat(codeBox.isEmpty).isFalse()
        assertThat(codeBox.size()).isEqualTo(1)
        assertThat(codeBox.codes).containsExactly(CODE_1F)

        codeBox.addCode(CODE_5H)
        assertThat(codeBox.size()).isEqualTo(2)
        assertThat(codeBox.codes).containsExactly(CODE_1F, CODE_5H)
    }

    @Test
    fun addingNull_isIgnored() {
        codeBox.addCode(null)
        assertThat(codeBox.size()).isEqualTo(0)
        assertThat(codeBox.codes).isEmpty()
    }

    @Test
    fun addingSameCodeTwice_onlyAddsItOnce() {
        codeBox.addCode(CODE_1F)
        codeBox.addCode(CODE_1F)
        assertThat(codeBox.size()).isEqualTo(1)
        assertThat(codeBox.codes).containsExactly(CODE_1F)
    }

    @Test
    fun gettingCodes_andThenAlteringList_doesNotModifyOriginal() {
        val codes = codeBox.codes
        assertThat(codes.contains(CODE_5H)).isFalse()
        codeBox.codes.add(CODE_5H)
        assertThat(codeBox.codes).isEqualTo(codes)
    }

    @Test
    fun addingMultipleCodesWithEmptyList_leavesCodesAsIs() {
        codeBox.addCode(CODE_1F)
        codeBox.addCodes(emptyList())
        assertThat(codeBox.codes).containsOnly(CODE_1F)
    }

    @Test
    fun addingMultipleCodes_addsEachExactlyOnceExceptNull() {
        codeBox.addCodes(listOf(CODE_1F, CODE_5F, CODE_5H, CODE_1F))
        assertThat(codeBox.codes).containsExactly(CODE_1F, CODE_5F, CODE_5H)
    }

    @Test
    fun clearingCodes() {
        codeBox.addCodes(listOf(CODE_1F, CODE_5F))
        assertThat(codeBox.size()).isEqualTo(2)

        codeBox.clear()

        assertThat(codeBox.isEmpty).isTrue()
    }

    @Test
    fun gettingCodesByCodeClass() {
        codeBox.addCodes(listOf(CODE_1F, CODE_5H, CODE_5F))

        assertThat(codeBox.getCodesBy(CodeClassId.CC1)).containsExactly(CODE_1F)
        assertThat(codeBox.getCodesBy(CodeClassId.CC2)).isEmpty()
        assertThat(codeBox.getCodesBy(CodeClassId.CC5)).containsExactly(CODE_5H, CODE_5F)
    }

    @Test
    fun clearingByCodeClassId_leavesOtherCategoriesUntouched() {
        codeBox.addCodes(listOf(CODE_1F, CODE_5H, CODE_5F))
        codeBox.clearBy(CodeClassId.CC1)
        assertThat(codeBox.codes).containsExactly(CODE_5H, CODE_5F)
        codeBox.clearBy(CodeClassId.CC2)
        assertThat(codeBox.codes).containsExactly(CODE_5H, CODE_5F)
        codeBox.clearBy(CodeClassId.CC5)
        assertThat(codeBox.isEmpty).isTrue()
    }

    @Test
    fun sizePerCodeClass() {
        codeBox.addCodes(listOf(CODE_1F, CODE_5H, CODE_5F))
        assertThat(codeBox.sizeOf(CodeClassId.CC1)).isEqualTo(1)
        assertThat(codeBox.sizeOf(CodeClassId.CC2)).isEqualTo(0)
        assertThat(codeBox.sizeOf(CodeClassId.CC5)).isEqualTo(2)
    }

    @Test
    fun assertingToString_withNoCodes() {
        assertThat(codeBox.isEmpty).isTrue()
        assertThat(codeBox.toString()).isEqualTo("[]")
    }

    @Test
    @Disabled("TODO")
    fun assertingToString_withMembers() {
        codeBox.addCodes(listOf(CODE_1F, CODE_5H, CODE_5F))
        assertThat(codeBox.toString()).isEqualTo(
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
        )
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
        assertThat(cb1 == cb1).isTrue()
        assertThat(cb2 == cb2).isTrue()
        assertThat(cb1 == cb2).isTrue()
        assertThat(cb1.hashCode()).isEqualTo(cb2.hashCode())
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
        assertThat(cb1 == cb2).isFalse()
        assertThat(cb1.hashCode()).isNotEqualTo(cb2.hashCode())
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
