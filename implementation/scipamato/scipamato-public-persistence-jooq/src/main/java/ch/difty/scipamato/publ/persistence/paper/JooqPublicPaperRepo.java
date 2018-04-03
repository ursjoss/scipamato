package ch.difty.scipamato.publ.persistence.paper;

import static ch.difty.scipamato.publ.db.tables.Paper.PAPER;

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
import ch.difty.scipamato.publ.db.tables.Paper;
import ch.difty.scipamato.publ.db.tables.records.PaperRecord;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;

/**
 * The repository to read {@link PublicPaper}s.
 *
 * @author u.joss
 */
@Repository
public class JooqPublicPaperRepo implements PublicPaperRepository {

    private final DSLContext                                      dsl;
    private final JooqSortMapper<PaperRecord, PublicPaper, Paper> sortMapper;
    private final PublicPaperFilterConditionMapper                filterConditionMapper;
    private final AuthorsAbbreviator                              authorsAbbreviator;

    public JooqPublicPaperRepo(final DSLContext dsl, final JooqSortMapper<PaperRecord, PublicPaper, Paper> sortMapper,
            final PublicPaperFilterConditionMapper filterConditionMapper, final AuthorsAbbreviator authorsAbbreviator) {
        this.dsl = dsl;
        this.sortMapper = sortMapper;
        this.filterConditionMapper = filterConditionMapper;
        this.authorsAbbreviator = authorsAbbreviator;
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
        final PaperRecord tuple = getDsl().selectFrom(getTable())
            .where(PAPER.NUMBER.equal(number))
            .fetchOneInto(getRecordClass());
        if (tuple != null)
            return map(tuple);
        else
            return null;
    }

    @Override
    public List<PublicPaper> findPageByFilter(final PublicPaperFilter filter, final PaginationContext pc) {
        final Condition conditions = filterConditionMapper.map(filter);
        final Collection<SortField<PublicPaper>> sortCriteria = getSortMapper().map(pc.getSort(), getTable());
        final List<PaperRecord> tuples = getDsl().selectFrom(getTable())
            .where(conditions)
            .orderBy(sortCriteria)
            .limit(pc.getPageSize())
            .offset(pc.getOffset())
            .fetchInto(getRecordClass());
        return tuples.stream()
            .map(this::map)
            .collect(Collectors.toList());
    }

    /** package-private for test purposes */
    PublicPaper map(final PaperRecord r) {
        final PublicPaper pp = PublicPaper.builder()
            .id(r.getId())
            .number(r.getNumber())
            .pmId(r.getPmId())
            .authors(r.getAuthors())
            .authorsAbbreviated(authorsAbbreviator.abbreviate(r.getAuthors()))
            .title(r.getTitle())
            .location(r.getLocation())
            .publicationYear(r.getPublicationYear())
            .goals(r.getGoals())
            .methods(r.getMethods())
            .population(r.getPopulation())
            .result(r.getResult())
            .comment(r.getComment())
            .build();
        pp.setCreated(r.getCreated() != null ? r.getCreated()
            .toLocalDateTime() : null);
        pp.setLastModified(r.getLastModified() != null ? r.getLastModified()
            .toLocalDateTime() : null);
        pp.setVersion(r.getVersion());
        return pp;
    }

    @Override
    public int countByFilter(final PublicPaperFilter filter) {
        final Condition conditions = filterConditionMapper.map(filter);
        return getDsl().fetchCount(getDsl().selectOne()
            .from(PAPER)
            .where(conditions));
    }

    @Override
    public List<Long> findPageOfNumbersByFilter(final PublicPaperFilter filter, final PaginationContext pc) {
        final Condition conditions = filterConditionMapper.map(filter);
        final Collection<SortField<PublicPaper>> sortCriteria = getSortMapper().map(pc.getSort(), getTable());
        return getDsl().select()
            .from(getTable())
            .where(conditions)
            .orderBy(sortCriteria)
            .limit(pc.getPageSize())
            .offset(pc.getOffset())
            .fetch(PAPER.NUMBER);
    }

}
