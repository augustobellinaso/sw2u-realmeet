package br.com.sw2u.realmeet;

import br.com.sw2u.realmeet.api.facade.RoomsApi;
import br.com.sw2u.realmeet.api.model.Room;
import java.util.concurrent.CompletableFuture;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomsController implements RoomsApi {

    @Override
    public CompletableFuture<ResponseEntity<Room>> listRooms(@Valid Long id) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(new Room().id(1L).name("Room #1")));
    }
}
