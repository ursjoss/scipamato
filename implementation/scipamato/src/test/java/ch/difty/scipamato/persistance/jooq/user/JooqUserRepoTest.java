package ch.difty.scipamato.persistance.jooq.user;

import static ch.difty.scipamato.db.tables.ScipamatoUser.SCIPAMATO_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.entity.User;
import ch.difty.scipamato.lib.NullArgumentException;
import ch.difty.scipamato.persistance.jooq.EntityRepository;
import ch.difty.scipamato.persistance.jooq.JooqEntityRepoTest;

public class JooqUserRepoTest extends JooqEntityRepoTest<ScipamatoUserRecord, User, Integer, ch.difty.scipamato.db.tables.ScipamatoUser, UserRecordMapper, UserFilter> {

    private static final Integer SAMPLE_ID = 3;

    private JooqUserRepo repo;

    @Mock
    private UserRoleRepository userRoleRepoMock;

    @Override
    protected Integer getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected JooqUserRepo getRepo() {
        if (repo == null) {
            repo = new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                    getApplicationProperties(), userRoleRepoMock);
        }
        return repo;
    }

    @Override
    protected EntityRepository<User, Integer, UserFilter> makeRepoFindingEntityById(User user) {
        return new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
                getApplicationProperties(), userRoleRepoMock) {
            private static final long serialVersionUID = 1L;

            @Override
            public User findById(Integer id, int version) {
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
    private ScipamatoUserRecord persistedRecord;

    @Override
    protected ScipamatoUserRecord getPersistedRecord() {
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
    protected Class<ScipamatoUserRecord> getRecordClass() {
        return ScipamatoUserRecord.class;
    }

    @Override
    protected ch.difty.scipamato.db.tables.ScipamatoUser getTable() {
        return SCIPAMATO_USER;
    }

    @Override
    protected TableField<ScipamatoUserRecord, Integer> getTableId() {
        return SCIPAMATO_USER.ID;
    }

    @Override
    protected TableField<ScipamatoUserRecord, Integer> getRecordVersion() {
        return SCIPAMATO_USER.VERSION;
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
            new JooqUserRepo(null, getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(),
                    userRoleRepoMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dsl must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), null, getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(),
                    userRoleRepoMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("mapper must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), getMapper(), null, getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(),
                    userRoleRepoMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("sortMapper must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), null, getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(), userRoleRepoMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("filterConditionMapper must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), null, getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(),
                    userRoleRepoMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("dateTimeService must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), null, getUpdateSetStepSetter(), getApplicationProperties(), userRoleRepoMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("insertSetStepSetter must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), null, getApplicationProperties(), userRoleRepoMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("updateSetStepSetter must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), null, userRoleRepoMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("applicationProperties must not be null.");
        }
        try {
            new JooqUserRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties(),
                    null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("userRoleRepo must not be null.");
        }
    }

    @Test
    @Override
    public void updating_withLanguageCodeNull_throws() {
        try {
            getRepo().update(getPersistedEntity(), null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("languageCode must not be null.");
            verify(getPersistedEntity(), times(1)).getId();
        }
    }

    @Test
    @Override
    public void updating_withEntityIdNull_throws() {
        expectUnpersistedEntityIdNull();
        try {
            repo.update(getUnpersistedEntity(), "de");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("entity.id must not be null.");
        }
        verify(getUnpersistedEntity(), times(2)).getId();
    }
}
