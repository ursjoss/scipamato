package ch.difty.scipamato.common.web.model;

import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.CodeLike;
import ch.difty.scipamato.common.persistence.CodeLikeService;

/**
 * Model used in core/public wicket pages to load {@link CodeLike} code
 * implementations
 *
 * @param <T>
 *     Code entity extending {@link CodeLike}
 * @author u.joss
 */
@SuppressWarnings("WeakerAccess")
public abstract class CodeLikeModel<T extends CodeLike, S extends CodeLikeService<T>>
    extends InjectedLoadableDetachableModel<T> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private S service;

    private final CodeClassId codeClassId;
    private final String      languageCode;

    protected CodeLikeModel(final CodeClassId codeClassId, final String languageCode) {
        super();
        this.codeClassId = AssertAs.INSTANCE.notNull(codeClassId, "codeClassId");
        this.languageCode = AssertAs.INSTANCE.notNull(languageCode, "languageCode");
    }

    /**
     * Protected constructor for testing without wicket application.
     *
     * @param codeClassId
     *     the id of the code class the code class like entities belong to
     * @param languageCode
     *     the two character language code
     * @param service
     *     the service with which the code class like entities are retrieved.
     */
    protected CodeLikeModel(final CodeClassId codeClassId, final String languageCode, final S service) {
        this(codeClassId, languageCode);
        this.service = service;
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
