package ch.difty.scipamato.public_.persistence.paper;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.public_.db.tables.Paper;
import ch.difty.scipamato.public_.db.tables.records.PaperRecord;
import ch.difty.scipamato.public_.entity.PublicPaper;

@RunWith(MockitoJUnitRunner.class)
public class JooqPublicPaperRepoTest {

    private PublicPaperRepository repo;

    @Mock
    private DSLContext                                      dslMock;
    @Mock
    private JooqSortMapper<PaperRecord, PublicPaper, Paper> sortMapperMock;
    @Mock
    private PublicPaperFilterConditionMapper                filterConditionMapperMock;

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
