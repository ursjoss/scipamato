package ch.difty.scipamato.common.web.model

import org.apache.wicket.injection.Injector
import org.apache.wicket.model.LoadableDetachableModel

abstract class InjectedLoadableDetachableModel<T> : LoadableDetachableModel<List<T>>() {

    init {
        @Suppress("LeakingThis")
        injectThis()
    }

    /** for overriding to get wicket-free test stubbing */
    protected open fun injectThis() {
        Injector.get().inject(this)
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
