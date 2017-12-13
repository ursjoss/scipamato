package ch.difty.scipamato.public_.persistence.paper;

import static ch.difty.scipamato.public_.db.tables.Paper.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.public_.db.tables.Paper;
import ch.difty.scipamato.public_.db.tables.records.PaperRecord;
import ch.difty.scipamato.public_.entity.PublicPaper;
import ch.difty.scipamato.public_.entity.filter.PublicPaperFilter;

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

    protected Class<? extends PaperRecord> getRecordClass() {
        return PaperRecord.class;
    }

    @Override
    public PublicPaper findByNumber(final Long number) {
        AssertAs.notNull(number, "number");
        return getDsl().selectFrom(getTable()).where(PAPER.NUMBER.equal(number)).fetchOneInto(PublicPaper.class);
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

    @Override
    public List<Long> findPageOfNumbersByFilter(PublicPaperFilter filter, PaginationContext pc) {
        final Condition conditions = filterConditionMapper.map(filter);
        final Collection<SortField<PublicPaper>> sortCriteria = getSortMapper().map(pc.getSort(), getTable());
        return getDsl().select().from(getTable()).where(conditions).orderBy(sortCriteria).limit(pc.getPageSize()).offset(pc.getOffset()).fetch(PAPER.NUMBER);
    }

}
