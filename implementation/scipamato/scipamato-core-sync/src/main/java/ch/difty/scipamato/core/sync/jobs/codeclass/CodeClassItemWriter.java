package ch.difty.scipamato.core.sync.jobs.codeclass;

import static ch.difty.scipamato.public_.db.public_.tables.CodeClass.*;

import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;

import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter;

/**
 * {@link ItemWriter} implementation to synchronize code classes from scipamato-core to public.
 *
 * <ul>
 * <li> Takes care of inserts and updates. </li>
 * <li> Every record will be updated at least with the current timestamp in last_synched. </li>
 * <li> id columns and audit fields are those of the scipamato-core tables </li>
 * </ul>
 *
 * @author u.joss
 */
public class CodeClassItemWriter extends ScipamatoItemWriter<PublicCodeClass> {

    public CodeClassItemWriter(final DSLContext jooqDslContextPublic) {
        super(jooqDslContextPublic, "code_class");
    }

    @Override
    protected int executeUpdate(PublicCodeClass i) {
        return getDslContext()
            .insertInto(CODE_CLASS)
            .columns(CODE_CLASS.CODE_CLASS_ID, CODE_CLASS.LANG_CODE, CODE_CLASS.NAME, CODE_CLASS.DESCRIPTION, CODE_CLASS.VERSION, CODE_CLASS.CREATED, CODE_CLASS.LAST_MODIFIED, CODE_CLASS.LAST_SYNCHED)
            .values(i.getCodeClassId(), i.getLangCode(), i.getName(), i.getDescription(), i.getVersion(), i.getCreated(), i.getLastModified(), i.getLastSynched())
            .onConflict(CODE_CLASS.CODE_CLASS_ID, CODE_CLASS.LANG_CODE)
            .doUpdate()
            .set(CODE_CLASS.NAME, i.getName())
            .set(CODE_CLASS.DESCRIPTION, i.getDescription())
            .set(CODE_CLASS.VERSION, i.getVersion())
            .set(CODE_CLASS.CREATED, i.getCreated())
            .set(CODE_CLASS.LAST_MODIFIED, i.getLastModified())
            .set(CODE_CLASS.LAST_SYNCHED, i.getLastSynched())
            .where(CODE_CLASS.CODE_CLASS_ID.eq(i.getCodeClassId()).and(CODE_CLASS.LANG_CODE.eq(i.getLangCode())))
            .execute();
    }

}
