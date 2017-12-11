package ch.difty.scipamato.core.web.pages;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.config.core.ApplicationProperties;
import ch.difty.scipamato.common.config.core.AuthorParserStrategy;

public abstract class SelfUpdatingPageTest<T extends BasePage<?>> extends BasePageTest<T> {

    @MockBean
    private ApplicationProperties applicationProperties;

    protected ApplicationProperties getAppProps() {
        return applicationProperties;
    }

    @Override
    protected final void setUpHook() {
        when(applicationProperties.getAuthorParserStrategy()).thenReturn(AuthorParserStrategy.DEFAULT);
        when(applicationProperties.getDefaultLocalization()).thenReturn("de");
        when(applicationProperties.getBrand()).thenReturn("SciPaMaTo");
        when(applicationProperties.getMinimumPaperNumberToBeRecycled()).thenReturn(7l);
    }

    @Test
    public void renderedPage_setsOutputMarkupIdToComponents() {
        getTester().startPage(makePage());
        assertThat(getTester().getComponentFromLastRenderedPage("contentPanel:form:title").getOutputMarkupId()).isTrue();
    }

}
