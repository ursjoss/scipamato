package ch.difty.scipamato.web.model;

import ch.difty.scipamato.entity.Code;
import ch.difty.scipamato.entity.CodeClassId;
import ch.difty.scipamato.persistence.CodeService;

/**
 * Model that offers a wicket page to load {@link Code}s.
 *
 * @author u.joss
 */
public class CodeModel extends CodeLikeModel<Code, CodeService> {

    private static final long serialVersionUID = 1L;

    public CodeModel(final CodeClassId codeClassId, final String languageCode) {
        super(codeClassId, languageCode);
    }

}
