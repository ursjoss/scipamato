package ch.difty.scipamato.core.sync.jobs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.core.sync.jobs.paper.PublicPaper;

@RunWith(MockitoJUnitRunner.class)
public class ScipamatoItemWriterTest {

    @Mock
    private DSLContext dslContext;

    @Test
    public void test() {
        final int[] i = { 0 };
        final ScipamatoItemWriter<PublicPaper> writer = new ScipamatoItemWriter<PublicPaper>(dslContext, "topic") {
            @Override
            protected int executeUpdate(final PublicPaper pp) {
                i[0] += pp.getPmId();
                return pp.getPmId();
            }
        };

        final List<PublicPaper> papers = new ArrayList<>();
        papers.add(PublicPaper
            .builder()
            .pmId(10)
            .build());
        papers.add(PublicPaper
            .builder()
            .pmId(20)
            .build());

        writer.write(papers);

        assertThat(i[0]).isEqualTo(30);
    }
}