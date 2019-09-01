package ch.difty.scipamato.core.web.model;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.core.entity.Code;

class CodeModelTest extends ModelTest {

    @Test
    void instantiating_withNullCodeClassId_throws() {
        assertDegenerateSupplierParameter(() -> new CodeModel(null, "de"), "codeClassId");
    }

    @Test
    void instantiating_withNullLanguageCode_throws() {
        assertDegenerateSupplierParameter(() -> new CodeModel(CodeClassId.CC1, null), "languageCode");
    }

    @Test
    void loading_delegatesToCodeService() {
        CodeClassId ccId = CodeClassId.CC1;
        String languageCode = "de";

        final List<Code> codes = new ArrayList<>();
        codes.add(new Code("1F", "code 1F", null, false, 1, "cc1", "", 1));
        codes.add(new Code("1N", "code 1N", null, false, 1, "cc1", "", 13));

        when(codeServiceMock.findCodesOfClass(ccId, languageCode)).thenReturn(codes);

        final CodeModel model = new CodeModel(CodeClassId.CC1, "de");

        assertThat(model.load())
            .extracting(Code.CodeFields.CODE.getFieldName())
            .containsExactly("1F", "1N");

        verify(codeServiceMock).findCodesOfClass(ccId, languageCode);

        verifyNoMoreInteractions(codeServiceMock);
    }

}
