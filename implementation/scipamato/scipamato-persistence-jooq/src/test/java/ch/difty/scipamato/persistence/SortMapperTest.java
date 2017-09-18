package ch.difty.scipamato.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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

import ch.difty.scipamato.NullArgumentException;
import ch.difty.scipamato.db.tables.records.PaperRecord;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.persistence.paging.Sort;
import ch.difty.scipamato.persistence.paging.Sort.Direction;
import ch.difty.scipamato.persistence.paging.Sort.SortProperty;

@RunWith(MockitoJUnitRunner.class)
public class SortMapperTest {

    private static final String SHOULD_HAVE_THROWN_EXCEPTION = "should have thrown exception";

    private final SortMapper<PaperRecord, Paper, ch.difty.scipamato.db.tables.Paper> mapper = new SortMapper<>();

    @Mock
    private SortProperty sortPropertyMock;

    private final List<SortProperty> sortProperties = new ArrayList<>();

    @After
    public void tearDown() {
        verifyNoMoreInteractions(sortPropertyMock);
    }

    @Test
    public void mapping_nullSortSpecification_returnsEmptyCollection() {
        assertThat(mapper.map(null, ch.difty.scipamato.db.tables.Paper.PAPER)).isEmpty();
    }

    @Test
    public void mapping_twoSorts_returnsSortFields() {
        sortProperties.add(new SortProperty("authors", Direction.DESC));
        sortProperties.add(new SortProperty("title", Direction.ASC));

        Collection<SortField<Paper>> sortFields = mapper.map(new Sort(sortProperties), ch.difty.scipamato.db.tables.Paper.PAPER);
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
        String existingFieldName = ch.difty.scipamato.db.tables.Paper.PAPER.AUTHORS.getName();
        TableField<PaperRecord, Paper> field = mapper.getTableField(existingFieldName.toLowerCase(), ch.difty.scipamato.db.tables.Paper.PAPER);
        assertThat(field).isNotNull();
        assertThat(field.getName()).isEqualTo(existingFieldName);
    }

    @Test
    public void gettingTableField_withNotExistingField_throws() {
        String notExistingFieldName = "foo";
        try {
            mapper.getTableField(notExistingFieldName, ch.difty.scipamato.db.tables.Paper.PAPER);
            fail(SHOULD_HAVE_THROWN_EXCEPTION);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(InvalidDataAccessApiUsageException.class).hasMessage("Could not find table field: foo; nested exception is java.lang.NoSuchFieldException: FOO");
        }
    }

    @Test
    public void gettingTableField_withNullField_throws() {
        String nullFieldName = null;
        try {
            mapper.getTableField(nullFieldName, ch.difty.scipamato.db.tables.Paper.PAPER);
            fail(SHOULD_HAVE_THROWN_EXCEPTION);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("sortFieldName must not be null.");
        }
    }

    @Test
    public void gettingTableField_withNulTable_returnsTableField() {
        String existingFieldName = ch.difty.scipamato.db.tables.Paper.PAPER.AUTHORS.getName();
        try {
            mapper.getTableField(existingFieldName, null);
            fail(SHOULD_HAVE_THROWN_EXCEPTION);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("table must not be null.");
        }
    }

    @Test
    public void convertsSortPropertyInCamelCase_toUnderscores() {
        sortProperties.add(new SortProperty("publicationYear", Direction.DESC));
        sortProperties.add(new SortProperty("populationParticipants", Direction.ASC));

        Collection<SortField<Paper>> sortFields = mapper.map(new Sort(sortProperties), ch.difty.scipamato.db.tables.Paper.PAPER);
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
