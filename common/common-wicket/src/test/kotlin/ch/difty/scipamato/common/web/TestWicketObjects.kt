package ch.difty.scipamato.common.web

import ch.difty.scipamato.common.config.ApplicationProperties
import com.giffing.wicket.spring.boot.context.scan.WicketHomePage
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX
import org.apache.wicket.bean.validation.PropertyValidator
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters

@WicketHomePage
class TestHomePage : AbstractPage<Unit>(PageParameters()) {

    override val properties: ApplicationProperties
        get() = TestApplicationProperties()
    override val isNavbarVisible: Boolean
        get() = true

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

class TestAbstractPage(model: IModel<TestRecord>) : AbstractPage<TestRecord>(CompoundPropertyModel.of(model)) {

    override fun onInitialize() {
        super.onInitialize()

        queuePanelHeadingFor("panel")
        queue(Form<TestRecord>("form"))
        queueFieldAndLabel(TextField<String>("name"))
        queue(newResponsePageButton("respPageButton") { TestAbstractPage(Model(TestRecord(10, "bar"))) })
    }

    override val properties: ApplicationProperties
        get() = TestApplicationProperties()
    override val isNavbarVisible: Boolean
        get() = true

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

class TestAbstractPanel(id: String, mode: Mode) : AbstractPanel<TestRecord>(id, null, mode) {

    override fun onInitialize() {
        super.onInitialize()
        queueFieldAndLabel(TextField<String>("foo"))
        queueFieldAndLabel(TextField<String>("bar"), PropertyValidator<String>())
        queueCheckBoxAndLabel(CheckBoxX("baz", Model.of(true)))
    }

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
