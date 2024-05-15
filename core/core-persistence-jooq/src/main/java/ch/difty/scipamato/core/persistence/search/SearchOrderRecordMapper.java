package ch.difty.scipamato.core.persistence.search;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.core.db.tables.records.SearchOrderRecord;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.AuditFields;
import ch.difty.scipamato.core.persistence.EntityRecordMapper;

/**
 * Mapper mapping {@link SearchOrderRecord}s into entity {@link SearchOrder}.
 *
 *
 * <b>Note:</b> the mapper leaves the nested list of {@link SearchCondition}s
 * empty.
 *
 * @author u.joss
 */
@Component
public class SearchOrderRecordMapper extends EntityRecordMapper<SearchOrderRecord, SearchOrder> {

    @NotNull
    @Override
    protected SearchOrder makeEntity() {
        return new SearchOrder();
    }

    @NotNull
    @Override
    protected AuditFields getAuditFieldsOf(@NotNull final SearchOrderRecord r) {
        return new AuditFields(r.getCreated(), r.getCreatedBy(), r.getLastModified(), r.getLastModifiedBy(), r.getVersion());
    }

    @Override
    protected void mapFields(@NotNull final SearchOrderRecord from, @NotNull final SearchOrder to) {
        to.setId(from.getId());
        to.setName(from.getName());
        to.setOwner(from.getOwner());
        to.setGlobal(from.getGlobal());
        // showExcluded is not persisted and therefore always false after mapping
    }
}
