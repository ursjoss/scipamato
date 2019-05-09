package ch.difty.scipamato.core.entity.newsletter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.*;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;
import ch.difty.scipamato.core.entity.projection.PaperSlim;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Newsletter extends IdScipamatoEntity<Integer> {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String            issue;
    private LocalDate         issueDate;
    @NotNull
    private PublicationStatus publicationStatus;

    /**
     * papers by newsletter topic.
     * With {@link NewsletterTopic} as keys and a list of {@link PaperSlim} as values.
     */
    private final Map<NewsletterTopic, List<PaperSlim>> papersByTopic = new HashMap<>();

    public enum NewsletterFields implements FieldEnumType {
        ID("id"),
        ISSUE("issue"),
        ISSUE_DATE("issueDate"),
        PUBLICATION_STATUS("publicationStatus"),
        PAPERS("papers"),
        TOPICS("topics");

        private final String name;

        NewsletterFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    @Override
    public String getDisplayValue() {
        return getIssue();
    }

    /**
     * @return true if the newsletter may be deleted
     */
    public boolean isDeletable() {
        return publicationStatus.isInProgress();
    }

    /**
     * Adds a paper to the newsletter for the given topic - or updates an existing
     * association. If the paper had been added previously with a different topic,
     * it will be reassigned to the new topic.
     *
     * @param paper
     *     the paper (as {@link PaperSlim} to add or update its association with the newspaper.
     *     Neither paper nor its id may be null.
     * @param topic
     *     the topic of the new association, may be null indicating 'no topic'
     */
    public void addPaper(final PaperSlim paper, final NewsletterTopic topic) {
        AssertAs.notNull(paper, "paper");
        AssertAs.notNull(paper.getId(), "paper.id");
        if (papersByTopic
            .values()
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList())
            .contains(paper))
            removePaperIfOnDifferentTopic(topic, paper);
        addPaperToTopic(topic, paper);
        cleanseEmptyTopics();
    }

    /**
     * Remove the paper from any topic except for the provided topic to keep.
     *
     * @param topicToKeep
     *     the topic from which we would not want to remove the paper from.
     * @param paper
     *     the paper to remove from any other topic than the provided topicToKeep.
     */
    private void removePaperIfOnDifferentTopic(final NewsletterTopic topicToKeep, final PaperSlim paper) {
        for (final Map.Entry<NewsletterTopic, List<PaperSlim>> topicWithPapers : papersByTopic.entrySet()) {
            final NewsletterTopic currentTopic = topicWithPapers.getKey();
            final List<PaperSlim> assignedPapers = topicWithPapers.getValue();
            if (topicsDiffer(topicToKeep, currentTopic))
                assignedPapers.remove(paper);
        }
    }

    /**
     * either topics can be null. the topics do *not* differ if a) both are null or b) both are not null
     * and are equal.
     */
    private boolean topicsDiffer(final NewsletterTopic t1, final NewsletterTopic t2) {
        return eitherOneOrTheOtherIsNull(t1, t2) || areDifferent(t1, t2);
    }

    private boolean eitherOneOrTheOtherIsNull(final NewsletterTopic t1, final NewsletterTopic t2) {
        return t1 == null ^ t2 == null;
    }

    /*
     * At least one of the two topics is expected to not be null, but not both
     */
    private boolean areDifferent(final NewsletterTopic t1, final NewsletterTopic t2) {
        return t1 == null || !t1.equals(t2);
    }

    private void addPaperToTopic(final NewsletterTopic topic, final PaperSlim paper) {
        papersByTopic
            .computeIfAbsent(topic, k -> new ArrayList<>())
            .add(paper);
    }

    /*
     * Walks through all associations and checks if the paper lists (values) are empty, i.e.
     * if the topic does not have papers assigned. If so, the association is removed.
     */
    private void cleanseEmptyTopics() {
        papersByTopic
            .entrySet()
            .removeIf(e -> e
                .getValue()
                .isEmpty());
    }

    /**
     * @return all associated papers, regardless of the topic.
     */
    public List<PaperSlim> getPapers() {
        return this.papersByTopic
            .values()
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    /**
     * @return all newsletter topics
     */
    public List<NewsletterTopic> getTopics() {
        return new ArrayList<>(this.papersByTopic.keySet());
    }
}
