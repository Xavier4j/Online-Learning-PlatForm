package club.doyoudo.platform.vo;

import club.doyoudo.platform.entity.Profile;
import club.doyoudo.platform.entity.Reply;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyWithProfile extends Reply {
    private Profile reviewerProfile;
    private Profile recipientProfile;
}
