package ch.difty.scipamato.publ.web.newstudies;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.publ.web.PageParameters;
import ch.difty.scipamato.publ.web.common.BasePage;
import ch.difty.scipamato.publ.web.paper.browse.PublicPaperDetailPage;

@MountPath("new-studies")
public class NewStudyListPage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    public NewStudyListPage(org.apache.wicket.request.mapper.parameter.PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(newLabel("h1Title"));

        queue(newLabel("introParagraph"));
        queue(newLink("dbLink", "http://ludok.swisstph.ch"));

        queue(newNewStudyCollection("newStudyCollection"));
    }

    private Label newLabel(String id) {
        return new Label(id, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null).getString());
    }

    private ExternalLink newLink(String id, String href) {
        return new ExternalLink(id, href, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null).getString());
    }

    private ListView<NewStudyTopic> newNewStudyCollection(String string) {
        return new ListView<NewStudyTopic>("topics", getNewStudyTopics()) {
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

    /**
     * Currently stubbed data from August 2017, will later come through service
     * (TODO)
     */
    private List<NewStudyTopic> getNewStudyTopics() {
        int topicIndex = 0;
        int studyIndex = 0;
        List<NewStudyTopic> topics = new ArrayList<>();
        List<NewStudy> newStudies1 = new ArrayList<>();
        newStudies1.add(new NewStudy(studyIndex++, 8924, "(Di et al.; 2017)",
                "USA: Grosse Kohortenstudie zeigt, dass auch ein PM2.5-Grenzwert von 12 µg/m3 oder 10 µg/m3 die Bevölkerung nicht schützt.",
                "Registerkohortenstudie in den USA zur Untersuchung, ob die Sterblichkeit mit der langfristigen Feinstaub- oder Ozonbelastung auch unterhalb der Grenzwerte zusammenhängen und ob es empfindliche Gruppen gäbe."));
        newStudies1.add(new NewStudy(studyIndex++, 8993, "(Makar et al.; 2017)",
                "USA: Das Risiko für Spitaleintritte war mit der langfristigen Feinstaubbelastung zwischen 8-12µg PM2.5/m3 höher als für Belastungen oberhalb des US-Grenzwerts.",
                "Kohortenstudie mit älteren Personen in den USA zur Untersuchung, ob die Sterblichkeit oder Spitaleintritte kausal mit der langsfristigen Feinstaubbelastung auch unterhalb des US-Grenzwerts zusammenhängen."));
        topics.add(new NewStudyTopic(topicIndex++, "Tiefe Belastungen", newStudies1));
        List<NewStudy> newStudies2 = new ArrayList<>();
        studyIndex = 0;
        newStudies2.add(new NewStudy(studyIndex++, 8973, "(Tallon et al.; 2017)",
                "USA: Die langfristige Feinstaub- und NO2-Belastung war mit einer beschleunigten Abnahme der Hirnleistung verbunden.",
                "Querschnittstudie an älteren Personen zur Untersuchung, ob die Hirnfunktion mit der Luftbelastung zusammenhänge und ob Krankheiten oder die Gemütslage den Zusammenhang beeinflussen."));
        newStudies2.add(new NewStudy(studyIndex++, 8983, "(Jayaraj et al.; 2017)",
                "Übersicht: Aufgrund epidemiologischer und klinischer Studien wird ein Zusammenhang zwischen neurodegenerativen Krankheiten und der kognitiven Funktion und der Schadstoffbelastung immer wahrscheinlicher.",
                "Übersicht zum Zusammenhang zwischen neurodegenerativen Krankheiten und der langfristigen Schadstoffbelastung und deren Wirkungsmechanismen."));
        newStudies2.add(new NewStudy(studyIndex++, 8984, "(Chen et al.; 2017)",
                "Kanada: Grosse Kohortenstudie findet Zusammenhang mit Demenz in Abhängigkeit von Wohnen in Strassennähe.",
                "Kanadische registerbasierte Kohortenstudie zur Untersuchung, ob Demenz, Parkinson oder Multiple Sklerose mit Wohnen in Strassennähe zusammenhängen."));
        topics.add(new NewStudyTopic(topicIndex++, "Hirnleistung und neurodegenerative Erkrankungen", newStudies2));
        List<NewStudy> newStudies3 = new ArrayList<>();
        studyIndex = 0;
        newStudies3.add(new NewStudy(studyIndex++, 8933, "(Guerreiro et al.; 2017)",
                "Europa: Der europäische Zielwert für Benzo(a)pyren wird in europäischen Städten um das Zwei- bis Dreifache überschritten und verursacht bis zu 450 zusätzliche Lungenkrebsfälle.",
                "Abschätzung der Benzoapyren-Belastung (PAK) in Europa im Jahr 2012 zur Untersuchung der Gesundheitsfolgen."));
        newStudies3.add(new NewStudy(studyIndex++, 8897, "(Rückerl et al.; 2017)",
                "Deutschland: Die Längenkonzentration und aktive Oberfläche ultrafeiner Partikel könnten bessere UFP-Parameter sein als beispielsweise die Partikelzahl.",
                "Panelstudie in Augsburg an Personen mit Diabetes oder einer genetischen Veranlagung zu verminderter antioxidativer Abwehr zur Untersuchung, ob Blutindikatoren der Entzündung und Gerinnung mit verschiedenen neuartigen Feinstaubparametern zusammenhängen."));
        newStudies3.add(new NewStudy(studyIndex++, 8861, "(Krall et al.; 2017)",
                "USA: Notfallmässige Spitaleintritte hingen mit der Feinstaubbelastung insgesamt und jener aus der Biomasseverbrennung zusammen.",
                "Zeitreihenstudie in vier US-Städten zur Untersuchung, ob Notfalleintritte wegen Atemwegserkrankungen mit der Feinstaubbelastung aus unterschiedlichen Quellen zusammenhängen."));
        topics.add(new NewStudyTopic(topicIndex++, "Feinstaubkomponenten und PAK", newStudies3));
        List<NewStudy> newStudies4 = new ArrayList<>();
        studyIndex = 0;
        newStudies4.add(new NewStudy(studyIndex++, 8916, "(Qiu et al.; 2017)",
                "China: Die langfristige Feinstaubbelastung erhöhte das Risiko für ischämische Hirnschläge, nicht aber für hämorrhagische Hirnschläge.",
                "Kohortenstudie an älteren Personen zur Untersuchung, ob die Inzidenz an Hirnschlägen mit der langfristigen Feinstaubbelastung zusammenhänge."));
        newStudies4.add(new NewStudy(studyIndex++, 8934, "(Desikan et al.; 2017)",
                "Grossbritannien: Hirnschlagpatienten - insbesondere solche mit ischämischem Hirnschlag - überlebten weniger lang, wenn sie höheren Feinstaubbelastungen ausgesetzt waren.",
                "Registerstudie im südlichen London zur Untersuchung, ob die Überlebenswahrscheinlichkeit nach Hirnschlag mit der langfristigen verkehrsbedingten Feinstaubbelastung und seiner Quellen (Abgas, Nicht-Abgas) zusammenhängt."));
        topics.add(new NewStudyTopic(topicIndex++, "Hirnschlag", newStudies4));
        return topics;
    }

}
