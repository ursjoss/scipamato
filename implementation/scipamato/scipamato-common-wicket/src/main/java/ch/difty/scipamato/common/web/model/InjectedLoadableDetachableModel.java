package ch.difty.scipamato.common.web.model;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;

public abstract class InjectedLoadableDetachableModel<T> extends LoadableDetachableModel<List<T>> {
    private static final long serialVersionUID = 1L;

    public InjectedLoadableDetachableModel() {
        injectThis();
    }

    /**
     * protected for overriding to get wicket-free test stubbing
     */
    protected void injectThis() {
        Injector.get()
            .inject(this);
    }
}
