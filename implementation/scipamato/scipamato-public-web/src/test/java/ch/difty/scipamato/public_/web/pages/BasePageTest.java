package ch.difty.scipamato.public_.web.pages;

import org.junit.Test;

import ch.difty.scipamato.public_.web.WicketTest;

public abstract class BasePageTest<T extends BasePage<?>> extends WicketTest {

    @Test
    public void assertPage() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        assertSpecificComponents();

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();

        doVerify();
    }

    /**
     * @return instantiated page
     */
    protected abstract T makePage();

    /**
     * @return page class to be tested
     */
    protected abstract Class<T> getPageClass();

    /**
     * Override if you want to assert specific components
     */
    protected void assertSpecificComponents() {
    }

    /**
     * Override if you need to verify mock calls
     */
    protected void doVerify() {
    }

}
