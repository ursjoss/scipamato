package ch.difty.scipamato.public_.web.model;

import static ch.difty.scipamato.common.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.public_.entity.CodeClass;
import ch.difty.scipamato.public_.persistence.api.CodeClassService;

public class CodeClassModelTest extends ModelTest {

    @MockBean
    private CodeClassService serviceMock;

    @Test
    public void instantiating_withNullLanguageCode_throws() {
        assertDegenerateSupplierParameter(() -> new CodeClassModel(null), "languageCode");
    }

    @Test
    public void loading_delegatesToCodeClassService() {
        String languageCode = "de";

        final List<CodeClass> codeClasses = new ArrayList<>();
        codeClasses.add(new CodeClass(1, "en", "cc1", ""));
        codeClasses.add(new CodeClass(2, "en", "cc2", ""));

        when(serviceMock.find(languageCode)).thenReturn(codeClasses);

        final CodeClassModel model = new CodeClassModel("de");

        assertThat(extractProperty(CodeClass.NAME).from(model.load())).containsExactly("cc1", "cc2");

        verify(serviceMock).find(languageCode);

        verifyNoMoreInteractions(serviceMock);
    }

}
