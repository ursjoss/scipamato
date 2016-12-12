package ch.difty.sipamato.persistance.jooq.paper.slim;

import static ch.difty.sipamato.db.tables.Paper.PAPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jooq.Condition;
import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.entity.filter.SearchTerm;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.JooqReadOnlyRepoTest;
import ch.difty.sipamato.persistance.jooq.ReadOnlyRepository;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;

public class JooqPaperSlimRepoTest extends JooqReadOnlyRepoTest<PaperRecord, PaperSlim, Long, ch.difty.sipamato.db.tables.Paper, PaperSlimRecordMapper, PaperFilter> {

    private static final Long SAMPLE_ID = 3l;

    private JooqPaperSlimRepo repo;

    @Override
    protected Long getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected ReadOnlyRepository<PaperRecord, PaperSlim, Long, PaperSlimRecordMapper, PaperFilter> getRepo() {
        if (repo == null) {
            repo = new JooqPaperSlimRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getLocalization());
        }
        return repo;
    }

    @Override
    protected ReadOnlyRepository<PaperRecord, PaperSlim, Long, PaperSlimRecordMapper, PaperFilter> makeRepoFindingEntityById(PaperSlim entity) {
        return new JooqPaperSlimRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getLocalization()) {
            private static final long serialVersionUID = 1L;

            @Override
            public PaperSlim findById(Long id) {
                return entity;
            }
        };
    }

    @Mock
    private PaperSlim unpersistedEntity, persistedEntity;

    @Override
    protected PaperSlim getPersistedEntity() {
        return persistedEntity;
    }

    @Override
    protected PaperSlim getUnpersistedEntity() {
        return unpersistedEntity;
    }

    @Mock
    private PaperRecord persistedRecord;

    @Override
    protected PaperRecord getPersistedRecord() {
        return persistedRecord;
    }

    @Mock
    private PaperSlimRecordMapper mapperMock;

    @Override
    protected PaperSlimRecordMapper getMapper() {
        return mapperMock;
    }

    @Override
    protected Class<PaperSlim> getEntityClass() {
        return PaperSlim.class;
    }

    @Override
    protected ch.difty.sipamato.db.tables.Paper getTable() {
        return PAPER;
    }

    @Override
    protected Class<PaperRecord> getRecordClass() {
        return PaperRecord.class;
    }

    @Override
    protected TableField<PaperRecord, Long> getTableId() {
        return PAPER.ID;
    }

    @Override
    protected void expectEntityIdsWithValues() {
        when(unpersistedEntity.getId()).thenReturn(SAMPLE_ID);
        when(persistedRecord.getId()).thenReturn(SAMPLE_ID);
    }

    @Override
    protected void expectUnpersistedEntityIdNull() {
        when(unpersistedEntity.getId()).thenReturn(null);
    }

    @Override
    protected void verifyUnpersistedEntityId() {
        verify(getUnpersistedEntity()).getId();
    }

    @Override
    protected void verifyPersistedRecordId() {
        verify(persistedRecord).getId();
    }

    @Mock
    private PaperFilter filterMock;

    @Override
    protected PaperFilter getFilter() {
        return filterMock;
    }

    @Test
    public void findingBySearchOrder_withNullSearchOrder_throws() {
        try {
            repo.findBySearchOrder((SearchOrder) null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchOrder must not be null.");
        }
    }

    @Test
    public void countingBySearchOrder_withNullSearchOrder_throws() {
        try {
            repo.countBySearchOrder((SearchOrder) null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchOrder must not be null.");
        }
    }

    @Test
    public void getConditions_withEmptySearchOrder() {
        SearchOrder searchOrder = new SearchOrder();
        Condition cond = repo.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo(
        // @formatter:off
            "(\n" +
            "  1 = 0\n" +
            "  and 1 = 1\n" +
            ")"
        // @formatter:on
        );
    }

    @Test
    public void getConditions_withEmptySearchOrderWithExclusion() {
        SearchOrder searchOrder = new SearchOrder();
        searchOrder.addExclusionOfPaperWithId(3);
        Condition cond = repo.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo(
        // @formatter:off
            "(\n" +
            "  1 = 0\n" +
            "  and \"PUBLIC\".\"PAPER\".\"ID\" not in (3)\n" +
            ")"
        // @formatter:on
        );
    }

    @Test
    public void getConditions_withSearchOrderWithIntegerSearchTerm() {
        SearchOrder searchOrder = new SearchOrder();
        SearchCondition sc1 = new SearchCondition(1l);
        sc1.addSearchTerm(SearchTerm.of(2l, 1, 1, "publicationYear", ">2014"));
        searchOrder.add(sc1);

        Condition cond = repo.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo(
        // @formatter:off
            "(\n" +
            "  (\n" +
            "    1 = 0\n" +
            "    or (\n" +
            "      1 = 1\n" +
            "      and publicationYear > 2014\n" +
            "    )\n" +
            "  )\n" +
            "  and 1 = 1\n" +
            ")"
        // @formatter:on
        );
    }

    @Test
    public void getConditions_withSearchOrderWithConditions() {
        SearchOrder searchOrder = makeSearchOrderWithConditions();

        Condition cond = repo.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo(
        // @formatter:off
            "(\n" +
            "  (\n" +
            "    1 = 0\n" +
            "    or (\n" +
            "      1 = 1\n" +
            "      and publicationYear between 2014 and 2015\n" +
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
            "      1 = 1\n" +
            "      and firstAuthorOverridden = false\n" +
            "      and 1 = 1\n" +
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
            "  and 1 = 1\n" +
            ")"
        // @formatter:on
        );
    }

    @Test
    public void getConditions_withSearchOrderWithConditionsAndExclusions() {
        SearchOrder searchOrder = makeSearchOrderWithConditions();

        searchOrder.addExclusionOfPaperWithId(5);
        searchOrder.addExclusionOfPaperWithId(17);

        Condition cond = repo.getConditionsFrom(searchOrder);
        assertThat(cond.toString()).isEqualTo(
        // @formatter:off
            "(\n" +
            "  (\n" +
            "    1 = 0\n" +
            "    or (\n" +
            "      1 = 1\n" +
            "      and publicationYear between 2014 and 2015\n" +
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
            "      1 = 1\n" +
            "      and firstAuthorOverridden = false\n" +
            "      and 1 = 1\n" +
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
        sc1.addSearchTerm(SearchTerm.of(1l, 2, 1, "authors", "turner"));
        sc1.addSearchTerm(SearchTerm.of(2l, 1, 1, "publicationYear", "2014-2015"));
        searchOrder.add(sc1);

        SearchCondition sc2 = new SearchCondition(2l);
        sc2.addSearchTerm(SearchTerm.of(3l, 0, 2, "firstAuthorOverridden", "false"));
        sc2.addCode(new Code("1F", "C1F", "", false, 1, "CC1", "", 0));
        sc2.addCode(new Code("5S", "C5S", "", false, 5, "CC5", "", 1));
        searchOrder.add(sc2);
        return searchOrder;
    }
}
