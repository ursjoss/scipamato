package ch.difty.scipamato.core.persistence.keyword;

import static ch.difty.scipamato.core.db.tables.Keyword.KEYWORD;
import static ch.difty.scipamato.core.db.tables.KeywordTr.KEYWORD_TR;
import static ch.difty.scipamato.core.db.tables.Language.LANGUAGE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.db.tables.records.KeywordRecord;
import ch.difty.scipamato.core.db.tables.records.KeywordTrRecord;
import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation;
import ch.difty.scipamato.core.persistence.AbstractRepo;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

@Repository
@Slf4j
@Profile("!wickettest")
public class JooqKeywordRepo extends AbstractRepo implements KeywordRepository {

    public JooqKeywordRepo(@Qualifier("dslContext") @NotNull final DSLContext dslContext,
        @NotNull DateTimeService dateTimeService) {
        super(dslContext, dateTimeService);
    }

    @NotNull
    @Override
    public List<Keyword> findAll(@NotNull final String languageCode) {
        final String lang = TranslationUtils.INSTANCE.trimLanguageCode(languageCode);
        // skipping the audit fields
        return getDsl()
            .select(KEYWORD.ID.as("K_ID"), DSL
                .coalesce(KEYWORD_TR.NAME, TranslationUtils.NOT_TRANSL)
                .as("K_NAME"), KEYWORD.SEARCH_OVERRIDE)
            .from(KEYWORD)
            .leftOuterJoin(KEYWORD_TR)
            .on(KEYWORD.ID
                .equal(KEYWORD_TR.KEYWORD_ID)
                .and(KEYWORD_TR.LANG_CODE.equal(lang)))
            .orderBy(KEYWORD_TR.NAME)
            .fetchInto(Keyword.class);
    }

    @NotNull
    @Override
    public List<KeywordDefinition> findPageOfKeywordDefinitions(@Nullable final KeywordFilter filter,
        @NotNull final PaginationContext pc) {
        final SelectOnConditionStep<Record> selectStep = getDsl()
            .select(KEYWORD.fields())
            .select(LANGUAGE.CODE)
            .select(KEYWORD_TR.fields())
            .from(KEYWORD)
            .crossJoin(LANGUAGE)
            .leftOuterJoin(KEYWORD_TR)
            .on(KEYWORD.ID.eq(KEYWORD_TR.KEYWORD_ID))
            .and(LANGUAGE.CODE.eq(KEYWORD_TR.LANG_CODE));
        final SelectConditionStep<Record> selectConditionStep = applyWhereCondition(filter, selectStep);
        // the subsequent grouping requires ordering by id then language_code
        final Map<Integer, Result<Record>> rawRecords = selectConditionStep
            .orderBy(KEYWORD.ID, LANGUAGE.CODE, KEYWORD_TR.ID)
            .fetchGroups(KEYWORD.ID);
        final List<KeywordDefinition> results = mapRawRecordsIntoKeywordDefinitions(rawRecords);
        considerSorting(pc, results);
        // need to page after sorting due to grouping, not profiting from DB filtering :-(
        return results
            .stream()
            .skip(pc.getOffset())
            .limit(pc.getPageSize())
            .collect(toList());
    }

    private <R extends Record> SelectConditionStep<R> applyWhereCondition(final KeywordFilter filter,
        final SelectJoinStep<R> selectStep) {
        if (filter != null && filter.getNameMask() != null) {
            final String nameMask = filter.getNameMask();
            if ("n.a.".equals(nameMask)) {
                return findEntriesWithNullNames(selectStep);
            } else {
                return findNameMaskInSearchOverrideOrTranslatedNames(selectStep, nameMask);
            }
        } else {
            return selectStep.where(DSL.noCondition());
        }
    }

    private <R extends Record> SelectConditionStep<R> findEntriesWithNullNames(final SelectJoinStep<R> selectStep) {
        return selectStep.where(KEYWORD.ID.in(DSL
            .selectDistinct(KEYWORD.ID)
            .from(KEYWORD)
            .crossJoin(LANGUAGE)
            .leftOuterJoin(KEYWORD_TR)
            .on(KEYWORD.ID.eq(KEYWORD_TR.KEYWORD_ID))
            .and(LANGUAGE.CODE.eq(KEYWORD_TR.LANG_CODE))
            .where(KEYWORD_TR.NAME.isNull())));
    }

    private <R extends Record> SelectConditionStep<R> findNameMaskInSearchOverrideOrTranslatedNames(
        final SelectJoinStep<R> selectStep, final String nameMask) {
        final String mask = '%' + nameMask + '%';
        return selectStep.where(KEYWORD.SEARCH_OVERRIDE
            .likeIgnoreCase(mask)
            .or(KEYWORD.ID.in(DSL
                .selectDistinct(KEYWORD_TR.KEYWORD_ID)
                .from(KEYWORD_TR)
                .where(KEYWORD_TR.NAME.likeIgnoreCase(mask)))));
    }

    private List<KeywordDefinition> mapRawRecordsIntoKeywordDefinitions(final Map<Integer, Result<Record>> rawRecords) {
        final List<KeywordDefinition> definitions = new ArrayList<>();
        for (final Map.Entry<Integer, Result<Record>> entry : rawRecords.entrySet()) {
            final List<KeywordTranslation> translations = entry
                .getValue()
                .stream()
                .map(r -> new KeywordTranslation(r.getValue(KEYWORD_TR.ID), r.getValue(LANGUAGE.CODE),
                    r.getValue(KEYWORD_TR.NAME), r.getValue(KEYWORD_TR.VERSION)))
                .collect(toList());
            final Record r = entry
                .getValue()
                .stream()
                .findFirst()
                .orElseThrow();
            definitions.add(
                toKeywordDefinition(entry.getKey(), r.getValue(KEYWORD.SEARCH_OVERRIDE), r.getValue(KEYWORD.VERSION),
                    translations));
        }
        return definitions;
    }

    @NotNull
    @Override
    public String getMainLanguage() {
        final String lang = getDsl()
            .select(LANGUAGE.CODE)
            .from(LANGUAGE)
            .where(LANGUAGE.MAIN_LANGUAGE.eq(true))
            .fetchOneInto(String.class);
        return lang != null ? lang : "en";
    }

    @NotNull
    @Override
    public KeywordDefinition newUnpersistedKeywordDefinition() {
        final List<KeywordTranslation> translations = getDsl()
            .select(LANGUAGE.CODE)
            .from(LANGUAGE)
            .fetchInto(String.class)
            .stream()
            .map(lc -> new KeywordTranslation(null, lc, null, 0))
            .collect(toList());
        return toKeywordDefinition(null, null, 0, translations);
    }

    /**
     * Currently only accepting name as sort. It's a bit hacky...
     */
    private void considerSorting(final PaginationContext pc, final List<KeywordDefinition> results) {
        for (final Sort.SortProperty sortProperty : pc.getSort()) {
            final String propName = sortProperty.getName();
            if (propName.equals(KEYWORD_TR.NAME.getName())) {
                if (sortProperty.getDirection() == Sort.Direction.DESC)
                    results.sort(comparing(KeywordDefinition::getName, String.CASE_INSENSITIVE_ORDER).reversed());
                else
                    results.sort(comparing(KeywordDefinition::getName, String.CASE_INSENSITIVE_ORDER));
            }
        }
    }

    @Override
    public int countByFilter(@Nullable final KeywordFilter filter) {
        final SelectJoinStep<Record1<Integer>> selectStep = getDsl()
            .selectCount()
            .from(KEYWORD);
        return applyWhereCondition(filter, selectStep).fetchOneInto(Integer.class);
    }

    @Nullable
    @Override
    public KeywordDefinition findKeywordDefinitionById(final int id) {
        final Map<Integer, Result<Record>> records = getDsl()
            .select(KEYWORD.fields())
            .select(LANGUAGE.CODE)
            .select(KEYWORD_TR.fields())
            .from(KEYWORD)
            .crossJoin(LANGUAGE)
            .leftOuterJoin(KEYWORD_TR)
            .on(KEYWORD.ID.eq(KEYWORD_TR.KEYWORD_ID))
            .and(LANGUAGE.CODE.eq(KEYWORD_TR.LANG_CODE))
            .where(KEYWORD.ID.eq(id))
            .orderBy(KEYWORD_TR.ID)
            .fetchGroups(KEYWORD.ID);
        if (!records.isEmpty())
            return mapRawRecordsIntoKeywordDefinitions(records).get(0);
        else
            return null;
    }

    @Nullable
    @Override
    public KeywordDefinition insert(@NotNull final KeywordDefinition entity) {
        if (entity.getId() != null) {
            throw new IllegalArgumentException("id must be null.");
        }

        final int userId = getUserId();

        final KeywordRecord ntRecord = getDsl()
            .insertInto(KEYWORD)
            .set(KEYWORD.SEARCH_OVERRIDE, entity.getSearchOverride())
            .set(KEYWORD.CREATED_BY, userId)
            .set(KEYWORD.LAST_MODIFIED_BY, userId)
            .returning()
            .fetchOne();
        final int ntId = ntRecord.get(KEYWORD.ID);
        final String searchOverride = ntRecord.get(KEYWORD.SEARCH_OVERRIDE);

        final List<KeywordTranslation> persistedTranslations = persistTranslations(entity, userId, ntId);
        final KeywordDefinition persistedEntity = toKeywordDefinition(ntId, searchOverride,
            ntRecord.get(KEYWORD.VERSION), persistedTranslations);
        log.info("{} inserted 1 record: {} with id {}.", getActiveUser().getUserName(), KEYWORD.getName(), ntId);
        return persistedEntity;
    }

    private List<KeywordTranslation> persistTranslations(final KeywordDefinition entity, final int userId,
        final int ntId) {
        final List<KeywordTranslation> kts = new ArrayList<>();
        for (final KeywordTranslation kt : entity.getTranslations(null)) {
            final KeywordTrRecord ktRecord = insertAndGetKeywordTr(ntId, userId, kt);
            if (ktRecord != null)
                kts.add(toKeywordTranslation(ktRecord));
        }
        return kts;
    }

    @NotNull
    @Override
    public KeywordDefinition update(@NotNull final KeywordDefinition entity) {
        Objects.requireNonNull(entity.getId());

        final int userId = getUserId();
        final List<KeywordTranslation> persistedTranslations = updateOrInsertAndLoadKeywordTranslations(entity, userId);
        final KeywordRecord record = updateAndLoadKeywordDefinition(entity, userId, entity.getVersion());
        return manageTranslations(record, entity, persistedTranslations);
    }

    private List<KeywordTranslation> updateOrInsertAndLoadKeywordTranslations(final KeywordDefinition entity,
        final int userId) {
        final Collection<KeywordTranslation> entityTranslations = entity.getTranslations(null);
        removeObsoletePersistedRecords(entity.getId(), entityTranslations);
        return addOrUpdate(entity, entityTranslations, userId);
    }

    private void removeObsoletePersistedRecords(final Integer id,
        final Collection<KeywordTranslation> entityTranslations) {
        final Result<KeywordTrRecord> persistedTranslations = loadTranslationsFromDbFor(id);
        removeObsoletePersistedRecordsFor(persistedTranslations, entityTranslations);
    }

    private Result<KeywordTrRecord> loadTranslationsFromDbFor(final Integer id) {
        return getDsl()
            .selectFrom(KEYWORD_TR)
            .where(KEYWORD_TR.KEYWORD_ID.eq(id))
            .fetch();
    }

    // package-private for testing purposes
    void removeObsoletePersistedRecordsFor(final Result<KeywordTrRecord> persistedTranslations,
        final Collection<KeywordTranslation> entityTranslations) {
        for (final KeywordTrRecord ktr : persistedTranslations)
            if (!isPresentIn(entityTranslations, ktr))
                ktr.delete();
    }

    // package-private for testing purposes
    @NotNull
    KeywordDefinition manageTranslations(@Nullable final KeywordRecord record, @NotNull final KeywordDefinition entity,
        final List<KeywordTranslation> persistedTranslations) {
        if (record != null) {
            final KeywordDefinition updatedEntity = toKeywordDefinition(entity.getId(),
                record.get(KEYWORD.SEARCH_OVERRIDE), record.get(KEYWORD.VERSION), persistedTranslations);
            log.info("{} updated 1 record: {} with id {}.", getActiveUser().getUserName(), KEYWORD.getName(),
                updatedEntity.getId());
            return updatedEntity;
        } else {
            throw new OptimisticLockingException(KEYWORD.getName(), entity.toString(),
                OptimisticLockingException.Type.UPDATE);
        }
    }

    private KeywordDefinition toKeywordDefinition(final Integer id, final String searchOverride, final int version,
        final List<KeywordTranslation> persistedTranslations) {
        return new KeywordDefinition(id, getMainLanguage(), searchOverride, version,
            persistedTranslations.toArray(new KeywordTranslation[0]));
    }

    private KeywordRecord updateAndLoadKeywordDefinition(final KeywordDefinition entity, final int userId,
        final int currentVersion) {
        return getDsl()
            .update(KEYWORD)
            .set(KEYWORD.SEARCH_OVERRIDE, entity.getSearchOverride())
            .set(KEYWORD.VERSION, currentVersion + 1)
            .set(KEYWORD.LAST_MODIFIED_BY, userId)
            .set(KEYWORD.LAST_MODIFIED, getTs())
            .where(KEYWORD.ID.eq(entity.getId()))
            .and(KEYWORD.VERSION.eq(currentVersion))
            .returning()
            .fetchOne();
    }

    private List<KeywordTranslation> addOrUpdate(final KeywordDefinition entity,
        final Collection<KeywordTranslation> entityTranslations, final int userId) {
        final List<KeywordTranslation> ktPersisted = new ArrayList<>();
        for (final KeywordTranslation kt : entityTranslations)
            addOrUpdateTranslation(kt, entity, userId, ktPersisted);
        return ktPersisted;
    }

    // package-private for test purposes
    void addOrUpdateTranslation(@NotNull final KeywordTranslation kt, @NotNull final KeywordDefinition entity,
        final int userId, @NotNull final List<KeywordTranslation> ktPersisted) {
        if (kt.getId() != null) {
            final int currentVersion = kt.getVersion();
            final KeywordTrRecord ktRecord = updateKeywordTr(entity, kt, userId, currentVersion);
            addOrThrow(ktRecord, kt.toString(), ktPersisted);
        } else {
            final KeywordTrRecord ktRecord = insertAndGetKeywordTr(entity.getId(), userId, kt);
            if (ktRecord != null)
                ktPersisted.add(toKeywordTranslation(ktRecord));
        }
    }

    // package-private for testing purposes
    void addOrThrow(@Nullable final KeywordTrRecord ktRecord, @NotNull final String translationAsString,
        final List<KeywordTranslation> ktPersisted) {
        if (ktRecord != null) {
            ktPersisted.add(toKeywordTranslation(ktRecord));
        } else {
            throw new OptimisticLockingException(KEYWORD_TR.getName(), translationAsString,
                OptimisticLockingException.Type.UPDATE);
        }
    }

    private boolean isPresentIn(final Collection<KeywordTranslation> translations, final KeywordTrRecord ktr) {
        for (final KeywordTranslation kt : translations) {
            final Integer id = kt.getId();
            if (id != null && id.equals(ktr.get(KEYWORD_TR.ID)))
                return true;
        }
        return false;
    }

    // package-private for stubbing purposes
    @Nullable
    KeywordTrRecord insertAndGetKeywordTr(final int keywordId, final int userId, @NotNull final KeywordTranslation kt) {
        return getDsl()
            .insertInto(KEYWORD_TR)
            .set(KEYWORD_TR.KEYWORD_ID, keywordId)
            .set(KEYWORD_TR.LANG_CODE, kt.getLangCode())
            .set(KEYWORD_TR.NAME, kt.getName())
            .set(KEYWORD_TR.CREATED_BY, userId)
            .set(KEYWORD_TR.LAST_MODIFIED_BY, userId)
            .returning()
            .fetchOne();
    }

    private KeywordTrRecord updateKeywordTr(final KeywordDefinition entity, final KeywordTranslation kt,
        final int userId, final int currentNttVersion) {
        final int keywordId = entity.getId();
        return getDsl()
            .update(KEYWORD_TR)
            .set(KEYWORD_TR.KEYWORD_ID, keywordId)
            .set(KEYWORD_TR.LANG_CODE, kt.getLangCode())
            .set(KEYWORD_TR.NAME, kt.getName())
            .set(KEYWORD_TR.LAST_MODIFIED_BY, userId)
            .set(KEYWORD_TR.LAST_MODIFIED, getTs())
            .set(KEYWORD_TR.VERSION, currentNttVersion + 1)
            .where(KEYWORD_TR.ID.eq(kt.getId()))
            .and(KEYWORD_TR.VERSION.eq(currentNttVersion))
            .returning()
            .fetchOne();
    }

    private KeywordTranslation toKeywordTranslation(final KeywordTrRecord record) {
        return new KeywordTranslation(record.get(KEYWORD_TR.ID), record.get(KEYWORD_TR.LANG_CODE),
            record.get(KEYWORD_TR.NAME), record.get(KEYWORD_TR.VERSION));
    }

    @Nullable
    @Override
    public KeywordDefinition delete(@NotNull final Integer id, final int version) {
        final KeywordDefinition toBeDeleted = findKeywordDefinitionById(id);
        if (toBeDeleted != null) {
            if (toBeDeleted.getVersion() == version) {
                final int deleteCount = getDsl()
                    .delete(KEYWORD)
                    .where(KEYWORD.ID.equal(id))
                    .and(KEYWORD.VERSION.eq(version))
                    .execute();
                logOrThrow(deleteCount, id, toBeDeleted);
            } else {
                throw new OptimisticLockingException(KEYWORD.getName(), OptimisticLockingException.Type.DELETE);
            }
        }
        return toBeDeleted;
    }

    // package-private for testing purposes
    void logOrThrow(final int deleteCount, final Integer id, final KeywordDefinition toBeDeleted) {
        if (deleteCount > 0) {
            log.info("{} deleted {} record: {} with id {}.", getActiveUser().getUserName(), deleteCount,
                KEYWORD.getName(), id);
        } else {
            throw new OptimisticLockingException(KEYWORD.getName(), toBeDeleted.toString(),
                OptimisticLockingException.Type.DELETE);
        }
    }
}
