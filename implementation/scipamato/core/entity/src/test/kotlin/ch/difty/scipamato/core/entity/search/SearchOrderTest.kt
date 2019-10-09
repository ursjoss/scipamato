package ch.difty.scipamato.core.entity.search

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID
import ch.difty.scipamato.core.entity.search.SearchOrder.SearchOrderFields.SHOW_EXCLUDED
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

private const val SO_NAME = "soName"

@ExtendWith(MockitoExtension::class)
internal class SearchOrderTest {

    private val so = SearchOrder(10L, SO_NAME, 1, false, null, null)

    @Mock
    private lateinit var mockCondition1: SearchCondition
    @Mock
    private lateinit var mockCondition2: SearchCondition

    private val searchConditions = ArrayList<SearchCondition>()
    private val excludedIds = ArrayList<Long>()

    @Test
    fun testGetters() {
        assertThat(so.id).isEqualTo(10)
        assertThat(so.owner).isEqualTo(1)
        assertThat(so.isGlobal).isEqualTo(false)
        assertThat(so.isShowExcluded).isEqualTo(false)
    }

    @Test
    fun testSetters() {
        so.id = 11L
        so.owner = 2
        so.name = SO_NAME
        so.isGlobal = true
        so.isShowExcluded = true

        assertThat(so.id).isEqualTo(11)
        assertThat(so.name).isEqualTo(SO_NAME)
        assertThat(so.owner).isEqualTo(2)
        assertThat(so.isGlobal).isEqualTo(true)
        assertThat(so.isShowExcluded).isEqualTo(true)
    }

    @Test
    fun whenInstantiating_withNullLists_containsNoItems() {
        assertThat(so.searchConditions).isEmpty()
        assertThat(so.excludedPaperIds).isEmpty()
    }

    @Test
    fun whenInstantiating_withEmptyConditionList_hasNoConditions() {
        assertThat(SearchOrder(searchConditions).searchConditions).isEmpty()
    }

    @Test
    fun whenInstantiating_withEmptyExclusionList_hasNoExclusions() {
        assertThat(excludedIds).isEmpty()
        assertThat(SearchOrder(10L, SO_NAME, 1, false, null, excludedIds).excludedPaperIds).isEmpty()
    }

    @Test
    fun whenInstantiating_withNonEmptyConditionList_hasHandedOverConditions() {
        searchConditions.addAll(listOf(mockCondition1, mockCondition2))
        assertThat(SearchOrder(searchConditions).searchConditions).containsExactly(mockCondition1, mockCondition2)
    }

    @Test
    fun whenInstantiating_withNonEmptyExclusionList_hasHandedOverExclusions() {
        excludedIds.add(3L)
        excludedIds.add(5L)
        assertThat(SearchOrder(10L, SO_NAME, 1, false, null, excludedIds).excludedPaperIds).containsExactly(3L, 5L)
    }

    @Test
    fun whenAddingNullCondition_itIsNotAdded() {
        so.add(null)
        assertThat(so.searchConditions).isEmpty()
    }

    @Test
    fun whenAddingCondition_itIsGettingAdded() {
        so.add(mockCondition1)
        assertThat(so.searchConditions).containsExactly(mockCondition1)
    }

    @Test
    fun whenRemovingSearchCondition_withNullParameter_doesNothing() {
        so.add(mockCondition1)
        so.add(mockCondition2)
        assertThat(so.searchConditions).containsExactly(mockCondition1, mockCondition2)

        so.remove(null)

        assertThat(so.searchConditions).containsExactly(mockCondition1, mockCondition2)
    }

    @Test
    fun whenRemovingSearchCondition_withConditionWhichIsPresent_doesRemoveIt() {
        so.add(mockCondition1)
        so.add(mockCondition2)
        assertThat(so.searchConditions).containsExactly(mockCondition1, mockCondition2)

        so.remove(mockCondition2)

        assertThat(so.searchConditions).containsExactly(mockCondition1)
    }

    @Test
    fun whenRemovingCondition_withConditionWhichIsNotPresent_doesNothing() {
        so.add(mockCondition2)
        assertThat(so.searchConditions).containsExactly(mockCondition2)

        so.remove(mockCondition1)

        assertThat(so.searchConditions).containsExactly(mockCondition2)
    }

    @Test
    fun whenAddingExclusion_itIsGettingAdded() {
        so.addExclusionOfPaperWithId(5L)
        assertThat(so.excludedPaperIds).containsExactly(5L)
    }

    @Test
    fun whenAddingExclusion_withExclusionAlreadyPresent_doesNotAddItAnymore() {
        so.addExclusionOfPaperWithId(5L)
        assertThat(so.excludedPaperIds).containsExactly(5L)
        so.addExclusionOfPaperWithId(5L)
        assertThat(so.excludedPaperIds).containsExactly(5L)
    }

    @Test
    fun whenRemovingExclusion_whichWasExcluded_doesRemoveIt() {
        so.addExclusionOfPaperWithId(5L)
        so.addExclusionOfPaperWithId(8L)
        assertThat(so.excludedPaperIds).containsExactly(5L, 8L)

        so.removeExclusionOfPaperWithId(5L)

        assertThat(so.excludedPaperIds).containsExactly(8L)
    }

    @Test
    fun whenRemovingExclusion_whichWasNotExcluded_doesNothing() {
        so.addExclusionOfPaperWithId(5L)
        assertThat(so.excludedPaperIds).containsExactly(5L)

        so.removeExclusionOfPaperWithId(8L)

        assertThat(so.excludedPaperIds).containsExactly(5L)
    }

    @Test
    fun testingToString_withNoConditionsOrExclusions() {
        assertThat(so.searchConditions).hasSize(0)
        assertThat(so.excludedPaperIds).hasSize(0)
        // TODO
        //        assertThat(so.toString()).isEqualTo(
        //            "SearchOrder[name=soName,owner=1,global=false,searchConditions=[],excludedPaperIds=[],
        //            showExcluded=false,id=10,createdBy=<null>,lastModifiedBy=<null>,created=<null>,
        //            lastModified=<null>,version=0]");
    }

    @Test
    fun testingToString_withConditionsAndExclusions() {
        so.add(mockCondition1)
        so.add(mockCondition2)
        so.addExclusionOfPaperWithId(3L)
        so.addExclusionOfPaperWithId(5L)
        // TODO fix
        // assertThat(so.toString()).isEqualTo(
        // "SearchOrder[name=soName,owner=1,global=false,searchConditions=[mockCondition1, mockCondition2],
        // excludedPaperIds=[3, 5],showExcluded=false,id=10,createdBy=<null>,lastModifiedBy=<null>,created=<null>,
        // lastModified=<null>,version=0]");
    }

    @Test
    fun testingDisplayValue_withNoConditions_returnsIDOnly() {
        assertThat(so.searchConditions).hasSize(0)
        assertThat(so.displayValue).isEqualTo("soName:  (10)")
    }

    @Test
    fun testingDisplayValue_forGlobalSearchOrderWithNoConditions_returnsIdPlusGlobalIndicator() {
        assertThat(so.searchConditions).hasSize(0)
        so.isGlobal = true
        assertThat(so.displayValue).isEqualTo("soName:  (10)*")
    }

    @Test
    fun testingDisplayValue_withoutName_forGlobalSearchOrderWithNoConditions_returnsIdPlusGlobalIndicator() {
        so.name = null
        assertThat(so.searchConditions).hasSize(0)
        so.isGlobal = true
        assertThat(so.displayValue).isEqualTo("-- (10)*")
    }

    @Test
    fun testingDisplayValue_withoutNameButWithSingleCondition_returnsIt() {
        val so1 = SearchOrder(10L, null, 1, false, null, excludedIds)
        so1.add(object : SearchCondition() {
            override fun getDisplayValue(): String = "f1DisplayValue"
        })

        assertThat(so1.displayValue).isEqualTo("f1DisplayValue (10)")
    }

    @Test
    fun testingDisplayValue_withSingleCondition_returnsIt() {
        so.add(object : SearchCondition() {
            override fun getDisplayValue(): String = "f1DisplayValue"
        })

        assertThat(so.displayValue).isEqualTo("soName: f1DisplayValue (10)")
    }

    @Test
    fun testingDisplayValue_withTwoConditions_joinsThemUsingOR() {
        so.add(object : SearchCondition() {
            override fun getDisplayValue(): String = "c1DisplayValue"
        })
        so.add(object : SearchCondition() {
            override fun getDisplayValue(): String = "c2DisplayValue"
        })

        assertThat(so.displayValue).isEqualTo("soName: c1DisplayValue; OR c2DisplayValue (10)")
    }

    @Test
    fun cannotAddTheSameSearchTermTwice() {
        val c1 = SearchCondition()
        c1.authors = "baz"
        c1.title = "foo"
        c1.publicationYear = "2016"
        c1.isFirstAuthorOverridden = true
        val c2 = SearchCondition()
        c2.authors = "baz"
        c2.title = "foo"
        c2.publicationYear = "2016"
        c2.isFirstAuthorOverridden = true

        assertThat(so.searchConditions).hasSize(0)
        so.add(c1)
        assertThat(so.searchConditions).hasSize(1)
        so.add(c2)
        assertThat(so.searchConditions).hasSize(1)
    }

    @Test
    fun createdDisplayValue_withNullValues_isEmpty() {
        assertThat(so.createdDisplayValue).isEqualTo("")
    }

    @Test
    fun createdDisplayValue_withNameOnly_hasName() {
        so.createdByName = "foo"
        assertThat(so.created).isNull()
        assertThat(so.createdDisplayValue).isEqualTo("foo")
    }

    @Test
    fun createdDisplayValue_withDateOnly_hasDate() {
        assertThat(so.createdByName).isNull()
        so.created = LocalDateTime.parse("2017-01-01T10:11:12.345")
        assertThat(so.createdDisplayValue).isEqualTo("2017-01-01 10:11:12")
    }

    @Test
    fun createdDisplayValue() {
        so.createdByName = "foo"
        so.created = LocalDateTime.parse("2017-01-01T10:11:12.345")
        assertThat(so.createdDisplayValue).isEqualTo("foo (2017-01-01 10:11:12)")
    }

    @Test
    fun defaultConstructor() {
        assertThat(SearchOrder()).isNotNull
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(SearchOrder::class.java)
            .withRedefinedSuperclass()
            .usingGetClass()
            .withIgnoredFields(
                SHOW_EXCLUDED.fieldName, CREATED.fieldName, CREATOR_ID.fieldName,
                MODIFIED.fieldName, MODIFIER_ID.fieldName)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .withPrefabValues(SearchCondition::class.java, SearchCondition(1L), SearchCondition(2L))
            .verify()
    }
}
