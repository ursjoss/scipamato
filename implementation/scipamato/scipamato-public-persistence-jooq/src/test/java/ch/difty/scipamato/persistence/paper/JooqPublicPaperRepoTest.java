package ch.difty.scipamato.persistence.paper;

import static ch.difty.scipamato.TestUtils.*;
import static org.mockito.Mockito.*;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.scipamato.db.tables.Paper;
import ch.difty.scipamato.db.tables.records.PaperRecord;
import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.persistence.JooqSortMapper;

@RunWith(MockitoJUnitRunner.class)
public class JooqPublicPaperRepoTest {

    private PublicPaperRepository repo;

    @Mock
    private DSLContext dslMock;
    @Mock
    private JooqSortMapper<PaperRecord, PublicPaper, Paper> sortMapperMock;
    @Mock
    private PublicPaperFilterConditionMapper filterConditionMapperMock;

    @Before
    public void setUp() {
        repo = new JooqPublicPaperRepo(dslMock, sortMapperMock, filterConditionMapperMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(dslMock, sortMapperMock, filterConditionMapperMock);
    }

    @Test
    public void findingByNumber_withNullNumber_throws() {
        assertDegenerateSupplierParameter(() -> repo.findByNumber(null), "number");
    }
}
