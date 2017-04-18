package ch.difty.sipamato.persistance.jooq;

import java.util.List;

public interface Page<T> extends Iterable<T> {

    List<T> getContent();

}
