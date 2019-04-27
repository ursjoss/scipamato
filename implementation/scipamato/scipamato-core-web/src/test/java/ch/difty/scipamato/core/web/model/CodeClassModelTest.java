package ch.difty.scipamato.core.web.model;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.persistence.CodeClassService;

class CodeClassModelTest extends ModelTest {

    @MockBean
    private CodeClassService serviceMock;

    @Test
    void instantiating_withNullLanguageCode_throws() {
        assertDegenerateSupplierParameter(() -> new CodeClassModel(null), "languageCode");
    }

    @Test
    void loading_delegatesToCodeClassService() {
        String languageCode = "de";

        final List<CodeClass> codeClasses = new ArrayList<>();
        codeClasses.add(new CodeClass(1, "cc1", ""));
        codeClasses.add(new CodeClass(2, "cc2", ""));

        when(serviceMock.find(languageCode)).thenReturn(codeClasses);

        final CodeClassModel model = new CodeClassModel("de");

        assertThat(model.load())
            .extracting(Keyword.KeywordFields.NAME.getName())
            .containsExactly("cc1", "cc2");

        verify(serviceMock).find(languageCode);

        verifyNoMoreInteractions(serviceMock);
    }

}
