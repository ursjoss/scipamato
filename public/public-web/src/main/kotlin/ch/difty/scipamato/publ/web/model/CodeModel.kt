package ch.difty.scipamato.publ.web.model

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.web.model.CodeLikeModel
import ch.difty.scipamato.publ.entity.Code
import ch.difty.scipamato.publ.persistence.api.CodeService

/**
 * Model that offers a wicket page to load [Code]s.
 */
class CodeModel(
    codeClassId: CodeClassId,
    languageCode: String,
) : CodeLikeModel<Code, CodeService>(codeClassId, languageCode) {

    /** just delegating to super, but making load visible to test  */
    public override fun load(): List<Code> = super.load()

    companion object {
        private const val serialVersionUID = 1L
    }
}
