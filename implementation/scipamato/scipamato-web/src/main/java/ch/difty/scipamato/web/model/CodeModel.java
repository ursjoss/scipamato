package ch.difty.scipamato.web.model;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.entity.Code;
import ch.difty.scipamato.entity.CodeClassId;
import ch.difty.scipamato.persistence.CodeService;

/**
 * Model that offers a wicket page to load {@link Code}s.
 *
 * @author u.joss
 */
public class CodeModel extends LoadableDetachableModel<List<Code>> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private CodeService service;

    private final CodeClassId codeClassId;
    private final String languageCode;

    public CodeModel(final CodeClassId codeClassId, final String languageCode) {
        Injector.get().inject(this);
        this.codeClassId = AssertAs.notNull(codeClassId, "codeClassId");
        this.languageCode = AssertAs.notNull(languageCode, "languageCode");
    }

    @Override
    protected List<Code> load() {
        return service.findCodesOfClass(codeClassId, languageCode);
    }

}
