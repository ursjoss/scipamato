package ch.difty.scipamato.core.entity.newsletter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.core.entity.CoreEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class NewsletterNewsletterTopic extends CoreEntity {
    private int    newsletterId;
    private int    newsletterTopicId;
    private int    sort;
    // not persisted, functionally dependent on newsletterTopicId
    private String title;

    @Override
    public String getDisplayValue() {
        return title;
    }

}
