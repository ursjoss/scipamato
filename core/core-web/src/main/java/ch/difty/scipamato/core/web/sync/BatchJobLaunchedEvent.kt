package ch.difty.scipamato.core.web.sync

import org.apache.wicket.ajax.AjaxRequestTarget
import java.io.Serializable

class BatchJobLaunchedEvent(var target: AjaxRequestTarget) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
