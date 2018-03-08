package ch.difty.scipamato.publ.web.paper.browse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextArea;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

@RunWith(MockitoJUnitRunner.class)
public class SimpleFilterPanelChangeEventTest {

    private SimpleFilterPanelChangeEvent e;

    @Mock
    private AjaxRequestTarget targetMock, targetMock2;

    @Mock
    private TextArea<?> mockComponent;

    @Before
    public void setUp() {
        when(mockComponent.getId()).thenReturn("id");
        when(mockComponent.getMarkupId()).thenReturn("mId");
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(targetMock, targetMock2);
    }

    @Test
    public void canRetrieveTarget() {
        e = new SimpleFilterPanelChangeEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
    }

    @Test
    public void usingMinimalConstructor_doesNotSetAnySpecialStuff() {
        e = new SimpleFilterPanelChangeEvent(targetMock);
        assertThat(e.getId()).isNull();
        assertThat(e.getMarkupId()).isNull();
    }

    @Test
    public void usingWithId_doesAddId() {
        e = new SimpleFilterPanelChangeEvent(targetMock).withId("foo");
        assertThat(e.getId()).isEqualTo("foo");
        assertThat(e.getMarkupId()).isNull();
    }

    @Test
    public void usingWithMarkupId_doesAddMarkupId() {
        e = new SimpleFilterPanelChangeEvent(targetMock).withMarkupId("bar");
        assertThat(e.getId()).isNull();
        assertThat(e.getMarkupId()).isEqualTo("bar");
    }

    @Test
    public void usingWithIdAndMarkupId_doesAddBoth() {
        e = new SimpleFilterPanelChangeEvent(targetMock).withId("hups")
            .withMarkupId("goo");
        assertThat(e.getId()).isEqualTo("hups");
        assertThat(e.getMarkupId()).isEqualTo("goo");
    }

    @Test
    public void canOverrideTarget() {
        e = new SimpleFilterPanelChangeEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
        e.setTarget(targetMock2);
        assertThat(e.getTarget()).isEqualTo(targetMock2);
    }

    @Test
    public void consideringAddingToTarget_withIdLessEvent_addsTarget() {
        e = new SimpleFilterPanelChangeEvent(targetMock);
        assertThat(e.getId()).isNull();

        e.considerAddingToTarget(mockComponent);

        verify(targetMock).add(mockComponent);
    }

    @Test
    public void consideringAddingToTarget_withDifferingId_doesNotAddTarget() {
        e = new SimpleFilterPanelChangeEvent(targetMock).withId("otherId")
            .withMarkupId("mId");
        e.considerAddingToTarget(mockComponent);
        verify(targetMock, never()).add(mockComponent);
    }

    @Test
    public void consideringAddingToTarget_withSameIdButNullMarkupId_addsTarget() {
        e = new SimpleFilterPanelChangeEvent(targetMock).withId("id");
        assertThat(e.getMarkupId()).isNull();

        e.considerAddingToTarget(mockComponent);

        verify(targetMock).add(mockComponent);
    }

    @Test
    public void consideringAddingToTarget_withSameIdAndDifferingMarkupId_addsTarget() {
        e = new SimpleFilterPanelChangeEvent(targetMock).withId("id")
            .withMarkupId("otherMarkupId");
        e.considerAddingToTarget(mockComponent);
        verify(targetMock).add(mockComponent);
    }

    @Test
    public void consideringAddingToTarget_withSameIdButSameMarkupId_doesNotAddTarget() {
        e = new SimpleFilterPanelChangeEvent(targetMock).withId("id")
            .withMarkupId("mId");
        e.considerAddingToTarget(mockComponent);
        verify(targetMock, never()).add(mockComponent);
    }

    @Test
    public void equals() {
        EqualsVerifier.forClass(SimpleFilterPanelChangeEvent.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

}
