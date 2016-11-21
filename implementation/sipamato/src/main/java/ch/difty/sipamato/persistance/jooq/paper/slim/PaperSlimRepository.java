package ch.difty.sipamato.persistance.jooq.paper.slim;

import java.util.List;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.ReadOnlyRepository;

public interface PaperSlimRepository extends ReadOnlyRepository<PaperRecord, PaperSlim, Long, PaperSlimRecordMapper, PaperFilter> {

    /**
     * Finds all {@link PaperSlim}s matching the provided {@link Paper} example - where all non-null fields are used as criteria.
     *
     * <ul>
     * <li>strings are matched <code>contain</code> like ignoring case</li>
     * <li>numbers are matched exactly</li>
     * </ul>
     *
     * @param {@link Paper}
     * @return list of {@link PaperSlim}s
     */
    // TODO use PaperSearchCriteria instead of Paper
    List<PaperSlim> findByExample(Paper example);

}
