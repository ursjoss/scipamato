package ch.difty.scipamato.common.persistence

import ch.difty.scipamato.common.entity.ScipamatoEntity
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.common.persistence.paging.Sort.Direction
import ch.difty.scipamato.common.persistence.paging.Sort.SortProperty
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.jooq.Record
import org.jooq.SortField
import org.jooq.Table
import org.jooq.TableField
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.dao.InvalidDataAccessApiUsageException

internal class SortMapperTest {

    private val mapperSpy = spyk<SortMapper<Record, ScipamatoEntity, Table<Record>>>()

    private val sortSpecMock = mockk<Sort>()
    private val tableMock = mockk<Table<Record>>()
    private val tableFieldMock = mockk<TableField<Record, ScipamatoEntity>>()
    private val sortFieldMock = mockk<SortField<ScipamatoEntity>>()

    private val sortProps = ArrayList<SortProperty>()

    @AfterEach
    fun tearDown() {
        confirmVerified(tableMock, sortSpecMock, tableFieldMock)
    }

    @Test
    fun mapping_withNullSortSpecification_returnsEmptyList() {
        mapperSpy.map(null, tableMock).shouldBeEmpty()
        true shouldBe true
    }

    @Test
    fun mapping_withEmptySortProperties_returnsEmptyList() {
        every { sortSpecMock.iterator() } returns sortProps.iterator()
        mapperSpy.map(sortSpecMock, tableMock).shouldBeEmpty()
        verify { sortSpecMock.iterator() }
    }

    @Test
    fun mapping_withSingleAscendingSortProperty_returnsOneAscendingSortField() {
        sortProps.add(SortProperty("field", Direction.ASC))
        every { sortSpecMock.iterator() } returns sortProps.iterator()
        every { tableFieldMock.asc() } returns sortFieldMock
        every { mapperSpy.getTableFieldFor(tableMock, "FIELD") } returns tableFieldMock

        mapperSpy.map(sortSpecMock, tableMock) shouldContainSame listOf(sortFieldMock)

        verify { sortSpecMock.iterator() }
        verify { tableFieldMock.asc() }
        verify { mapperSpy.getTableFieldFor(tableMock, "FIELD") }
    }

    @Test
    fun mapping_withTwoDescendingSortProperties_returnsTwoDescendingSortFields() {
        sortProps.add(SortProperty("field", Direction.DESC))
        sortProps.add(SortProperty("field2", Direction.DESC))
        every { sortSpecMock.iterator() } returns sortProps.iterator()
        every { tableFieldMock.desc() } returns sortFieldMock
        every { mapperSpy.getTableFieldFor(tableMock, "FIELD") } returns tableFieldMock
        every { mapperSpy.getTableFieldFor(tableMock, "FIELD2") } returns tableFieldMock

        mapperSpy.map(sortSpecMock, tableMock) shouldContainSame listOf(sortFieldMock, sortFieldMock)

        verify { sortSpecMock.iterator() }
        verify(exactly = 2) { tableFieldMock.desc() }
        verify { mapperSpy.getTableFieldFor(tableMock, "FIELD") }
        verify { mapperSpy.getTableFieldFor(tableMock, "FIELD2") }
    }

    @Test
    fun mapping_withWrongFieldName_throwsInvalidDataAccessApiUsageException() {
        sortProps.add(SortProperty("inexistentField", Direction.ASC))
        every { sortSpecMock.iterator() } returns sortProps.iterator()
        every { mapperSpy.getTableFieldFor(tableMock, "INEXISTENT_FIELD") } throws NoSuchFieldException()

        invoking { mapperSpy.map(sortSpecMock, tableMock) } shouldThrow
            InvalidDataAccessApiUsageException::class withMessage
            "Could not find table field: inexistentField; nested exception is java.lang.NoSuchFieldException"

        verify { sortSpecMock.iterator() }
        verify { mapperSpy.getTableFieldFor(tableMock, "INEXISTENT_FIELD") }
    }

    @Test
    fun mapping_withIllegalAccess_throwsInvalidDataAccessApiUsageException() {
        sortProps.add(SortProperty("illegalField", Direction.ASC))
        every { sortSpecMock.iterator() } returns sortProps.iterator()
        every { mapperSpy.getTableFieldFor(tableMock, "ILLEGAL_FIELD") } throws IllegalAccessException()

        invoking { mapperSpy.map(sortSpecMock, tableMock) } shouldThrow
            InvalidDataAccessApiUsageException::class withMessage
            "Could not find table field: illegalField; nested exception is java.lang.IllegalAccessException"

        verify { sortSpecMock.iterator() }
        verify { mapperSpy.getTableFieldFor(tableMock, "ILLEGAL_FIELD") }
    }
}
