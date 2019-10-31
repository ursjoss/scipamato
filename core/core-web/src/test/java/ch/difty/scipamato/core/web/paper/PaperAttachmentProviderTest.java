package ch.difty.scipamato.core.web.paper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.core.entity.PaperAttachment;

@ExtendWith(MockitoExtension.class)
class PaperAttachmentProviderTest {

    private PaperAttachmentProvider provider;

    @Mock
    private PaperAttachment mockAttachment1, mockAttachment2, mockAttachment3, mockAttachment4;

    private final List<PaperAttachment> attachments = new ArrayList<>();

    @BeforeEach
    void setUp() {
        attachments.addAll(Arrays.asList(mockAttachment1, mockAttachment2, mockAttachment3, mockAttachment4));
        provider = new PaperAttachmentProvider(Model.ofList(attachments));
    }

    @Test
    void degenerateConstruction_withNullSearchOrderModel1() {
        Assertions.assertThrows(NullPointerException.class, () -> new PaperAttachmentProvider(Model.ofList(null)));
    }

    @Test
    void providerSize_equals_conditionSize() {
        assertThat(provider.size()).isEqualTo(attachments.size());
    }

    @Test
    void iterator_fromStartWithPageSizeLargerThanActualSize_returnsAll() {
        assertThat(provider.iterator(0, 100))
            .toIterable()
            .containsExactly(mockAttachment1, mockAttachment2, mockAttachment3, mockAttachment4);
    }

    @Test
    void iterator_fromStartWithLimitingPageSize_returnsPageFullFromStart() {
        assertThat(provider.iterator(0, 2))
            .toIterable()
            .containsExactly(mockAttachment1, mockAttachment2);
    }

    @Test
    void iterator_fromIndex1WithLimitingPageSize_returnsPageFullFromIndex() {
        assertThat(provider.iterator(1, 2))
            .toIterable()
            .containsExactly(mockAttachment2, mockAttachment3);
    }

    @Test
    void gettingModel() {
        IModel<PaperAttachment> model = provider.model(mockAttachment1);
        assertThat(model.getObject()).isEqualTo(mockAttachment1);
    }
}
