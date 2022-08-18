package fun.is.quarkus.italktomyself.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fun.is.quarkus.italktomyself.dto.HeartBeatDto;
import fun.is.quarkus.italktomyself.dto.InstanceOfMeDto;
import fun.is.quarkus.italktomyself.model.HeartBeat;
import fun.is.quarkus.italktomyself.model.InstanceOfMe;

@Mapper(componentModel = "cdi")
public interface DtoMapper {
    
    HeartBeat dtoToHeartBeat(HeartBeatDto dto);
    HeartBeatDto heartBeatToDto(HeartBeat heartbeat);
    
    @Mapping(target = "url", source = "url")
    InstanceOfMeDto instanceOfMeToDto(String url, InstanceOfMe instance);
}
