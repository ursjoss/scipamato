package ch.difty.scipamato.entity;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.scipamato.entity.filter.SearchCondition;

@RunWith(MockitoJUnitRunner.class)
public class SearchOrderTest {

    private static final String SO_NAME = "soName";

    private final SearchOrder so = new SearchOrder(10l, SO_NAME, 1, false, null, null);

    @Mock
    public SearchCondition mockCondition1;
    @Mock
    public SearchCondition mockCondition2;

    private List<SearchCondition> searchConditions = new ArrayList<>();
    private List<Long> excludedIds = new ArrayList<>();

    @Test
    public void testGetters() {
        assertThat(so.getId()).isEqualTo(10);
        assertThat(so.getOwner()).isEqualTo(1);
        assertThat(so.isGlobal()).isEqualTo(false);
        assertThat(so.isShowExcluded()).isEqualTo(false);
    }

    @Test
    public void testSetters() {
        so.setId(11l);
        so.setOwner(2);
        so.setName(SO_NAME);
        so.setGlobal(true);
        so.setShowExcluded(true);

        assertThat(so.getId()).isEqualTo(11);
        assertThat(so.getName()).isEqualTo(SO_NAME);
        assertThat(so.getOwner()).isEqualTo(2);
        assertThat(so.isGlobal()).isEqualTo(true);
        assertThat(so.isShowExcluded()).isEqualTo(true);
    }

    @Test
    public void whenInstantiating_withNullLists_containsNoItems() {
        assertThat(so.getSearchConditions()).isEmpty();
        assertThat(so.getExcludedPaperIds()).isEmpty();
    }

    @Test
    public void whenInstantiating_withEmptyConditionList_hasNoConditions() {
        assertThat(new SearchOrder(searchConditions).getSearchConditions()).isEmpty();
    }

    @Test
    public void whenInstantiating_withEmptyExclusionList_hasNoExclusions() {
        assertThat(excludedIds).isEmpty();
        assertThat(new SearchOrder(10l, SO_NAME, 1, false, null, excludedIds).getExcludedPaperIds()).isEmpty();
    }

    @Test
    public void whenInstantiating_withNonEmptyConditionList_hasHandedOverConditions() {
        searchConditions.addAll(Arrays.asList(mockCondition1, mockCondition2));
        assertThat(new SearchOrder(searchConditions).getSearchConditions()).containsExactly(mockCondition1, mockCondition2);
    }

    @Test
    public void whenInstantiating_withNonEmptyExlusionList_hasHandedOverExclusions() {
        excludedIds.add(3l);
        excludedIds.add(5l);
        assertThat(new SearchOrder(10l, SO_NAME, 1, false, null, excludedIds).getExcludedPaperIds()).containsExactly(3l, 5l);
    }

    @Test
    public void whenAddingNullCondition_itIsNotAdded() {
        so.add(null);
        assertThat(so.getSearchConditions()).isEmpty();
    }

    @Test
    public void whenAddingCondition_itIsGettingAdded() {
        so.add(mockCondition1);
        assertThat(so.getSearchConditions()).containsExactly(mockCondition1);
    }

    @Test
    public void whenRemovingSearchCondition_withNullParameter_doesNothing() {
        so.add(mockCondition1);
        so.add(mockCondition2);
        assertThat(so.getSearchConditions()).containsExactly(mockCondition1, mockCondition2);

        so.remove(null);

        assertThat(so.getSearchConditions()).containsExactly(mockCondition1, mockCondition2);
    }

    @Test
    public void whenRemovingSearchCondition_withConditionWhichIsPresent_doesRemoveIt() {
        so.add(mockCondition1);
        so.add(mockCondition2);
        assertThat(so.getSearchConditions()).containsExactly(mockCondition1, mockCondition2);

        so.remove(mockCondition2);

        assertThat(so.getSearchConditions()).containsExactly(mockCondition1);
    }

    @Test
    public void whenRemovingCondition_withConditionWhichIsNotPresent_doesNothing() {
        so.add(mockCondition2);
        assertThat(so.getSearchConditions()).containsExactly(mockCondition2);

        so.remove(mockCondition1);

        assertThat(so.getSearchConditions()).containsExactly(mockCondition2);
    }

    @Test
    public void whenAddingExclusion_itIsGettingAdded() {
        so.addExclusionOfPaperWithId(5l);
        assertThat(so.getExcludedPaperIds()).containsExactly(5l);
    }

    @Test
    public void whenAddingExclusion_withExclusionAlreadyPresent_doesNotAddItAnymore() {
        so.addExclusionOfPaperWithId(5l);
        assertThat(so.getExcludedPaperIds()).containsExactly(5l);
        so.addExclusionOfPaperWithId(5l);
        assertThat(so.getExcludedPaperIds()).containsExactly(5l);
    }

    @Test
    public void whenRemovingExclusion_whichWasExcluded_doesRemoveIt() {
        so.addExclusionOfPaperWithId(5l);
        so.addExclusionOfPaperWithId(8l);
        assertThat(so.getExcludedPaperIds()).containsExactly(5l, 8l);

        so.removeExlusionOfPaperWithId(5l);

        assertThat(so.getExcludedPaperIds()).containsExactly(8l);
    }

    @Test
    public void whenRemovingExclusion_whichWasNotExcluded_doesNothing() {
        so.addExclusionOfPaperWithId(5l);
        assertThat(so.getExcludedPaperIds()).containsExactly(5l);

        so.removeExlusionOfPaperWithId(8l);

        assertThat(so.getExcludedPaperIds()).containsExactly(5l);
    }

    @Test
    public void testingToString_withNoConditionsOrExclusions() {
        assertThat(so.getSearchConditions()).hasSize(0);
        assertThat(so.getExcludedPaperIds()).hasSize(0);
        assertThat(so.toString()).isEqualTo(
                "SearchOrder[name=soName,owner=1,global=false,searchConditions=[],excludedPaperIds=[],showExcluded=false,id=10,created=<null>,createdBy=<null>,lastModified=<null>,lastModifiedBy=<null>,version=0]");
    }

    @Test
    public void testingToString_withConditionsAndExclusions() {
        so.add(mockCondition1);
        so.add(mockCondition2);
        so.addExclusionOfPaperWithId(3l);
        so.addExclusionOfPaperWithId(5l);
        assertThat(so.toString()).isEqualTo(
                "SearchOrder[name=soName,owner=1,global=false,searchConditions=[mockCondition1, mockCondition2],excludedPaperIds=[3, 5],showExcluded=false,id=10,created=<null>,createdBy=<null>,lastModified=<null>,lastModifiedBy=<null>,version=0]");
    }

    @Test
    public void testingDisplayValue_withNoConditions_returnsIDOnly() {
        assertThat(so.getSearchConditions()).hasSize(0);
        assertThat(so.getDisplayValue()).isEqualTo("soName:  (10)");
    }

    @Test
    public void testingDisplayValue_forGlobalSearchOrderWithNoConditions_returnsIdPlusGlobalIndicator() {
        assertThat(so.getSearchConditions()).hasSize(0);
        so.setGlobal(true);
        assertThat(so.getDisplayValue()).isEqualTo("soName:  (10)*");
    }

    @Test
    public void testingDisplayValue_withoutName_forGlobalSearchOrderWithNoConditions_returnsIdPlusGlobalIndicator() {
        so.setName(null);
        assertThat(so.getSearchConditions()).hasSize(0);
        so.setGlobal(true);
        assertThat(so.getDisplayValue()).isEqualTo("-- (10)*");
    }

    @Test
    public void testingDisplayValue_withoutNameButWithSingleCondition_returnsIt() {
        SearchOrder so1 = new SearchOrder(10l, null, 1, false, null, excludedIds);
        so1.add(new SearchCondition() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getDisplayValue() {
                return "f1DisplayValue";
            }
        });

        assertThat(so1.getDisplayValue()).isEqualTo("f1DisplayValue (10)");
    }

    @Test
    public void testingDisplayValue_withSingleCondition_returnsIt() {
        so.add(new SearchCondition() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getDisplayValue() {
                return "f1DisplayValue";
            }
        });

        assertThat(so.getDisplayValue()).isEqualTo("soName: f1DisplayValue (10)");
    }

    @Test
    public void testingDisplayValue_withTwoConditions_joinsThemUsingOR() {
        so.add(new SearchCondition() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getDisplayValue() {
                return "c1DisplayValue";
            }
        });
        so.add(new SearchCondition() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getDisplayValue() {
                return "c2DisplayValue";
            }
        });

        assertThat(so.getDisplayValue()).isEqualTo("soName: c1DisplayValue; OR c2DisplayValue (10)");
    }

    @Test
    public void cannotAddTheSameSearchTermTwice() {
        SearchCondition c1 = new SearchCondition();
        c1.setAuthors("baz");
        c1.setTitle("foo");
        c1.setPublicationYear("2016");
        c1.setFirstAuthorOverridden(true);
        SearchCondition c2 = new SearchCondition();
        c2.setAuthors("baz");
        c2.setTitle("foo");
        c2.setPublicationYear("2016");
        c2.setFirstAuthorOverridden(true);

        assertThat(so.getSearchConditions()).hasSize(0);
        so.add(c1);
        assertThat(so.getSearchConditions()).hasSize(1);
        so.add(c2);
        assertThat(so.getSearchConditions()).hasSize(1);
    }

    @Test
    public void createdDisplayValue_withNullValues_isEmpty() {
        assertThat(so.getCreatedDisplayValue()).isEqualTo("");
    }

    @Test
    public void createdDisplayValue_withNameOnly_hasName() {
        so.setCreatedByName("foo");
        assertThat(so.getCreated()).isNull();
        assertThat(so.getCreatedDisplayValue()).isEqualTo("foo");
    }

    @Test
    public void createdDisplayValue_withDateOnly_hasDate() {
        assertThat(so.getCreatedByName()).isNull();
        so.setCreated(LocalDateTime.parse("2017-01-01T10:11:12.345"));
        assertThat(so.getCreatedDisplayValue()).isEqualTo("2017-01-01 10:11:12");
    }

    @Test
    public void createdDisplayValue() {
        so.setCreatedByName("foo");
        so.setCreated(LocalDateTime.parse("2017-01-01T10:11:12.345"));
        assertThat(so.getCreatedDisplayValue()).isEqualTo("foo (2017-01-01 10:11:12)");
    }
}