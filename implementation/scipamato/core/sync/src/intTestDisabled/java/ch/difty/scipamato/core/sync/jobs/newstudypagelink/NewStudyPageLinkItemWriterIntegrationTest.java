package ch.difty.scipamato.core.sync.jobs.newstudypagelink;

import static ch.difty.scipamato.publ.db.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest;
import ch.difty.scipamato.publ.db.tables.records.NewStudyPageLinkRecord;

@SuppressWarnings("SameParameterValue")
class NewStudyPageLinkItemWriterIntegrationTest
    extends AbstractItemWriterIntegrationTest<PublicNewStudyPageLink, NewStudyPageLinkItemWriter> {

    private static final String LANG_CODE      = "de";
    private static final int    SORT_EXISTING  = 1;
    private static final int    SORT_NEW       = 20;
    private static final String TITLE          = "foo";
    private static final String URL            = "http://a.b";
    private static final String TITLE_EXISTING = "Web Suche";

    private PublicNewStudyPageLink newNewStudyPageLink, existingNewStudyPageLink;

    @Override
    protected NewStudyPageLinkItemWriter newWriter() {
        return new NewStudyPageLinkItemWriter(dsl);
    }

    @Override
    public void setUpEntities() {
        newNewStudyPageLink = newNewStudyPageLink(LANG_CODE, SORT_NEW, TITLE, URL);

        existingNewStudyPageLink = getExistingNewStudyPageLinkFromDb(LANG_CODE, SORT_EXISTING);
        assertThat(existingNewStudyPageLink.getTitle()).isEqualTo(TITLE_EXISTING);
        existingNewStudyPageLink.setTitle(TITLE_EXISTING + "XX");
    }

    private PublicNewStudyPageLink newNewStudyPageLink(String langCode, int sort, String title, String url) {
        return PublicNewStudyPageLink
            .builder()
            .langCode(langCode)
            .sort(sort)
            .title(title)
            .url(url)
            .lastSynched(Timestamp.valueOf(LocalDateTime.now()))
            .build();
    }

    private PublicNewStudyPageLink getExistingNewStudyPageLinkFromDb(String langCode, int sort) {
        final NewStudyPageLinkRecord r = dsl
            .selectFrom(NEW_STUDY_PAGE_LINK)
            .where(NEW_STUDY_PAGE_LINK.LANG_CODE
                .eq(langCode)
                .and(NEW_STUDY_PAGE_LINK.SORT.eq(sort)))
            .fetchOne();
        return PublicNewStudyPageLink
            .builder()
            .langCode(r.getLangCode())
            .sort(r.getSort())
            .title(r.getTitle())
            .url(r.getUrl())
            .lastSynched(r.getLastSynched())
            .build();
    }

    @AfterEach
    void tearDown() {
        dsl
            .deleteFrom(NEW_STUDY_PAGE_LINK)
            .where(NEW_STUDY_PAGE_LINK.LANG_CODE
                .eq(LANG_CODE)
                .and(NEW_STUDY_PAGE_LINK.SORT.eq(SORT_NEW)))
            .execute();
        dsl
            .update(NEW_STUDY_PAGE_LINK)
            .set(NEW_STUDY_PAGE_LINK.TITLE, TITLE_EXISTING)
            .where(NEW_STUDY_PAGE_LINK.LANG_CODE
                .eq(LANG_CODE)
                .and(NEW_STUDY_PAGE_LINK.SORT.eq(SORT_EXISTING)))
            .execute();
    }

    @Test
    void insertingNewNewStudyPageLink_succeeds() {
        String langCode = newNewStudyPageLink.getLangCode();
        int sort = newNewStudyPageLink.getSort();
        assertNewStudyPageLinkDoesNotExistWith(langCode, sort);
        assertThat(getWriter().executeUpdate(newNewStudyPageLink)).isEqualTo(1);
        assertNewStudyPageLinkExistsWith(langCode, sort);
    }

    private void assertNewStudyPageLinkExistsWith(String langCode, int sort) {
        assertRecordCountForId(langCode, sort, 1);
    }

    private void assertNewStudyPageLinkDoesNotExistWith(String langCode, int sort) {
        assertRecordCountForId(langCode, sort, 0);
    }

    private void assertRecordCountForId(String langCode, int sort, int size) {
        assertThat(dsl
            .selectOne()
            .from(NEW_STUDY_PAGE_LINK)
            .where(NEW_STUDY_PAGE_LINK.LANG_CODE.eq(langCode))
            .and(NEW_STUDY_PAGE_LINK.SORT.eq(sort))
            .fetch()).hasSize(size);
    }

    @Test
    void updatingExistingNewStudyPageLink_succeeds() {
        assertThat(getWriter().executeUpdate(existingNewStudyPageLink)).isEqualTo(1);
    }

}
