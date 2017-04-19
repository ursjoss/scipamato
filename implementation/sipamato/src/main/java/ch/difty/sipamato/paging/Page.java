package ch.difty.sipamato.paging;

import java.util.List;

@Deprecated
public interface Page<T> extends Iterable<T> {

    List<T> getContent();

}
