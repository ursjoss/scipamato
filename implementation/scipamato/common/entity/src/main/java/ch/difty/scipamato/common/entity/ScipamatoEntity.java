package ch.difty.scipamato.common.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
@EqualsAndHashCode(exclude = { "created", "lastModified" })
public class ScipamatoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime created;
    private LocalDateTime lastModified;
    private int           version;

    public enum ScipamatoEntityFields implements FieldEnumType {
        CREATED("created"),
        MODIFIED("lastModified"),
        VERSION("version");

        private final String name;

        ScipamatoEntityFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
