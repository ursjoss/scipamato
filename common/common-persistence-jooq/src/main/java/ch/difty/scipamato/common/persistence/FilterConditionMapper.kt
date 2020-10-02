package ch.difty.scipamato.common.persistence

import org.springframework.stereotype.Component

/**
 * Indicates that the annotated class implements a mapper converting
 * a generic filter into a jOOQ condition.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
annotation class FilterConditionMapper
