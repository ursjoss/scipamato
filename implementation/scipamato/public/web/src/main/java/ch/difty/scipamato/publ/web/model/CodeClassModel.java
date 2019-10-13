package ch.difty.scipamato.publ.web.model;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.web.model.CodeClassLikeModel;
import ch.difty.scipamato.publ.entity.CodeClass;
import ch.difty.scipamato.publ.persistence.api.CodeClassService;

/**
 * Model that offers a wicket page to load {@link CodeClass}es.
 *
 * @author u.joss
 */
public class CodeClassModel extends CodeClassLikeModel<CodeClass, CodeClassService> {

    private static final long serialVersionUID = 1L;

    public CodeClassModel(@NotNull final String languageCode) {
        super(languageCode);
    }

    /** just delegating to super, but making load visible to test */
    @NotNull
    @Override
    protected List<CodeClass> load() {
        return super.load();
    }
}
