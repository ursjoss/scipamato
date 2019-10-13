package ch.difty.scipamato.publ.persistence.codeclass;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.codeclass.JooqCodeClassLikeService;
import ch.difty.scipamato.publ.entity.CodeClass;
import ch.difty.scipamato.publ.persistence.api.CodeClassService;

/**
 * jOOQ specific implementation of the {@link CodeClassService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqCodeClassService extends JooqCodeClassLikeService<CodeClass, CodeClassRepository>
    implements CodeClassService {

    public JooqCodeClassService(@NotNull final CodeClassRepository codeClassRepository) {
        super(codeClassRepository);
    }
}
