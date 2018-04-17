package ch.difty.scipamato.common.persistence;

import java.lang.annotation.*;

import org.springframework.stereotype.Component;

/**
 * Indicates that the annotated class implements a mapper converting a generic filter into a jOOQ condition.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface FilterConditionMapper {
}