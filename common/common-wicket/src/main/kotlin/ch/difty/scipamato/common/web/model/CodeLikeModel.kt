package ch.difty.scipamato.common.web.model

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.entity.CodeLike
import ch.difty.scipamato.common.persistence.CodeLikeService
import org.apache.wicket.spring.injection.annot.SpringBean

/**
 * Model used in core/public wicket pages to load [CodeLike] code implementations
 *
 * @param [T]  Code entity extending [CodeLike]
 */
abstract class CodeLikeModel<T : CodeLike, S : CodeLikeService<T>> protected constructor(
    val codeClassId: CodeClassId,
    val languageCode: String,
) : InjectedLoadableDetachableModel<T>() {

    @SpringBean
    private lateinit var service: S

    /**
     * Protected constructor for testing without wicket application.
     *
     * @param codeClassId the id of the code class the code class like entities belong to
     * @param languageCode the two character language code
     * @param service the service with which the code class like entities are retrieved.
     */
    protected constructor(
        codeClassId: CodeClassId,
        languageCode: String,
        service: S,
    ) : this(codeClassId, languageCode) {
        this.service = service
    }

    override fun load(): List<T> = service.findCodesOfClass(codeClassId, languageCode)

    companion object {
        private const val serialVersionUID = 1L
    }
}
