package ch.difty.scipamato.web.pages;

import org.junit.Test;

import ch.difty.scipamato.web.WicketTest;

public abstract class BasePageTest<T extends BasePage<?>> extends WicketTest {

    @Test
    public void assertPage() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        assertSpecificComponents();

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();
    }

    /**
     * @return instantiated page
     */
    protected abstract T makePage();

    /**
     * @return  page class to be tested
     */
    protected abstract Class<T> getPageClass();

    /**
     * Override if you want to assert specific components
     */
    protected void assertSpecificComponents() {
    }

}
