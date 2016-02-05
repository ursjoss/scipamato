package ch.difty.sipamato;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

public class PaperEntryPage extends BasePage {

    private static final long serialVersionUID = 1L;

    private Model<Paper> model = Model.of(new Paper());

    protected void onInitialize() {
        super.onInitialize();

        Form<Paper> form = new Form<Paper>("form") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                System.out.println("form submitted. Paper:");
                System.out.println(model.getObject());
            }
        };
        add(form);

        form.add(new Label("authorLabel", new StringResourceModel("author.label", this, null)));
        TextField<String> authorField = new TextField<String>(Paper.AUTHOR, PropertyModel.of(model, Paper.AUTHOR));
        form.add(authorField);

        form.add(new Label("firstAuthorLabel", new StringResourceModel("firstAuthor.label", this, null)));
        TextField<String> firstAuthorField = new TextField<String>(Paper.FIRST_AUTHOR, PropertyModel.of(model, Paper.FIRST_AUTHOR));
        firstAuthorField.setEnabled(false);
        firstAuthorField.setOutputMarkupId(true);
        form.add(firstAuthorField);

        authorField.add(new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(firstAuthorField);
            }
        });

        form.add(new Label("titleLabel", new StringResourceModel("title.label", this, null)));
        TextField<String> titleField = new TextField<String>(Paper.TITLE, PropertyModel.of(model, Paper.TITLE));
        form.add(titleField);

        form.add(new Label("locationLabel", new StringResourceModel("location.label", this, null)));
        TextField<String> locationField = new TextField<String>(Paper.LOCATION, PropertyModel.of(model, Paper.LOCATION));
        form.add(locationField);
    };
}
