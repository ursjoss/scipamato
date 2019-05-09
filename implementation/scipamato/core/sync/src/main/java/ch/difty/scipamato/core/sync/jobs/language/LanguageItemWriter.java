package ch.difty.scipamato.core.sync.jobs.language;

import static ch.difty.scipamato.publ.db.public_.tables.Language.LANGUAGE;

import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;

import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter;

/**
 * {@link ItemWriter} implementation to synchronize languages from scipamato-core
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
public class LanguageItemWriter extends ScipamatoItemWriter<PublicLanguage> {

    @SuppressWarnings("WeakerAccess")
    public LanguageItemWriter(final DSLContext jooqDslContextPublic) {
        super(jooqDslContextPublic, "language");
    }

    @Override
    protected int executeUpdate(final PublicLanguage l) {
        return getDslContext()
            .insertInto(LANGUAGE)
            .columns(LANGUAGE.CODE, LANGUAGE.MAIN_LANGUAGE, LANGUAGE.LAST_SYNCHED)
            .values(l.getCode(), l.getMainLanguage(), l.getLastSynched())
            .onConflict(LANGUAGE.CODE)
            .doUpdate()
            .set(LANGUAGE.CODE, l.getCode())
            .set(LANGUAGE.MAIN_LANGUAGE, l.getMainLanguage())
            .set(LANGUAGE.LAST_SYNCHED, l.getLastSynched())
            .where(LANGUAGE.CODE.eq(l.getCode()))
            .execute();
    }

}
