package ch.difty.scipamato.publ.web.model;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.publ.entity.CodeClass;
import ch.difty.scipamato.publ.persistence.api.CodeClassService;

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

        assertThat(extractProperty(CodeClass.CodeClassFields.NAME.getName()).from(model.load())).containsExactly("cc1",
            "cc2");

        verify(serviceMock).find(languageCode);

        verifyNoMoreInteractions(serviceMock);
    }

}
