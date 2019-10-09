package ch.difty.scipamato.core.web.model;

import java.util.Locale;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

import ch.difty.scipamato.core.web.AbstractWicketTest;

public abstract class ModelTest extends AbstractWicketTest {

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
        // override if necessary
    }

}
