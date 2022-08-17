package fun.is.quarkus.italktomyself.dto;

import java.util.UUID;

public record HeartBeatDto(UUID sender, UUID messageId, String url) {}
