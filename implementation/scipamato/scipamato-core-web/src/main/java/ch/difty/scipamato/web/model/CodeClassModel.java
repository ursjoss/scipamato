package ch.difty.scipamato.web.model;

import ch.difty.scipamato.entity.CodeClass;
import ch.difty.scipamato.persistence.CodeClassService;

/**
 * Model that offers a wicket page to load {@link CodeClass}es.
 *
 * @author u.joss
 */
public class CodeClassModel extends CodeClassLikeModel<CodeClass, CodeClassService> {

    private static final long serialVersionUID = 1L;

    public CodeClassModel(final String languageCode) {
        super(languageCode);
    }

}
