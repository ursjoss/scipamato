package ch.difty.scipamato.core.sync.jobs.code;

import java.sql.Timestamp;

import ch.difty.scipamato.db.public_.public_.tables.pojos.Code;

/**
 * Subclassing the scipamato-public {@link Code} so we can refer to it with a name
 * which is distinct from the scipamato-core code.
 *
 * @author u.joss
 */
public class PublicCode extends Code {

    private static final long serialVersionUID = 1L;

    public PublicCode(final String code, final String langCode, final Integer codeClassId, final String name, final String comment, final Integer sort, final Integer version, final Timestamp created,
            final Timestamp lastModified, final Timestamp lastSynched) {
        super(code, langCode, codeClassId, name, comment, sort, version, created, lastModified, lastSynched);
    }

}
