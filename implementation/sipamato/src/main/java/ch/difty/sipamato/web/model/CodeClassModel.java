package ch.difty.sipamato.web.model;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.sipamato.entity.CodeClass;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.service.CodeClassService;

/**
 * Model that offers a wicket page to load {@link CodeClass}es.
 *
 * @author u.joss
 */
public class CodeClassModel extends LoadableDetachableModel<List<CodeClass>> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private CodeClassService service;

    private final String languageCode;

    public CodeClassModel(final String languageCode) {
        Injector.get().inject(this);
        this.languageCode = AssertAs.notNull(languageCode, "languageCode");
    }

    @Override
    protected List<CodeClass> load() {
        return service.find(languageCode);
    }

}
