package ch.difty.sipamato.persistance.jooq.repo;

import java.io.Serializable;

import ch.difty.sipamato.entity.SipamatoEntity;

public interface EntityTranslator<E extends SipamatoEntity, P extends Serializable, R extends Serializable> {

    P fromEntityToPojo(E entity);

    R fromPojoToRecord(P pojo);

    P fromRecordToPojo(R record);

    E fromPojoToEntity(P pojo);

}
