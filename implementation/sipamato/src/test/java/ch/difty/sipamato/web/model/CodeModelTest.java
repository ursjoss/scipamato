package ch.difty.sipamato.web.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import ch.difty.sipamato.SipamatoApplication;
import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.CodeClassId;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.service.CodeService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CodeModelTest {

    @Autowired
    private SipamatoApplication application;

    @Autowired
    private ApplicationContext applicationContextMock;

    private WicketTester tester;

    @MockBean
    private CodeService serviceMock;

    @Before
    public final void setUp() {
        ReflectionTestUtils.setField(application, "applicationContext", applicationContextMock);
        tester = new WicketTester(application);
        Locale locale = new Locale("en_US");
        tester.getSession().setLocale(locale);
    }

    @Test
    public void instantiating_withNullCodeClassId_throws() {
        try {
            new CodeModel(null, "de");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("codeClassId must not be null.");
        }
    }

    @Test
    public void instantiating_withNullLanguageCode_throws() {
        try {
            new CodeModel(CodeClassId.CC1, null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("languageCode must not be null.");
        }
    }

    @Test
    public void loading_delegatesToCodeService() {
        CodeClassId ccId = CodeClassId.CC1;
        String languageCode = "de";

        final List<Code> codes = new ArrayList<>();
        codes.add(new Code("1F", "code 1F", 1, "cc1", ""));
        codes.add(new Code("1N", "code 1N", 1, "cc1", ""));

        when(serviceMock.findCodesOfClass(ccId, languageCode)).thenReturn(codes);

        final CodeModel model = new CodeModel(CodeClassId.CC1, "de");

        assertThat(extractProperty(Code.CODE).from(model.load())).containsExactly("1F", "1N");

        verify(serviceMock).findCodesOfClass(ccId, languageCode);

        verifyNoMoreInteractions(serviceMock);
    }

}
