package fun.is.quarkus.italktomyself.dto;

import java.util.UUID;

public record ReplyDto(UUID sender, UUID replyId, UUID messageId, String reply) {}
