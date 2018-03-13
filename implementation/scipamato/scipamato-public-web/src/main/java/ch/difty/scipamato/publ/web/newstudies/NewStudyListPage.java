package ch.difty.scipamato.publ.web.newstudies;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.publ.entity.NewStudy;
import ch.difty.scipamato.publ.entity.NewStudyTopic;
import ch.difty.scipamato.publ.persistence.api.NewStudyTopicService;
import ch.difty.scipamato.publ.web.PageParameters;
import ch.difty.scipamato.publ.web.common.BasePage;
import ch.difty.scipamato.publ.web.paper.browse.PublicPaperDetailPage;

@MountPath("new-studies")
public class NewStudyListPage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private NewStudyTopicService newStudyTopicService;

    public NewStudyListPage(org.apache.wicket.request.mapper.parameter.PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(newLabel("h1Title"));

        queue(newLabel("introParagraph"));
        queue(newLink("dbLink", "http://ludok.swisstph.ch"));

        queue(newNewStudyCollection("topics"));
    }

    private Label newLabel(String id) {
        return new Label(id, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null).getString());
    }

    private ExternalLink newLink(String id, String href) {
        return new ExternalLink(id, href, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null).getString());
    }

    private ListView<NewStudyTopic> newNewStudyCollection(String id) {
        return new ListView<NewStudyTopic>(id, newStudyTopicService.findMostRecentNewStudyTopics()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<NewStudyTopic> topic) {
                topic.add(new Label("topicTitle", new PropertyModel<String>(topic.getModel(), "title")));
                topic.add(new ListView<NewStudy>("topicStudies", topic.getModelObject()
                    .getStudies()) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void populateItem(ListItem<NewStudy> study) {
                        study.add(new Label("headline", new PropertyModel<String>(study.getModel(), "headline")));
                        study.add(new Label("description", new PropertyModel<String>(study.getModel(), "description")));
                        study.add(newLinkToStudy("reference", study));
                    }
                });
            }
        };
    }

    private Link<NewStudy> newLinkToStudy(String id, ListItem<NewStudy> study) {
        org.apache.wicket.request.mapper.parameter.PageParameters pp = new org.apache.wicket.request.mapper.parameter.PageParameters();
        pp.set(PageParameters.NUMBER.getName(), study.getModelObject()
            .getNumber());
        Link<NewStudy> link = new BookmarkablePageLink<>(id, PublicPaperDetailPage.class, pp);
        link.add(new Label("referenceLabel", new PropertyModel<String>(study.getModel(), "reference")));
        return link;
    }

}
