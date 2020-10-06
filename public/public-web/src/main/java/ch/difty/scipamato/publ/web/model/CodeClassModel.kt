package ch.difty.scipamato.publ.web.model

import ch.difty.scipamato.common.web.model.CodeClassLikeModel
import ch.difty.scipamato.publ.entity.CodeClass
import ch.difty.scipamato.publ.persistence.api.CodeClassService

/**
 * Model that offers a wicket page to load [CodeClass]es.
 *
 * @author u.joss
 */
class CodeClassModel(languageCode: String) : CodeClassLikeModel<CodeClass, CodeClassService>(languageCode) {

    /** just delegating to super, but making load visible to test  */
    @Suppress("RedundantOverride")
    override fun load(): List<CodeClass> = super.load()

    companion object {
        private const val serialVersionUID = 1L
    }
}
