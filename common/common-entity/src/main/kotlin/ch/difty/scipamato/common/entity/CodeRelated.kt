package ch.difty.scipamato.common.entity

import java.io.Serializable

/**
 * Marker Interfaces for code like classes to be used to specify the generics in code specific service classes.
 */
interface CodeLike : Serializable

/**
 * Marker Interfaces for code class like classes to be used
 * to specify the generics in code class specific service classes.
 */
interface CodeClassLike : Serializable

/**
 * Code Classes are collections of code that can be maintained separately for a paper.
 * In order to profit from i18n, there is a separate set of tables with one entry per member here.
 */
@Suppress("MagicNumber")
enum class CodeClassId(val id: Int) {

    CC1(1),
    CC2(2),
    CC3(3),
    CC4(4),
    CC5(5),
    CC6(6),
    CC7(7),
    CC8(8);

    companion object {
        private val ID2ENUM: Map<Int, CodeClassId> = entries.associateBy { it.id }

        fun fromId(id: Int): java.util.Optional<CodeClassId> =
            java.util.Optional.ofNullable(ID2ENUM[id])
    }
}
