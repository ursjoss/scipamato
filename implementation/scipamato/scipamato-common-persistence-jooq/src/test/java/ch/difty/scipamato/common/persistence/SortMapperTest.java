package ch.difty.scipamato.common.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.jooq.*;
import org.jooq.impl.TableImpl;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.entity.ScipamatoEntity;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.common.persistence.paging.Sort.SortProperty;

@RunWith(MockitoJUnitRunner.class)
public class SortMapperTest {

    @Spy
    private SortMapper<Record, ScipamatoEntity, TableImpl<Record>> mapperSpy;

    @Mock
    private Sort                                sortSpecMock;
    @Mock
    private TableImpl<Record>                   tableMock;
    @Mock
    private TableField<Record, ScipamatoEntity> tableFieldMock;
    @Mock
    private SortField<ScipamatoEntity>          sortFieldMock;

    private final List<SortProperty> sortProps = new ArrayList<>();

    @After
    public void tearDown() {
        verifyNoMoreInteractions(tableMock, sortSpecMock, tableFieldMock, sortFieldMock);
    }

    @Test
    public void mapping_withNullSortSpecification_returnsEmptyList() {
        assertThat(mapperSpy.map(null, tableMock))
            .isNotNull()
            .isEmpty();
    }

    @Test
    public void mapping_withEmptySortProperties_returnsEmptyList() {
        when(sortSpecMock.iterator()).thenReturn(sortProps.iterator());
        assertThat(mapperSpy.map(sortSpecMock, tableMock))
            .isNotNull()
            .isEmpty();
        verify(sortSpecMock).iterator();
    }

    @Test
    public void mapping_withSingleAscendingSortProperty_returnsOneAscendingSortField()
        throws NoSuchFieldException, SecurityException, IllegalAccessException {
        sortProps.add(new SortProperty("field", Direction.ASC));
        when(sortSpecMock.iterator()).thenReturn(sortProps.iterator());
        when(tableFieldMock.asc()).thenReturn(sortFieldMock);
        doReturn(tableFieldMock)
            .when(mapperSpy)
            .getTableFieldFor(tableMock, "FIELD");

        assertThat(mapperSpy.map(sortSpecMock, tableMock)).containsExactly(sortFieldMock);

        verify(sortSpecMock).iterator();
        verify(tableFieldMock).asc();
        verify(mapperSpy).getTableFieldFor(tableMock, "FIELD");
    }

    @Test
    public void mapping_withTwoDescendingSortProperties_returnsTwoDescendingSortFields()
        throws NoSuchFieldException, SecurityException, IllegalAccessException {
        sortProps.add(new SortProperty("field", Direction.DESC));
        sortProps.add(new SortProperty("field2", Direction.DESC));
        when(sortSpecMock.iterator()).thenReturn(sortProps.iterator());
        when(tableFieldMock.desc()).thenReturn(sortFieldMock);
        doReturn(tableFieldMock)
            .when(mapperSpy)
            .getTableFieldFor(tableMock, "FIELD");
        doReturn(tableFieldMock)
            .when(mapperSpy)
            .getTableFieldFor(tableMock, "FIELD2");

        assertThat(mapperSpy.map(sortSpecMock, tableMock)).containsExactly(sortFieldMock, sortFieldMock);

        verify(sortSpecMock).iterator();
        verify(tableFieldMock, times(2)).desc();
        verify(mapperSpy).getTableFieldFor(tableMock, "FIELD");
        verify(mapperSpy).getTableFieldFor(tableMock, "FIELD2");
    }

    @Test
    public void mapping_withWrongFieldName_throwsInvalidDataAccessApiUsageException()
        throws NoSuchFieldException, SecurityException, IllegalAccessException {
        sortProps.add(new SortProperty("inexistentField", Direction.ASC));
        when(sortSpecMock.iterator()).thenReturn(sortProps.iterator());
        doThrow(new NoSuchFieldException())
            .when(mapperSpy)
            .getTableFieldFor(tableMock, "INEXISTENT_FIELD");

        try {
            mapperSpy.map(sortSpecMock, tableMock);
            fail("should have thrown");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage(
                    "Could not find table field: inexistentField; nested exception is java.lang.NoSuchFieldException");
        }

        verify(sortSpecMock).iterator();
        verify(mapperSpy).getTableFieldFor(tableMock, "INEXISTENT_FIELD");
    }

    @Test
    public void mapping_withIllegalAccess_throwsInvalidDataAccessApiUsageException()
        throws NoSuchFieldException, SecurityException, IllegalAccessException {
        sortProps.add(new SortProperty("illegalField", Direction.ASC));
        when(sortSpecMock.iterator()).thenReturn(sortProps.iterator());
        doThrow(new IllegalAccessException())
            .when(mapperSpy)
            .getTableFieldFor(tableMock, "ILLEGAL_FIELD");

        try {
            mapperSpy.map(sortSpecMock, tableMock);
            fail("should have thrown");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage(
                    "Could not find table field: illegalField; nested exception is java.lang.IllegalAccessException");
        }

        verify(sortSpecMock).iterator();
        verify(mapperSpy).getTableFieldFor(tableMock, "ILLEGAL_FIELD");
    }

    @Test
    public void mapping_withNullTable_throws() throws SecurityException {
        sortProps.add(new SortProperty("illegalField", Direction.ASC));
        when(sortSpecMock.iterator()).thenReturn(sortProps.iterator());

        try {
            mapperSpy.map(sortSpecMock, null);
            fail("should have thrown");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("table must not be null.");
        }

        verify(sortSpecMock).iterator();
    }

    /**
     * quite a mocking orgy
     */
    @SuppressWarnings("unchecked")
    @Test
    public void tryGettingAsFarAsPossibleIntoMethodGetTableFieldFor()
        throws NoSuchFieldException, SecurityException, IllegalAccessException {
        Name name = mock(Name.class);
        Schema schema = mock(Schema.class);
        Table<Record> aliased = mock(Table.class);
        Field<?> fieldMock = mock(Field.class);
        Field<?>[] parameters = new Field<?>[] { fieldMock };
        TableField<Record, String> tableFieldMock = mock(TableField.class);

        when(name.qualified()).thenReturn(true);

        final TableImpl<Record> table = new TableImpl<>(name, schema, aliased, parameters) {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unused")
            public final TableField<Record, String> FOO = tableFieldMock;
        };
        mapperSpy.getTableFieldFor(table, "FOO");
    }

}
