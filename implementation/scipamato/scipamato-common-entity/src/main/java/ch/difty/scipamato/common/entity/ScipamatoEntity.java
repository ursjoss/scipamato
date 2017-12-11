package ch.difty.scipamato.common.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = { "created", "lastModified" })
public class ScipamatoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String CREATED = "created";
    public static final String MODIFIED = "lastModified";
    public static final String VERSION = "version";

    private LocalDateTime created;
    private LocalDateTime lastModified;
    private int version;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
