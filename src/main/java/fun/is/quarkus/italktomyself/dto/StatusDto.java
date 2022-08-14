package fun.is.quarkus.italktomyself.dto;

import java.util.List;
import java.util.UUID;

public record StatusDto(UUID instanceId, List<InstanceOfMeDto> instances, List<HeartBeatDto> pendingHeartBeats) {}
