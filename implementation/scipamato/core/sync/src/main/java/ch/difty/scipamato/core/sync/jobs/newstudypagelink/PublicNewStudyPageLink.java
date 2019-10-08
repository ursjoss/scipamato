package ch.difty.scipamato.core.sync.jobs.newstudypagelink;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.experimental.Delegate;

import ch.difty.scipamato.publ.db.tables.pojos.NewStudyPageLink;

/**
 * Facade to the scipamato-public {@link NewStudyPageLink} so we can refer to it with a
 * name that is clearly distinct from the scipamato-core NewStudyPageLink.
 * <p>
 * Also decouples from the jOOQ generated entity class that has constructor
 * parameters sorted based on the order of columns in the table. We don't have
 * control over that and thus avoid passing in constructor arguments.
 *
 * @author u.joss
 */
public class PublicNewStudyPageLink {

    @Delegate
    private final NewStudyPageLink delegate;

    @Builder
    private PublicNewStudyPageLink(final String langCode, final int sort, final String title, final String url,
        final Timestamp lastSynched) {
        delegate = new NewStudyPageLink();
        delegate.setLangCode(langCode);
        delegate.setSort(sort);
        delegate.setTitle(title);
        delegate.setUrl(url);
        delegate.setLastSynched(lastSynched);
    }
}
