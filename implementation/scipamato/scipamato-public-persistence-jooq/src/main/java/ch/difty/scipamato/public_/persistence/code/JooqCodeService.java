package ch.difty.scipamato.public_.persistence.code;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.code.JooqCodeLikeService;
import ch.difty.scipamato.public_.entity.Code;
import ch.difty.scipamato.public_.persistence.api.CodeService;

/**
 * jOOQ specific implementation of the {@link CodeService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqCodeService extends JooqCodeLikeService<Code, CodeRepository> implements CodeService {

    private static final long serialVersionUID = 1L;

    public JooqCodeService(final CodeRepository CodeRepository) {
        super(CodeRepository);
    }
}
