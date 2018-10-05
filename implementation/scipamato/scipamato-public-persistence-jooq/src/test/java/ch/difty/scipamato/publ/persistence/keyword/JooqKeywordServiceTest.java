package ch.difty.scipamato.publ.persistence.keyword;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.publ.entity.Keyword;

@RunWith(MockitoJUnitRunner.class)
public class JooqKeywordServiceTest {

    @Mock
    private KeywordRepository repoMock;

    @Test
    public void findingKeywords_delegatesToRepo() {
        JooqKeywordService service = new JooqKeywordService(repoMock);

        String languageCode = "de";

        List<Keyword> keywords = new ArrayList<>();
        keywords.add(new Keyword(1, 1, "en", "Keyword1", null));
        keywords.add(new Keyword(1, 1, "fr", "Keyword2", null));
        when(repoMock.findKeywords(languageCode)).thenReturn(keywords);

        assertThat(service.findKeywords(languageCode))
            .extracting(Keyword.KeywordFields.LANG_CODE.getName())
            .containsOnly("en", "fr");

        verify(repoMock).findKeywords(languageCode);

        verifyNoMoreInteractions(repoMock);
    }
}