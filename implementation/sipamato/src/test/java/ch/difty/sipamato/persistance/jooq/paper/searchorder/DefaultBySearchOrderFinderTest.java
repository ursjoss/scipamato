package ch.difty.sipamato.persistance.jooq.paper.searchorder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.entity.filter.SearchTerm;
import ch.difty.sipamato.entity.filter.SearchTermType;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.JooqSortMapper;
import ch.difty.sipamato.persistance.jooq.paper.slim.PaperSlimRecordMapper;

public class DefaultBySearchOrderFinderTest {

    private DefaultBySearchOrderFinder<PaperSlim, PaperSlimRecordMapper> finder;

    @Mock
    private DSLContext dslMock;
    @Mock
    private PaperSlimRecordMapper mapperMock;
    @Mock
    private JooqSortMapper<PaperRecord, PaperSlim, ch.difty.sipamato.db.tables.Paper> sortMapperMock;
    @Mock
    private PaperSlim unpersistedEntity;
    @Mock
    private PaperSlim persistedEntity;

    @Before
    public void setUp() {
        finder = new DefaultBySearchOrderFinder<>(dslMock, mapperMock, sortMapperMock, PaperRecord.class);
    }

    @Test
    public void findingBySearchOrder_withNullSearchOrder_throws() {
        try {
            finder.findBySearchOrder((SearchOrder) null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchOrder must not be null.");
        }
    }

    @Test
    public void countingBySearchOrder_withNullSearchOrder_throws() {
        try {
            finder.countBySearchOrder((SearchOrder) null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchOrder must not be null.");
        }
    }

    @Test
    public void getConditions_withEmptySearchOrder() {
        SearchOrder searchOrder = new SearchOrder();
        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo("1 = 0");
    }

    @Test
    public void getConditions_withEmptySearchOrderWithExclusion_IgnoresTheExclusions() {
        SearchOrder searchOrder = new SearchOrder();
        searchOrder.addExclusionOfPaperWithId(3);
        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo("1 = 0");
    }

    @Test
    public void getConditions_withSearchOrderWithIntegerSearchTerm() {
        SearchOrder searchOrder = new SearchOrder();
        SearchCondition sc1 = new SearchCondition(1l);
        sc1.addSearchTerm(SearchTerm.of(2l, SearchTermType.INTEGER.getId(), 1, "publicationYear", ">2014"));
        searchOrder.add(sc1);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo("publicationYear > 2014");
    }

    @Test
    public void getConditions_withSearchOrderWithAuditSearchTermForCreatedUser() {
        SearchOrder searchOrder = new SearchOrder();
        SearchCondition sc1 = new SearchCondition(1l);
        sc1.addSearchTerm(SearchTerm.of(2l, SearchTermType.AUDIT.getId(), 1, "PAPER.CREATED_BY", "mkj"));
        searchOrder.add(sc1);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo(
            // @formatter:off
            "\"PUBLIC\".\"PAPER\".\"ID\" in (\n" +
            "  select \"PUBLIC\".\"PAPER\".\"ID\"\n" +
            "  from \"PUBLIC\".\"PAPER\"\n" +
            "    join \"PUBLIC\".\"USER\"\n" +
            "    on PAPER.CREATED_BY = \"PUBLIC\".\"USER\".\"ID\"\n" +
            "  where lower(\"PUBLIC\".\"USER\".\"USER_NAME\") like '%mkj%'\n" +
            ")");
            // @formatter:on
    }

    @Test
    public void getConditions_withSearchOrderWithAuditSearchTermForCreationTimeStamp() {
        SearchOrder searchOrder = new SearchOrder();
        SearchCondition sc1 = new SearchCondition(1l);
        SearchTerm st = SearchTerm.of(2l, SearchTermType.AUDIT.getId(), 1, "PAPER.CREATED", ">=\"2017-02-01 23:55:12\"");
        sc1.addSearchTerm(st);
        searchOrder.add(sc1);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo("PAPER.CREATED >= timestamp '2017-02-01 23:55:12.0'");
    }

    @Test
    public void getConditions_withSearchOrderWithAuditSearchTermForLastModTimeStamp() {
        SearchOrder searchOrder = new SearchOrder();
        SearchCondition sc1 = new SearchCondition(1l);
        SearchTerm st = SearchTerm.of(2l, SearchTermType.AUDIT.getId(), 1, "PAPER.LAST_MODIFIED", "<2017-02-01 23:55:12");
        sc1.addSearchTerm(st);
        searchOrder.add(sc1);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo("PAPER.LAST_MODIFIED < timestamp '2017-02-01 23:55:12.0'");
    }

    @Test
    public void getConditions_withSearchOrderWithConditions() {
        SearchOrder searchOrder = makeSearchOrderWithConditions();

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo(
        // @formatter:off
            "(\n" +
            "  (\n" +
            "    publicationYear between 2014 and 2015\n" +
            "    and lower(cast(authors as varchar)) like ('%' || replace(\n" +
            "      replace(\n" +
            "        replace(\n" +
            "          lower('turner'), \n" +
            "          '!', \n" +
            "          '!!'\n" +
            "        ), \n" +
            "        '%', \n" +
            "        '!%'\n" +
            "      ), \n" +
            "      '_', \n" +
            "      '!_'\n" +
            "    ) || '%') escape '!'\n" +
            "  )\n" +
            "  or (\n" +
            "    firstAuthorOverridden = false\n" +
            "    and exists (\n" +
            "      select 1 \"one\"\n" +
            "      from \"PUBLIC\".\"PAPER_CODE\"\n" +
            "      where (\n" +
            "        \"PUBLIC\".\"PAPER_CODE\".\"PAPER_ID\" = \"PUBLIC\".\"PAPER\".\"ID\"\n" +
            "        and lower(\"PUBLIC\".\"PAPER_CODE\".\"CODE\") = '1f'\n" +
            "      )\n" +
            "    )\n" +
            "    and exists (\n" +
            "      select 1 \"one\"\n" +
            "      from \"PUBLIC\".\"PAPER_CODE\"\n" +
            "      where (\n" +
            "        \"PUBLIC\".\"PAPER_CODE\".\"PAPER_ID\" = \"PUBLIC\".\"PAPER\".\"ID\"\n" +
            "        and lower(\"PUBLIC\".\"PAPER_CODE\".\"CODE\") = '5s'\n" +
            "      )\n" +
            "    )\n" +
            "  )\n" +
            ")"
        // @formatter:on
        );
    }

    @Test
    public void getConditions_withSearchOrderWithConditionsAndExclusions_ignoresExclusions() {
        SearchOrder searchOrder = makeSearchOrderWithConditions();

        searchOrder.addExclusionOfPaperWithId(3);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo(
            // @formatter:off
            "(\n" +
            "  (\n" +
            "    (\n" +
            "      publicationYear between 2014 and 2015\n" +
            "      and lower(cast(authors as varchar)) like ('%' || replace(\n" +
            "        replace(\n" +
            "          replace(\n" +
            "            lower('turner'), \n" +
            "            '!', \n" +
            "            '!!'\n" +
            "          ), \n" +
            "          '%', \n" +
            "          '!%'\n" +
            "        ), \n" +
            "        '_', \n" +
            "        '!_'\n" +
            "      ) || '%') escape '!'\n" +
            "    )\n" +
            "    or (\n" +
            "      firstAuthorOverridden = false\n" +
            "      and exists (\n" +
            "        select 1 \"one\"\n" +
            "        from \"PUBLIC\".\"PAPER_CODE\"\n" +
            "        where (\n" +
            "          \"PUBLIC\".\"PAPER_CODE\".\"PAPER_ID\" = \"PUBLIC\".\"PAPER\".\"ID\"\n" +
            "          and lower(\"PUBLIC\".\"PAPER_CODE\".\"CODE\") = '1f'\n" +
            "        )\n" +
            "      )\n" +
            "      and exists (\n" +
            "        select 1 \"one\"\n" +
            "        from \"PUBLIC\".\"PAPER_CODE\"\n" +
            "        where (\n" +
            "          \"PUBLIC\".\"PAPER_CODE\".\"PAPER_ID\" = \"PUBLIC\".\"PAPER\".\"ID\"\n" +
            "          and lower(\"PUBLIC\".\"PAPER_CODE\".\"CODE\") = '5s'\n" +
            "        )\n" +
            "      )\n" +
            "    )\n" +
            "  )\n" +
            "  and \"PUBLIC\".\"PAPER\".\"ID\" not in (3)\n" +
            ")"
            // @formatter:on
        );
    }

    @Test
    public void getConditions_withSearchOrderWithConditionsAndInvertedExclusions_onlySelectsTheExclusions() {
        SearchOrder searchOrder = makeSearchOrderWithConditions();
        searchOrder.setInvertExclusions(true);
        searchOrder.addExclusionOfPaperWithId(3);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo("\"PUBLIC\".\"PAPER\".\"ID\" in (3)");
    }

    @Test
    public void getConditions_withSearchOrderWithConditionsAndExclusions() {
        SearchOrder searchOrder = makeSearchOrderWithConditions();

        searchOrder.addExclusionOfPaperWithId(5);
        searchOrder.addExclusionOfPaperWithId(17);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo(
        // @formatter:off
            "(\n" +
            "  (\n" +
            "    (\n" +
            "      publicationYear between 2014 and 2015\n" +
            "      and lower(cast(authors as varchar)) like ('%' || replace(\n" +
            "        replace(\n" +
            "          replace(\n" +
            "            lower('turner'), \n" +
            "            '!', \n" +
            "            '!!'\n" +
            "          ), \n" +
            "          '%', \n" +
            "          '!%'\n" +
            "        ), \n" +
            "        '_', \n" +
            "        '!_'\n" +
            "      ) || '%') escape '!'\n" +
            "    )\n" +
            "    or (\n" +
            "      firstAuthorOverridden = false\n" +
            "      and exists (\n" +
            "        select 1 \"one\"\n" +
            "        from \"PUBLIC\".\"PAPER_CODE\"\n" +
            "        where (\n" +
            "          \"PUBLIC\".\"PAPER_CODE\".\"PAPER_ID\" = \"PUBLIC\".\"PAPER\".\"ID\"\n" +
            "          and lower(\"PUBLIC\".\"PAPER_CODE\".\"CODE\") = '1f'\n" +
            "        )\n" +
            "      )\n" +
            "      and exists (\n" +
            "        select 1 \"one\"\n" +
            "        from \"PUBLIC\".\"PAPER_CODE\"\n" +
            "        where (\n" +
            "          \"PUBLIC\".\"PAPER_CODE\".\"PAPER_ID\" = \"PUBLIC\".\"PAPER\".\"ID\"\n" +
            "          and lower(\"PUBLIC\".\"PAPER_CODE\".\"CODE\") = '5s'\n" +
            "        )\n" +
            "      )\n" +
            "    )\n" +
            "  )\n" +
            "  and \"PUBLIC\".\"PAPER\".\"ID\" not in (\n" +
            "    5, 17\n" +
            "  )\n" +
            ")"
        // @formatter:on
        );
    }

    private SearchOrder makeSearchOrderWithConditions() {
        SearchOrder searchOrder = new SearchOrder();

        SearchCondition sc1 = new SearchCondition(1l);
        sc1.addSearchTerm(SearchTerm.of(1l, SearchTermType.STRING.getId(), 1, "authors", "turner"));
        sc1.addSearchTerm(SearchTerm.of(2l, SearchTermType.INTEGER.getId(), 1, "publicationYear", "2014-2015"));
        searchOrder.add(sc1);

        SearchCondition sc2 = new SearchCondition(2l);
        sc2.addSearchTerm(SearchTerm.of(3l, SearchTermType.BOOLEAN.getId(), 2, "firstAuthorOverridden", "false"));
        sc2.addCode(new Code("1F", "C1F", "", false, 1, "CC1", "", 0));
        sc2.addCode(new Code("5S", "C5S", "", false, 5, "CC5", "", 1));
        searchOrder.add(sc2);
        return searchOrder;
    }

    // TODO find simple way to test the jooq part for remaining methods
}
