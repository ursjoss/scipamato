package ch.difty.scipamato.web.model;

import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.entity.Code;
import ch.difty.scipamato.entity.CodeClassId;
import ch.difty.scipamato.persistence.CodeService;

/**
 * Model that offers a wicket page to load {@link Code}s.
 *
 * @author u.joss
 */
public class CodeModel extends AbstractCodeModel<Code> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private CodeService service;

    public CodeModel(final CodeClassId codeClassId, final String languageCode) {
        super(codeClassId, languageCode);
    }

    @Override
    protected List<Code> load() {
        return service.findCodesOfClass(getCodeClassId(), getLanguageCode());
    }

}
