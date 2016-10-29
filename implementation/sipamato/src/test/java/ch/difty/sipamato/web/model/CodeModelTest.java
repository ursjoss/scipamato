package ch.difty.sipamato.web.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.CodeClassId;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.service.CodeService;

public class CodeModelTest extends ModelTest {

    @MockBean
    private CodeService serviceMock;

    @Test
    public void instantiating_withNullCodeClassId_throws() {
        try {
            new CodeModel(null, "de");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("codeClassId must not be null.");
        }
    }

    @Test
    public void instantiating_withNullLanguageCode_throws() {
        try {
            new CodeModel(CodeClassId.CC1, null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("languageCode must not be null.");
        }
    }

    @Test
    public void loading_delegatesToCodeService() {
        CodeClassId ccId = CodeClassId.CC1;
        String languageCode = "de";

        final List<Code> codes = new ArrayList<>();
        codes.add(new Code("1F", "code 1F", 1, "cc1", "", 1));
        codes.add(new Code("1N", "code 1N", 1, "cc1", "", 13));

        when(serviceMock.findCodesOfClass(ccId, languageCode)).thenReturn(codes);

        final CodeModel model = new CodeModel(CodeClassId.CC1, "de");

        assertThat(extractProperty(Code.CODE).from(model.load())).containsExactly("1F", "1N");

        verify(serviceMock).findCodesOfClass(ccId, languageCode);

        verifyNoMoreInteractions(serviceMock);
    }

}
