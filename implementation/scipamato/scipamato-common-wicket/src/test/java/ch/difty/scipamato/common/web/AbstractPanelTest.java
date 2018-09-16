package ch.difty.scipamato.common.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.junit.Test;

public class AbstractPanelTest extends WicketBaseTest {

    @Test
    public void testViewMode_withOneArgConstructor() {
        assertViewMode(new AbstractPanel<>("panel") {
            private static final long serialVersionUID = 1L;
        });
    }

    @Test
    public void testViewMode_withTwoArgConstructor() {
        assertViewMode(new AbstractPanel<>("panel", Model.of(new TestRecord(1, "foo"))) {
            private static final long serialVersionUID = 1L;
        });
    }

    @Test
    public void testViewMode_withThreeArgConstructor() {
        assertViewMode(new AbstractPanel<>("id", Model.of(new TestRecord(1, "foo")), Mode.VIEW) {
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
        assertEditMode(new AbstractPanel<>("panel", Model.of(new TestRecord(1, "foo")), Mode.EDIT) {
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
        assertSearchMode(new AbstractPanel<>("panel", Model.of(new TestRecord(1, "foo")), Mode.SEARCH) {
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

    @Test
    public void queuingFieldAndLabel_withPropertyValidatorInEditMode_addsLatterToComponent() {
        FormComponent<?> fc = mock(FormComponent.class);
        when(fc.getId()).thenReturn("fcId");
        PropertyValidator<?> pv = mock(PropertyValidator.class);

        AbstractPanel<TestRecord> p = new TestAbstractPanel("panel", Mode.EDIT);
        p.queueFieldAndLabel(fc, pv);

        verify(fc, times(3)).getId();
        verify(fc).setLabel(isA(StringResourceModel.class));
        verify(fc).add(pv);
        verifyNoMoreInteractions(fc, pv);
    }

    @Test
    public void queuingFieldAndLabel_withPropertyValidatorInViewMode_addsNothingToComponent() {
        FormComponent<?> fc = mock(FormComponent.class);
        when(fc.getId()).thenReturn("fcId");
        PropertyValidator<?> pv = mock(PropertyValidator.class);

        AbstractPanel<TestRecord> p = new TestAbstractPanel("panel", Mode.VIEW);
        p.queueFieldAndLabel(fc, pv);

        verify(fc, times(3)).getId();
        verify(fc).setLabel(isA(StringResourceModel.class));
        verify(fc, never()).add(pv);
        verifyNoMoreInteractions(fc, pv);
    }

    @Test
    public void queuingFieldAndLabel_withNullPropertyValidator_addsNothingToComponent() {
        FormComponent<?> fc = mock(FormComponent.class);
        when(fc.getId()).thenReturn("fcId");

        AbstractPanel<TestRecord> p = new TestAbstractPanel("panel", Mode.EDIT);
        p.queueFieldAndLabel(fc, null);

        verify(fc, times(3)).getId();
        verify(fc).setLabel(isA(StringResourceModel.class));
        verifyNoMoreInteractions(fc);
    }

}
