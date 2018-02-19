package ch.difty.scipamato.core.web.paper.jasper;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.User;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

/**
 * Custom implementation of the {@link SimplePdfExporterConfiguration} which is
 * clusterable and uses the builder pattern to set the parameters relevant to
 * scipamato.
 *
 * @author u.joss
 */
public class ScipamatoPdfExporterConfiguration extends SimplePdfExporterConfiguration
        implements ClusterablePdfExporterConfiguration {

    private static final long serialVersionUID = 1L;

    private ScipamatoPdfExporterConfiguration(final Builder builder) {
        if (builder.author != null)
            setMetadataAuthor(builder.author);
        if (builder.title != null || builder.paperTitle != null)
            setMetadataTitle(makeTitle(builder));
        if (getMetadataTitle() != null)
            setDisplayMetadataTitle(true);
        if (builder.subject != null)
            setMetadataSubject(builder.subject);
        if (builder.creator != null)
            setMetadataCreator(builder.creator);
        if (builder.keywords != null) {
            setMetadataKeywords(builder.keywords);
            setTagged(true);
        }
        setCompressed(builder.compression);
    }

    private String makeTitle(final Builder builder) {
        final StringBuilder sb = new StringBuilder();
        if (builder.title != null)
            sb.append(builder.title);
        if (sb.length() > 0 && (builder.paperAuthor != null || builder.paperTitle != null))
            sb.append(" - ");
        if (builder.paperAuthor != null)
            sb.append(builder.paperAuthor)
                .append(" et al.");
        if (builder.paperAuthor != null && builder.paperTitle != null)
            sb.append(": ");
        if (builder.paperTitle != null)
            sb.append(builder.paperTitle);
        return sb.toString();
    }

    public static class Builder {
        private static final String QUOTE = "\"";

        private final String title;
        private String       author;
        private String       paperTitle;
        private String       paperAuthor;
        private String       subject;
        private String       creator;
        private String       keywords;
        private boolean      compression = false;

        /**
         * Derive the metadata title from headerPart and the paper number (if you also
         * set the paperTitle, it will be appended as well...)
         *
         * @param headerPart
         * @param number
         *            the unique number of the paper
         */
        public Builder(final String headerPart, final Long number) {
            final StringBuilder sb = new StringBuilder();
            if (headerPart != null) {
                sb.append(headerPart);
            }
            if (number != null) {
                if (sb.length() > 0)
                    sb.append(" ");
                sb.append(number);
            }
            if (sb.length() > 0)
                this.title = sb.toString();
            else
                this.title = null;
        }

        /**
         * Set the metadata title directly. (if you also set the paperTitle, it will be
         * appended as well...)
         *
         * @param title
         */
        public Builder(final String title) {
            this.title = title;
        }

        public Builder withAuthor(final String author) {
            this.author = author;
            return this;
        }

        public Builder withAuthor(final User user) {
            if (user != null)
                this.author = user.getFirstName() + " " + user.getLastName();
            return this;
        }

        public Builder withPaperTitle(final String paperTitle) {
            this.paperTitle = paperTitle;
            return this;
        }

        public Builder withPaperAuthor(final String paperAuthor) {
            this.paperAuthor = paperAuthor;
            return this;
        }

        public Builder withSubject(final String subject) {
            this.subject = subject;
            return this;
        }

        public Builder withCreator(final String creator) {
            this.creator = creator;
            return this;
        }

        public Builder withCompression() {
            this.compression = true;
            return this;
        }

        public Builder withCodes(final List<Code> codes) {
            if (!CollectionUtils.isEmpty(codes)) {
                final StringBuilder sb = new StringBuilder();
                for (final Code c : codes) {
                    if (sb.length() > 0)
                        sb.append(",");
                    final String codeName = c.getName();
                    final boolean hasSpaces = hasSpaces(codeName);
                    if (hasSpaces)
                        sb.append(QUOTE);
                    sb.append(codeName);
                    if (hasSpaces)
                        sb.append(QUOTE);
                }
                this.keywords = sb.toString();
            }
            return this;
        }

        private boolean hasSpaces(final String code) {
            return code.contains(" ");
        }

        public ScipamatoPdfExporterConfiguration build() {
            return new ScipamatoPdfExporterConfiguration(this);
        }

    }

}
