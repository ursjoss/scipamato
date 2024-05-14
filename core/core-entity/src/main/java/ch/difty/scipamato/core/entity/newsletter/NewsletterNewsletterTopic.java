package ch.difty.scipamato.core.entity.newsletter;

import lombok.AllArgsConstructor;
import lombok.Data;

import ch.difty.scipamato.core.entity.CoreEntity;

@Data
@AllArgsConstructor
public class NewsletterNewsletterTopic extends CoreEntity {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private int    newsletterId;
    private int    newsletterTopicId;
    private int    sort;
    // not persisted, functionally dependent on newsletterTopicId
    private String title;

    @Override
    public String getDisplayValue() {
        return title;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other == null || this.getClass() != other.getClass())
            return false;
        final NewsletterNewsletterTopic o = (NewsletterNewsletterTopic) other;
        return this.newsletterId == o.newsletterId && this.newsletterTopicId == o.newsletterTopicId;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + newsletterId;
        result = 31 * result + newsletterTopicId;
        return result;
    }

}
