package ch.difty.scipamato.core.web.user;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.entity.search.UserFilter;
import ch.difty.scipamato.core.persistence.UserService;

class UserProvider extends SortableDataProvider<User, String> implements IFilterStateLocator<UserFilter> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private UserService service;

    private UserFilter filter;

    UserProvider() {
        this(null);
    }

    UserProvider(UserFilter filter) {
        Injector
            .get()
            .inject(this);
        this.filter = filter != null ? filter : new UserFilter();
        setSort(User.UserFields.USER_NAME.getFieldName(), SortOrder.ASCENDING);
    }

    /** package-private for test purposes */
    void setService(UserService service) {
        this.service = service;
    }

    @Override
    public Iterator<User> iterator(long offset, long size) {
        Sort.Direction dir = getSort().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortProp = getSort().getProperty();
        PaginationContext pc = new PaginationRequest((int) offset, (int) size, dir, sortProp);
        return service
            .findPageByFilter(filter, pc)
            .iterator();
    }

    @Override
    public long size() {
        return service.countByFilter(filter);
    }

    @Override
    public IModel<User> model(User entity) {
        return new Model<>(entity);
    }

    @Override
    public UserFilter getFilterState() {
        return filter;
    }

    @Override
    public void setFilterState(UserFilter state) {
        this.filter = state;
    }
}