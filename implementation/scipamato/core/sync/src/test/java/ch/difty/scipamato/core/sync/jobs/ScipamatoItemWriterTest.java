package ch.difty.scipamato.core.sync.jobs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.core.sync.jobs.paper.PublicPaper;

@ExtendWith(MockitoExtension.class)
class ScipamatoItemWriterTest {

    private int tracker = 0;

    @Mock
    private DSLContext dslContext;

    private final List<PublicPaper> papers = new ArrayList<>();

    private final PublicPaper p1 = PublicPaper
        .builder()
        .pmId(1)
        .build();
    private final PublicPaper p2 = PublicPaper
        .builder()
        .pmId(10)
        .build();

    private ScipamatoItemWriter<PublicPaper> writer;

    @BeforeEach
    void setUp() {
        writer = new ScipamatoItemWriter<>(dslContext, "topic") {
            @Override
            protected int executeUpdate(final PublicPaper pp) {
                tracker += pp.getPmId();
                return pp.getPmId();
            }
        };
    }

    @Test
    void writingTwoPapers() {
        papers.add(p1);
        papers.add(p2);

        writer.write(papers);

        assertThat(tracker).isEqualTo(11);
    }

    @Test
    void writingOnePaper() {
        papers.add(p1);

        writer.write(papers);

        assertThat(tracker).isEqualTo(1);
    }
}