package fun.is.quarkus.italktomyself.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
    private UUID sender;
    private UUID replyId;
    private UUID messageId;
    String url;
}
