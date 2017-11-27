package ch.difty.scipamato.persistence.codeclass;

import java.util.List;

import ch.difty.scipamato.entity.CodeClassLike;

public interface CodeClassLikeRepository<T extends CodeClassLike> {

    /**
     * Find the localized CodeClasses of type {@code T}
     *
     * @param languageCode
     * @return a list of code classes
     */
    List<T> find(String languageCode);
}
