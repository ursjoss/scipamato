package ch.difty.scipamato.core.persistence.paper.slim;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.entity.search.SearchTerm;
import ch.difty.scipamato.core.entity.search.SearchTermType;
import ch.difty.scipamato.core.persistence.paper.searchorder.JooqBySearchOrderRepo;

public class JooqPaperSlimBySearchOrderRepoTest {

    private JooqBySearchOrderRepo<PaperSlim, PaperSlimRecordMapper> finder;

    @Mock
    private DSLContext                                                                      dslMock;
    @Mock
    private PaperSlimRecordMapper                                                           mapperMock;
    @Mock
    private JooqSortMapper<PaperRecord, PaperSlim, ch.difty.scipamato.core.db.tables.Paper> sortMapperMock;
    @Mock
    private PaperSlim                                                                       unpersistedEntity;
    @Mock
    private PaperSlim                                                                       persistedEntity;

    @Before
    public void setUp() {
        finder = new JooqPaperSlimBySearchOrderRepo(dslMock, mapperMock, sortMapperMock);
    }

    @Test
    public void findingBySearchOrder_withNullSearchOrder_throws() {
        assertDegenerateSupplierParameter(() -> finder.findBySearchOrder(null), "searchOrder");
    }

    @Test
    public void countingBySearchOrder_withNullSearchOrder_throws() {
        assertDegenerateSupplierParameter(() -> finder.countBySearchOrder(null), "searchOrder");
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
        SearchCondition sc1 = new SearchCondition(1L);
        sc1.addSearchTerm(SearchTerm.newSearchTerm(2L, SearchTermType.INTEGER.getId(), 1, "publicationYear", ">2014"));
        searchOrder.add(sc1);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo("publication_year > 2014");
    }

    @Test
    public void getConditions_withSearchOrderWithAuditSearchTermForCreatedUser() {
        SearchOrder searchOrder = new SearchOrder();
        SearchCondition sc1 = new SearchCondition(1L);
        sc1.addSearchTerm(SearchTerm.newSearchTerm(2L, SearchTermType.AUDIT.getId(), 1, "paper.created_by", "mkj"));
        searchOrder.add(sc1);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualToIgnoringCase(
            // @formatter:off
            "\"public\".\"paper\".\"id\" in (\n" +
            "  select \"public\".\"paper\".\"id\"\n" +
            "  from \"public\".\"paper\"\n" +
            "    join \"public\".\"scipamato_user\"\n" +
            "    on paper.created_by = \"public\".\"scipamato_user\".\"id\"\n" +
            "  where lower(\"public\".\"scipamato_user\".\"user_name\") like '%mkj%'\n" +
            ")");
            // @formatter:on
    }

    @Test
    public void getConditions_withSearchOrderWithAuditSearchTermForCreationTimeStamp() {
        SearchOrder searchOrder = new SearchOrder();
        SearchCondition sc1 = new SearchCondition(1L);
        SearchTerm st = SearchTerm.newSearchTerm(2L, SearchTermType.AUDIT.getId(), 1, "paper.created",
            ">=\"2017-02-01 23:55:12\"");
        sc1.addSearchTerm(st);
        searchOrder.add(sc1);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo("paper.created >= timestamp '2017-02-01 23:55:12.0'");
    }

    @Test
    public void getConditions_withSearchOrderWithAuditSearchTermForLastModTimeStamp() {
        SearchOrder searchOrder = new SearchOrder();
        SearchCondition sc1 = new SearchCondition(1L);
        SearchTerm st = SearchTerm.newSearchTerm(2L, SearchTermType.AUDIT.getId(), 1, "paper.last_modified",
            "<2017-02-01 23:55:12");
        sc1.addSearchTerm(st);
        searchOrder.add(sc1);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo("paper.last_modified < timestamp '2017-02-01 23:55:12.0'");
    }

    @Test
    public void getConditions_withSearchOrderWithConditions() {
        SearchOrder searchOrder = makeSearchOrderWithConditions();

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualToIgnoringCase(
            // @formatter:off
            "(\n" +
            "  (\n" +
            "    publication_year between 2014 and 2015\n" +
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
            "    first_author_overridden = false\n" +
            "    and exists (\n" +
            "      select 1 \"one\"\n" +
            "      from \"public\".\"paper_code\"\n" +
            "      where (\n" +
            "        \"public\".\"paper_code\".\"paper_id\" = \"public\".\"paper\".\"id\"\n" +
            "        and lower(\"public\".\"paper_code\".\"code\") = '1f'\n" +
            "      )\n" +
            "    )\n" +
            "    and exists (\n" +
            "      select 1 \"one\"\n" +
            "      from \"public\".\"paper_code\"\n" +
            "      where (\n" +
            "        \"public\".\"paper_code\".\"paper_id\" = \"public\".\"paper\".\"id\"\n" +
            "        and lower(\"public\".\"paper_code\".\"code\") = '5s'\n" +
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
        assertThat(cond.toString()).isEqualToIgnoringCase(
            // @formatter:off
            "(\n" +
            "  (\n" +
            "    (\n" +
            "      publication_year between 2014 and 2015\n" +
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
            "      first_author_overridden = false\n" +
            "      and exists (\n" +
            "        select 1 \"one\"\n" +
            "        from \"public\".\"paper_code\"\n" +
            "        where (\n" +
            "          \"public\".\"paper_code\".\"paper_id\" = \"public\".\"paper\".\"id\"\n" +
            "          and lower(\"public\".\"paper_code\".\"code\") = '1f'\n" +
            "        )\n" +
            "      )\n" +
            "      and exists (\n" +
            "        select 1 \"one\"\n" +
            "        from \"public\".\"paper_code\"\n" +
            "        where (\n" +
            "          \"public\".\"paper_code\".\"paper_id\" = \"public\".\"paper\".\"id\"\n" +
            "          and lower(\"public\".\"paper_code\".\"code\") = '5s'\n" +
            "        )\n" +
            "      )\n" +
            "    )\n" +
            "  )\n" +
            "  and \"public\".\"paper\".\"id\" not in (3)\n" +
            ")"
            // @formatter:on
        );
    }

    @Test
    public void getConditions_withSearchOrderWithConditionsAndInvertedExclusions_onlySelectsTheExclusions() {
        SearchOrder searchOrder = makeSearchOrderWithConditions();
        searchOrder.setShowExcluded(true);
        searchOrder.addExclusionOfPaperWithId(3);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualToIgnoringCase("\"public\".\"paper\".\"id\" in (3)");
    }

    @Test
    public void getConditions_withSearchOrderWithConditionsAndExclusions() {
        SearchOrder searchOrder = makeSearchOrderWithConditions();

        searchOrder.addExclusionOfPaperWithId(5);
        searchOrder.addExclusionOfPaperWithId(17);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualToIgnoringCase(
            // @formatter:off
            "(\n" +
            "  (\n" +
            "    (\n" +
            "      publication_year between 2014 and 2015\n" +
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
            "      first_author_overridden = false\n" +
            "      and exists (\n" +
            "        select 1 \"one\"\n" +
            "        from \"public\".\"paper_code\"\n" +
            "        where (\n" +
            "          \"public\".\"paper_code\".\"paper_id\" = \"public\".\"paper\".\"id\"\n" +
            "          and lower(\"public\".\"paper_code\".\"code\") = '1f'\n" +
            "        )\n" +
            "      )\n" +
            "      and exists (\n" +
            "        select 1 \"one\"\n" +
            "        from \"public\".\"paper_code\"\n" +
            "        where (\n" +
            "          \"public\".\"paper_code\".\"paper_id\" = \"public\".\"paper\".\"id\"\n" +
            "          and lower(\"public\".\"paper_code\".\"code\") = '5s'\n" +
            "        )\n" +
            "      )\n" +
            "    )\n" +
            "  )\n" +
            "  and \"public\".\"paper\".\"id\" not in (\n" +
            "    5, 17\n" +
            "  )\n" +
            ")"
        // @formatter:on
        );
    }

    private SearchOrder makeSearchOrderWithConditions() {
        SearchOrder searchOrder = new SearchOrder();

        SearchCondition sc1 = new SearchCondition(1L);
        sc1.addSearchTerm(SearchTerm.newSearchTerm(1L, SearchTermType.STRING.getId(), 1, "authors", "turner"));
        sc1.addSearchTerm(
            SearchTerm.newSearchTerm(2L, SearchTermType.INTEGER.getId(), 1, "publicationYear", "2014-2015"));
        searchOrder.add(sc1);

        SearchCondition sc2 = new SearchCondition(2L);
        sc2.addSearchTerm(
            SearchTerm.newSearchTerm(3L, SearchTermType.BOOLEAN.getId(), 2, "firstAuthorOverridden", "false"));
        sc2.addCode(new Code("1F", "C1F", "", false, 1, "CC1", "", 0));
        sc2.addCode(new Code("5S", "C5S", "", false, 5, "CC5", "", 1));
        searchOrder.add(sc2);
        return searchOrder;
    }

    @Test
    public void getConditions_withSearchOrderWithSingleConditionCoveringNewspaperTopicAndHeadline() {
        SearchOrder searchOrder = new SearchOrder();

        SearchCondition sc1 = new SearchCondition(1L);
        sc1.setNewsletterHeadline("hl");
        sc1.setNewsletterTopic(new NewsletterTopic(1, "nt1"));
        searchOrder.add(sc1);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualToIgnoringCase(
            // @formatter:off
        "exists (\n" +
        "  select 1 \"one\"\n" +
        "  from \"public\".\"paper_newsletter\"\n" +
        "  where (\n" +
        "    \"public\".\"paper_newsletter\".\"paper_id\" = \"public\".\"paper\".\"id\"\n" +
        "    and \"public\".\"paper_newsletter\".\"newsletter_topic_id\" = 1\n" +
        "    and lower(\"public\".\"paper_newsletter\".\"headline\") like lower('%hl%')\n" +
        "  )\n" +
        ")"
        // @formatter:on
        );
    }

    @Test
    public void getConditions_withSearchOrderWithSingleConditionCoveringNewspaperTopic() {
        SearchOrder searchOrder = new SearchOrder();

        SearchCondition sc1 = new SearchCondition(1L);
        sc1.setNewsletterTopic(new NewsletterTopic(1, "nt1"));
        searchOrder.add(sc1);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualToIgnoringCase(
            // @formatter:off
        "exists (\n" +
        "  select 1 \"one\"\n" +
        "  from \"public\".\"paper_newsletter\"\n" +
        "  where (\n" +
        "    \"public\".\"paper_newsletter\".\"paper_id\" = \"public\".\"paper\".\"id\"\n" +
        "    and \"public\".\"paper_newsletter\".\"newsletter_topic_id\" = 1\n" +
        "  )\n" +
        ")"
        // @formatter:on
        );
    }

    @Test
    public void getConditions_withSearchOrderWithSingleConditionCoveringNewspaperHeadline() {
        SearchOrder searchOrder = new SearchOrder();

        SearchCondition sc1 = new SearchCondition(1L);
        sc1.setNewsletterHeadline("hl");
        searchOrder.add(sc1);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualToIgnoringCase(
            // @formatter:off
        "exists (\n" +
        "  select 1 \"one\"\n" +
        "  from \"public\".\"paper_newsletter\"\n" +
        "  where (\n" +
        "    \"public\".\"paper_newsletter\".\"paper_id\" = \"public\".\"paper\".\"id\"\n" +
        "    and lower(\"public\".\"paper_newsletter\".\"headline\") like lower('%hl%')\n" +
        "  )\n" +
        ")"
        // @formatter:on
        );
    }

    @Test
    public void getConditions_withSearchOrderWithTwoonditionCoveringNewspaperTopicAndHeadline() {
        SearchOrder searchOrder = new SearchOrder();

        SearchCondition sc1 = new SearchCondition(1L);
        sc1.setNewsletterHeadline("hl");
        searchOrder.add(sc1);
        SearchCondition sc2 = new SearchCondition(2L);
        sc2.setNewsletterTopic(new NewsletterTopic(1, "nt1"));
        searchOrder.add(sc2);

        Condition cond = finder.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualToIgnoringCase(
            // @formatter:off
        "(\n" +
        "  exists (\n" +
        "    select 1 \"one\"\n" +
        "    from \"public\".\"paper_newsletter\"\n" +
        "    where (\n" +
        "      \"public\".\"paper_newsletter\".\"paper_id\" = \"public\".\"paper\".\"id\"\n" +
        "      and lower(\"public\".\"paper_newsletter\".\"headline\") like lower('%hl%')\n" +
        "    )\n" +
        "  )\n" +
        "  or exists (\n" +
        "    select 1 \"one\"\n" +
        "    from \"public\".\"paper_newsletter\"\n" +
        "    where (\n" +
        "      \"public\".\"paper_newsletter\".\"paper_id\" = \"public\".\"paper\".\"id\"\n" +
        "      and \"public\".\"paper_newsletter\".\"newsletter_topic_id\" = 1\n" +
        "    )\n" +
        "  )\n" +
        ")"
        // @formatter:on
        );
    }
}
