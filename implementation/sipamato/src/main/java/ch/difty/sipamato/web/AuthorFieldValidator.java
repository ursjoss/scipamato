package ch.difty.sipamato.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import com.google.common.base.Strings;

/**
 * Validates author strings, which are expected to be similar to this:<p/>
 *
 * <code>Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.</code>
 *
 * @author u.joss
 */
public class AuthorFieldValidator implements IValidator<String> {
    private static final long serialVersionUID = 1L;

    private final Pattern PATTERN = Pattern.compile("^\\w+(\\s\\w+){0,}(,\\s\\w+(\\s\\w+){0,}){0,}\\.$");

    @Override
    public void validate(final IValidatable<String> validatable) {
        final String candidate = Strings.isNullOrEmpty(validatable.getValue()) ? "" : validatable.getValue();
        final Matcher m = PATTERN.matcher(candidate);
        if (!m.matches()) {
            final ValidationError error = new ValidationError(this);
            validatable.error(error);
        }
    }

}