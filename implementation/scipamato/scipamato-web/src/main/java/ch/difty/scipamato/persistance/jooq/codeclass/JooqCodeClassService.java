package ch.difty.scipamato.persistance.jooq.codeclass;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.entity.CodeClass;
import ch.difty.scipamato.persistence.CodeClassService;

/**
 * jOOQ specific implementation of the {@link CodeClassService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqCodeClassService implements CodeClassService {

    private static final long serialVersionUID = 1L;

    private CodeClassRepository repo;

    @Autowired
    public void setRepository(final CodeClassRepository repo) {
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public List<CodeClass> find(final String languageCode) {
        return repo.find(languageCode);
    }

}
