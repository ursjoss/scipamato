package ch.difty.scipamato.publ.web.model;

import java.util.List;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.web.model.CodeLikeModel;
import ch.difty.scipamato.publ.entity.Code;
import ch.difty.scipamato.publ.persistence.api.CodeService;

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

    /** just delegating to super, but making load visible to test */
    @Override
    protected List<Code> load() {
        return super.load();
    }

}
