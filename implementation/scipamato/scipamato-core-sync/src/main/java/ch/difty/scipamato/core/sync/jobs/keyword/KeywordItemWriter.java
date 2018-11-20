package ch.difty.scipamato.core.sync.jobs.keyword;

import static ch.difty.scipamato.publ.db.public_.tables.Keyword.KEYWORD;

import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;

import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter;

/**
 * {@link ItemWriter} implementation to synchronize keywords from scipamato-core to
 * public.
 *
 * <ul>
 * <li>Takes care of inserts and updates.</li>
 * <li>Every record will be updated at least with the current timestamp in
 * last_synched.</li>
 * <li>id columns and audit fields are those of the scipamato-core tables</li>
 * </ul>
 *
 * @author u.joss
 */
public class KeywordItemWriter extends ScipamatoItemWriter<PublicKeyword> {

    @SuppressWarnings("WeakerAccess")
    public KeywordItemWriter(final DSLContext jooqDslContextPublic) {
        super(jooqDslContextPublic, "keyword");
    }

    @Override
    protected int executeUpdate(final PublicKeyword c) {
        return getDslContext()
            .insertInto(KEYWORD)
            .columns(KEYWORD.ID, KEYWORD.KEYWORD_ID, KEYWORD.LANG_CODE, KEYWORD.NAME, KEYWORD.VERSION, KEYWORD.CREATED,
                KEYWORD.LAST_MODIFIED, KEYWORD.LAST_SYNCHED, KEYWORD.SEARCH_OVERRIDE)
            .values(c.getId(), c.getKeywordId(), c.getLangCode(), c.getName(), c.getVersion(), c.getCreated(),
                c.getLastModified(), c.getLastSynched(), c.getSearchOverride())
            .onConflict(KEYWORD.ID)
            .doUpdate()
            .set(KEYWORD.KEYWORD_ID, c.getKeywordId())
            .set(KEYWORD.LANG_CODE, c.getLangCode())
            .set(KEYWORD.NAME, c.getName())
            .set(KEYWORD.VERSION, c.getVersion())
            .set(KEYWORD.CREATED, c.getCreated())
            .set(KEYWORD.LAST_MODIFIED, c.getLastModified())
            .set(KEYWORD.LAST_SYNCHED, c.getLastSynched())
            .set(KEYWORD.SEARCH_OVERRIDE, c.getSearchOverride())
            .where(KEYWORD.ID.eq(c.getId()))
            .execute();
    }

}
