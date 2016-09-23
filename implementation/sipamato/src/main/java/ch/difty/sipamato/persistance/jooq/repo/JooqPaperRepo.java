package ch.difty.sipamato.persistance.jooq.repo;

import static ch.difty.sipamato.db.h2.tables.Paper.PAPER;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ch.difty.sipamato.db.h2.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.Asserts;
import ch.difty.sipamato.persistance.jooq.mapper.PaperRecordMapper;
import ch.difty.sipamato.persistance.repository.PaperRepository;

// TODO extend from abstract JooqRepo
@Repository
@Profile("DB_JOOQ")
@Transactional(readOnly = true)
public class JooqPaperRepo implements PaperRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JooqPaperRepo.class);

    private final DSLContext dsl;
    private final PaperRecordMapper mapper;
    private final InsertSetStepSetter<PaperRecord, Paper> insertSetStepSetter;
    private final UpdateSetStepSetter<PaperRecord, Paper> updateSetStepSetter;

    @Autowired
    public JooqPaperRepo(final DSLContext dsl, PaperRecordMapper mapper, InsertSetStepSetter<PaperRecord, Paper> insertSetStepSetter, UpdateSetStepSetter<PaperRecord, Paper> updateSetStepSetter) {
        Asserts.notNull(dsl, "dsl");
        Asserts.notNull(mapper, "mapper");
        Asserts.notNull(insertSetStepSetter, "insertSetStepSetter");
        Asserts.notNull(updateSetStepSetter, "updateSetStepSetter");
        this.dsl = dsl;
        this.mapper = mapper;
        this.insertSetStepSetter = insertSetStepSetter;
        this.updateSetStepSetter = updateSetStepSetter;
    }

    /** protected for test purposes */
    protected DSLContext getDslContext() {
        return dsl;
    }

    /** protected for test purposes */
    protected PaperRecordMapper getMapper() {
        return mapper;
    }

    /** protected for test purposes */
    protected InsertSetStepSetter<PaperRecord, Paper> getInsertSetStepSetter() {
        return insertSetStepSetter;
    }

    /** protected for test purposes */
    protected UpdateSetStepSetter<PaperRecord, Paper> getUpdateSetStepSetter() {
        return updateSetStepSetter;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public Paper add(final Paper paper) {
        Asserts.notNull(paper, "paper");

        InsertSetMoreStep<PaperRecord> step = insertSetStepSetter.setNonKeyFieldsFor(dsl.insertInto(PAPER), paper);
        insertSetStepSetter.considerSettingKeyOf(step, paper);

        PaperRecord saved = step.returning().fetchOne();
        if (saved != null) {
            LOGGER.info("Inserted 1 record: {} with id {}.", PAPER.getName(), saved.getId());
            return mapper.map(saved);
        } else {
            LOGGER.warn("Unable to insert {} record", PAPER.getName());
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public Paper delete(final Integer id) {
        Asserts.notNull(id, "id");

        final Paper toBeDeleted = findById(id);
        if (toBeDeleted != null) {
            final int deleteCount = dsl.delete(PAPER).where(PAPER.ID.equal(id.longValue())).execute();
            if (deleteCount > 0) {
                LOGGER.info("Deleted {} record: {} with id {}.", deleteCount, PAPER.getName(), id);
            } else {
                LOGGER.error("Unable to delete {} with id {}", PAPER.getName(), id);
            }
        }
        return toBeDeleted;
    }

    /** {@inheritDoc} */
    @Override
    public List<Paper> findAll() {
        return dsl.selectFrom(PAPER).fetchInto(Paper.class);
    }

    @Override
    public Paper findById(final Integer id) {
        Asserts.notNull(id);
        return dsl.selectFrom(PAPER).where(PAPER.ID.equal(id.longValue())).fetchOneInto(Paper.class);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = false)
    public Paper update(final Paper e) {
        Asserts.notNull(e, "entity");
        Integer id = e.getId();
        Asserts.notNull(id, "entity.id");

        PaperRecord updated = updateSetStepSetter.setFieldsFor(dsl.update(PAPER), e).where(PAPER.ID.equal(id.longValue())).returning().fetchOne();
        if (updated != null) {
            LOGGER.info("Updated 1 record: {} with id {}.", PAPER.getName(), id);
            return mapper.map(updated);
        } else {
            LOGGER.warn("Unable to update {} record with id {}.", PAPER.getName(), id);
            return null;
        }
    }

}
