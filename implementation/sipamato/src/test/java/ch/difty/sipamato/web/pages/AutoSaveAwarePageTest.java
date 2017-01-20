package ch.difty.sipamato.web.pages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.sipamato.config.ApplicationProperties;
import ch.difty.sipamato.config.AuthorParserStrategy;
import ch.difty.sipamato.service.Localization;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

public abstract class AutoSaveAwarePageTest<T extends BasePage<?>> extends BasePageTest<T> {

    @MockBean
    private ApplicationProperties applicationProperties;

    @MockBean
    private Localization localization;

    protected ApplicationProperties getAppProps() {
        return applicationProperties;
    }

    protected Localization getLocalization() {
        return localization;
    }

    @Override
    protected final void setUpHook() {
        when(applicationProperties.getAuthorParserStrategy()).thenReturn(AuthorParserStrategy.DEFAULT);
        when(applicationProperties.getAutoSaveIntervalInSeconds()).thenReturn(0);
        when(applicationProperties.isAutoSavingEnabled()).thenReturn(false);
        when(applicationProperties.getDefaultLocalization()).thenReturn("de");
        when(applicationProperties.getBrand()).thenReturn("SiPaMaTo");
        when(localization.getLocalization()).thenReturn("de");
    }

    private void setAutoSaveMode() {
        reset(applicationProperties);
        when(applicationProperties.getAuthorParserStrategy()).thenReturn(AuthorParserStrategy.DEFAULT);
        when(applicationProperties.getAutoSaveIntervalInSeconds()).thenReturn(15);
        when(applicationProperties.isAutoSavingEnabled()).thenReturn(true);
    }

    @Test
    public void renderedPage_withAutoSavingEnabled_addsInfoMessageAndRendersDirtyHintInvisible() {
        setAutoSaveMode();

        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        getTester().assertComponent("navbar", Navbar.class);
        getTester().assertComponent("navbar:container", TransparentWebMarkupContainer.class);
        getTester().assertInvisible("navbar:container:collapse:extraItems:1");
        getTester().assertInfoMessages("The Paper is auto-saved every 15 seconds.");

        assertSpecificComponents();
    }

    @Test
    public void renderedPage_withoutAutoSavingEnabled_addsNoAjaxTimerBehavior() {
        getTester().startPage(makePage());
        assertThat(getTester().getComponentFromLastRenderedPage("contentPanel:form").getBehaviors(AbstractAjaxTimerBehavior.class)).isEmpty();
    }

    @Test
    public void renderedPage_withoutAutoSavingEnabled_doesNotSetOutputMarkupIdToComponents() {
        getTester().startPage(makePage());
        assertThat(getTester().getComponentFromLastRenderedPage("contentPanel:form:title").getOutputMarkupId()).isFalse();
    }

    @Test
    public void renderedPage_withAutoSavingEnabled_addsAjaxTimerBehavior() {
        setAutoSaveMode();
        getTester().startPage(makePage());
        assertThat(getTester().getComponentFromLastRenderedPage("contentPanel:form").getBehaviors(AbstractAjaxTimerBehavior.class)).isNotEmpty();
    }

    @Test
    public void renderedPage_withAutoSavingEnabled_doesSetOutputMarkupIdToComponents() {
        setAutoSaveMode();
        getTester().startPage(makePage());
        assertThat(getTester().getComponentFromLastRenderedPage("contentPanel:form:title").getOutputMarkupId()).isTrue();
    }

}
