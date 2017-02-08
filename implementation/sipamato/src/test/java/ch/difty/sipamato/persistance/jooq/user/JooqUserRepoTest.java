package ch.difty.sipamato.persistance.jooq.user;

import static ch.difty.sipamato.db.tables.User.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.UserRecord;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.persistance.jooq.EntityRepository;
import ch.difty.sipamato.persistance.jooq.JooqEntityRepoTest;

public class JooqUserRepoTest extends JooqEntityRepoTest<UserRecord, User, Integer, ch.difty.sipamato.db.tables.User, UserRecordMapper, UserFilter> {

    private static final Integer SAMPLE_ID = 3;

    private JooqUserRepo repo;

    @Override
    protected Integer getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected JooqUserRepo getRepo() {
        if (repo == null) {
            repo = new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getJooqConfig());
        }
        return repo;
    }

    @Override
    protected EntityRepository<User, Integer, UserFilter> makeRepoFindingEntityById(User user) {
        return new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                getJooqConfig()) {
            private static final long serialVersionUID = 1L;

            @Override
            public User findById(Integer id) {
                return user;
            }
        };
    }

    @Mock
    private User unpersistedEntity, persistedEntity;

    @Override
    protected User getUnpersistedEntity() {
        return unpersistedEntity;
    }

    @Override
    protected User getPersistedEntity() {
        return persistedEntity;
    }

    @Mock
    private UserRecord persistedRecord;

    @Override
    protected UserRecord getPersistedRecord() {
        return persistedRecord;
    }

    @Mock
    private UserRecordMapper mapperMock;

    @Override
    protected UserRecordMapper getMapper() {
        return mapperMock;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected Class<UserRecord> getRecordClass() {
        return UserRecord.class;
    }

    @Override
    protected ch.difty.sipamato.db.tables.User getTable() {
        return USER;
    }

    @Override
    protected TableField<UserRecord, Integer> getTableId() {
        return USER.ID;
    }

    @Mock
    private UserFilter filterMock;

    @Override
    protected UserFilter getFilter() {
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
            new JooqUserRepo(null, getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dsl must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), null, getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("mapper must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), getMapper(), null, getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("sortMapper must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), null, getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("filterConditionMapper must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), null, getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dateTimeService must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), null, getInsertSetStepSetter(), getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("localization must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), null, getUpdateSetStepSetter(), getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("insertSetStepSetter must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), null, getJooqConfig());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("updateSetStepSetter must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getLocalization(), getInsertSetStepSetter(), getUpdateSetStepSetter(), null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("jooqConfig must not be null.");
        }
    }

}
