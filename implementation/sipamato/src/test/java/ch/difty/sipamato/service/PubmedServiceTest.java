package ch.difty.sipamato.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.IOException;

import javax.xml.transform.stream.StreamSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class PubmedServiceTest {

    private PubmedService service;

    @Mock
    private Jaxb2Marshaller marshallerMock;

    @Before
    public void setUp() {
        service = new PubmedService(marshallerMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(marshallerMock);
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
        verify(marshallerMock).unmarshal(Mockito.isA(StreamSource.class));
    }

}
