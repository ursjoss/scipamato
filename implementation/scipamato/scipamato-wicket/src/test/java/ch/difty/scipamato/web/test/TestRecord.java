package ch.difty.scipamato.web.test;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(includeFieldNames=false)
@AllArgsConstructor
public class TestRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    Integer id;
    String name;
}