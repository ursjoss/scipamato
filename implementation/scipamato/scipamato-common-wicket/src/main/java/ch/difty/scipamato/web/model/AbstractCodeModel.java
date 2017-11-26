package ch.difty.scipamato.web.model;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.entity.CodeClassId;
import ch.difty.scipamato.entity.ScipamatoEntity;

/**
 * Common base class for Code Models.
 * @author u.joss
 *
 * @param <C> Code entity
 */
public abstract class AbstractCodeModel<C extends ScipamatoEntity> extends LoadableDetachableModel<List<C>> {

    private static final long serialVersionUID = 1L;

    private final CodeClassId codeClassId;
    private final String languageCode;

    protected AbstractCodeModel(final CodeClassId codeClassId, final String languageCode) {
        Injector.get().inject(this);
        this.codeClassId = AssertAs.notNull(codeClassId, "codeClassId");
        this.languageCode = AssertAs.notNull(languageCode, "languageCode");
    }

    protected CodeClassId getCodeClassId() {
        return codeClassId;
    }

    protected String getLanguageCode() {
        return languageCode;
    }

}
