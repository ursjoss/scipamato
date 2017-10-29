package ch.difty.scipamato.web;

import org.junit.Test;

public class TestHomePageTest extends WicketTest {

    @Test
    public void canStartApplication() {
        getTester().startPage(TestHomePage.class);
        getTester().assertRenderedPage(TestHomePage.class);
    }
}
