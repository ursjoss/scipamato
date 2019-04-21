package ch.difty.scipamato.core.web.model;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.persistence.KeywordService;

public class KeywordModelTest extends ModelTest {

    @MockBean
    private KeywordService serviceMock;

    @Test
    public void instantiating_withNullLanguageCode_throws() {
        assertDegenerateSupplierParameter(() -> new KeywordModel(null), "languageCode");
    }

    @Test
    public void loading_delegatesToKeywordService() {
        String languageCode = "de";

        final List<Keyword> topics = new ArrayList<>();
        topics.add(new Keyword(1, "n1", null));
        topics.add(new Keyword(2, "n2", "nn2"));

        when(serviceMock.findAll(languageCode)).thenReturn(topics);

        final KeywordModel model = new KeywordModel("de");

        final List<Keyword> keywords = model.load();

        assertThat(keywords)
            .extracting(Keyword.KeywordFields.NAME.getName())
            .containsExactly("n1", "n2");
        assertThat(keywords)
            .extracting(Keyword.KeywordFields.SEARCH_OVERRIDE.getName())
            .containsExactly(null, "nn2");

        verify(serviceMock).findAll(languageCode);

        verifyNoMoreInteractions(serviceMock);
    }

}