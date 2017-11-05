package ch.difty.scipamato.web;

import static org.assertj.core.api.Assertions.*;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.junit.Test;

import ch.difty.scipamato.web.test.TestAbstractPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;

public class AbstractPanelTest extends WicketTest {

    @Test
    public void testViewMode_withOneArgConstructor() {
        assertViewMode(new AbstractPanel<TestRecord>("panel") {
            private static final long serialVersionUID = 1L;
        });
    }

    @Test
    public void testViewMode_withTwoArgConstructor() {
        assertViewMode(new AbstractPanel<TestRecord>("panel", Model.of(new TestRecord(1, "foo"))) {
            private static final long serialVersionUID = 1L;
        });
    }

    @Test
    public void testViewMode_withThreeArgConstructor() {
        assertViewMode(new AbstractPanel<TestRecord>("id", Model.of(new TestRecord(1, "foo")), Mode.VIEW) {
            private static final long serialVersionUID = 1L;
        });
    }

    private void assertViewMode(AbstractPanel<TestRecord> p) {
        assertThat(p.getMode()).isEqualTo(Mode.VIEW);
        assertThat(p.getSubmitLinkResourceLabel()).isEqualTo("button.disabled.label");
        assertThat(p.isViewMode()).isTrue();
        assertThat(p.isEditMode()).isFalse();
        assertThat(p.isSearchMode()).isFalse();
    }

    @Test
    public void testEditMode() {
        assertEditMode(new AbstractPanel<TestRecord>("panel", Model.of(new TestRecord(1, "foo")), Mode.EDIT) {
            private static final long serialVersionUID = 1L;
        });
    }

    private void assertEditMode(AbstractPanel<TestRecord> p) {
        assertThat(p.getMode()).isEqualTo(Mode.EDIT);
        assertThat(p.getSubmitLinkResourceLabel()).isEqualTo("button.save.label");
        assertThat(p.isViewMode()).isFalse();
        assertThat(p.isEditMode()).isTrue();
        assertThat(p.isSearchMode()).isFalse();
    }

    @Test
    public void testSearchMode() {
        assertSearchMode(new AbstractPanel<TestRecord>("panel", Model.of(new TestRecord(1, "foo")), Mode.SEARCH) {
            private static final long serialVersionUID = 1L;
        });
    }

    private void assertSearchMode(AbstractPanel<TestRecord> p) {
        assertThat(p.getMode()).isEqualTo(Mode.SEARCH);
        assertThat(p.getSubmitLinkResourceLabel()).isEqualTo("button.search.label");
        assertThat(p.isViewMode()).isFalse();
        assertThat(p.isEditMode()).isFalse();
        assertThat(p.isSearchMode()).isTrue();
    }

    @Test
    public void assertTestAbstractPanel_inViewMode() {
        getTester().startComponentInPage(new TestAbstractPanel("panel", Mode.VIEW));
        assertComponents();
    }

    private void assertComponents() {
        getTester().assertLabel("panel:fooLabel", "Foo");
        getTester().assertComponent("panel:foo", TextField.class);
        getTester().assertLabel("panel:barLabel", "Bar");
        getTester().assertComponent("panel:bar", TextField.class);
        getTester().assertLabel("panel:bazLabel", "Baz");
        getTester().assertComponent("panel:baz", CheckBoxX.class);
    }

}
