package ch.difty.scipamato.persistance.jooq.search;

import static ch.difty.scipamato.db.tables.SearchOrder.*;

import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.db.tables.records.SearchOrderRecord;
import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.entity.filter.SearchCondition;
import ch.difty.scipamato.persistance.jooq.InsertSetStepSetter;

/**
 * The insert step setter used for inserting new {@link SearchOrder}s.<p>
 *
 * <b>Note:</b> the {@link SearchCondition}s are not inserted here.
 *
 * @author u.joss
 */
@Component
public class SearchOrderInsertSetStepSetter implements InsertSetStepSetter<SearchOrderRecord, SearchOrder> {

    /** {@inheritDoc} */
    @Override
    public InsertSetMoreStep<SearchOrderRecord> setNonKeyFieldsFor(InsertSetStep<SearchOrderRecord> step, SearchOrder e) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(e, "entity");

        // @formatter:off
        return step
            .set(SEARCH_ORDER.NAME, e.getName())
            .set(SEARCH_ORDER.OWNER, e.getOwner())
            .set(SEARCH_ORDER.GLOBAL, e.isGlobal())

            .set(SEARCH_ORDER.CREATED_BY, e.getCreatedBy())
            .set(SEARCH_ORDER.LAST_MODIFIED_BY, e.getLastModifiedBy());
        // @formatter:on
    }

    /** {@inheritDoc} */
    @Override
    public void considerSettingKeyOf(InsertSetMoreStep<SearchOrderRecord> step, SearchOrder entity) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(entity, "entity");
        Long id = entity.getId();
        if (id != null)
            step.set(SEARCH_ORDER.ID, id);
    }

    /** {@inheritDoc} */
    @Override
    public void resetIdToEntity(SearchOrder entity, SearchOrderRecord saved) {
        if (saved != null) {
            entity.setId(saved.getId());
        }
    }
}
