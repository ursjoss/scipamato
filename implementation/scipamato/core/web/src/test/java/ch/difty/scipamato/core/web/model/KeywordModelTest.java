package ch.difty.scipamato.core.web.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.entity.keyword.Keyword;

class KeywordModelTest extends ModelTest {

    @Test
    void loading_delegatesToKeywordService() {
        String languageCode = "de";

        final List<Keyword> topics = new ArrayList<>();
        topics.add(new Keyword(1, "n1", null));
        topics.add(new Keyword(2, "n2", "nn2"));

        when(keywordServiceMock.findAll(languageCode)).thenReturn(topics);

        final KeywordModel model = new KeywordModel("de");

        final List<Keyword> keywords = model.load();

        assertThat(keywords)
            .extracting(Keyword.KeywordFields.NAME.getFieldName())
            .containsExactly("n1", "n2");
        assertThat(keywords)
            .extracting(Keyword.KeywordFields.SEARCH_OVERRIDE.getFieldName())
            .containsExactly(null, "nn2");

        verify(keywordServiceMock).findAll(languageCode);

        verifyNoMoreInteractions(keywordServiceMock);
    }
}
