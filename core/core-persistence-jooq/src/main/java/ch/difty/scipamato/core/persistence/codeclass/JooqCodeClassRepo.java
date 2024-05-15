package ch.difty.scipamato.core.persistence.codeclass;

import static ch.difty.scipamato.common.persistence.TranslationUtilsKt.NOT_TRANSL;
import static ch.difty.scipamato.common.persistence.TranslationUtilsKt.trimLanguageCode;
import static ch.difty.scipamato.core.db.tables.CodeClass.CODE_CLASS;
import static ch.difty.scipamato.core.db.tables.CodeClassTr.CODE_CLASS_TR;
import static ch.difty.scipamato.core.db.tables.Language.LANGUAGE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.db.tables.records.CodeClassRecord;
import ch.difty.scipamato.core.db.tables.records.CodeClassTrRecord;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition;
import ch.difty.scipamato.core.entity.codeclass.CodeClassFilter;
import ch.difty.scipamato.core.entity.codeclass.CodeClassTranslation;
import ch.difty.scipamato.core.persistence.AbstractRepo;
import ch.difty.scipamato.core.persistence.ConditionalSupplier;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

@Repository
@CacheConfig(cacheNames = "codeClasses")
@Slf4j
@Profile("!wickettest")
public class JooqCodeClassRepo extends AbstractRepo implements CodeClassRepository {

    public JooqCodeClassRepo(@Qualifier("dslContext") @NotNull final DSLContext dslContext, @NotNull final DateTimeService dateTimeService) {
        super(dslContext, dateTimeService);
    }

    @NotNull
    @Override
    @Cacheable
    public List<CodeClass> find(@NotNull final String languageCode) {
        final String lang = trimLanguageCode(languageCode);
        // skipping the audit fields
        return getDsl()
            .select(CODE_CLASS.ID.as("CC_ID"), DSL
                .coalesce(CODE_CLASS_TR.NAME, NOT_TRANSL)
                .as("CC_NAME"), DSL
                .coalesce(CODE_CLASS_TR.DESCRIPTION, NOT_TRANSL)
                .as("CC_DESCRIPTION"))
            .from(CODE_CLASS)
            .leftOuterJoin(CODE_CLASS_TR)
            .on(CODE_CLASS.ID
                .equal(CODE_CLASS_TR.CODE_CLASS_ID)
                .and(CODE_CLASS_TR.LANG_CODE.equal(lang)))
            .orderBy(CODE_CLASS.ID)
            .fetchInto(CodeClass.class);
    }

    @NotNull
    @Override
    public List<CodeClassDefinition> findPageOfCodeClassDefinitions(@Nullable final CodeClassFilter filter, @NotNull final PaginationContext pc) {
        final SelectOnConditionStep<Record> selectStep = getDsl()
            .select(CODE_CLASS.fields())
            .select(LANGUAGE.CODE)
            .select(CODE_CLASS_TR.fields())
            .from(CODE_CLASS)
            .crossJoin(LANGUAGE)
            .leftOuterJoin(CODE_CLASS_TR)
            .on(CODE_CLASS.ID.eq(CODE_CLASS_TR.CODE_CLASS_ID))
            .and(LANGUAGE.CODE.eq(CODE_CLASS_TR.LANG_CODE));
        final SelectConditionStep<Record> selectConditionStep = applyWhereCondition(filter, selectStep);
        // the subsequent grouping requires ordering by id
        final Map<Integer, Result<Record>> rawRecords = selectConditionStep
            .orderBy(CODE_CLASS.ID)
            .fetchGroups(CODE_CLASS.ID);
        final List<CodeClassDefinition> results = mapRawRecordsIntoCodeClassDefinitions(rawRecords);
        considerSorting(pc, results);
        // need to page after sorting due to grouping, not profiting from DB filtering :-(
        return results
            .stream()
            .skip(pc.getOffset())
            .limit(pc.getPageSize())
            .collect(toList());
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
    public CodeClassDefinition newUnpersistedCodeClassDefinition() {
        final List<CodeClassTranslation> translations = getDsl()
            .select(LANGUAGE.CODE)
            .from(LANGUAGE)
            .fetchInto(String.class)
            .stream()
            .map(lc -> new CodeClassTranslation(null, lc, null, null, 0))
            .collect(toList());
        return toCodeClassDefinition(null, 0, translations);
    }

    private void considerSorting(@NotNull final PaginationContext pc, @NotNull final List<CodeClassDefinition> results) {
        for (final Sort.SortProperty sortProperty : pc.getSort()) {
            final String propName = sortProperty.getName();
            if (propName.equals(CODE_CLASS_TR.NAME.getName())) {
                compareBy(results, sortProperty, comparing(CodeClassDefinition::getName, String.CASE_INSENSITIVE_ORDER));
            } else if (propName.equals("translationsAsString")) {
                compareBy(results, sortProperty, comparing(CodeClassDefinition::getTranslationsAsString));
            }
        }
    }

    private void compareBy(@NotNull final List<CodeClassDefinition> results, @NotNull final Sort.SortProperty sortProperty,
        @NotNull final Comparator<CodeClassDefinition> byComparator) {
        if (sortProperty.getDirection() == Sort.Direction.DESC)
            results.sort(byComparator.reversed());
        else
            results.sort(byComparator);
    }

    @Override
    public int countByFilter(@Nullable final CodeClassFilter filter) {
        final SelectJoinStep<Record1<Integer>> selectStep = getDsl()
            .selectCount()
            .from(CODE_CLASS);
        final Integer count = applyWhereCondition(filter, selectStep).fetchOneInto(Integer.class);
        return count != null ? count : 0;
    }

    @NotNull
    private <R extends Record> SelectConditionStep<R> applyWhereCondition(@Nullable final CodeClassFilter filter,
        @NotNull final SelectJoinStep<R> selectStep) {
        if (filter != null) {
            return selectStep.where(filterToCondition(filter));
        } else {
            return selectStep.where(DSL.noCondition());
        }
    }

    @NotNull
    private Condition filterToCondition(@NotNull final CodeClassFilter filter) {
        final ConditionalSupplier conditions = new ConditionalSupplier();
        if (filter.getNameMask() != null) {
            final String mask = '%' + filter.getNameMask() + '%';
            conditions.add(() -> CODE_CLASS.ID.in(DSL
                .selectDistinct(CODE_CLASS_TR.CODE_CLASS_ID)
                .from(CODE_CLASS_TR)
                .where(CODE_CLASS_TR.NAME.likeIgnoreCase(mask))));
        }
        if (filter.getDescriptionMask() != null) {
            final String mask = '%' + filter.getDescriptionMask() + '%';
            conditions.add(() -> CODE_CLASS.ID.in(DSL
                .selectDistinct(CODE_CLASS_TR.CODE_CLASS_ID)
                .from(CODE_CLASS_TR)
                .where(CODE_CLASS_TR.DESCRIPTION.likeIgnoreCase(mask))));
        }
        return conditions.combineWithAnd();
    }

    @NotNull
    private List<CodeClassDefinition> mapRawRecordsIntoCodeClassDefinitions(@NotNull final Map<Integer, @NotNull Result<Record>> rawRecords) {
        final List<CodeClassDefinition> definitions = new ArrayList<>();
        for (final Map.Entry<Integer, Result<Record>> entry : rawRecords.entrySet()) {
            final List<CodeClassTranslation> translations = entry
                .getValue()
                .stream()
                .map(r -> new CodeClassTranslation(r.getValue(CODE_CLASS_TR.ID), r.getValue(LANGUAGE.CODE), r.getValue(CODE_CLASS_TR.NAME),
                    r.getValue(CODE_CLASS_TR.DESCRIPTION), r.getValue(CODE_CLASS_TR.VERSION)))
                .collect(toList());
            final Record r = entry
                .getValue()
                .stream()
                .findFirst()
                .orElseThrow();
            definitions.add(toCodeClassDefinition(entry.getKey(), r.getValue(CODE_CLASS.VERSION), translations));
        }
        return definitions;
    }

    @Nullable
    @Override
    public CodeClassDefinition findCodeClassDefinition(@NotNull final Integer id) {
        final Map<Integer, Result<Record>> records = getDsl()
            .select(CODE_CLASS.fields())
            .select(LANGUAGE.CODE)
            .select(CODE_CLASS_TR.fields())
            .from(CODE_CLASS)
            .crossJoin(LANGUAGE)
            .leftOuterJoin(CODE_CLASS_TR)
            .on(CODE_CLASS.ID.eq(CODE_CLASS_TR.CODE_CLASS_ID))
            .and(LANGUAGE.CODE.eq(CODE_CLASS_TR.LANG_CODE))
            .where(CODE_CLASS.ID.eq(id))
            .orderBy(CODE_CLASS_TR.ID)
            .fetchGroups(CODE_CLASS.ID);
        if (!records.isEmpty())
            return mapRawRecordsIntoCodeClassDefinitions(records).getFirst();
        else
            return null;
    }

    @NotNull
    @Override
    @CacheEvict(allEntries = true)
    public CodeClassDefinition saveOrUpdate(@NotNull final CodeClassDefinition codeClassDefinition) {
        final int userId = getUserId();

        final CodeClassRecord ccRecord = getDsl()
            .insertInto(CODE_CLASS)
            .set(CODE_CLASS.ID, codeClassDefinition.getId())
            .set(CODE_CLASS.CREATED_BY, userId)
            .set(CODE_CLASS.LAST_MODIFIED_BY, userId)
            .onDuplicateKeyUpdate()
            .set(CODE_CLASS.CREATED_BY, userId)
            .set(CODE_CLASS.LAST_MODIFIED_BY, userId)
            .set(CODE_CLASS.LAST_MODIFIED, getTs())
            .set(CODE_CLASS.VERSION, CODE_CLASS.VERSION.add(1))
            .returning()
            .fetchOne();
        Objects.requireNonNull(ccRecord);
        final Integer id = ccRecord.get(CODE_CLASS.ID);
        final List<CodeClassTranslation> persistedTranslations = updateOrInsertAndLoadCodeClassTranslations(codeClassDefinition, userId);
        final CodeClassDefinition persistedEntity = toCodeClassDefinition(id, ccRecord.get(CODE_CLASS.VERSION), persistedTranslations);
        log.info("{} saved 1 record: {} with id {}.", getActiveUser().getUserName(), CODE_CLASS.getName(), id);
        return persistedEntity;
    }

    @NotNull
    private CodeClassDefinition toCodeClassDefinition(@Nullable final Integer id, final int version,
        @NotNull final List<CodeClassTranslation> persistedTranslations) {
        return new CodeClassDefinition(id, getMainLanguage(), version, persistedTranslations.toArray(new CodeClassTranslation[0]));
    }

    @NotNull
    private List<CodeClassTranslation> updateOrInsertAndLoadCodeClassTranslations(@NotNull final CodeClassDefinition entity, final int userId) {
        Objects.requireNonNull(entity.getId());
        final Result<CodeClassTrRecord> persistedTranslations = loadCodeClassTranslationsFromDbFor(entity.getId());
        final Collection<CodeClassTranslation> entityTranslations = entity.getTranslations(null);
        removeObsoletePersistedRecordsFor(persistedTranslations, entityTranslations);
        return addOrUpdate(entity, entityTranslations, userId);
    }

    @NotNull
    private Result<CodeClassTrRecord> loadCodeClassTranslationsFromDbFor(@NotNull final Integer codeClassId) {
        return getDsl()
            .selectFrom(CODE_CLASS_TR)
            .where(CODE_CLASS_TR.ID.eq(codeClassId))
            .fetch();
    }

    // package-protected for test purposes
    void removeObsoletePersistedRecordsFor(@NotNull final Result<CodeClassTrRecord> persistedTranslations,
        @NotNull final Collection<CodeClassTranslation> entityTranslations) {
        for (final CodeClassTrRecord pcctr : persistedTranslations) {
            if (!isPresentIn(entityTranslations, pcctr))
                pcctr.delete();
        }
    }

    private boolean isPresentIn(@NotNull final Collection<CodeClassTranslation> translations, @NotNull final CodeClassTrRecord cct) {
        for (final CodeClassTranslation ct : translations) {
            if (ct.getId() != null && ct
                .getId()
                .equals(cct.get(CODE_CLASS_TR.ID)))
                return true;
        }
        return false;
    }

    @NotNull
    private List<CodeClassTranslation> addOrUpdate(@NotNull final CodeClassDefinition entity,
        @NotNull final Collection<CodeClassTranslation> entityTranslations, final int userId) {
        final List<CodeClassTranslation> cctsPersisted = new ArrayList<>();
        for (final CodeClassTranslation cct : entityTranslations) {
            if (cct.getDescription() == null)
                cct.setDescription("");
            if (cct.getId() != null) {
                considerAdding(updateCodeClassTr(entity, cct, userId, cct.getVersion()), cctsPersisted, cct);
            } else {
                final CodeClassTrRecord cctRecord = insertAndGetCodeClassTr(entity.getId(), userId, cct);
                if (cctRecord != null)
                    cctsPersisted.add(toCodeClassTranslation(cctRecord));
            }
        }
        return cctsPersisted;
    }

    // package-private for testing purposes
    void considerAdding(@Nullable final CodeClassTrRecord cctRecord, @NotNull final List<CodeClassTranslation> cctsPersisted,
        @NotNull final CodeClassTranslation cct) {
        if (cctRecord != null) {
            cctsPersisted.add(toCodeClassTranslation(cctRecord));
        } else {
            throw new OptimisticLockingException(CODE_CLASS_TR.getName(), cct.toString(), OptimisticLockingException.Type.UPDATE);
        }
    }

    @Nullable
    private CodeClassTrRecord insertAndGetCodeClassTr(@Nullable final Integer id, final int userId, @NotNull final CodeClassTranslation cct) {
        return getDsl()
            .insertInto(CODE_CLASS_TR)
            .set(CODE_CLASS_TR.CODE_CLASS_ID, id)
            .set(CODE_CLASS_TR.LANG_CODE, cct.getLangCode())
            .set(CODE_CLASS_TR.NAME, cct.getName())
            .set(CODE_CLASS_TR.DESCRIPTION, cct.getDescription())
            .set(CODE_CLASS_TR.CREATED_BY, userId)
            .set(CODE_CLASS_TR.LAST_MODIFIED_BY, userId)
            .returning()
            .fetchOne();
    }

    @Nullable
    private CodeClassTrRecord updateCodeClassTr(@NotNull final CodeClassDefinition entity, @NotNull final CodeClassTranslation ct, final int userId,
        final int currentCctVersion) {
        final Integer id = entity.getId();
        return getDsl()
            .update(CODE_CLASS_TR)
            .set(CODE_CLASS_TR.CODE_CLASS_ID, id)
            .set(CODE_CLASS_TR.LANG_CODE, ct.getLangCode())
            .set(CODE_CLASS_TR.NAME, ct.getName())
            .set(CODE_CLASS_TR.DESCRIPTION, ct.getDescription())
            .set(CODE_CLASS_TR.LAST_MODIFIED_BY, userId)
            .set(CODE_CLASS_TR.LAST_MODIFIED, getTs())
            .set(CODE_CLASS_TR.VERSION, currentCctVersion + 1)
            .where(CODE_CLASS_TR.ID.eq(ct.getId()))
            .and(CODE_CLASS_TR.VERSION.eq(currentCctVersion))
            .returning()
            .fetchOne();
    }

    @NotNull
    private CodeClassTranslation toCodeClassTranslation(@NotNull final CodeClassTrRecord record) {
        return new CodeClassTranslation(record.get(CODE_CLASS_TR.ID), record.get(CODE_CLASS_TR.LANG_CODE), record.get(CODE_CLASS_TR.NAME),
            record.get(CODE_CLASS_TR.DESCRIPTION), record.get(CODE_CLASS_TR.VERSION));
    }

    @Nullable
    @Override
    @CacheEvict(allEntries = true)
    public CodeClassDefinition delete(@NotNull final Integer id, final int version) {
        final CodeClassDefinition toBeDeleted = findCodeClassDefinition(id);
        if (toBeDeleted != null) {
            if (toBeDeleted.getVersion() == version) {
                final int deleteCount = deleteCodeClassMatching(id, version);
                logOrThrow(deleteCount, id, toBeDeleted.toString());
            } else {
                throw new OptimisticLockingException(CODE_CLASS.getName(), OptimisticLockingException.Type.DELETE);
            }
        }
        return toBeDeleted;
    }

    private int deleteCodeClassMatching(@NotNull final Integer id, final int version) {
        return getDsl()
            .delete(CODE_CLASS)
            .where(CODE_CLASS.ID.equal(id))
            .and(CODE_CLASS.VERSION.eq(version))
            .execute();
    }

    // package-private for testing purposes
    void logOrThrow(final int deleteCount, @NotNull final Integer id, @NotNull final String deletedAsString) {
        if (deleteCount > 0) {
            log.info("{} deleted {} record: {} with code class {}.", getActiveUser().getUserName(), deleteCount, CODE_CLASS.getName(), id);
        } else {
            throw new OptimisticLockingException(CODE_CLASS.getName(), deletedAsString, OptimisticLockingException.Type.DELETE);
        }
    }
}
