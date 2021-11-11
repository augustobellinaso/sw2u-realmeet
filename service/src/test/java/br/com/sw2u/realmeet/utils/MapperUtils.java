package br.com.sw2u.realmeet.utils;

import br.com.sw2u.realmeet.mapper.RoomMapper;
import org.mapstruct.factory.Mappers;

public final class MapperUtils {

    private MapperUtils() {
    }

    public static RoomMapper roomMapper() {
        return Mappers.getMapper(RoomMapper.class);
    }
}
