package ch.difty.scipamato.core.web.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.core.config.ApplicationCoreProperties;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy;

public abstract class SelfUpdatingPageTest<T extends BasePage<?>> extends BasePageTest<T> {

    @MockBean
    private ApplicationCoreProperties applicationProperties;

    protected ApplicationCoreProperties getAppProps() {
        return applicationProperties;
    }

    @Override
    protected final void setUpHook() {
        when(applicationProperties.getAuthorParserStrategy()).thenReturn(AuthorParserStrategy.PUBMED);
        when(applicationProperties.getDefaultLocalization()).thenReturn("de");
        when(applicationProperties.getBrand()).thenReturn("SciPaMaTo");
        when(applicationProperties.getMinimumPaperNumberToBeRecycled()).thenReturn(7L);
    }

    @Test
    public void renderedPage_setsOutputMarkupIdToComponents() {
        getTester().startPage(makePage());
        assertThat(getTester()
            .getComponentFromLastRenderedPage("contentPanel:form:title")
            .getOutputMarkupId()).isTrue();
    }

    @Test
    public void test() {
        SelfUpdatingPage p = new SelfUpdatingPage<>(Model.of(new CodeClass(1, "CC1", ""))) {
            @Override
            protected Form<CodeClass> getForm() {
                return null;
            }
        };
        p.implSpecificOnInitialize();
    }

}
