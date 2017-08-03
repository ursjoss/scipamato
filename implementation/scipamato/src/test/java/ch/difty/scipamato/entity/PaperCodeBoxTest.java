package ch.difty.scipamato.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import ch.difty.scipamato.lib.NullArgumentException;

public class PaperCodeBoxTest {

    private static final LocalDateTime CREAT = LocalDateTime.parse("2017-01-01T08:00:00.123");
    private static final LocalDateTime MOD = LocalDateTime.parse("2017-01-02T09:00:00.456");

    private static final Code CODE_1F = makeCode(CodeClassId.CC1, "F", 1);
    private static final Code CODE_5H = makeCode(CodeClassId.CC5, "H", 7);
    private static final Code CODE_5F = makeCode(CodeClassId.CC5, "F", 5);

    private final PaperCodeBox codeBox = new PaperCodeBox();

    private static Code makeCode(CodeClassId codeClassId, String codePart2, int sort) {
        int ccId = codeClassId.getId();
        String code = ccId + codePart2;

        return new Code(code, "Code " + code, null, false, ccId, codeClassId.name(), "", sort, CREAT, 1, MOD, 2, 3);
    }

    @Test
    public void newCodeBox_withNoCodes() {
        assertThat(codeBox.isEmpty()).isTrue();
        assertThat(codeBox.size()).isEqualTo(0);
        assertThat(codeBox.getCodes()).isEmpty();
    }

    @Test
    public void addingCodes_increasesSize() {
        codeBox.addCode(CODE_1F);
        assertThat(codeBox.isEmpty()).isFalse();
        assertThat(codeBox.size()).isEqualTo(1);
        assertThat(codeBox.getCodes()).containsExactly(CODE_1F);

        codeBox.addCode(CODE_5H);
        assertThat(codeBox.size()).isEqualTo(2);
        assertThat(codeBox.getCodes()).containsExactly(CODE_1F, CODE_5H);
    }

    @Test
    public void addingNull_isIgnored() {
        codeBox.addCode(null);
        assertThat(codeBox.size()).isEqualTo(0);
        assertThat(codeBox.getCodes()).isEmpty();
    }

    @Test
    public void addingSameCodeTwice_onlyAddsItOnce() {
        codeBox.addCode(CODE_1F);
        codeBox.addCode(CODE_1F);
        assertThat(codeBox.size()).isEqualTo(1);
        assertThat(codeBox.getCodes()).containsExactly(CODE_1F);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void gettingCodes_andThenAlteringList_throws() {
        codeBox.getCodes().add(CODE_5H);
    }

    @Test
    public void addingMultipleCodesWithNullOrEmptyList_leavesCodesAsIs() {
        codeBox.addCode(CODE_1F);
        codeBox.addCodes(null);
        codeBox.addCodes(new ArrayList<Code>());
        assertThat(codeBox.getCodes()).containsOnly(CODE_1F);
    }

    @Test
    public void addingMultipleCodes_addsEachExactlyOnceExceptNull() {
        codeBox.addCodes(Arrays.asList(CODE_1F, CODE_5F, null, CODE_5H, CODE_1F));
        assertThat(codeBox.getCodes()).containsExactly(CODE_1F, CODE_5F, CODE_5H);
    }

    @Test
    public void clearingCodes() {
        codeBox.addCodes(Arrays.asList(CODE_1F, CODE_5F));
        assertThat(codeBox.size()).isEqualTo(2);

        codeBox.clear();

        assertThat(codeBox.isEmpty()).isTrue();
    }

    @Test
    public void gettingCodesByCodeClass_withNullCodeClassId_throws() {
        try {
            codeBox.getCodesBy(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("codeClassId must not be null.");
        }
    }

    @Test
    public void gettingCodesByCodeClass() {
        codeBox.addCodes(Arrays.asList(CODE_1F, CODE_5H, CODE_5F));

        assertThat(codeBox.getCodesBy(CodeClassId.CC1)).containsExactly(CODE_1F);
        assertThat(codeBox.getCodesBy(CodeClassId.CC2)).isEmpty();
        assertThat(codeBox.getCodesBy(CodeClassId.CC5)).containsExactly(CODE_5H, CODE_5F);
    }

    @Test
    public void clearingByCodeClassId_withNullParameter_throws() {
        try {
            codeBox.clearBy(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("codeClassId must not be null.");
        }
    }

    @Test
    public void clearingByCodeClassId_leavesOtherCategoriesUntouched() {
        codeBox.addCodes(Arrays.asList(CODE_1F, CODE_5H, CODE_5F));
        codeBox.clearBy(CodeClassId.CC1);
        assertThat(codeBox.getCodes()).containsExactly(CODE_5H, CODE_5F);
        codeBox.clearBy(CodeClassId.CC2);
        assertThat(codeBox.getCodes()).containsExactly(CODE_5H, CODE_5F);
        codeBox.clearBy(CodeClassId.CC5);
        assertThat(codeBox.isEmpty()).isTrue();
    }

    @Test
    public void sizePerCodeClass_withNullCodeClass_throws() {
        try {
            codeBox.sizeOf(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("codeClassId must not be null.");
        }
    }

    @Test
    public void sizePerCodeClass() {
        codeBox.addCodes(Arrays.asList(CODE_1F, CODE_5H, CODE_5F));
        assertThat(codeBox.sizeOf(CodeClassId.CC1)).isEqualTo(1);
        assertThat(codeBox.sizeOf(CodeClassId.CC2)).isEqualTo(0);
        assertThat(codeBox.sizeOf(CodeClassId.CC5)).isEqualTo(2);
    }

    @Test
    public void assertingToString_withNoCodes() {
        assertThat(codeBox.isEmpty()).isTrue();
        assertThat(codeBox.toString()).isEqualTo("[]");
    }

    @Test
    public void assertingToString_withMembers() {
        codeBox.addCodes(Arrays.asList(CODE_1F, CODE_5H, CODE_5F));
        assertThat(codeBox.toString()).isEqualTo(
        // @formatter:off
              "["
            +   "codesOfClass1=["
            +     "Code[code=1F,name=Code 1F,comment=<null>,internal=false,codeClass=CodeClass[id=1],sort=1,created=2017-01-01T08:00:00.123,createdBy=1,lastModified=2017-01-02T09:00:00.456,lastModifiedBy=2,version=3]"
            +   "]"
            +  ",codesOfClass5=["
            +     "Code[code=5H,name=Code 5H,comment=<null>,internal=false,codeClass=CodeClass[id=5],sort=7,created=2017-01-01T08:00:00.123,createdBy=1,lastModified=2017-01-02T09:00:00.456,lastModifiedBy=2,version=3]"
            +   ", Code[code=5F,name=Code 5F,comment=<null>,internal=false,codeClass=CodeClass[id=5],sort=5,created=2017-01-01T08:00:00.123,createdBy=1,lastModified=2017-01-02T09:00:00.456,lastModifiedBy=2,version=3]"
            +   "]"
            + "]"
         // @formatter:on
        );
    }

    @Test
    public void equality_ofEmptyCodeBoxes() {
        CodeBox cb1 = new PaperCodeBox();
        CodeBox cb2 = new PaperCodeBox();
        assertEqualityOf(cb1, cb2);
    }

    @Test
    public void equality_ofCodeHoldingCodeBoxes() {
        CodeBox cb1 = new PaperCodeBox();
        cb1.addCode(CODE_1F);
        cb1.addCode(CODE_5H);
        CodeBox cb2 = new PaperCodeBox();
        cb2.addCode(CODE_1F);
        cb2.addCode(CODE_5H);
        assertEqualityOf(cb1, cb2);
    }

    @Test
    public void equality_ofCodeHoldingCodeBoxes_despiteDifferentOrder() {
        CodeBox cb1 = new PaperCodeBox();
        cb1.addCode(CODE_1F);
        cb1.addCode(CODE_5H);
        CodeBox cb2 = new PaperCodeBox();
        cb2.addCode(CODE_5H);
        cb2.addCode(CODE_1F);
        assertEqualityOf(cb1, cb2);
    }

    private void assertEqualityOf(CodeBox cb1, CodeBox cb2) {
        assertThat(cb1.equals(cb1)).isTrue();
        assertThat(cb2.equals(cb2)).isTrue();
        assertThat(cb1.equals(null)).isFalse();
        assertThat(cb1.equals("")).isFalse();
        assertThat(cb2.equals("")).isFalse();
        assertThat(cb1.equals(cb2)).isTrue();
        assertThat(cb1.hashCode()).isEqualTo(cb2.hashCode());
    }

    @Test
    public void inequality_ofCodeBoxes() {
        CodeBox cb1 = new PaperCodeBox();
        cb1.addCode(CODE_1F);
        CodeBox cb2 = new PaperCodeBox();
        cb2.addCode(CODE_1F);
        cb2.addCode(CODE_5H);
        assertInequalityOf(cb1, cb2);
    }

    @Test
    public void inequality_ofCodeBoxes2() {
        CodeBox cb1 = new PaperCodeBox();
        CodeBox cb2 = new PaperCodeBox();
        cb2.addCode(CODE_5H);
        assertInequalityOf(cb1, cb2);
    }

    private void assertInequalityOf(CodeBox cb1, CodeBox cb2) {
        assertThat(cb1.equals(cb2)).isFalse();
        assertThat(cb1.hashCode()).isNotEqualTo(cb2.hashCode());
    }

}
