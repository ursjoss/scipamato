package ch.difty.scipamato.web.model;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.entity.CodeClass;
import ch.difty.scipamato.lib.NullArgumentException;
import ch.difty.scipamato.service.CodeClassService;

public class CodeClassModelTest extends ModelTest {

    @MockBean
    private CodeClassService serviceMock;

    @Test
    public void instantiating_withNullLanguageCode_throws() {
        try {
            new CodeClassModel(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("languageCode must not be null.");
        }
    }

    @Test
    public void loading_delegatesToCodeClassService() {
        String languageCode = "de";

        final List<CodeClass> codeClasses = new ArrayList<>();
        codeClasses.add(new CodeClass(1, "cc1", ""));
        codeClasses.add(new CodeClass(2, "cc2", ""));

        when(serviceMock.find(languageCode)).thenReturn(codeClasses);

        final CodeClassModel model = new CodeClassModel("de");

        assertThat(extractProperty(CodeClass.NAME).from(model.load())).containsExactly("cc1", "cc2");

        verify(serviceMock).find(languageCode);

        verifyNoMoreInteractions(serviceMock);
    }

}
