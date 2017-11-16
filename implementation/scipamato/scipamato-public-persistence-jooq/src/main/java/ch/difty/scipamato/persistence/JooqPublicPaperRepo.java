package ch.difty.scipamato.persistence;

import static ch.difty.scipamato.db.tables.Paper.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.db.tables.Paper;
import ch.difty.scipamato.db.tables.records.PaperRecord;
import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.persistence.paging.PaginationContext;

/**
 * The repository to read {@link PublicPaper}s.
 *
 * @author u.joss
 */
@Repository
public class JooqPublicPaperRepo implements PublicPaperRepository {

    private static final long serialVersionUID = 1L;

    private final DSLContext dsl;
    private final JooqSortMapper<PaperRecord, PublicPaper, Paper> sortMapper;
    private final PublicPaperFilterConditionMapper filterConditionMapper;

    @Autowired
    public JooqPublicPaperRepo(final DSLContext dsl, final JooqSortMapper<PaperRecord, PublicPaper, Paper> sortMapper, final PublicPaperFilterConditionMapper filterConditionMapper) {
        this.dsl = dsl;
        this.sortMapper = sortMapper;
        this.filterConditionMapper = filterConditionMapper;
    }

    private DSLContext getDsl() {
        return dsl;
    }

    private JooqSortMapper<PaperRecord, PublicPaper, Paper> getSortMapper() {
        return sortMapper;
    }

    private Paper getTable() {
        return PAPER;
    }

    private TableField<PaperRecord, Long> getTableId() {
        return PAPER.ID;
    }

    protected Class<? extends PaperRecord> getRecordClass() {
        return PaperRecord.class;
    }

    @Override
    public PublicPaper findById(final Long id) {
        AssertAs.notNull(id, "id");
        return getDsl().selectFrom(getTable()).where(getTableId().equal(id)).fetchOneInto(PublicPaper.class);
    }

    @Override
    public List<PublicPaper> findPageByFilter(final PublicPaperFilter filter, final PaginationContext pc) {
        final Condition conditions = filterConditionMapper.map(filter);
        final Collection<SortField<PublicPaper>> sortCriteria = getSortMapper().map(pc.getSort(), getTable());
        final List<PaperRecord> tuples = getDsl().selectFrom(getTable()).where(conditions).orderBy(sortCriteria).limit(pc.getPageSize()).offset(pc.getOffset()).fetchInto(getRecordClass());
        return tuples.stream().map(this::map).collect(Collectors.toList());
    }

    private PublicPaper map(final PaperRecord r) {
        PublicPaper pp = PublicPaper
            .builder()
            .id(r.getId())
            .number(r.getNumber())
            .pmId(r.getPmId())
            .authors(r.getAuthors())
            .title(r.getTitle())
            .location(r.getLocation())
            .publicationYear(r.getPublicationYear())
            .goals(r.getGoals())
            .methods(r.getMethods())
            .population(r.getPopulation())
            .result(r.getResult())
            .comment(r.getComment())
            .build();
        pp.setCreated(r.getCreated() != null ? r.getCreated().toLocalDateTime() : null);
        pp.setLastModified(r.getLastModified() != null ? r.getLastModified().toLocalDateTime() : null);
        pp.setVersion(r.getVersion());
        return pp;
    }

    @Override
    public int countByFilter(final PublicPaperFilter filter) {
        final Condition conditions = filterConditionMapper.map(filter);
        return getDsl().fetchCount(getDsl().selectOne().from(PAPER).where(conditions));
    }

}
