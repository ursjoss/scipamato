package ch.difty.sipamato;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SipamatoApplication.class)
@WebAppConfiguration
@DirtiesContext
public class SipamatoHomePageTest {

    private WicketTester tester;

    @Autowired
    private SipamatoApplication application;

    @Before
    public void setUp() {
        tester = new WicketTester(application);
    }

    @Test
    public void testRenderMyPage() {
        tester.startPage(SipamatoHomePage.class);
        tester.assertRenderedPage(SipamatoHomePage.class);
        tester.assertComponent("message", Label.class);
    }
}