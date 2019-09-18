package ch.difty.scipamato.core.web.paper.result

import ch.difty.scipamato.core.entity.PaperSlimFilter
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider

class JRisAdapater(dataProvider: AbstractPaperSlimProvider<out PaperSlimFilter>) {

    fun build(): String {
        return "foo"
    }
}
