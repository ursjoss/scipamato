package ch.difty.scipamato.core.persistence.paper.slim

import ch.difty.scipamato.common.persistence.JooqSortMapper
import ch.difty.scipamato.core.db.tables.records.PaperRecord
import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import ch.difty.scipamato.core.entity.projection.PaperSlim
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.entity.search.SearchTerm
import ch.difty.scipamato.core.entity.search.SearchTermType
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.jooq.DSLContext
import org.junit.jupiter.api.Test

internal class JooqPaperSlimBySearchOrderRepoTest {

    private val dslMock = mock<DSLContext>()
    private val mapperMock = mock<PaperSlimRecordMapper>()
    private val sortMapperMock = mock<JooqSortMapper<PaperRecord, PaperSlim, ch.difty.scipamato.core.db.tables.Paper>>()

    private var finder = JooqPaperSlimBySearchOrderRepo(dslMock, mapperMock, sortMapperMock)

    @Test
    fun getConditions_withEmptySearchOrder() {
        val searchOrder = SearchOrder()
        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualTo("1 = 0")
    }

    @Test
    fun getConditions_withEmptySearchOrderWithExclusion_IgnoresTheExclusions() {
        val searchOrder = SearchOrder()
        searchOrder.addExclusionOfPaperWithId(3)
        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualTo("1 = 0")
    }

    @Test
    fun getConditions_withSearchOrderWithIntegerSearchTerm() {
        val searchOrder = SearchOrder()
        val sc1 = SearchCondition(1L)
        sc1.addSearchTerm(SearchTerm.newSearchTerm(2L, SearchTermType.INTEGER.id, 1, "publicationYear", ">2014"))
        searchOrder.add(sc1)

        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualTo("publication_year > 2014")
    }

    @Test
    fun getConditions_withSearchOrderWithAuditSearchTermForCreatedUser() {
        val searchOrder = SearchOrder()
        val sc1 = SearchCondition(1L)
        sc1.addSearchTerm(SearchTerm.newSearchTerm(2L, SearchTermType.AUDIT.id, 1, "paper.created_by", "mkj"))
        searchOrder.add(sc1)

        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualToIgnoringCase(
                """"public"."paper"."id" in (
                  |  select "public"."paper"."id"
                  |  from "public"."paper"
                  |    join "public"."scipamato_user"
                  |    on paper.created_by = "public"."scipamato_user"."id"
                  |  where lower("public"."scipamato_user"."user_name") like '%mkj%'
                  |)""".trimMargin()
        )
    }

    @Test
    fun getConditions_withSearchOrderWithAuditSearchTermForCreationTimeStamp() {
        val searchOrder = SearchOrder()
        val sc1 = SearchCondition(1L)
        val st = SearchTerm.newSearchTerm(2L, SearchTermType.AUDIT.id, 1, "paper.created", """>="2017-02-01 23:55:12"""")
        sc1.addSearchTerm(st)
        searchOrder.add(sc1)

        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualTo("paper.created >= timestamp '2017-02-01 23:55:12.0'")
    }

    @Test
    fun getConditions_withSearchOrderWithAuditSearchTermForLastModTimeStamp() {
        val searchOrder = SearchOrder()
        val sc1 = SearchCondition(1L)
        val st = SearchTerm.newSearchTerm(2L, SearchTermType.AUDIT.id, 1, "paper.last_modified", "<2017-02-01 23:55:12")
        sc1.addSearchTerm(st)
        searchOrder.add(sc1)

        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualTo("paper.last_modified < timestamp '2017-02-01 23:55:12.0'")
    }

    @Test
    fun getConditions_withSearchOrderWithConditions() {
        val searchOrder = makeSearchOrderWithConditions()

        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualToIgnoringCase(
                """(
                  |  (
                  |    publication_year between 2014 and 2015
                  |    and lower(cast(authors as varchar)) like lower(('%' || replace(
                  |      replace(
                  |        replace(
                  |          'turner', 
                  |          '!', 
                  |          '!!'
                  |        ), 
                  |        '%', 
                  |        '!%'
                  |      ), 
                  |      '_', 
                  |      '!_'
                  |    ) || '%')) escape '!'
                  |  )
                  |  or (
                  |    first_author_overridden = false
                  |    and exists (
                  |      select 1 "one"
                  |      from "public"."paper_code"
                  |      where (
                  |        "public"."paper_code"."paper_id" = "public"."paper"."id"
                  |        and lower("public"."paper_code"."code") = '1f'
                  |      )
                  |    )
                  |    and exists (
                  |      select 1 "one"
                  |      from "public"."paper_code"
                  |      where (
                  |        "public"."paper_code"."paper_id" = "public"."paper"."id"
                  |        and lower("public"."paper_code"."code") = '5s'
                  |      )
                  |    )
                  |  )
                  |)""".trimMargin()
        )
    }

    @Test
    fun getConditions_withSearchOrderWithConditionsAndExclusions_ignoresExclusions() {
        val searchOrder = makeSearchOrderWithConditions()

        searchOrder.addExclusionOfPaperWithId(3)

        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualToIgnoringCase(
                """(
                  |  (
                  |    (
                  |      publication_year between 2014 and 2015
                  |      and lower(cast(authors as varchar)) like lower(('%' || replace(
                  |        replace(
                  |          replace(
                  |            'turner', 
                  |            '!', 
                  |            '!!'
                  |          ), 
                  |          '%', 
                  |          '!%'
                  |        ), 
                  |        '_', 
                  |        '!_'
                  |      ) || '%')) escape '!'
                  |    )
                  |    or (
                  |      first_author_overridden = false
                  |      and exists (
                  |        select 1 "one"
                  |        from "public"."paper_code"
                  |        where (
                  |          "public"."paper_code"."paper_id" = "public"."paper"."id"
                  |          and lower("public"."paper_code"."code") = '1f'
                  |        )
                  |      )
                  |      and exists (
                  |        select 1 "one"
                  |        from "public"."paper_code"
                  |        where (
                  |          "public"."paper_code"."paper_id" = "public"."paper"."id"
                  |          and lower("public"."paper_code"."code") = '5s'
                  |        )
                  |      )
                  |    )
                  |  )
                  |  and "public"."paper"."id" not in (3)
                  |)""".trimMargin()
        )
    }

    @Test
    fun getConditions_withSearchOrderWithConditionsAndInvertedExclusions_onlySelectsTheExclusions() {
        val searchOrder = makeSearchOrderWithConditions()
        searchOrder.isShowExcluded = true
        searchOrder.addExclusionOfPaperWithId(3)

        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualToIgnoringCase(""""public"."paper"."id" in (3)""")
    }

    @Test
    fun getConditions_withSearchOrderWithConditionsAndExclusions() {
        val searchOrder = makeSearchOrderWithConditions()

        searchOrder.addExclusionOfPaperWithId(5)
        searchOrder.addExclusionOfPaperWithId(17)

        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualToIgnoringCase(
                """(
                  |  (
                  |    (
                  |      publication_year between 2014 and 2015
                  |      and lower(cast(authors as varchar)) like lower(('%' || replace(
                  |        replace(
                  |          replace(
                  |            'turner', 
                  |            '!', 
                  |            '!!'
                  |          ), 
                  |          '%', 
                  |          '!%'
                  |        ), 
                  |        '_', 
                  |        '!_'
                  |      ) || '%')) escape '!'
                  |    )
                  |    or (
                  |      first_author_overridden = false
                  |      and exists (
                  |        select 1 "one"
                  |        from "public"."paper_code"
                  |        where (
                  |          "public"."paper_code"."paper_id" = "public"."paper"."id"
                  |          and lower("public"."paper_code"."code") = '1f'
                  |        )
                  |      )
                  |      and exists (
                  |        select 1 "one"
                  |        from "public"."paper_code"
                  |        where (
                  |          "public"."paper_code"."paper_id" = "public"."paper"."id"
                  |          and lower("public"."paper_code"."code") = '5s'
                  |        )
                  |      )
                  |    )
                  |  )
                  |  and "public"."paper"."id" not in (
                  |    5, 17
                  |  )
                  |)""".trimMargin()
        )
    }

    private fun makeSearchOrderWithConditions(): SearchOrder {
        val searchOrder = SearchOrder()

        val sc1 = SearchCondition(1L)
        sc1.addSearchTerm(SearchTerm.newSearchTerm(1L, SearchTermType.STRING.id, 1, "authors", "turner"))
        sc1.addSearchTerm(SearchTerm.newSearchTerm(2L, SearchTermType.INTEGER.id, 1, "publicationYear", "2014-2015"))
        searchOrder.add(sc1)

        val sc2 = SearchCondition(2L)
        sc2.addSearchTerm(SearchTerm.newSearchTerm(3L, SearchTermType.BOOLEAN.id, 2, "firstAuthorOverridden", "false"))
        sc2.addCode(Code("1F", "C1F", "", false, 1, "CC1", "", 0))
        sc2.addCode(Code("5S", "C5S", "", false, 5, "CC5", "", 1))
        searchOrder.add(sc2)
        return searchOrder
    }

    @Test
    fun getConditions_withSearchOrderWithSingleConditionCoveringNewspaperTopicAndHeadline() {
        val searchOrder = SearchOrder()

        val sc1 = SearchCondition(1L)
        sc1.newsletterHeadline = "hl"
        sc1.setNewsletterTopic(NewsletterTopic(1, "nt1"))
        searchOrder.add(sc1)

        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualToIgnoringCase(
                """exists (
                 |  select 1 "one"
                 |  from "public"."paper_newsletter"
                 |    join "public"."newsletter"
                 |    on "public"."paper_newsletter"."newsletter_id" = "public"."newsletter"."id"
                 |  where (
                 |    "public"."paper_newsletter"."paper_id" = "public"."paper"."id"
                 |    and "public"."paper_newsletter"."newsletter_topic_id" = 1
                 |    and lower("public"."paper_newsletter"."headline") like lower('%hl%')
                 |  )
                 |)""".trimMargin()
        )
    }

    @Test
    fun getConditions_withSearchOrderWithSingleConditionCoveringNewspaperTopic() {
        val searchOrder = SearchOrder()

        val sc1 = SearchCondition(1L)
        sc1.setNewsletterTopic(NewsletterTopic(1, "nt1"))
        searchOrder.add(sc1)

        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualToIgnoringCase(
                """exists (
                  |  select 1 "one"
                  |  from "public"."paper_newsletter"
                  |    join "public"."newsletter"
                  |    on "public"."paper_newsletter"."newsletter_id" = "public"."newsletter"."id"
                  |  where (
                  |    "public"."paper_newsletter"."paper_id" = "public"."paper"."id"
                  |    and "public"."paper_newsletter"."newsletter_topic_id" = 1
                  |  )
                  |)""".trimMargin()
        )
    }

    @Test
    fun getConditions_withSearchOrderWithSingleConditionCoveringNewspaperHeadline() {
        val searchOrder = SearchOrder()

        val sc1 = SearchCondition(1L)
        sc1.newsletterHeadline = "hl"
        searchOrder.add(sc1)

        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualToIgnoringCase(
                """exists (
                  |  select 1 "one"
                  |  from "public"."paper_newsletter"
                  |    join "public"."newsletter"
                  |    on "public"."paper_newsletter"."newsletter_id" = "public"."newsletter"."id"
                  |  where (
                  |    "public"."paper_newsletter"."paper_id" = "public"."paper"."id"
                  |    and lower("public"."paper_newsletter"."headline") like lower('%hl%')
                  |  )
                  |)""".trimMargin()
        )
    }

    @Test
    fun getConditions_withSearchOrderWithSingleConditionCoveringNewspaperIssue() {
        val searchOrder = SearchOrder()

        val sc1 = SearchCondition(1L)
        sc1.newsletterIssue = "i"
        searchOrder.add(sc1)

        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualToIgnoringCase(
                """exists (
                  |  select 1 "one"
                  |  from "public"."paper_newsletter"
                  |    join "public"."newsletter"
                  |    on "public"."paper_newsletter"."newsletter_id" = "public"."newsletter"."id"
                  |  where (
                  |    "public"."paper_newsletter"."paper_id" = "public"."paper"."id"
                  |    and lower("public"."newsletter"."issue") like lower('%i%')
                  |  )
                  |)""".trimMargin()
        )
    }

    @Test
    fun getConditions_withSearchOrderWithTwoConditionsCoveringNewspaperTopicAndHeadline() {
        val searchOrder = SearchOrder()

        val sc1 = SearchCondition(1L)
        sc1.newsletterHeadline = "hl"
        searchOrder.add(sc1)
        val sc2 = SearchCondition(2L)
        sc2.setNewsletterTopic(NewsletterTopic(1, "nt1"))
        searchOrder.add(sc2)

        val cond = finder.getConditionsFrom(searchOrder)
        assertThat(cond.toString()).isEqualToIgnoringCase(
                """(
                  |  exists (
                  |    select 1 "one"
                  |    from "public"."paper_newsletter"
                  |      join "public"."newsletter"
                  |      on "public"."paper_newsletter"."newsletter_id" = "public"."newsletter"."id"
                  |    where (
                  |      "public"."paper_newsletter"."paper_id" = "public"."paper"."id"
                  |      and lower("public"."paper_newsletter"."headline") like lower('%hl%')
                  |    )
                  |  )
                  |  or exists (
                  |    select 1 "one"
                  |    from "public"."paper_newsletter"
                  |      join "public"."newsletter"
                  |      on "public"."paper_newsletter"."newsletter_id" = "public"."newsletter"."id"
                  |    where (
                  |      "public"."paper_newsletter"."paper_id" = "public"."paper"."id"
                  |      and "public"."paper_newsletter"."newsletter_topic_id" = 1
                  |    )
                  |  )
                  |)""".trimMargin()
        )
    }
}
