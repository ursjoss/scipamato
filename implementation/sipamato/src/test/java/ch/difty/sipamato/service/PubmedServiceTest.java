package ch.difty.sipamato.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.xml.transform.stream.StreamSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import ch.difty.sipamato.entity.xml.SipamatoPubmedArticleTest;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.pubmed.PubmedArticleSet;

@RunWith(MockitoJUnitRunner.class)
public class PubmedServiceTest {

    private PubmedService service;

    @Mock
    private Jaxb2Marshaller unmarshallerMock;

    @Before
    public void setUp() {
        service = new PubmedService(unmarshallerMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(unmarshallerMock);
    }

    @Test
    public void degenerateConstruction_nullUnmarshaller_throws() {
        try {
            new PubmedService(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("unmarshaller must not be null.");
        }
    }

    @Test
    public void unmarshallingNull_throws() {
        try {
            service.unmarshal(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("xmlString must not be null.");
        }
    }

    @Test
    public void nonValidXml_returnsNull() throws XmlMappingException, IOException {
        assertThat(service.unmarshal("")).isNull();
        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    @Test
    public void gettingArticles_withUnarshallerException_returnsEmptyList() {
        when(unmarshallerMock.unmarshal(isA(StreamSource.class))).thenThrow(new UnmarshallingFailureException("boom"));
        assertThat(service.getArticlesFrom("some invalid xml")).isEmpty();
        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    @Test
    public void gettingArticles_withPumbedArticleSetWithoutArticleCollection_returnsEmptyList() {
        PubmedArticleSet pubmedArticleSet = new PubmedArticleSet();
        when(unmarshallerMock.unmarshal(isA(StreamSource.class))).thenReturn(pubmedArticleSet);
        assertThat(service.getArticlesFrom("some valid xml")).isEmpty();
        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    @Test
    public void gettingArticles_withPumbedArticleSetWithoutArticleCollectionx_returnsEmptyList() {
        when(unmarshallerMock.unmarshal(isA(StreamSource.class))).thenReturn(makeMinimalValidPubmedArticleSet());

        assertThat(service.getArticlesFrom("some valid xml")).isNotEmpty();

        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    public static PubmedArticleSet makeMinimalValidPubmedArticleSet() {
        PubmedArticleSet pubmedArticleSet = new PubmedArticleSet();
        pubmedArticleSet.getPubmedArticleOrPubmedBookArticle().add(SipamatoPubmedArticleTest.makeMinimalValidPubmedArticle());
        return pubmedArticleSet;
    }

}
