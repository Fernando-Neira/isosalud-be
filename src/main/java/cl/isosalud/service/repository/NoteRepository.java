package cl.isosalud.service.repository;

import cl.isosalud.service.entity.NoteEntity;
import cl.isosalud.service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<NoteEntity, Integer> {

    List<NoteEntity> findAllByDestUserEntity(UserEntity userEntity);

    List<NoteEntity> findAllByAuthorUserEntityAndDestUserEntity(UserEntity authorUserEntity, UserEntity destUserEntity);

}