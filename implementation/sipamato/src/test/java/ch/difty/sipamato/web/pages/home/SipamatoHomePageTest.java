package ch.difty.sipamato.web.pages.home;

import ch.difty.sipamato.web.pages.BasePageTest;

public class SipamatoHomePageTest extends BasePageTest<SipamatoHomePage> {

    @Override
    protected SipamatoHomePage makePage() {
        return new SipamatoHomePage(null);
    }

    @Override
    protected Class<SipamatoHomePage> getPageClass() {
        return SipamatoHomePage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        getTester().assertLabel("message", "Hello SiPaMaTo!");
        getTester().assertLabel("currentTime", "2016-12-09T06:02:13");
    }

}
