package ch.difty.scipamato.publ.web.model

import ch.difty.scipamato.common.web.model.InjectedLoadableDetachableModel
import ch.difty.scipamato.publ.entity.Keyword
import ch.difty.scipamato.publ.persistence.api.KeywordService
import org.apache.wicket.spring.injection.annot.SpringBean

class KeywordModel(
    private val languageCode: String,
) : InjectedLoadableDetachableModel<Keyword>() {

    @SpringBean
    private lateinit var service: KeywordService

    public override fun load(): List<Keyword> = service.findKeywords(languageCode)

    companion object {
        private const val serialVersionUID = 1L
    }
}
