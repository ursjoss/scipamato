package ch.difty.scipamato.core.entity.newsletter;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;

@Data
@EqualsAndHashCode(callSuper = false)
public class NewsletterTopicFilter extends ScipamatoFilter {
    private static final long serialVersionUID = 1L;

}
