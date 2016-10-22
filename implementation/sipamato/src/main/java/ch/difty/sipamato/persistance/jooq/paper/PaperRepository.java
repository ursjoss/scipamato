package ch.difty.sipamato.persistance.jooq.paper;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.EntityRepository;

public interface PaperRepository extends EntityRepository<PaperRecord, Paper, Long, PaperRecordMapper, PaperFilter> {

    /**
     * Finds the {@link Paper} with associated codes matching the provided id.
     * Both the codes and the associated code classes are retrieved from the database in the language
     * with the provided language code.
     *
     * @param id - must not be null
     * @param languageCode the languageCode to retrieve the strings for, e.g. 'en' or 'de'
     * @return the persisted entity <code>T</code> or null if it can't be found.
     * @throws NullArgumentException if the id is null.
     */
    Paper findCompleteById(Long id, String languageCode);

}
