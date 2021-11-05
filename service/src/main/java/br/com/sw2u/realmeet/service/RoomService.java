package br.com.sw2u.realmeet.service;

import static java.util.Objects.requireNonNull;

import br.com.sw2u.realmeet.api.model.RoomDTO;
import br.com.sw2u.realmeet.domain.entity.Room;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.exception.RoomNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomDTO getRoom(Long id){
        requireNonNull(id);
        Room room = roomRepository.findById(id).orElseThrow(RoomNotFoundException::new);
        return new RoomDTO().id(room.getId()).name(room.getName()).seats(room.getSeats());
    }
}
