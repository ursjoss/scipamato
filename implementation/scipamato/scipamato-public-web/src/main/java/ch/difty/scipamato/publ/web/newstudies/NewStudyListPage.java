package ch.difty.scipamato.publ.web.newstudies;

import static java.util.stream.Collectors.toList;

import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.publ.entity.NewStudy;
import ch.difty.scipamato.publ.entity.NewStudyPageLink;
import ch.difty.scipamato.publ.entity.NewStudyTopic;
import ch.difty.scipamato.publ.entity.Newsletter;
import ch.difty.scipamato.publ.persistence.api.NewStudyTopicService;
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider;
import ch.difty.scipamato.publ.web.PublicPageParameters;
import ch.difty.scipamato.publ.web.common.BasePage;
import ch.difty.scipamato.publ.web.paper.browse.PublicPaperDetailPage;

/**
 * The page lists 'new studies', i.e. studies that were collected and flagged by
 * the SciPaMaTo-team as eligible for this page. By default, the newest
 * collection of new studies is presented.
 * <p>
 * With the use of page-parameters, an older collection of new studies can be
 * selected instead.
 * <p>
 * The page is typically shown in an iframe of a CMS.
 *
 * @author Urs Joss
 */
@MountPath("new-studies")
@Slf4j
public class NewStudyListPage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    private static final String TARGET = "target";
    private static final String BLANK  = "_blank";
    private static final String LABEL  = "Label";

    @SpringBean
    private NewStudyTopicService newStudyTopicService;

    @SpringBean(name = "simplonFontResourceProvider")
    private CommercialFontResourceProvider simplonFontResourceProvider;

    public NewStudyListPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void renderAdditionalCommercialFonts(final IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(simplonFontResourceProvider.getCssResourceReference()));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(
            CssHeaderItem.forReference(new CssResourceReference(NewStudyListPage.class, "NewStudyListPage.css")));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        newIntroSection();
        newNewsletterSection();

        newExternalLinkSection();
        newArchiveSectionWithPreviousNewsletters();
    }

    /**
     * Introductory paragraph
     */
    private void newIntroSection() {
        queue(newLabel("h1Title"));
        queue(newLabel("introParagraph"));
        queue(newDbSearchLink("dbLink", getProperties().getCmsUrlSearchPage()));
    }

    private ExternalLink newDbSearchLink(final String id, final String href) {
        return new ExternalLink(id, href, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null).getString()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put(TARGET, BLANK);
            }
        };
    }

    /**
     * The actual newsletter/new study list part with topics and nested studies
     */
    private void newNewsletterSection() {
        queue(newNewStudyCollection("topics"));
    }

    private ListView<NewStudyTopic> newNewStudyCollection(final String id) {
        final List<NewStudyTopic> topics = retrieveStudyCollection();

        getPaperIdManager().initialize(extractPaperNumbersFrom(topics));

        return new ListView<>(id, topics) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<NewStudyTopic> topic) {
                topic.add(new Label("topicTitle",
                    new PropertyModel<String>(topic.getModel(), NewStudyTopic.NewStudyTopicFields.TITLE.getName())));
                topic.add(new ListView<NewStudy>("topicStudies", topic
                    .getModelObject()
                    .getStudies()) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void populateItem(ListItem<NewStudy> study) {
                        study.add(new Label("headline",
                            new PropertyModel<String>(study.getModel(), NewStudy.NewStudyFields.HEADLINE.getName())));
                        study.add(new Label("description", new PropertyModel<String>(study.getModel(),
                            NewStudy.NewStudyFields.DESCRIPTION.getName())));
                        study.add(newLinkToStudy("reference", study));
                    }
                });
            }
        };
    }

    private List<NewStudyTopic> retrieveStudyCollection() {
        final StringValue issue = getPageParameters().get(PublicPageParameters.ISSUE.getName());
        if (issue == null || issue.isNull() || issue.isEmpty())
            return newStudyTopicService.findMostRecentNewStudyTopics(getLanguageCode());
        else
            return newStudyTopicService.findNewStudyTopicsForNewsletterIssue(issue.toString(), getLanguageCode());
    }

    private List<? extends Long> extractPaperNumbersFrom(final List<NewStudyTopic> topics) {
        return topics
            .stream()
            .map(NewStudyTopic::getStudies)
            .flatMap(ns -> ns.stream())
            .map(NewStudy::getNumber)
            .collect(toList());
    }

    /**
     * Link pointing to the study detail page with the current study
     *
     * @param id
     *     the id on the html page. Also expects a label with the same id +
     *     tag 'Label'
     * @param study
     *     the current study as ListItem
     * @return the link
     */
    private Link<NewStudy> newLinkToStudy(final String id, final ListItem<NewStudy> study) {
        PageParameters pp = new PageParameters();
        pp.set(PublicPageParameters.NUMBER.getName(), study
            .getModelObject()
            .getNumber());
        Link<NewStudy> link = new Link<NewStudy>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                getPaperIdManager().setFocusToItem(study
                    .getModelObject()
                    .getNumber());
                setResponsePage(new PublicPaperDetailPage(pp, getPageReference()));
            }
        };
        link.add(new Label(id + LABEL,
            new PropertyModel<String>(study.getModel(), NewStudy.NewStudyFields.REFERENCE.getName())));
        return link;
    }

    /**
     * Any links configured in database table new_study_page_links
     * will be published in this section.
     */
    private void newExternalLinkSection() {
        queue(newLinkList("links"));
    }

    private ListView<NewStudyPageLink> newLinkList(final String id) {
        final List<NewStudyPageLink> links = newStudyTopicService.findNewStudyPageLinks(getLanguageCode());
        return new ListView<>(id, links) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<NewStudyPageLink> link) {
                link.add(newExternalLink("link", link));
            }
        };
    }

    private BootstrapExternalLink newExternalLink(final String id, final ListItem<NewStudyPageLink> linkItem) {
        final IModel<String> href = Model.of(linkItem
            .getModelObject()
            .getUrl());
        final BootstrapExternalLink link = new BootstrapExternalLink(id, href) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put(TARGET, BLANK);
            }
        };
        link.setLabel(Model.of(linkItem
            .getModelObject()
            .getTitle()));
        link.add(new Label(id + LABEL, Model.of(linkItem
            .getModelObject()
            .getTitle())));
        return link;
    }

    /**
     * The archive section lists links pointing to previous newsletters with their studies.
     */
    private void newArchiveSectionWithPreviousNewsletters() {
        queue(newLabel("h2ArchiveTitle"));
        queue(newNewsletterArchive("archive"));
    }

    private Label newLabel(final String id) {
        return new Label(id, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null).getString());
    }

    private ListView<Newsletter> newNewsletterArchive(final String id) {
        final int newsletterCount = getProperties().getNumberOfPreviousNewslettersInArchive();
        final List<Newsletter> newsletters = newStudyTopicService.findArchivedNewsletters(newsletterCount,
            getLanguageCode());
        return new ListView<>(id, newsletters) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Newsletter> nl) {
                nl.add(newLinkToArchivedNewsletter("monthName", nl));
            }
        };
    }

    private Link<Newsletter> newLinkToArchivedNewsletter(final String id, final ListItem<Newsletter> newsletter) {
        final PageParameters pp = new PageParameters();
        pp.set(PublicPageParameters.ISSUE.getName(), newsletter
            .getModelObject()
            .getIssue());
        final Link<Newsletter> link = new Link<>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(new NewStudyListPage(pp));
            }
        };
        link.add(new Label(id + LABEL, newsletter
            .getModelObject()
            .getMonthName(getLanguageCode())));
        return link;
    }

}
