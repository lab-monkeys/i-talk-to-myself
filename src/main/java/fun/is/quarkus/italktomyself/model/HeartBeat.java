package fun.is.quarkus.italktomyself.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeartBeat {
    UUID sender;
    UUID messageId;
    String url;
}
