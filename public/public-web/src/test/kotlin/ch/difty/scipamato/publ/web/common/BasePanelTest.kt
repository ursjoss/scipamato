package ch.difty.scipamato.publ.web.common

import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.publ.web.WicketTest
import org.amshove.kluent.shouldBeEqualTo
import org.apache.wicket.model.Model
import org.junit.jupiter.api.Test

class BasePanelTest : WicketTest() {

    private lateinit var panel: BasePanel<String>

    @Test
    fun instantiatingWithIdOnly() {
        panel = object : BasePanel<String>("panel") {
            private val serialVersionUID: Long = 1L
        }
        panel.localization shouldBeEqualTo "en_us"
    }

    @Test
    fun instantiatingWithIdAndModel() {
        panel = object : BasePanel<String>("panel", Model.of("foo")) {
            private val serialVersionUID: Long = 1L
        }
        panel.localization shouldBeEqualTo "en_us"
    }

    @Test
    fun instantiatingWithIdAndModelAndMode() {
        panel = object : BasePanel<String>("panel", Model.of("foo"), Mode.EDIT) {
            private val serialVersionUID: Long = 1L
        }
        panel.localization shouldBeEqualTo "en_us"
    }
}
