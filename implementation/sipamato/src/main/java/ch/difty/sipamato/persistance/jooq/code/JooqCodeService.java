package ch.difty.sipamato.persistance.jooq.code;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.CodeClassId;
import ch.difty.sipamato.service.CodeService;

/**
 * jOOQ specific implementation of the {@link CodeService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqCodeService implements CodeService {

    private static final long serialVersionUID = 1L;

    private CodeRepository repo;

    @Autowired
    public void setRepository(final CodeRepository repo) {
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public List<Code> findCodesOfClass(final CodeClassId codeClassId, final String languageCode) {
        return repo.findCodesOfClass(codeClassId, languageCode);
    }

}
