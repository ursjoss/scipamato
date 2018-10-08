package ch.difty.scipamato.core.sync.jobs.language;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.experimental.Delegate;

import ch.difty.scipamato.publ.db.public_.tables.pojos.Language;

/**
 * Facade to the scipamato-public {@link Language} so we can refer to it with a
 * name that is clearly distinct from the scipamato-core language.
 * <p>
 * Also decouples from the jOOQ generated entity class that has constructor
 * parameters sorted based on the order of columns in the table. We don't have
 * control over that and thus avoid passing in constructor arguments.
 *
 * @author u.joss
 */
public class PublicLanguage {

    private static final long serialVersionUID = 1L;

    @Delegate
    private final Language delegate;

    @Builder
    private PublicLanguage(final String code, final boolean mainLanguage, final Timestamp lastSynched) {
        delegate = new Language();
        delegate.setCode(code);
        delegate.setMainLanguage(mainLanguage);
        delegate.setLastSynched(lastSynched);
    }
}
