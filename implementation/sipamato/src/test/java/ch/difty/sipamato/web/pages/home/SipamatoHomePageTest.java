package ch.difty.sipamato.web.pages.home;

import org.apache.wicket.markup.html.basic.Label;

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
        getTester().assertComponent("message", Label.class);
    }

}
