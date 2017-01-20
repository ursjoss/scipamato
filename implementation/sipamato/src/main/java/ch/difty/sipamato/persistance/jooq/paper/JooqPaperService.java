package ch.difty.sipamato.persistance.jooq.paper;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Paper> findByIds(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            List<Paper> papers = getRepository().findByIds(ids);
            enrichAuditNamesOfAll(papers);
            return papers;
        } else {
            return new ArrayList<Paper>();
        }

    }

}
