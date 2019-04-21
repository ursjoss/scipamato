package ch.difty.scipamato.core.sync.launcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.TestUtils;

@ExtendWith(MockitoExtension.class)
public class UnsynchronizedEntitiesWarnerTest {

    private UnsynchronizedEntitiesWarner warner;

    @Mock
    private DSLContext jooqCore;

    @BeforeEach
    public void setUp() {
        warner = new UnsynchronizedEntitiesWarner(jooqCore);
    }

    @Test
    public void degenerateConstruction() {
        TestUtils.assertDegenerateSupplierParameter(() -> new UnsynchronizedEntitiesWarner(null), "jooqCore");
    }

    @Test
    public void findingUnsynchronizedPapers_withNoRecords_returnsEmptyOptional() {
        UnsynchronizedEntitiesWarner warnerSpy = spy(UnsynchronizedEntitiesWarner.class);
        doReturn(Collections.emptyList())
            .when(warnerSpy)
            .retrieveRecords();
        assertThat(warnerSpy.findUnsynchronizedPapers()).isEmpty();
        verify(warnerSpy).retrieveRecords();
    }

    @Test
    public void findingUnsynchronizedPapers_withRecords_returnsResultingMessage() {
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