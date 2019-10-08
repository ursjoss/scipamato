package ch.difty.scipamato.core.sync.jobs.newstudypagelink;

import static ch.difty.scipamato.publ.db.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK;

import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;

import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter;

/**
 * {@link ItemWriter} implementation to synchronize newStudyPageLinks from scipamato-core
 * to public.
 *
 * <ul>
 * <li>Takes care of inserts and updates.</li>
 * <li>Every record will be updated at least with the current timestamp in
 * last_synched.</li>
 * </ul>
 *
 * @author u.joss
 */
public class NewStudyPageLinkItemWriter extends ScipamatoItemWriter<PublicNewStudyPageLink> {

    public NewStudyPageLinkItemWriter(final DSLContext jooqDslContextPublic) {
        super(jooqDslContextPublic, "newStudyPageLink");
    }

    @Override
    protected int executeUpdate(final PublicNewStudyPageLink l) {
        return getDslContext()
            .insertInto(NEW_STUDY_PAGE_LINK)
            .columns(NEW_STUDY_PAGE_LINK.LANG_CODE, NEW_STUDY_PAGE_LINK.SORT, NEW_STUDY_PAGE_LINK.TITLE,
                NEW_STUDY_PAGE_LINK.URL, NEW_STUDY_PAGE_LINK.LAST_SYNCHED)
            .values(l.getLangCode(), l.getSort(), l.getTitle(), l.getUrl(), l.getLastSynched())
            .onConflict(NEW_STUDY_PAGE_LINK.LANG_CODE, NEW_STUDY_PAGE_LINK.SORT)
            .doUpdate()
            .set(NEW_STUDY_PAGE_LINK.LANG_CODE, l.getLangCode())
            .set(NEW_STUDY_PAGE_LINK.SORT, l.getSort())
            .set(NEW_STUDY_PAGE_LINK.TITLE, l.getTitle())
            .set(NEW_STUDY_PAGE_LINK.URL, l.getUrl())
            .set(NEW_STUDY_PAGE_LINK.LAST_SYNCHED, l.getLastSynched())
            .where(NEW_STUDY_PAGE_LINK.LANG_CODE
                .eq(l.getLangCode())
                .and(NEW_STUDY_PAGE_LINK.SORT.eq(l.getSort())))
            .execute();
    }

}
