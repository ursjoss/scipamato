package ch.difty.sipamato.web.model;

import java.util.Locale;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import ch.difty.sipamato.SipamatoApplication;

@SpringBootTest
@RunWith(SpringRunner.class)
//Activate one of the two next lines when running the tests from ecplise - but don't commit as it might break the maven build
//@ActiveProfiles( {"DB_JOOQ", "test", "postgres"})
//@ActiveProfiles( {"DB_JOOQ", "test", "h2"})
public abstract class ModelTest {

    @Autowired
    private SipamatoApplication application;

    @Autowired
    private ApplicationContext applicationContextMock;

    private WicketTester tester;

    @Before
    public final void setUp() {
        ReflectionTestUtils.setField(application, "applicationContext", applicationContextMock);
        tester = new WicketTester(application);
        Locale locale = new Locale("en_US");
        tester.getSession().setLocale(locale);
        setUpLocal();
    }

    /**
     * Override if the actual test class needs a setUp
     */
    protected void setUpLocal() {
    }

}
