package ch.difty.scipamato.publ.web.model;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.publ.entity.Code;
import ch.difty.scipamato.publ.persistence.api.CodeService;

public class CodeModelTest extends ModelTest {

    @MockBean
    private CodeService serviceMock;

    @Test
    public void instantiating_withNullCodeClassId_throws() {
        assertDegenerateSupplierParameter(() -> new CodeModel(null, "de"), "codeClassId");
    }

    @Test
    public void instantiating_withNullLanguageCode_throws() {
        assertDegenerateSupplierParameter(() -> new CodeModel(CodeClassId.CC1, null), "languageCode");
    }

    @Test
    public void loading_delegatesToCodeService() {
        CodeClassId ccId = CodeClassId.CC1;
        String languageCode = "de";

        final List<Code> codes = new ArrayList<>();
        codes.add(new Code(1, "1F", "en", "code 1F", null, 1));
        codes.add(new Code(1, "1N", "en", "code 1N", null, 2));

        when(serviceMock.findCodesOfClass(ccId, languageCode)).thenReturn(codes);

        final CodeModel model = new CodeModel(CodeClassId.CC1, "de");

        assertThat(extractProperty(Code.CodeFields.CODE.getName()).from(model.load())).containsExactly("1F", "1N");

        verify(serviceMock).findCodesOfClass(ccId, languageCode);

        verifyNoMoreInteractions(serviceMock);
    }

}
