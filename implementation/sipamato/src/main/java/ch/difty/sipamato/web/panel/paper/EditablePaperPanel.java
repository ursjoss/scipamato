package ch.difty.sipamato.web.panel.paper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.logic.parsing.AuthorParser;
import ch.difty.sipamato.logic.parsing.AuthorParserFactory;
import ch.difty.sipamato.web.jasper.SipamatoPdfExporterConfiguration;
import ch.difty.sipamato.web.jasper.summary_sp.PaperSummaryDataSource;
import ch.difty.sipamato.web.pages.Mode;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;

public abstract class EditablePaperPanel extends PaperPanel<Paper> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private AuthorParserFactory authorParserFactory;

    public EditablePaperPanel(String id) {
        super(id, null, Mode.EDIT);
    }

    public EditablePaperPanel(String id, IModel<Paper> model) {
        super(id, model, Mode.EDIT);
    }

    public EditablePaperPanel(String id, IModel<Paper> model, Mode ignoredMode) {
        super(id, model, Mode.EDIT);
    }

    @Override
    protected TextField<String> makeFirstAuthor(String firstAuthorId, CheckBox firstAuthorOverridden) {
        TextField<String> firstAuthor = new TextField<String>(firstAuthorId) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(firstAuthorOverridden.getModelObject());
            }
        };
        firstAuthor.add(new PropertyValidator<String>());
        return firstAuthor;
    }

    @Override
    protected PaperSummaryDataSource getSummaryDataSource() {
        String populationLabel = new StringResourceModel("population" + LABEL_RECOURCE_TAG, this, null).getString();
        String methodsLabel = new StringResourceModel("methods" + LABEL_RECOURCE_TAG, this, null).getString();
        String resultLabel = new StringResourceModel("result" + LABEL_RECOURCE_TAG, this, null).getString();
        String brand = getProperties().getBrand();
        String headerPart = brand + "-" + new StringResourceModel("headerPart", this, null).getString();

        // TODO consider setting Full name of creator into Paper too - if so use here
        String author = getModelObject().getCreatedByName();
        String paperTitle = getModelObject().getTitle();
        String subject = getModelObject().getGoals();
        List<Code> codes = getModelObject().getCodes();
        SipamatoPdfExporterConfiguration config = new SipamatoPdfExporterConfiguration.Builder(headerPart, getModelObject().getId()).withCreator(brand)
                .withPaperTitle(paperTitle)
                .withSubject(subject)
                .withAuthor(author)
                .withCodes(codes)
                .withCompression()
                .build();
        return new PaperSummaryDataSource(getModelObject(), populationLabel, methodsLabel, resultLabel, headerPart, brand, config);
    }

    @Override
    protected void addAuthorBehavior(TextArea<String> authors, CheckBox firstAuthorOverridden, TextField<String> firstAuthor) {
        firstAuthorOverridden.add(makeFirstAuthorChangeBehavior(authors, firstAuthorOverridden, firstAuthor));
        authors.add(makeFirstAuthorChangeBehavior(authors, firstAuthorOverridden, firstAuthor));
    }

    /*
     * Behavior to parse the authors string and populate the firstAuthor field - but only if not overridden. 
     */
    private OnChangeAjaxBehavior makeFirstAuthorChangeBehavior(TextArea<String> authors, CheckBox overridden, TextField<String> firstAuthor) {
        return new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (!overridden.getModelObject()) {
                    AuthorParser p = authorParserFactory.createParser(authors.getValue());
                    firstAuthor.setModelObject(p.getFirstAuthor().orElse(null));
                }
                target.add(firstAuthor);
            }
        };
    }

    @Override
    protected void addCodeClass1ChangeBehavior(final TextField<String> mainCodeOfCodeClass1, final BootstrapMultiSelect<Code> codeClass1) {
        codeClass1.add(makeCodeClass1ChangeBehavior(codeClass1, mainCodeOfCodeClass1));
    }

    private OnChangeAjaxBehavior makeCodeClass1ChangeBehavior(final BootstrapMultiSelect<Code> codeClass1, final TextField<String> mainCodeOfCodeClass1) {
        return new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                if (codeClass1 != null && codeClass1.getModelObject() != null) {
                    final Collection<Code> codesOfClass1 = codeClass1.getModelObject();
                    switch (codesOfClass1.size()) {
                    case 0:
                        setMainCodeOfClass1(null, mainCodeOfCodeClass1, target);
                        break;
                    case 1:
                        setMainCodeOfClass1(codesOfClass1.iterator().next().getCode(), mainCodeOfCodeClass1, target);
                        break;
                    default:
                        ensureMainCodeIsPartOfCodes(codesOfClass1, mainCodeOfCodeClass1, target);
                        break;
                    }
                }
            }

            private void setMainCodeOfClass1(final String code, final TextField<String> mainCodeOfCodeClass1, final AjaxRequestTarget target) {
                mainCodeOfCodeClass1.setModelObject(code);
                target.add(mainCodeOfCodeClass1);
            }

            private void ensureMainCodeIsPartOfCodes(Collection<Code> codesOfClass1, TextField<String> mainCodeOfCodeClass1, AjaxRequestTarget target) {
                final Optional<String> main = Optional.ofNullable(mainCodeOfCodeClass1.getModelObject());
                final Optional<String> match = codesOfClass1.stream().map(c -> c.getCode()).filter(c -> main.isPresent() && main.get().equals(c)).findFirst();
                if (main.isPresent() && !match.isPresent()) {
                    mainCodeOfCodeClass1.setModelObject(null);
                    target.add(mainCodeOfCodeClass1);
                }
            }

        };
    }

}
