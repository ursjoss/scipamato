package ch.difty.sipamato.entity;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class CodeBoxTest {

    private static final Code CODE_1F = makeCode(CodeClassId.CC1, "F");
    private static final Code CODE_5H = makeCode(CodeClassId.CC5, "H");
    private static final Code CODE_5F = makeCode(CodeClassId.CC5, "F");

    private final CodeBox codeBox = new CodeBox();

    private static Code makeCode(CodeClassId codeClassId, String codePart2) {
        int ccId = codeClassId.getId();
        String code = ccId + codePart2;
        return new Code(code, "Code " + code, ccId, codeClassId.name(), "");
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

}
