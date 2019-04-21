package ch.difty.scipamato.publ.web.themes.markup.html.publ;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.request.resource.ResourceReference;
import org.junit.jupiter.api.Test;

public class ScipamatoPublicThemeTest {

    @Test
    public void newTheme_withNoSpecificName_hasDefaultName() {
        ScipamatoPublicTheme theme = new ScipamatoPublicTheme(false);
        assertThat(theme.name()).isEqualTo("scipamato-public");
    }

    @Test
    public void newTheme_withSomeName_hasSomeName() {
        ScipamatoPublicTheme theme = new ScipamatoPublicTheme(false, "somename");
        assertThat(theme.name()).isEqualTo("somename");
    }

    @Test
    public void newTheme_notUsingLessOverCss_hasCssReference() {
        ScipamatoPublicTheme theme = new ScipamatoPublicTheme(false);
        assertThat(theme.getDependencies()).hasSize(1);
        ResourceReference resourceReference = ((CssReferenceHeaderItem) theme
            .getDependencies()
            .get(0)).getReference();
        assertThat(resourceReference).isInstanceOf(ScipamatoPublicCssReference.class);
    }

    @Test
    public void newTheme_usingLessOverCss_hasLessReference() {
        ScipamatoPublicTheme theme = new ScipamatoPublicTheme(true);
        assertThat(theme.getDependencies()).hasSize(1);
        ResourceReference resourceReference = ((CssReferenceHeaderItem) theme
            .getDependencies()
            .get(0)).getReference();
        assertThat(resourceReference).isInstanceOf(ScipamatoPublicLessReference.class);
    }

}
