package ch.difty.scipamato.core.web.paper.search;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.core.web.paper.SearchOrderChangeEvent;

@ExtendWith(MockitoExtension.class)
class SearchOrderChangeEventTest {

    private SearchOrderChangeEvent e;

    @Mock
    private AjaxRequestTarget targetMock, targetMock2;

    @Test
    void canRetrieveTarget() {
        e = new SearchOrderChangeEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
    }

    @Test
    void usingMinimalConstructor_doesNotSetAnySpecialStuff() {
        e = new SearchOrderChangeEvent(targetMock);
        assertThat(e.getExcludedId()).isNull();
        assertThat(e.getDroppedConditionId()).isNull();
        assertThat(e.isNewSearchOrderRequested()).isFalse();
    }

    @Test
    void usingWithExcludedPaperId_doesAddExclusionId() {
        e = new SearchOrderChangeEvent(targetMock).withExcludedPaperId(5L);
        assertThat(e.getExcludedId()).isEqualTo(5L);
    }

    @Test
    void usingWithDroppedConditionId_doesAddConditionId() {
        e = new SearchOrderChangeEvent(targetMock).withDroppedConditionId(5L);
        assertThat(e.getDroppedConditionId()).isEqualTo(5L);
    }

    @Test
    void requestingNewSearchOrder_setsFlagAccordingly() {
        e = new SearchOrderChangeEvent(targetMock).requestingNewSearchOrder();
        assertThat(e.isNewSearchOrderRequested()).isTrue();
    }

    @Test
    void requestingNewSearchOrder_withExcludedPaperIdAndNewSearchOrderRequest_newSearchOrderRequestWins() {
        e = new SearchOrderChangeEvent(targetMock)
            .withExcludedPaperId(5L)
            .requestingNewSearchOrder();
        assertThat(e.isNewSearchOrderRequested()).isTrue();
        assertThat(e.getExcludedId()).isNull();
    }

    @Test
    void requestingNewSearchOrder_withNewSearchOrderRequestAndThenExcludedPaperId_exclusionWins() {
        e = new SearchOrderChangeEvent(targetMock)
            .requestingNewSearchOrder()
            .withExcludedPaperId(5L);
        assertThat(e.getExcludedId()).isEqualTo(5L);
        assertThat(e.isNewSearchOrderRequested()).isFalse();
    }

    @Test
    void canOverrideTarget() {
        e = new SearchOrderChangeEvent(targetMock);
        assertThat(e.getTarget()).isEqualTo(targetMock);
        e.setTarget(targetMock2);
        assertThat(e.getTarget()).isEqualTo(targetMock2);
    }

    @Test
    void equals() {
        EqualsVerifier
            .forClass(SearchOrderChangeEvent.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }
}
