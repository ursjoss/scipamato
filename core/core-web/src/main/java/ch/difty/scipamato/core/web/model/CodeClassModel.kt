package ch.difty.scipamato.core.web.model

import ch.difty.scipamato.common.web.model.CodeClassLikeModel
import ch.difty.scipamato.core.entity.CodeClass
import ch.difty.scipamato.core.persistence.CodeClassService

/**
 * Model that offers a wicket page to load [CodeClass]es.
 *
 * @author u.joss
 */
class CodeClassModel(languageCode: String) : CodeClassLikeModel<CodeClass, CodeClassService>(languageCode) {
    /** just delegating to super, but making load visible to test  */
    override fun load(): List<CodeClass> = super.load()

    companion object {
        private const val serialVersionUID = 1L
    }
}
