package ch.difty.scipamato.core.entity.search;

import static java.util.Comparator.comparing;

import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.PaperCodeBox;

public class SearchConditionCodeBox extends PaperCodeBox {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Override
    public String toString() {
        return getCodes()
            .stream()
            .sorted(comparing(Code::getCode))
            .map(Code::getCode)
            .collect(Collectors.joining("&"));
    }
}
