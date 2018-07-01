package ch.difty.scipamato.core.web.sync;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaBehavior.Effect;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.core.sync.launcher.SyncJobLauncher;
import ch.difty.scipamato.core.sync.launcher.SyncJobResult;
import ch.difty.scipamato.core.web.common.BasePage;

public class RefDataSyncPage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private SyncJobLauncher jobLauncher;

    public RefDataSyncPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        queue(new BootstrapForm<Void>("synchForm"));
        queue(newButton("synchronize"));
    }

    private BootstrapAjaxButton newButton(String id) {
        StringResourceModel labelModel = new StringResourceModel("button." + id + ".label", this, null);
        return new LaddaAjaxButton(id, labelModel, Type.Primary) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                SyncJobResult result = jobLauncher.launch();
                if (result.isSuccessful()) {
                    info(new StringResourceModel("feedback.msg.success", this, null).getString());
                } else {
                    error(new StringResourceModel("feedback.msg.failed", this, null).getString());
                }
                for (String msg : result.getMessages()) {
                    info(msg);
                }
                target.add(getFeedbackPanel());
            }
        }.setEffect(Effect.ZOOM_IN);
    }

}
