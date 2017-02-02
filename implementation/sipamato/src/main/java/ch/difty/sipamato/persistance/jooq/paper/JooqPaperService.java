package ch.difty.sipamato.persistance.jooq.paper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
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

    /** {@inheritDocs} */
    @Override
    public List<Paper> findBySearchOrder(SearchOrder searchOrder) {
        return getRepository().findBySearchOrder(searchOrder);
    }

    /** {@inheritDocs} */
    @Override
    public Page<Paper> findBySearchOrder(SearchOrder searchOrder, Pageable pageable) {
        return getRepository().findBySearchOrder(searchOrder, pageable);
    }

    /** {@inheritDocs} */
    @Override
    public int countBySearchOrder(SearchOrder searchOrder) {
        return getRepository().countBySearchOrder(searchOrder);
    }

}
