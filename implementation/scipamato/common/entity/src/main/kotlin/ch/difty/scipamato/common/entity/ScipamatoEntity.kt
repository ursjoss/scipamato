package ch.difty.scipamato.common.entity

import java.io.Serializable
import java.time.LocalDateTime

interface FieldEnumType {
    val fieldName: String
}

open class ScipamatoEntity(
    var created: LocalDateTime? = null,
    var lastModified: LocalDateTime? = null,
    var version: Int = 0
) : Serializable {

    enum class ScipamatoEntityFields(override val fieldName: String) : FieldEnumType {
        CREATED("created"),
        MODIFIED("lastModified"),
        VERSION("version");
    }

    override fun toString(): String = "ScipamatoEntity[created=$created,lastModified=$lastModified,version=$version]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return (version == (other as ScipamatoEntity).version)
    }

    override fun hashCode(): Int {
        return version
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
