package ch.difty.scipamato.publ.persistence.paper.location;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.publ.persistence.paper.JournalExtractor;

/**
 * Simple implementation of {@link JournalExtractor}.
 *
 * <ol>
 * <li>if the location contains a period before the end of the string, we're
 * taking everything up to and excluding the period.</li>
 * <li>otherwise if the location contains a '(' (actually a ' ('), we're using
 * everything up to (and excluding) the ' ('.</li>
 * <li>otherwise use the entire location string</li>
 * </ol>
 *
 * @author Urs Joss
 */
@Component
public class SimpleJournalExtractor implements JournalExtractor {

    @Override
    public String extractJournal(final String location) {
        if (StringUtils.isBlank(location))
            return "";

        final int firstPeriodIndex = location.indexOf('.');
        if (hasIntermediatePeriod(firstPeriodIndex, location.length()))
            return location.substring(0, firstPeriodIndex);
        else {
            return getHead(location);
        }
    }

    private boolean hasIntermediatePeriod(final int firstPeriodIndex, final int length) {
        return firstPeriodIndex > 0 && firstPeriodIndex < length - 1;
    }

    /**
     * Takes the string up to and excluding the first occurrence of ' ('
     */
    private String getHead(final String location) {
        final int parensIndex = location.indexOf('(');
        if (parensIndex > 0)
            return location.substring(0, parensIndex - 1);
        else
            return location;
    }

}
