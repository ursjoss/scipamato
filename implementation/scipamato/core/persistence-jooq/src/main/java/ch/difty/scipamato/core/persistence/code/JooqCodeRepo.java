package ch.difty.scipamato.core.persistence.code;

import static ch.difty.scipamato.core.db.tables.Code.CODE;
import static ch.difty.scipamato.core.db.tables.CodeClass.CODE_CLASS;
import static ch.difty.scipamato.core.db.tables.CodeClassTr.CODE_CLASS_TR;
import static ch.difty.scipamato.core.db.tables.CodeTr.CODE_TR;
import static ch.difty.scipamato.core.db.tables.Language.LANGUAGE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.db.tables.records.CodeRecord;
import ch.difty.scipamato.core.db.tables.records.CodeTrRecord;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.entity.code.CodeTranslation;
import ch.difty.scipamato.core.persistence.AbstractRepo;
import ch.difty.scipamato.core.persistence.ConditionalSupplier;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.persistence.codeclass.CodeClassRepository;

@Repository
@CacheConfig(cacheNames = "codes")
@Slf4j
@Profile("!wickettest")
public class JooqCodeRepo extends AbstractRepo implements CodeRepository {

    private final CodeClassRepository codeClassRepo;

    public JooqCodeRepo(@Qualifier("dslContext") final DSLContext dslContext, final DateTimeService dateTimeService,
        final CodeClassRepository codeClassRepo) {
        super(dslContext, dateTimeService);
        this.codeClassRepo = AssertAs.INSTANCE.notNull(codeClassRepo, "codeClassRepo");
    }

    @Override
    @Cacheable
    public List<Code> findCodesOfClass(final CodeClassId codeClassId, final String languageCode) {
        AssertAs.INSTANCE.notNull(codeClassId, "codeClassId");
        AssertAs.INSTANCE.notNull(languageCode, "languageCode");
        final String lang = TranslationUtils.INSTANCE.trimLanguageCode(languageCode);
        // skipping the audit fields
        return getDsl()
            .select(CODE.CODE_.as("C_ID"), DSL
                    .coalesce(CODE_TR.NAME, TranslationUtils.INSTANCE.getNOT_TRANSL())
                    .as("C_NAME"), CODE_TR.COMMENT.as("C_COMMENT"), CODE.INTERNAL.as("C_INTERNAL"),
                CODE_CLASS.ID.as("CC_ID"), DSL
                    .coalesce(CODE_CLASS_TR.NAME, TranslationUtils.INSTANCE.getNOT_TRANSL())
                    .as("CC_NAME"), DSL
                    .coalesce(CODE_CLASS_TR.DESCRIPTION, TranslationUtils.INSTANCE.getNOT_TRANSL())
                    .as("CC_DESCRIPTION"), CODE.SORT.as("C_SORT"))
            .from(CODE)
            .join(CODE_CLASS)
            .on(CODE.CODE_CLASS_ID.equal(CODE_CLASS.ID))
            .leftOuterJoin(CODE_TR)
            .on(CODE.CODE_
                .equal(CODE_TR.CODE)
                .and(CODE_TR.LANG_CODE.equal(lang)))
            .leftOuterJoin(CODE_CLASS_TR)
            .on(CODE_CLASS.ID
                .equal(CODE_CLASS_TR.CODE_CLASS_ID)
                .and(CODE_CLASS_TR.LANG_CODE.equal(lang)))
            .where(CODE.CODE_CLASS_ID.equal(codeClassId.getId()))
            .orderBy(CODE_CLASS.ID, CODE.SORT)
            .fetchInto(Code.class);
    }

    @Override
    public List<CodeDefinition> findPageOfCodeDefinitions(final CodeFilter filter, final PaginationContext pc) {
        final SelectOnConditionStep<Record> selectStep = getDsl()
            .select(CODE.fields())
            .select(LANGUAGE.CODE)
            .select(CODE_TR.fields())
            .from(CODE)
            .crossJoin(LANGUAGE)
            .leftOuterJoin(CODE_TR)
            .on(CODE.CODE_.eq(CODE_TR.CODE))
            .and(LANGUAGE.CODE.eq(CODE_TR.LANG_CODE));
        final SelectConditionStep<Record> selectConditionStep = applyWhereCondition(filter, selectStep);
        // the subsequent grouping requires ordering by id then language_code
        final Map<String, Result<Record>> rawRecords = selectConditionStep
            .orderBy(CODE.SORT)
            .fetchGroups(CODE.CODE_);
        final List<CodeDefinition> results = mapRawRecordsIntoCodeDefinitions(rawRecords);
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
    public CodeDefinition newUnpersistedCodeDefinition() {
        final List<CodeTranslation> translations = getDsl()
            .select(LANGUAGE.CODE)
            .from(LANGUAGE)
            .fetchInto(String.class)
            .stream()
            .map(lc -> new CodeTranslation(null, lc, null, null, 0))
            .collect(toList());
        return toCodeDefinition(null, null, 1, false, 0, translations);
    }

    private void considerSorting(final PaginationContext pc, final List<CodeDefinition> results) {
        for (final Sort.SortProperty sortProperty : pc.getSort()) {
            final String propName = sortProperty.getName();
            if (propName.equals(CODE.SORT.getName())) {
                compareBy(results, sortProperty, comparing(CodeDefinition::getSort));
            } else if (propName.equals(CODE_TR.NAME.getName())) {
                compareBy(results, sortProperty, comparing(CodeDefinition::getName, String.CASE_INSENSITIVE_ORDER));
            } else if (propName.equals(CODE.CODE_.getName())) {
                compareBy(results, sortProperty, comparing(CodeDefinition::getCode));
            } else if (propName.equals(CODE.INTERNAL.getName())) {
                compareBy(results, sortProperty, comparing(CodeDefinition::isInternal));
            } else if (propName.equals("translationsAsString")) {
                compareBy(results, sortProperty, comparing(CodeDefinition::getTranslationsAsString));
            }
        }
    }

    private void compareBy(final List<CodeDefinition> results, Sort.SortProperty sortProperty,
        final Comparator<CodeDefinition> byComparator) {
        if (sortProperty.getDirection() == Sort.Direction.DESC)
            results.sort(byComparator.reversed());
        else
            results.sort(byComparator);
    }

    @Override
    public int countByFilter(final CodeFilter filter) {
        final SelectJoinStep<Record1<Integer>> selectStep = getDsl()
            .selectCount()
            .from(CODE);
        return applyWhereCondition(filter, selectStep).fetchOneInto(Integer.class);
    }

    private <R extends Record> SelectConditionStep<R> applyWhereCondition(final CodeFilter filter,
        final SelectJoinStep<R> selectStep) {
        if (filter != null) {
            return selectStep.where(filterToCondition(filter));
        } else {
            return selectStep.where(DSL.trueCondition());
        }
    }

    private Condition filterToCondition(final CodeFilter filter) {
        final ConditionalSupplier conditions = new ConditionalSupplier();
        if (filter.getNameMask() != null) {
            final String mask = '%' + filter.getNameMask() + '%';
            conditions.add(() -> CODE.CODE_.in(DSL
                .selectDistinct(CODE_TR.CODE)
                .from(CODE_TR)
                .where(CODE_TR.NAME.likeIgnoreCase(mask))));
        }
        if (filter.getInternal() != null) {
            conditions.add(() -> CODE.INTERNAL.eq(filter.getInternal()));
        }
        if (filter.getCodeClass() != null) {
            conditions.add(() -> CODE.CODE_CLASS_ID.eq(filter
                .getCodeClass()
                .getId()));
        }
        if (filter.getCommentMask() != null) {
            final String mask = '%' + filter.getCommentMask() + '%';
            conditions.add(() -> CODE.CODE_.in(DSL
                .selectDistinct(CODE_TR.CODE)
                .from(CODE_TR)
                .where(CODE_TR.COMMENT.likeIgnoreCase(mask))));
        }
        return conditions.combineWithAnd();
    }

    private List<CodeDefinition> mapRawRecordsIntoCodeDefinitions(final Map<String, Result<Record>> rawRecords) {
        final List<CodeDefinition> definitions = new ArrayList<>();
        final Map<Integer, CodeClass> codeClasses = codeClassRepo
            .find(getMainLanguage())
            .stream()
            .collect(Collectors.toMap(CodeClass::getId, cc -> cc));
        for (final Map.Entry<String, Result<Record>> entry : rawRecords.entrySet()) {
            final List<CodeTranslation> translations = entry
                .getValue()
                .stream()
                .map(r -> new CodeTranslation(r.getValue(CODE_TR.ID), r.getValue(LANGUAGE.CODE),
                    r.getValue(CODE_TR.NAME), r.getValue(CODE_TR.COMMENT), r.getValue(CODE_TR.VERSION)))
                .collect(toList());
            final Record r = entry
                .getValue()
                .stream()
                .findFirst()
                .orElseThrow();
            final CodeClass cc = codeClasses.get(r.getValue(CODE.CODE_CLASS_ID));
            definitions.add(toCodeDefinition(entry.getKey(), cc, r.getValue(CODE.SORT), r.getValue(CODE.INTERNAL),
                r.getValue(CODE.VERSION), translations));
        }
        return definitions;
    }

    @Override
    public CodeDefinition findCodeDefinition(final String code) {
        final Map<String, Result<Record>> records = getDsl()
            .select(CODE.fields())
            .select(LANGUAGE.CODE)
            .select(CODE_TR.fields())
            .from(CODE)
            .crossJoin(LANGUAGE)
            .leftOuterJoin(CODE_TR)
            .on(CODE.CODE_.eq(CODE_TR.CODE))
            .and(LANGUAGE.CODE.eq(CODE_TR.LANG_CODE))
            .where(CODE.CODE_.eq(code))
            .orderBy(CODE_TR.ID)
            .fetchGroups(CODE.CODE_);
        if (!records.isEmpty())
            return mapRawRecordsIntoCodeDefinitions(records).get(0);
        else
            return null;
    }

    @Override
    @CacheEvict(allEntries = true)
    public CodeDefinition saveOrUpdate(final CodeDefinition codeDefinition) {
        AssertAs.INSTANCE.notNull(codeDefinition, "codeDefinition");
        AssertAs.INSTANCE.notNull(codeDefinition.getCodeClass(), "codeDefinition.codeClass");
        AssertAs.INSTANCE.notNull(codeDefinition
            .getCodeClass()
            .getId(), "codeDefinition.codeClass.id");
        final int userId = getUserId();

        final CodeRecord cRecord = getDsl()
            .insertInto(CODE)
            .set(CODE.CODE_, codeDefinition.getCode())
            .set(CODE.CODE_CLASS_ID, codeDefinition
                .getCodeClass()
                .getId())
            .set(CODE.SORT, codeDefinition.getSort())
            .set(CODE.INTERNAL, codeDefinition.isInternal())
            .set(CODE.CREATED_BY, userId)
            .set(CODE.LAST_MODIFIED_BY, userId)
            .onDuplicateKeyUpdate()
            .set(CODE.CODE_CLASS_ID, codeDefinition
                .getCodeClass()
                .getId())
            .set(CODE.SORT, codeDefinition.getSort())
            .set(CODE.INTERNAL, codeDefinition.isInternal())
            .set(CODE.CREATED_BY, userId)
            .set(CODE.LAST_MODIFIED_BY, userId)
            .set(CODE.LAST_MODIFIED, getTs())
            .set(CODE.VERSION, CODE.VERSION.add(1))
            .returning()
            .fetchOne();
        final String code = cRecord.get(CODE.CODE_);
        final CodeClass codeClass = codeClassRepo
            .find(getMainLanguage())
            .stream()
            .filter(cc -> cc
                .getId()
                .equals(cRecord.get(CODE.CODE_CLASS_ID)))
            .findFirst()
            .orElse(null);

        final List<CodeTranslation> persistedTranslations = updateOrInsertAndLoadCodeTranslations(codeDefinition,
            userId);
        final CodeDefinition persistedEntity = toCodeDefinition(code, codeClass, cRecord.get(CODE.SORT),
            cRecord.get(CODE.INTERNAL), cRecord.get(CODE.VERSION), persistedTranslations);
        log.info("{} saved 1 record: {} with id {}.", getActiveUser().getUserName(), CODE.getName(), code);
        return persistedEntity;
    }

    private CodeDefinition toCodeDefinition(final String code, final CodeClass codeClass, final int sort,
        final boolean internal, final int version, final List<CodeTranslation> persistedTranslations) {
        return new CodeDefinition(code, getMainLanguage(), codeClass, sort, internal, version,
            persistedTranslations.toArray(new CodeTranslation[0]));
    }

    private List<CodeTranslation> updateOrInsertAndLoadCodeTranslations(final CodeDefinition entity, final int userId) {
        final Result<CodeTrRecord> persistedTranslations = loadCodeTranslationsFromDbFor(entity.getCode());
        final Collection<CodeTranslation> entityTranslations = entity
            .getTranslations()
            .values();
        removeObsoletePersistedRecordsFor(persistedTranslations, entityTranslations);
        return addOrUpdate(entity, entityTranslations, userId);
    }

    private Result<CodeTrRecord> loadCodeTranslationsFromDbFor(final String code) {
        return getDsl()
            .selectFrom(CODE_TR)
            .where(CODE_TR.CODE.eq(code))
            .fetch();
    }

    // package-private for testing purposes
    void removeObsoletePersistedRecordsFor(final Result<CodeTrRecord> persistedTranslations,
        final Collection<CodeTranslation> entityTranslations) {
        for (final CodeTrRecord pctr : persistedTranslations)
            if (!isPresentIn(entityTranslations, pctr))
                pctr.delete();
    }

    private boolean isPresentIn(final Collection<CodeTranslation> entityTranslations, final CodeTrRecord pctr) {
        for (final CodeTranslation entityTranslation : entityTranslations) {
            final Integer entityTrId = entityTranslation.getId();
            if (entityTrId != null && entityTrId.equals(pctr.get(CODE_TR.ID)))
                return true;
        }
        return false;
    }

    private List<CodeTranslation> addOrUpdate(final CodeDefinition entity,
        final Collection<CodeTranslation> entityTranslations, final int userId) {
        final List<CodeTranslation> ctsPersisted = new ArrayList<>();
        for (final CodeTranslation ct : entityTranslations) {
            if (ct.getId() != null) {
                considerAdding(updateCodeTr(entity, ct, userId, ct.getVersion()), ctsPersisted, ct);
            } else {
                final CodeTrRecord ctRecord = insertAndGetCodeTr(entity.getCode(), userId, ct);
                ctsPersisted.add(toCodeTranslation(ctRecord));
            }
        }
        return ctsPersisted;
    }

    // package-private for testing purposes
    void considerAdding(final CodeTrRecord ctRecord, final List<CodeTranslation> ctsPersisted,
        final CodeTranslation ct) {
        if (ctRecord != null) {
            ctsPersisted.add(toCodeTranslation(ctRecord));
        } else {
            throw new OptimisticLockingException(CODE_TR.getName(), ct.toString(),
                OptimisticLockingException.Type.UPDATE);
        }
    }

    private CodeTrRecord insertAndGetCodeTr(final String code, final int userId, final CodeTranslation ct) {
        return getDsl()
            .insertInto(CODE_TR)
            .set(CODE_TR.CODE, code)
            .set(CODE_TR.LANG_CODE, ct.getLangCode())
            .set(CODE_TR.NAME, ct.getName())
            .set(CODE_TR.CREATED_BY, userId)
            .set(CODE_TR.LAST_MODIFIED_BY, userId)
            .returning()
            .fetchOne();
    }

    private CodeTrRecord updateCodeTr(final CodeDefinition entity, final CodeTranslation ct, final int userId,
        final int currentNttVersion) {
        final String code = entity.getCode();
        return getDsl()
            .update(CODE_TR)
            .set(CODE_TR.CODE, code)
            .set(CODE_TR.LANG_CODE, ct.getLangCode())
            .set(CODE_TR.NAME, ct.getName())
            .set(CODE_TR.COMMENT, ct.getComment())
            .set(CODE_TR.LAST_MODIFIED_BY, userId)
            .set(CODE_TR.LAST_MODIFIED, getTs())
            .set(CODE_TR.VERSION, currentNttVersion + 1)
            .where(CODE_TR.ID.eq(ct.getId()))
            .and(CODE_TR.VERSION.eq(currentNttVersion))
            .returning()
            .fetchOne();
    }

    private CodeTranslation toCodeTranslation(final CodeTrRecord record) {
        return new CodeTranslation(record.get(CODE_TR.ID), record.get(CODE_TR.LANG_CODE), record.get(CODE_TR.NAME),
            record.get(CODE_TR.COMMENT), record.get(CODE_TR.VERSION));
    }

    @Override
    @CacheEvict(allEntries = true)
    public CodeDefinition delete(final String code, final int version) {
        AssertAs.INSTANCE.notNull(code, "code");

        final CodeDefinition toBeDeleted = findCodeDefinition(code);
        if (toBeDeleted != null) {
            if (toBeDeleted.getVersion() == version) {
                final int deleteCount = deleteCodeMatching(code, version);
                logOrThrow(deleteCount, code, toBeDeleted.toString());
            } else {
                throw new OptimisticLockingException(CODE.getName(), OptimisticLockingException.Type.DELETE);
            }
        }
        return toBeDeleted;
    }

    private int deleteCodeMatching(final String code, final int version) {
        return getDsl()
            .delete(CODE)
            .where(CODE.CODE_.equal(code))
            .and(CODE.VERSION.eq(version))
            .execute();
    }

    // package-private for testing purposes
    void logOrThrow(final int deleteCount, final String code, final String deletedAsString) {
        if (deleteCount > 0) {
            log.info("{} deleted {} record: {} with code {}.", getActiveUser().getUserName(), deleteCount,
                CODE.getName(), code);
        } else {
            throw new OptimisticLockingException(CODE.getName(), deletedAsString,
                OptimisticLockingException.Type.DELETE);
        }
    }

    @Override
    public CodeClass getCodeClass1(final String langCode) {
        return codeClassRepo
            .find(langCode)
            .stream()
            .findFirst()
            .orElse(null);
    }

}
