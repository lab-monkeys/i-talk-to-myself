package fun.is.quarkus.italktomyself.mapper;

import org.mapstruct.Mapper;
import fun.is.quarkus.italktomyself.dto.HeartBeatDto;
import fun.is.quarkus.italktomyself.model.HeartBeat;

@Mapper(componentModel = "cdi")
public interface DtoMapper {
    
    HeartBeat dtoToHeartBeat(HeartBeatDto dto);
    HeartBeatDto heartBeatToDto(HeartBeat heartbeat);
}
