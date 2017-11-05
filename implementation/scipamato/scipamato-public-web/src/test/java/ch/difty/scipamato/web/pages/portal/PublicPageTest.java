package ch.difty.scipamato.web.pages.portal;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.web.pages.BasePageTest;

public class PublicPageTest extends BasePageTest<PublicPage> {

    @Override
    protected PublicPage makePage() {
        return new PublicPage(new PageParameters());
    }

    @Override
    protected Class<PublicPage> getPageClass() {
        return PublicPage.class;
    }

}
