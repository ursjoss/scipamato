package ch.difty.scipamato.publ.persistence.code;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.code.JooqCodeLikeService;
import ch.difty.scipamato.publ.entity.Code;
import ch.difty.scipamato.publ.persistence.api.CodeService;

/**
 * jOOQ specific implementation of the {@link CodeService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqCodeService extends JooqCodeLikeService<Code, CodeRepository> implements CodeService {

    public JooqCodeService(final CodeRepository codeRepository) {
        super(codeRepository);
    }
}
