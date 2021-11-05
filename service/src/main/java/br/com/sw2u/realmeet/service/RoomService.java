package br.com.sw2u.realmeet.service;

import static java.util.Objects.requireNonNull;

import br.com.sw2u.realmeet.domain.entity.Room;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.exception.RoomNotFoundException;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room findById(Long id){
        requireNonNull(id);
        return roomRepository.findById(id).orElseThrow(RoomNotFoundException::new);
    }
}
