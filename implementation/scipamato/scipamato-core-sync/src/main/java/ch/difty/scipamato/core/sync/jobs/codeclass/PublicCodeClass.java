package ch.difty.scipamato.core.sync.jobs.codeclass;

import java.sql.Timestamp;

import ch.difty.scipamato.db.public_.public_.tables.pojos.CodeClass;

/**
 * Subclassing the scipamato-public {@link CodeClass} so we can refer to it with a name
 * which is distinct from the scipamato-core code class.
 *
 * @author u.joss
 */
public class PublicCodeClass extends CodeClass {

    private static final long serialVersionUID = 1L;

    public PublicCodeClass(final Integer codeClassId, final String langCode, final String name, final String description, final Integer version, final Timestamp created, final Timestamp lastModified,
            final Timestamp lastSynched) {
        super(codeClassId, langCode, name, description, version, created, lastModified, lastSynched);
    }

}
