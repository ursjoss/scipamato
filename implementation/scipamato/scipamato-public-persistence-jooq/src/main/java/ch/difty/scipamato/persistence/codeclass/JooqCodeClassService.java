package ch.difty.scipamato.persistence.codeclass;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.entity.CodeClass;
import ch.difty.scipamato.persistence.CodeClassService;

/**
 * jOOQ specific implementation of the {@link CodeClassService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqCodeClassService extends JooqCodeClassLikeService<CodeClass, CodeClassRepository> implements CodeClassService {

    private static final long serialVersionUID = 1L;

}
