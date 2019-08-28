package ch.difty.scipamato.core.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import ch.difty.scipamato.TestApplication;
import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.core.logic.parsing.AuthorParserFactory;
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy;
import ch.difty.scipamato.core.logic.parsing.DefaultAuthorParserFactory;
import ch.difty.scipamato.core.persistence.*;
import ch.difty.scipamato.core.pubmed.PubmedArticleService;
import ch.difty.scipamato.core.pubmed.PubmedImporter;

@SpringBootTest
@ActiveProfiles("wickettest")
public abstract class AbstractWicketTest {

    @Bean
    public AuthorParserFactory authorParserFactory() {
        return new DefaultAuthorParserFactory(AuthorParserStrategy.PUBMED);
    }

    @Autowired
    protected TestApplication    application;
    @Autowired
    protected ApplicationContext applicationContextMock;
    @Autowired
    protected DateTimeService    dateTimeService;

    @MockBean
    protected ScipamatoWebSessionFacade sessionFacadeMock;
    @MockBean
    protected ItemNavigator<Long>       itemNavigatorMock;
    @MockBean
    protected PubmedImporter            pubmedImporterMock;

    @MockBean
    protected PubmedArticleService   pubmedArticleServiceMock;
    @MockBean
    protected CodeService            codeServiceMock;
    @MockBean
    protected CodeClassService       codeClassServiceMock;
    @MockBean
    protected KeywordService         keywordServiceMock;
    @MockBean
    protected NewsletterService      newsletterServiceMock;
    @MockBean
    protected NewsletterTopicService newsletterTopicServiceMock;
    @MockBean
    protected PaperService           paperServiceMock;
    @MockBean
    protected PaperSlimService       paperSlimServiceMock;
    @MockBean
    protected SearchOrderService     searchOrderServiceMock;
    @MockBean
    protected UserService            userServiceMock;

}
