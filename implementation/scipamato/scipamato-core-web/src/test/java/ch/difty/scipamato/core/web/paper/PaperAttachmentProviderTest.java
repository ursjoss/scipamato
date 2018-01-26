package ch.difty.scipamato.core.web.paper;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.core.entity.PaperAttachment;

@RunWith(MockitoJUnitRunner.class)
public class PaperAttachmentProviderTest {

    private PaperAttachmentProvider provider;

    @Mock
    private PaperAttachment mockAttachment1, mockAttachment2, mockAttachment3, mockAttachment4;

    private final List<PaperAttachment> attachments = new ArrayList<>();

    @Before
    public void setUp() {
        attachments.addAll(Arrays.asList(mockAttachment1, mockAttachment2, mockAttachment3, mockAttachment4));
        provider = new PaperAttachmentProvider(Model.ofList(attachments));
    }

    @Test
    public void degenerateConstruction_withNullSearchOrderModel() {
        assertDegenerateSupplierParameter(() -> new PaperAttachmentProvider(null), "attachmentsModel");
    }

    @Test
    public void degenerateConstruction_withNullSearchOrderModel1() {
        List<PaperAttachment> nullList = null;
        assertDegenerateSupplierParameter(() -> new PaperAttachmentProvider(Model.ofList(nullList)), "attachments");
    }

    @Test
    public void providerSize_equals_conditionSize() {
        assertThat(provider.size()).isEqualTo(attachments.size());
    }

    @Test
    public void iterator_fromStartWithPageSizeLargerThanActualSize_returnsAll() {
        assertThat(provider.iterator(0, 100)).containsExactly(mockAttachment1, mockAttachment2, mockAttachment3,
            mockAttachment4);
    }

    @Test
    public void iterator_fromStartWithLimitingPageSize_returnsPageFullFromStart() {
        assertThat(provider.iterator(0, 2)).containsExactly(mockAttachment1, mockAttachment2);
    }

    @Test
    public void iterator_fromIndex1WithLimitingPageSize_returnsPageFullFromIndex() {
        assertThat(provider.iterator(1, 2)).containsExactly(mockAttachment2, mockAttachment3);
    }

    @Test
    public void gettingModel() {
        IModel<PaperAttachment> model = provider.model(mockAttachment1);
        assertThat(model.getObject()).isEqualTo(mockAttachment1);
    }

}
