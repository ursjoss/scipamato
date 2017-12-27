package ch.difty.scipamato.core.persistence;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.TableField;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import ch.difty.scipamato.common.persistence.SortMapper;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.common.persistence.paging.Sort.SortProperty;
import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.Paper;

@RunWith(MockitoJUnitRunner.class)
public class SortMapperTest {

    private final SortMapper<PaperRecord, Paper, ch.difty.scipamato.core.db.tables.Paper> mapper = new SortMapper<>();

    @Mock
    private SortProperty sortPropertyMock;

    private final List<SortProperty> sortProperties = new ArrayList<>();

    @After
    public void tearDown() {
        verifyNoMoreInteractions(sortPropertyMock);
    }

    @Test
    public void mapping_nullSortSpecification_returnsEmptyCollection() {
        assertThat(mapper.map(null, ch.difty.scipamato.core.db.tables.Paper.PAPER)).isEmpty();
    }

    @Test
    public void mapping_twoSorts_returnsSortFields() {
        sortProperties.add(new SortProperty("authors", Direction.DESC));
        sortProperties.add(new SortProperty("title", Direction.ASC));

        Collection<SortField<Paper>> sortFields = mapper.map(new Sort(sortProperties),
            ch.difty.scipamato.core.db.tables.Paper.PAPER);
        assertThat(sortFields).hasSize(2);

        Iterator<SortField<Paper>> it = sortFields.iterator();
        SortField<Paper> sf = it.next();
        assertThat(sf.getName()).isEqualToIgnoringCase("authors");
        assertThat(sf.getOrder()).isEqualTo(SortOrder.DESC);

        sf = it.next();
        assertThat(sf.getName()).isEqualToIgnoringCase("title");
        assertThat(sf.getOrder()).isEqualTo(SortOrder.ASC);
    }

    @Test
    public void gettingTableField_withExistingField_returnsTableField() {
        String existingFieldName = ch.difty.scipamato.core.db.tables.Paper.PAPER.AUTHORS.getName();
        TableField<PaperRecord, Paper> field = mapper.getTableField(existingFieldName.toLowerCase(),
            ch.difty.scipamato.core.db.tables.Paper.PAPER);
        assertThat(field).isNotNull();
        assertThat(field.getName()).isEqualTo(existingFieldName);
    }

    @Test
    public void gettingTableField_withNotExistingField_throws() {
        String notExistingFieldName = "foo";
        try {
            mapper.getTableField(notExistingFieldName, ch.difty.scipamato.core.db.tables.Paper.PAPER);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage("Could not find table field: foo; nested exception is java.lang.NoSuchFieldException: FOO");
        }
    }

    @Test
    public void gettingTableField_withNullField_throws() {
        String nullFieldName = null;
        assertDegenerateSupplierParameter(
            () -> mapper.getTableField(nullFieldName, ch.difty.scipamato.core.db.tables.Paper.PAPER), "sortFieldName");
    }

    @Test
    public void gettingTableField_withNulTable_returnsTableField() {
        String existingFieldName = ch.difty.scipamato.core.db.tables.Paper.PAPER.AUTHORS.getName();
        assertDegenerateSupplierParameter(() -> mapper.getTableField(existingFieldName, null), "table");
    }

    @Test
    public void convertsSortPropertyInCamelCase_toUnderscores() {
        sortProperties.add(new SortProperty("publicationYear", Direction.DESC));
        sortProperties.add(new SortProperty("populationParticipants", Direction.ASC));

        Collection<SortField<Paper>> sortFields = mapper.map(new Sort(sortProperties),
            ch.difty.scipamato.core.db.tables.Paper.PAPER);
        assertThat(sortFields).hasSize(2);

        Iterator<SortField<Paper>> it = sortFields.iterator();
        SortField<Paper> sf = it.next();
        assertThat(sf.getName()).isEqualToIgnoringCase("publication_year");
        assertThat(sf.getOrder()).isEqualTo(SortOrder.DESC);

        sf = it.next();
        assertThat(sf.getName()).isEqualToIgnoringCase("population_participants");
        assertThat(sf.getOrder()).isEqualTo(SortOrder.ASC);
    }
}
