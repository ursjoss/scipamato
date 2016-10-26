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
import ch.difty.sipamato.entity.CodeClass;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.service.CodeClassService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CodeClassModelTest {

    @Autowired
    private SipamatoApplication application;

    @Autowired
    private ApplicationContext applicationContextMock;

    private WicketTester tester;

    @MockBean
    private CodeClassService serviceMock;

    // TODO extract into baseClass
    @Before
    public final void setUp() {
        ReflectionTestUtils.setField(application, "applicationContext", applicationContextMock);
        tester = new WicketTester(application);
        Locale locale = new Locale("en_US");
        tester.getSession().setLocale(locale);
    }

    @Test
    public void instantiating_withNullLanguageCode_throws() {
        try {
            new CodeClassModel(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("languageCode must not be null.");
        }
    }

    @Test
    public void loading_delegatesToCodeClassService() {
        String languageCode = "de";

        final List<CodeClass> codeClasses = new ArrayList<>();
        codeClasses.add(new CodeClass(1, "cc1", ""));
        codeClasses.add(new CodeClass(2, "cc2", ""));

        when(serviceMock.find(languageCode)).thenReturn(codeClasses);

        final CodeClassModel model = new CodeClassModel("de");

        assertThat(extractProperty(CodeClass.NAME).from(model.load())).containsExactly("cc1", "cc2");

        verify(serviceMock).find(languageCode);

        verifyNoMoreInteractions(serviceMock);
    }

}
