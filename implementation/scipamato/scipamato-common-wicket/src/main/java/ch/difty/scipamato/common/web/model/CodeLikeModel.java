package ch.difty.scipamato.common.web.model;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.CodeLike;
import ch.difty.scipamato.common.persistence.CodeLikeService;

/**
 * Model used in core/public wicket pages to load {@link CodeLike} code implementations
 *
 * @author u.joss
 *
 * @param <T> Code entity extending {@link CodeLike}
 */
public abstract class CodeLikeModel<T extends CodeLike, S extends CodeLikeService<T>> extends LoadableDetachableModel<List<T>> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private S service;

    private final CodeClassId codeClassId;
    private final String languageCode;

    protected CodeLikeModel(final CodeClassId codeClassId, final String languageCode) {
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

    @Override
    protected List<T> load() {
        return service.findCodesOfClass(getCodeClassId(), getLanguageCode());
    }
}
