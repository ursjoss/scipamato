package ch.difty.sipamato.persistance.jooq.search;

import static ch.difty.sipamato.db.tables.SearchOrder.SEARCH_ORDER;

import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.db.tables.records.SearchOrderRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetter;

/**
 * The insert step setter used for inserting new {@link SearchOrder}s.<p/>
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
            .set(SEARCH_ORDER.GLOBAL, e.isGlobal());
        // @formatter:on
    }

    /** {@inheritDoc} */
    @Override
    public void considerSettingKeyOf(InsertSetMoreStep<SearchOrderRecord> step, SearchOrder entity) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(entity, "entity");
        Long id = entity.getId();
        if (id != null)
            step.set(SEARCH_ORDER.ID, id.longValue());
    }

    /** {@inheritDoc} */
    @Override
    public void resetIdToEntity(SearchOrder entity, SearchOrderRecord saved) {
        if (saved != null) {
            entity.setId(saved.getId());
        }
    }
}
