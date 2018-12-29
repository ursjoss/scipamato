package ch.difty.scipamato.core.persistence.codeclass;

import static ch.difty.scipamato.core.db.tables.CodeClass.CODE_CLASS;
import static ch.difty.scipamato.core.db.tables.CodeClassTr.CODE_CLASS_TR;
import static ch.difty.scipamato.core.db.tables.Language.LANGUAGE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.db.tables.records.CodeClassRecord;
import ch.difty.scipamato.core.db.tables.records.CodeClassTrRecord;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code_class.CodeClassDefinition;
import ch.difty.scipamato.core.entity.code_class.CodeClassFilter;
import ch.difty.scipamato.core.entity.code_class.CodeClassTranslation;
import ch.difty.scipamato.core.persistence.AbstractRepo;
import ch.difty.scipamato.core.persistence.ConditionalSupplier;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

@Repository
@CacheConfig(cacheNames = "codeClasses")
@Slf4j
public class JooqCodeClassRepo extends AbstractRepo implements CodeClassRepository {

    public JooqCodeClassRepo(@Qualifier("dslContext") final DSLContext dslContext,
        final DateTimeService dateTimeService) {
        super(dslContext, dateTimeService);
    }

    @Override
    @Cacheable
    public List<CodeClass> find(final String languageCode) {
        final String lang = TranslationUtils.trimLanguageCode(languageCode);
        // skipping the audit fields
        return getDsl()
            .select(CODE_CLASS.ID.as("CC_ID"), DSL
                .coalesce(CODE_CLASS_TR.NAME, TranslationUtils.NOT_TRANSL)
                .as("CC_NAME"), DSL
                .coalesce(CODE_CLASS_TR.DESCRIPTION, TranslationUtils.NOT_TRANSL)
                .as("CC_DESCRIPTION"))
            .from(CODE_CLASS)
            .leftOuterJoin(CODE_CLASS_TR)
            .on(CODE_CLASS.ID
                .equal(CODE_CLASS_TR.CODE_CLASS_ID)
                .and(CODE_CLASS_TR.LANG_CODE.equal(lang)))
            .orderBy(CODE_CLASS.ID)
            .fetchInto(CodeClass.class);
    }

    @Override
    public List<CodeClassDefinition> findPageOfCodeClassDefinitions(final CodeClassFilter filter,
        final PaginationContext pc) {
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
            .skip((long) pc.getOffset())
            .limit((long) pc.getPageSize())
            .collect(toList());
    }

    @Override
    public String getMainLanguage() {
        return getDsl()
            .select(LANGUAGE.CODE)
            .from(LANGUAGE)
            .where(LANGUAGE.MAIN_LANGUAGE.eq(true))
            .fetchOneInto(String.class);
    }

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

    private void considerSorting(final PaginationContext pc, final List<CodeClassDefinition> results) {
        for (final Sort.SortProperty sortProperty : pc.getSort()) {
            final String propName = sortProperty.getName();
            if (propName.equals(CODE_CLASS_TR.NAME.getName())) {
                compareBy(results, sortProperty,
                    comparing(CodeClassDefinition::getName, String.CASE_INSENSITIVE_ORDER));
            } else if (propName.equals("translationsAsString")) {
                compareBy(results, sortProperty, comparing(CodeClassDefinition::getTranslationsAsString));
            }
        }
    }

    private void compareBy(final List<CodeClassDefinition> results, Sort.SortProperty sortProperty,
        final Comparator<CodeClassDefinition> byComparator) {
        if (sortProperty.getDirection() == Sort.Direction.DESC)
            results.sort(byComparator.reversed());
        else
            results.sort(byComparator);
    }

    @Override
    public int countByFilter(final CodeClassFilter filter) {
        final SelectJoinStep<Record1<Integer>> selectStep = getDsl()
            .selectCount()
            .from(CODE_CLASS);
        return applyWhereCondition(filter, selectStep).fetchOneInto(Integer.class);
    }

    private <R extends Record> SelectConditionStep<R> applyWhereCondition(final CodeClassFilter filter,
        final SelectJoinStep<R> selectStep) {
        if (filter != null) {
            return selectStep.where(filterToCondition(filter));
        } else {
            return selectStep.where(DSL.trueCondition());
        }
    }

    private Condition filterToCondition(final CodeClassFilter filter) {
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

    private List<CodeClassDefinition> mapRawRecordsIntoCodeClassDefinitions(
        final Map<Integer, Result<Record>> rawRecords) {
        final List<CodeClassDefinition> definitions = new ArrayList<>();
        for (final Map.Entry<Integer, Result<Record>> entry : rawRecords.entrySet()) {
            final List<CodeClassTranslation> translations = entry
                .getValue()
                .stream()
                .map(r -> new CodeClassTranslation(r.getValue(CODE_CLASS_TR.ID), r.getValue(LANGUAGE.CODE),
                    r.getValue(CODE_CLASS_TR.NAME), r.getValue(CODE_CLASS_TR.DESCRIPTION),
                    r.getValue(CODE_CLASS_TR.VERSION)))
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

    @Override
    public CodeClassDefinition findCodeClassDefinition(final Integer id) {
        AssertAs.notNull(id, "id");
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
            return mapRawRecordsIntoCodeClassDefinitions(records).get(0);
        else
            return null;
    }

    @Override
    @CacheEvict(allEntries = true)
    public CodeClassDefinition saveOrUpdate(final CodeClassDefinition codeClassDefinition) {
        AssertAs.notNull(codeClassDefinition, "codeClassDefinition");
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
        final Integer id = ccRecord.get(CODE_CLASS.ID);
        final List<CodeClassTranslation> persistedTranslations = updateOrInsertAndLoadCodeClassTranslations(
            codeClassDefinition, userId);
        final CodeClassDefinition persistedEntity = toCodeClassDefinition(id, ccRecord.get(CODE_CLASS.VERSION),
            persistedTranslations);
        log.info("{} saved 1 record: {} with id {}.", getActiveUser().getUserName(), CODE_CLASS.getName(), id);
        return persistedEntity;
    }

    private CodeClassDefinition toCodeClassDefinition(final Integer id, final int version,
        final List<CodeClassTranslation> persistedTranslations) {
        return new CodeClassDefinition(id, getMainLanguage(), version,
            persistedTranslations.toArray(new CodeClassTranslation[0]));
    }

    private List<CodeClassTranslation> updateOrInsertAndLoadCodeClassTranslations(final CodeClassDefinition entity,
        final int userId) {
        final Collection<CodeClassTranslation> entityTranslations = entity
            .getTranslations()
            .values();
        removeObsoletePersistedRecords(entity, entityTranslations);
        return addOrUpdate(entity, entityTranslations, userId);
    }

    private void removeObsoletePersistedRecords(final CodeClassDefinition entity,
        final Collection<CodeClassTranslation> entityTranslations) {
        final Result<CodeClassTrRecord> persistedTranslations = getDsl()
            .selectFrom(CODE_CLASS_TR)
            .where(CODE_CLASS_TR.ID.eq(entity.getId()))
            .fetch();
        for (final CodeClassTrRecord cctr : persistedTranslations) {
            if (!isPresentIn(entityTranslations, cctr))
                cctr.delete();
        }
    }

    private List<CodeClassTranslation> addOrUpdate(final CodeClassDefinition entity,
        final Collection<CodeClassTranslation> entityTranslations, final int userId) {
        final List<CodeClassTranslation> cctsPersisted = new ArrayList<>();
        for (final CodeClassTranslation cct : entityTranslations) {
            if (cct.getDescription() == null)
                cct.setDescription("");
            if (cct.getId() != null) {
                final int currentVersion = cct.getVersion();
                final CodeClassTrRecord cctRecord = updateCodeClassTr(entity, cct, userId, currentVersion);
                if (cctRecord != null) {
                    cctsPersisted.add(toCodeClassTranslation(cctRecord));
                } else {
                    throw new OptimisticLockingException(CODE_CLASS_TR.getName(), cct.toString(),
                        OptimisticLockingException.Type.UPDATE);
                }
            } else {
                final CodeClassTrRecord cctRecord = insertAndGetCodeClassTr(entity.getId(), userId, cct);
                cctsPersisted.add(toCodeClassTranslation(cctRecord));
            }
        }
        return cctsPersisted;
    }

    private boolean isPresentIn(final Collection<CodeClassTranslation> translations, final CodeClassTrRecord cct) {
        for (final CodeClassTranslation ct : translations) {
            if (ct.getId() != null && ct
                .getId()
                .equals(cct.get(CODE_CLASS_TR.ID)))
                return true;
        }
        return false;
    }

    private CodeClassTrRecord insertAndGetCodeClassTr(final Integer id, final int userId,
        final CodeClassTranslation cct) {
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

    private CodeClassTrRecord updateCodeClassTr(final CodeClassDefinition entity, final CodeClassTranslation ct,
        final int userId, final int currentCctVersion) {
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

    private CodeClassTranslation toCodeClassTranslation(final CodeClassTrRecord record) {
        return new CodeClassTranslation(record.get(CODE_CLASS_TR.ID), record.get(CODE_CLASS_TR.LANG_CODE),
            record.get(CODE_CLASS_TR.NAME), record.get(CODE_CLASS_TR.DESCRIPTION), record.get(CODE_CLASS_TR.VERSION));
    }

    @Override
    @CacheEvict(allEntries = true)
    public CodeClassDefinition delete(final Integer id, final int version) {
        AssertAs.notNull(id, "id");

        final CodeClassDefinition toBeDeleted = findCodeClassDefinition(id);
        if (toBeDeleted != null) {
            if (toBeDeleted.getVersion() == version) {
                final int deleteCount = getDsl()
                    .delete(CODE_CLASS)
                    .where(CODE_CLASS.ID.equal(id))
                    .and(CODE_CLASS.VERSION.eq(version))
                    .execute();
                if (deleteCount > 0) {
                    log.info("{} deleted {} record: {} with code class {}.", getActiveUser().getUserName(), deleteCount,
                        CODE_CLASS.getName(), id);
                } else {
                    throw new OptimisticLockingException(CODE_CLASS.getName(), toBeDeleted.toString(),
                        OptimisticLockingException.Type.DELETE);
                }
            } else {
                throw new OptimisticLockingException(CODE_CLASS.getName(), OptimisticLockingException.Type.DELETE);
            }
        }
        return toBeDeleted;
    }

}
