package ch.difty.scipamato.core.sync.launcher;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UnsynchronizedEntitiesWarnerTest {

    @Test
    void degenerateConstruction() {
        assertDegenerateSupplierParameter(() -> new UnsynchronizedEntitiesWarner(null), "jooqCore");
    }

    @Test
    void findingUnsynchronizedPapers_withNoRecords_returnsEmptyOptional() {
        UnsynchronizedEntitiesWarner warnerSpy = spy(UnsynchronizedEntitiesWarner.class);
        doReturn(Collections.emptyList())
            .when(warnerSpy)
            .retrieveRecords();
        assertThat(warnerSpy.findUnsynchronizedPapers()).isEmpty();
        verify(warnerSpy).retrieveRecords();
    }

    @Test
    void findingUnsynchronizedPapers_withRecords_returnsResultingMessage() {
        final List<Long> numbers = List.of(5L, 18L, 3L);
        UnsynchronizedEntitiesWarner warnerSpy = spy(UnsynchronizedEntitiesWarner.class);
        doReturn(numbers)
            .when(warnerSpy)
            .retrieveRecords();
        assertThat(warnerSpy
            .findUnsynchronizedPapers()
            .get()).isEqualTo("Papers not synchronized due to missing codes: Number 5, 18, 3.");
        verify(warnerSpy).retrieveRecords();
    }
}