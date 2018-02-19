package ch.difty.scipamato.core.persistence.codeclass;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.codeclass.JooqCodeClassLikeService;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.persistence.CodeClassService;

/**
 * jOOQ specific implementation of the {@link CodeClassService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqCodeClassService extends JooqCodeClassLikeService<CodeClass, CodeClassRepository>
        implements CodeClassService {

    public JooqCodeClassService(final CodeClassRepository codeClassRepository) {
        super(codeClassRepository);
    }
}
