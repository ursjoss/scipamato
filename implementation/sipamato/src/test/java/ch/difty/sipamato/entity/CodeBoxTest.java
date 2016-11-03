package ch.difty.sipamato.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import ch.difty.sipamato.lib.NullArgumentException;

public class CodeBoxTest {

    private static final Code CODE_1F = makeCode(CodeClassId.CC1, "F", 1);
    private static final Code CODE_5H = makeCode(CodeClassId.CC5, "H", 7);
    private static final Code CODE_5F = makeCode(CodeClassId.CC5, "F", 5);

    private final CodeBox codeBox = new CodeBox();

    private static Code makeCode(CodeClassId codeClassId, String codePart2, int sort) {
        int ccId = codeClassId.getId();
        String code = ccId + codePart2;
        return new Code(code, "Code " + code, null, ccId, codeClassId.name(), "", sort);
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
            +     "Code[code=1F,name=Code 1F,comment=<null>,codeClass=CodeClass[id=1],sort=1]"
            +   "]"
            +  ",codesOfClass5=["
            +     "Code[code=5H,name=Code 5H,comment=<null>,codeClass=CodeClass[id=5],sort=7]"
            +   ", Code[code=5F,name=Code 5F,comment=<null>,codeClass=CodeClass[id=5],sort=5]"
            +   "]"
            + "]"
         // @formatter:on
        );
    }
}
