package ch.difty.scipamato.core.web.user;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.difty.scipamato.common.AssertAs;

/**
 * Validator to verify if the text field contains a password that matches
 * the password hash passed into the constructor.
 */
public class CurrentPasswordMatchesValidator implements IValidator<String> {

    private final PasswordEncoder passwordEncoder;
    private final String          currentPasswordHashPersisted;

    public CurrentPasswordMatchesValidator(PasswordEncoder passwordEncoder, final String currentPasswordHashPersisted) {
        this.passwordEncoder = AssertAs.notNull(passwordEncoder, "passwordEncoder");
        this.currentPasswordHashPersisted = AssertAs.notNull(currentPasswordHashPersisted,
            "currentPasswordHashPersisted");
    }

    @Override
    public void validate(final IValidatable<String> validatable) {
        final String pwCandidate = validatable.getValue();
        if (!passwordEncoder.matches(pwCandidate, currentPasswordHashPersisted)) {
            ValidationError error = new ValidationError();
            error.addKey(getClass().getSimpleName() + "." + "not-matching");
            validatable.error(error);
        }
    }

}