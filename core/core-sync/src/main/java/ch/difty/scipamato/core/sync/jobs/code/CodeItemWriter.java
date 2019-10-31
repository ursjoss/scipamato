package ch.difty.scipamato.core.sync.jobs.code;

import static ch.difty.scipamato.publ.db.tables.Code.CODE;

import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;

import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter;

/**
 * {@link ItemWriter} implementation to synchronize codes from scipamato-core to
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
public class CodeItemWriter extends ScipamatoItemWriter<PublicCode> {

    public CodeItemWriter(@NotNull final DSLContext jooqDslContextPublic) {
        super(jooqDslContextPublic, "code");
    }

    @Override
    protected int executeUpdate(@NotNull final PublicCode c) {
        return getDslContext()
            .insertInto(CODE)
            .columns(CODE.CODE_, CODE.LANG_CODE, CODE.CODE_CLASS_ID, CODE.NAME, CODE.COMMENT, CODE.SORT, CODE.VERSION,
                CODE.CREATED, CODE.LAST_MODIFIED, CODE.LAST_SYNCHED)
            .values(c.getCode(), c.getLangCode(), c.getCodeClassId(), c.getName(), c.getComment(), c.getSort(),
                c.getVersion(), c.getCreated(), c.getLastModified(), c.getLastSynched())
            .onConflict(CODE.CODE_, CODE.LANG_CODE)
            .doUpdate()
            .set(CODE.CODE_CLASS_ID, c.getCodeClassId())
            .set(CODE.NAME, c.getName())
            .set(CODE.COMMENT, c.getComment())
            .set(CODE.SORT, c.getSort())
            .set(CODE.VERSION, c.getVersion())
            .set(CODE.CREATED, c.getCreated())
            .set(CODE.LAST_MODIFIED, c.getLastModified())
            .set(CODE.LAST_SYNCHED, c.getLastSynched())
            .where(CODE.CODE_
                .eq(c.getCode())
                .and(CODE.LANG_CODE.eq(c.getLangCode())))
            .execute();
    }
}
