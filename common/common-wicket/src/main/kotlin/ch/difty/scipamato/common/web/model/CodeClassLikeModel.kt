package ch.difty.scipamato.common.web.model

import ch.difty.scipamato.common.entity.CodeClassLike
import ch.difty.scipamato.common.persistence.CodeClassLikeService
import org.apache.wicket.spring.injection.annot.SpringBean

/**
 * Model used in core/public wicket pages to load [CodeClassLike] code class implementations
 *
 * @param [T] code class implementations of [CodeClassLike]
 * @param [S] service implementations of [CodeClassLikeService]
 */
abstract class CodeClassLikeModel<T : CodeClassLike, in S : CodeClassLikeService<T>>(
    private val languageCode: String,
) : InjectedLoadableDetachableModel<T>() {

    @SpringBean
    private lateinit var service: S

    /**
     * Protected constructor for testing without wicket application.
     *
     * @param languageCode the two character language code, e.g. 'en' or 'de'
     * @param service the service to retrieve the code class like entities
     */
    protected constructor(languageCode: String, service: S) : this(languageCode) {
        this.service = service
    }

    public override fun load(): List<T> = service.find(languageCode)

    companion object {
        private const val serialVersionUID = 1L
    }
}
