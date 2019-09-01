package ch.difty.scipamato.publ.web.model;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.publ.entity.Keyword;
import ch.difty.scipamato.publ.persistence.api.KeywordService;

class KeywordModelTest extends ModelTest {

    @MockBean
    private KeywordService serviceMock;

    @Test
    void instantiating_withNullLanguageCode_throws() {
        assertDegenerateSupplierParameter(() -> new KeywordModel(null), "languageCode");
    }

    @Test
    void loading_delegatesToService() {
        String languageCode = "de";

        final List<Keyword> keywords = new ArrayList<>();
        keywords.add(new Keyword(10, 1, "en", "k1", null));
        keywords.add(new Keyword(11, 2, "en", "k2", null));

        when(serviceMock.findKeywords(languageCode)).thenReturn(keywords);

        final KeywordModel model = new KeywordModel("de");

        assertThat(model.load())
            .extracting(Keyword.KeywordFields.NAME.getName())
            .containsExactly("k1", "k2");

        verify(serviceMock).findKeywords(languageCode);

        verifyNoMoreInteractions(serviceMock);
    }
}
