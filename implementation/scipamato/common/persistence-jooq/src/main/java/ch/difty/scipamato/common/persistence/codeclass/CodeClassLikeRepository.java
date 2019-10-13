package ch.difty.scipamato.common.persistence.codeclass;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.entity.CodeClassLike;

/**
 * Generic interface for code class like entities of type {@code T}.
 *
 * @param <T>
 *     code classes of type {@code T}, extending {@link CodeClassLike}
 * @author u.joss
 */
public interface CodeClassLikeRepository<T extends CodeClassLike> {

    /**
     * Find the localized CodeClasses of type {@code T}
     *
     * @param languageCode
     *     the language code, e.g. 'en' or 'de'
     * @return a list of code classes implementing {@code CodeClassLike}
     */
    @NotNull
    List<T> find(@NotNull String languageCode);
}
