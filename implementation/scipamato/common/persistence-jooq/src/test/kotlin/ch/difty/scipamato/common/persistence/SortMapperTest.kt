package ch.difty.scipamato.common.persistence

import ch.difty.scipamato.common.NullArgumentException
import ch.difty.scipamato.common.entity.ScipamatoEntity
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.common.persistence.paging.Sort.Direction
import ch.difty.scipamato.common.persistence.paging.Sort.SortProperty
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.jooq.Record
import org.jooq.SortField
import org.jooq.TableField
import org.jooq.impl.TableImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.dao.InvalidDataAccessApiUsageException
import java.util.*

internal class SortMapperTest {

    private val mapperSpy = spy<SortMapper<Record, ScipamatoEntity, TableImpl<Record>>>()

    private val sortSpecMock = mock<Sort>()
    private val tableMock = mock<TableImpl<Record>>()
    private val tableFieldMock = mock<TableField<Record, ScipamatoEntity>>()
    private val sortFieldMock = mock<SortField<ScipamatoEntity>>()

    private val sortProps = ArrayList<SortProperty>()

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(tableMock, sortSpecMock, tableFieldMock, sortFieldMock)
    }

    @Test
    fun mapping_withNullSortSpecification_returnsEmptyList() {
        assertThat(mapperSpy.map(null, tableMock))
                .isNotNull
                .isEmpty()
    }

    @Test
    fun mapping_withEmptySortProperties_returnsEmptyList() {
        whenever(sortSpecMock.iterator()).thenReturn(sortProps.iterator())
        assertThat(mapperSpy.map(sortSpecMock, tableMock))
                .isNotNull
                .isEmpty()
        verify(sortSpecMock).iterator()
    }

    @Test
    fun mapping_withSingleAscendingSortProperty_returnsOneAscendingSortField() {
        sortProps.add(SortProperty("field", Direction.ASC))
        whenever(sortSpecMock.iterator()).thenReturn(sortProps.iterator())
        whenever(tableFieldMock.asc()).thenReturn(sortFieldMock)
        doReturn(tableFieldMock)
                .whenever<SortMapper<Record, ScipamatoEntity, TableImpl<Record>>>(mapperSpy)
                .getTableFieldFor(tableMock, "FIELD")

        assertThat(mapperSpy.map(sortSpecMock, tableMock)).containsExactly(sortFieldMock)

        verify(sortSpecMock).iterator()
        verify(tableFieldMock).asc()
        verify(mapperSpy).getTableFieldFor(tableMock, "FIELD")
    }

    @Test
    fun mapping_withTwoDescendingSortProperties_returnsTwoDescendingSortFields() {
        sortProps.add(SortProperty("field", Direction.DESC))
        sortProps.add(SortProperty("field2", Direction.DESC))
        whenever(sortSpecMock.iterator()).thenReturn(sortProps.iterator())
        whenever(tableFieldMock.desc()).thenReturn(sortFieldMock)
        doReturn(tableFieldMock)
                .whenever<SortMapper<Record, ScipamatoEntity, TableImpl<Record>>>(mapperSpy)
                .getTableFieldFor(tableMock, "FIELD")
        doReturn(tableFieldMock)
                .whenever<SortMapper<Record, ScipamatoEntity, TableImpl<Record>>>(mapperSpy)
                .getTableFieldFor(tableMock, "FIELD2")

        assertThat(mapperSpy.map(sortSpecMock, tableMock)).containsExactly(sortFieldMock, sortFieldMock)

        verify(sortSpecMock).iterator()
        verify(tableFieldMock, times(2)).desc()
        verify(mapperSpy).getTableFieldFor(tableMock, "FIELD")
        verify(mapperSpy).getTableFieldFor(tableMock, "FIELD2")
    }

    @Test
    fun mapping_withWrongFieldName_throwsInvalidDataAccessApiUsageException() {
        sortProps.add(SortProperty("inexistentField", Direction.ASC))
        whenever(sortSpecMock.iterator()).thenReturn(sortProps.iterator())
        doThrow(NoSuchFieldException())
                .whenever<SortMapper<Record, ScipamatoEntity, TableImpl<Record>>>(mapperSpy)
                .getTableFieldFor(tableMock, "INEXISTENT_FIELD")

        try {
            mapperSpy.map(sortSpecMock, tableMock)
            fail<Any>("should have thrown")
        } catch (ex: Exception) {
            assertThat(ex)
                    .isInstanceOf(InvalidDataAccessApiUsageException::class.java)
                    .hasMessage(
                            "Could not find table field: inexistentField; nested exception is java.lang.NoSuchFieldException")
        }

        verify(sortSpecMock).iterator()
        verify<SortMapper<Record, ScipamatoEntity, TableImpl<Record>>>(mapperSpy).getTableFieldFor(tableMock, "INEXISTENT_FIELD")
    }

    @Test
    fun mapping_withIllegalAccess_throwsInvalidDataAccessApiUsageException() {
        sortProps.add(SortProperty("illegalField", Direction.ASC))
        whenever(sortSpecMock.iterator()).thenReturn(sortProps.iterator())
        doThrow(IllegalAccessException())
                .whenever<SortMapper<Record, ScipamatoEntity, TableImpl<Record>>>(mapperSpy)
                .getTableFieldFor(tableMock, "ILLEGAL_FIELD")

        try {
            mapperSpy.map(sortSpecMock, tableMock)
            fail<Any>("should have thrown")
        } catch (ex: Exception) {
            assertThat(ex)
                    .isInstanceOf(InvalidDataAccessApiUsageException::class.java)
                    .hasMessage(
                            "Could not find table field: illegalField; nested exception is java.lang.IllegalAccessException")
        }

        verify(sortSpecMock).iterator()
        verify<SortMapper<Record, ScipamatoEntity, TableImpl<Record>>>(mapperSpy).getTableFieldFor(tableMock, "ILLEGAL_FIELD")
    }

    @Test
    fun mapping_withNullTable_throws() {
        sortProps.add(SortProperty("illegalField", Direction.ASC))
        whenever(sortSpecMock.iterator()).thenReturn(sortProps.iterator())

        try {
            mapperSpy.map(sortSpecMock, null)
            fail<Any>("should have thrown")
        } catch (ex: Exception) {
            assertThat(ex)
                    .isInstanceOf(NullArgumentException::class.java)
                    .hasMessage("table must not be null.")
        }

        verify(sortSpecMock).iterator()
    }

}
