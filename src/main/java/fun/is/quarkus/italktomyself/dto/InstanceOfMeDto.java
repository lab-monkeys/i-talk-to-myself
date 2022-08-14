package fun.is.quarkus.italktomyself.dto;

import java.util.UUID;

public record InstanceOfMeDto(UUID instanceId, boolean active) {}
