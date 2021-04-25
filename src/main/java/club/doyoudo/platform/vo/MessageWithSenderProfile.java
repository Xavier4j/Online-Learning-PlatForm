package club.doyoudo.platform.vo;

import club.doyoudo.platform.entity.Message;
import club.doyoudo.platform.entity.Profile;
import lombok.Data;

@Data
public class MessageWithSenderProfile extends Message {
    private Profile senderProfile;
}
