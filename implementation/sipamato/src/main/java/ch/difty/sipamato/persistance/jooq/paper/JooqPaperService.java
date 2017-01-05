package ch.difty.sipamato.persistance.jooq.paper;

import org.springframework.stereotype.Service;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.persistance.jooq.JooqEntityService;
import ch.difty.sipamato.service.PaperService;

/**
 * jOOQ specific implementation of the {@link PaperService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqPaperService extends JooqEntityService<Long, PaperRecord, Paper, PaperFilter, PaperRecordMapper, PaperRepository> implements PaperService {

    private static final long serialVersionUID = 1L;

}
