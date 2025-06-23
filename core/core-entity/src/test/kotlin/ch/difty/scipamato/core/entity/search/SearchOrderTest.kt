package ch.difty.scipamato.core.entity.search

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID
import ch.difty.scipamato.core.entity.search.SearchOrder.SearchOrderFields.SHOW_EXCLUDED
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

private const val SO_NAME = "soName"

internal class SearchOrderTest {

    private val so = SearchOrder(10L, SO_NAME, 1, false, null, null)

    private val mockCondition1 = SearchCondition().apply { id = "sc1" }

    private val mockCondition2 = SearchCondition().apply { id = "sc2" }

    private val searchConditions = ArrayList<SearchCondition>()
    private val excludedIds = ArrayList<Long>()

    @Test
    fun testGetters() {
        so.id shouldBeEqualTo 10
        so.owner shouldBeEqualTo 1
        so.isGlobal shouldBeEqualTo false
        so.isShowExcluded shouldBeEqualTo false
    }

    @Test
    fun testSetters() {
        so.id = 11L
        so.owner = 2
        so.name = SO_NAME
        so.isGlobal = true
        so.isShowExcluded = true

        so.id as Long shouldBeEqualTo 11
        so.name shouldBeEqualTo SO_NAME
        so.owner shouldBeEqualTo 2
        so.isGlobal shouldBeEqualTo true
        so.isShowExcluded shouldBeEqualTo true
    }

    @Test
    fun whenInstantiating_withNullLists_containsNoItems() {
        so.searchConditions.shouldBeEmpty()
        so.excludedPaperIds.shouldBeEmpty()
    }

    @Test
    fun whenInstantiating_withEmptyConditionList_hasNoConditions() {
        SearchOrder(searchConditions).searchConditions.shouldBeEmpty()
    }

    @Test
    fun whenInstantiating_withEmptyExclusionList_hasNoExclusions() {
        excludedIds.shouldBeEmpty()
        SearchOrder(10L, SO_NAME, 1, false, null, excludedIds).excludedPaperIds.shouldBeEmpty()
    }

    @Test
    fun whenInstantiating_withNonEmptyConditionList_hasHandedOverConditions() {
        searchConditions.addAll(listOf(mockCondition1, mockCondition2))
        SearchOrder(searchConditions).searchConditions shouldContainAll listOf(mockCondition1, mockCondition2)
    }

    @Test
    fun whenInstantiating_withNonEmptyExclusionList_hasHandedOverExclusions() {
        excludedIds.add(3L)
        excludedIds.add(5L)
        SearchOrder(10L, SO_NAME, 1, false, null, excludedIds).excludedPaperIds shouldContainAll listOf(3L, 5L)
    }

    @Test
    fun whenAddingNullCondition_itIsNotAdded() {
        so.add(null)
        so.searchConditions.shouldBeEmpty()
    }

    @Test
    fun whenAddingCondition_itIsGettingAdded() {
        so.add(mockCondition1)
        so.searchConditions shouldContainAll listOf(mockCondition1)
    }

    @Test
    fun whenRemovingSearchCondition_withNullParameter_doesNothing() {
        so.add(mockCondition1)
        so.add(mockCondition2)
        so.searchConditions shouldContainAll listOf(mockCondition1, mockCondition2)

        so.remove(null)

        so.searchConditions shouldContainAll listOf(mockCondition1, mockCondition2)
    }

    @Test
    fun whenRemovingSearchCondition_withConditionWhichIsPresent_doesRemoveIt() {
        so.add(mockCondition1)
        so.add(mockCondition2)
        so.searchConditions shouldContainAll listOf(mockCondition1, mockCondition2)

        so.remove(mockCondition2)

        so.searchConditions shouldContainAll listOf(mockCondition1)
    }

    @Test
    fun whenRemovingCondition_withConditionWhichIsNotPresent_doesNothing() {
        so.add(mockCondition2)
        so.searchConditions shouldContainAll listOf(mockCondition2)

        so.remove(mockCondition1)

        so.searchConditions shouldContainAll listOf(mockCondition2)
    }

    @Test
    fun whenAddingExclusion_itIsGettingAdded() {
        so.addExclusionOfPaperWithId(5L)
        so.excludedPaperIds shouldContainAll listOf(5L)
    }

    @Test
    fun whenAddingExclusion_withExclusionAlreadyPresent_doesNotAddItAnymore() {
        so.addExclusionOfPaperWithId(5L)
        so.excludedPaperIds shouldContainAll listOf(5L)
        so.addExclusionOfPaperWithId(5L)
        so.excludedPaperIds shouldContainAll listOf(5L)
    }

    @Test
    fun whenRemovingExclusion_whichWasExcluded_doesRemoveIt() {
        so.addExclusionOfPaperWithId(5L)
        so.addExclusionOfPaperWithId(8L)
        so.excludedPaperIds shouldContainAll listOf(5L, 8L)

        so.removeExclusionOfPaperWithId(5L)

        so.excludedPaperIds shouldContainAll listOf(8L)
    }

    @Test
    fun whenRemovingExclusion_whichWasNotExcluded_doesNothing() {
        so.addExclusionOfPaperWithId(5L)
        so.excludedPaperIds shouldContainAll listOf(5L)

        so.removeExclusionOfPaperWithId(8L)

        so.excludedPaperIds shouldContainAll listOf(5L)
    }

    @Test
    fun testingToString_withNoConditionsOrExclusions() {
        so.searchConditions shouldHaveSize 0
        so.excludedPaperIds shouldHaveSize 0
        // TODO
        //        so.toString() shouldBeEqualTo
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
        // so.toString() shouldBeEqualTo
        // "SearchOrder[name=soName,owner=1,global=false,searchConditions=[mockCondition1, mockCondition2],
        // excludedPaperIds=[3, 5],showExcluded=false,id=10,createdBy=<null>,lastModifiedBy=<null>,created=<null>,
        // lastModified=<null>,version=0]");
    }

    @Test
    fun testingFullDisplayValue_withNoConditions_returnsIDOnly() {
        so.searchConditions shouldHaveSize 0
        so.fullDisplayValue shouldBeEqualTo "soName:  (10)"
    }

    @Test
    fun testingFullDisplayValue_forGlobalSearchOrderWithNoConditions_returnsIdPlusGlobalIndicator() {
        so.searchConditions shouldHaveSize 0
        so.isGlobal = true
        so.fullDisplayValue shouldBeEqualTo "soName:  (10)*"
    }

    @Test
    fun testingFullDisplayValue_withoutName_forGlobalSearchOrderWithNoConditions_returnsIdPlusGlobalIndicator() {
        so.name = null
        so.searchConditions shouldHaveSize 0
        so.isGlobal = true
        so.fullDisplayValue shouldBeEqualTo "-- (10)*"
    }

    @Test
    fun testingFullDisplayValue_withoutNameButWithSingleCondition_returnsIt() {
        val so1 = SearchOrder(10L, null, 1, false, null, excludedIds)
        so1.add(object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = "f1DisplayValue"
        })

        so1.displayValue shouldBeEqualTo "f1DisplayValue (10)"
    }

    @Test
    fun testingFullDisplayValue_withoutNameButWithSingleConditionAndExclusions_returnsIt() {
        val so1 = SearchOrder(10L, null, 1, false, null, listOf(2L))
        so1.add(object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = "f1DisplayValue"
        })

        so1.displayValue shouldBeEqualTo "f1DisplayValue ! (10)"
    }

    @Test
    fun testingFullDisplayValue_withSingleCondition_returnsIt() {
        so.add(object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = "f1DisplayValue"
        })

        so.fullDisplayValue shouldBeEqualTo "soName: f1DisplayValue (10)"
    }

    @Test
    fun testingFullDisplayValue_withTwoConditions_joinsThemUsingOR() {
        so.add(object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = "c1DisplayValue"
        })
        so.add(object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = "c2DisplayValue"
        })

        so.fullDisplayValue shouldBeEqualTo "soName: c1DisplayValue; OR c2DisplayValue (10)"
    }

    @Test
    fun testingDisplayValue_withFullDisplayValueBelowThreshold_returnsFullDisplayValue() {
        so.add(object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = "f1DisplayValue"
        })

        so.displayValue shouldBeEqualTo "soName: f1DisplayValue (10)"
    }

    @Test
    fun testingDisplayValue_withFullDisplayValueAboveThreshold_returnsTruncatedFullDisplayValue() {
        so.add(object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = "c1DisplayValue"
        })
        so.add(object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = "c2DisplayValue"
        })
        so.add(object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = "c3DisplayValue"
        })
        so.add(object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = "c4DisplayValue"
        })
        so.add(object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = "c5DisplayValue"
        })
        so.add(object : SearchCondition() {
            private val serialVersionUID: Long = 1L
            override fun getDisplayValue(): String = "c6DisplayValue"
        })
        so.displayValue shouldBeEqualTo "soName: c1DisplayValue; OR c2DisplayValue; " +
            "OR c3DisplayValue; OR c4DisplayValue; OR c5DisplayValu... (10)"

        so.addExclusionOfPaperWithId(1L)

        so.displayValue shouldBeEqualTo "soName: c1DisplayValue; OR c2DisplayValue; " +
            "OR c3DisplayValue; OR c4DisplayValue; OR c5DisplayValu... ! (10)"

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

        so.searchConditions shouldHaveSize 0
        so.add(c1)
        so.searchConditions shouldHaveSize 1
        so.add(c2)
        so.searchConditions shouldHaveSize 1
    }

    @Test
    fun createdDisplayValue_withNullValues_isEmpty() {
        so.createdDisplayValue shouldBeEqualTo ""
    }

    @Test
    fun createdDisplayValue_withNameOnly_hasName() {
        so.createdByName = "foo"
        so.created.shouldBeNull()
        so.createdDisplayValue shouldBeEqualTo "foo"
    }

    @Test
    fun createdDisplayValue_withDateOnly_hasDate() {
        so.createdByName.shouldBeNull()
        so.created = LocalDateTime.parse("2017-01-01T10:11:12.345")
        so.createdDisplayValue shouldBeEqualTo "2017-01-01 10:11:12"
    }

    @Test
    fun createdDisplayValue() {
        so.createdByName = "foo"
        so.created = LocalDateTime.parse("2017-01-01T10:11:12.345")
        so.createdDisplayValue shouldBeEqualTo "foo (2017-01-01 10:11:12)"
    }

    @Test
    fun defaultConstructor() {
        SearchOrder().shouldNotBeNull()
    }

    @Test
    fun equals() {
        EqualsVerifier.simple()
            .forClass(SearchOrder::class.java)
            .withRedefinedSuperclass()
            .usingGetClass()
            .withIgnoredFields(
                SHOW_EXCLUDED.fieldName, CREATED.fieldName, CREATOR_ID.fieldName,
                MODIFIED.fieldName, MODIFIER_ID.fieldName
            )
            .withPrefabValues(SearchCondition::class.java, SearchCondition(1L), SearchCondition(2L))
            .verify()
    }
}
