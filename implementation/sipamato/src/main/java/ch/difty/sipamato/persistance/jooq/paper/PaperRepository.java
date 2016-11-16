package ch.difty.sipamato.persistance.jooq.paper;

import java.util.List;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.persistance.jooq.EntityRepository;

public interface PaperRepository extends EntityRepository<PaperRecord, Paper, Long, PaperRecordMapper, PaperFilter> {

    /**
     * Finds all {@link Paper}s matching the provided examples - where all non-null fields are used as criteria.
     *
     * <ul>
     * <li>strings are matched <code>contain</code> like ignoring case</li>
     * <li>numbers are matched exactly</li>
     * </ul>
     *
     * @param {@link Paper}
     * @return list of {@link Paper}s
     */
    List<Paper> findByExample(Paper example);

}
