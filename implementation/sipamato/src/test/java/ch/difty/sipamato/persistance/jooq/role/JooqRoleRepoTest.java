package ch.difty.sipamato.persistance.jooq.role;

import static ch.difty.sipamato.db.tables.Role.ROLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.RoleRecord;
import ch.difty.sipamato.entity.Role;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.EntityRepository;
import ch.difty.sipamato.persistance.jooq.JooqEntityRepoTest;

public class JooqRoleRepoTest extends JooqEntityRepoTest<RoleRecord, Role, Integer, ch.difty.sipamato.db.tables.Role, RoleRecordMapper, RoleFilter> {

    private static final Integer SAMPLE_ID = 3;

    private JooqRoleRepo repo;

    @Override
    protected Integer getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected JooqRoleRepo getRepo() {
        if (repo == null) {
            repo = new JooqRoleRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getJooqConfig());
        }
        return repo;
    }

    @Override
    protected EntityRepository<RoleRecord, Role, Integer, RoleRecordMapper, RoleFilter> makeRepoFindingEntityById(Role paper) {
        return new JooqRoleRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                getJooqConfig()) {
            private static final long serialVersionUID = 1L;

            @Override
            public Role findById(Integer id) {
                return paper;
            }
        };
    }

    @Mock
    private Role unpersistedEntity, persistedEntity;

    @Override
    protected Role getUnpersistedEntity() {
        return unpersistedEntity;
    }

    @Override
    protected Role getPersistedEntity() {
        return persistedEntity;
    }

    @Mock
    private RoleRecord persistedRecord;

    @Override
    protected RoleRecord getPersistedRecord() {
        return persistedRecord;
    }

    @Mock
    private RoleRecordMapper mapperMock;

    @Override
    protected RoleRecordMapper getMapper() {
        return mapperMock;
    }

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    @Override
    protected Class<RoleRecord> getRecordClass() {
        return RoleRecord.class;
    }

    @Override
    protected ch.difty.sipamato.db.tables.Role getTable() {
        return ROLE;
    }

    @Override
    protected TableField<RoleRecord, Integer> getTableId() {
        return ROLE.ID;
    }

    @Mock
    private RoleFilter filterMock;

    @Override
    protected RoleFilter getFilter() {
        return filterMock;
    }

    @Override
    protected void expectEntityIdsWithValues() {
        when(unpersistedEntity.getId()).thenReturn(SAMPLE_ID);
        when(persistedRecord.getId()).thenReturn(SAMPLE_ID);
    }

    @Override
    protected void expectUnpersistedEntityIdNull() {
        when(unpersistedEntity.getId()).thenReturn(null);
    }

    @Override
    protected void verifyUnpersistedEntityId() {
        verify(getUnpersistedEntity()).getId();
    }

    @Override
    protected void verifyPersistedRecordId() {
        verify(persistedRecord).getId();
    }

    @Test
    public void degenerateConstruction() {
        try {
            new JooqRoleRepo(null, getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dsl must not be null.");
        }
        try {
            new JooqRoleRepo(getDsl(), null, getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("mapper must not be null.");
        }
        try {
            new JooqRoleRepo(getDsl(), getMapper(), null, getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("sortMapper must not be null.");
        }
        try {
            new JooqRoleRepo(getDsl(), getMapper(), getSortMapper(), null, getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("filterConditionMapper must not be null.");
        }
        try {
            new JooqRoleRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), null, getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dateTimeService must not be null.");
        }
        try {
            new JooqRoleRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), null, getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("localization must not be null.");
        }
        try {
            new JooqRoleRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), null, getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("insertSetStepSetter must not be null.");
        }
        try {
            new JooqRoleRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), null, getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("updateSetStepSetter must not be null.");
        }
        try {
            new JooqRoleRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("jooqConfig must not be null.");
        }
    }

}
