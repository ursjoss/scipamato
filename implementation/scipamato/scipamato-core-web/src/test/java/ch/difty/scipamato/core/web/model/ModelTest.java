package ch.difty.scipamato.core.web.model;

import java.util.Locale;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import ch.difty.scipamato.core.ScipamatoCoreApplication;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public abstract class ModelTest {

    @Autowired
    private ScipamatoCoreApplication application;

    @Autowired
    private ApplicationContext applicationContextMock;

    @BeforeEach
    public final void setUp() {
        ReflectionTestUtils.setField(application, "applicationContext", applicationContextMock);
        final WicketTester tester = new WicketTester(application);
        Locale locale = new Locale("en_US");
        tester
            .getSession()
            .setLocale(locale);
        setUpLocal();
    }

    /**
     * Override if the actual test class needs a setUp
     */
    @SuppressWarnings("WeakerAccess")
    protected void setUpLocal() {
    }

}
