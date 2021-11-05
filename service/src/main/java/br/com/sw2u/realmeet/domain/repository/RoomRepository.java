package br.com.sw2u.realmeet.domain.repository;

import br.com.sw2u.realmeet.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {}