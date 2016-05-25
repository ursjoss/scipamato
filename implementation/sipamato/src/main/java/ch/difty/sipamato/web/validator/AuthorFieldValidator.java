package ch.difty.sipamato.web.validator;

import java.util.regex.Pattern;

import org.apache.wicket.validation.validator.PatternValidator;

/**
 * Validates author strings, which are expected to be similar to this:<p/>
 *
 * <code>Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.</code>
 *
 * @author u.joss
 */
public class AuthorFieldValidator extends PatternValidator {
    private static final long serialVersionUID = 1L;

    private static final AuthorFieldValidator INSTANCE = new AuthorFieldValidator();

    public static AuthorFieldValidator getInstance() {
        return INSTANCE;
    }

    protected AuthorFieldValidator() {
        super("^\\w+(\\s\\w+){0,}(,\\s\\w+(\\s\\w+){0,}){0,}\\.$", Pattern.CASE_INSENSITIVE);
    }

}