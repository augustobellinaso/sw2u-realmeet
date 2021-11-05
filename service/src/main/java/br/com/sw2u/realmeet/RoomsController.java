package br.com.sw2u.realmeet;

import br.com.sw2u.realmeet.api.facade.RoomsApi;
import br.com.sw2u.realmeet.api.model.RoomDTO;
import br.com.sw2u.realmeet.service.RoomService;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomsController implements RoomsApi {

    private final RoomService roomService;

    public RoomsController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public CompletableFuture<ResponseEntity<RoomDTO>> getRoom(Long id) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(roomService.getRoom(id)));
    }
}
