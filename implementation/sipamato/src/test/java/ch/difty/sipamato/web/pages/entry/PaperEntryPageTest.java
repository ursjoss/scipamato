package ch.difty.sipamato.web.pages.entry;

import org.apache.wicket.markup.html.form.Form;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.web.pages.AbstractPageTest;

public class PaperEntryPageTest extends AbstractPageTest<PaperEntryPage> {

    @Override
    protected Class<PaperEntryPage> getPageClass() {
        return PaperEntryPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);

        assertLabeledTextArea(b, Paper.AUTHOR);
        assertLabeledTextField(b, Paper.FIRST_AUTHOR);
        assertLabeledTextArea(b, Paper.TITLE);
        assertLabeledTextField(b, Paper.LOCATION);
    }
}
