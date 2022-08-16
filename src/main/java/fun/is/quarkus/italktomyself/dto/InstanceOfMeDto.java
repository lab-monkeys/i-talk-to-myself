package fun.is.quarkus.italktomyself.dto;

import java.util.UUID;

public record InstanceOfMeDto(UUID instanceId, String url, boolean active) {}
